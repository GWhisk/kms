package jg.proj.kmon;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import jg.proj.kms.RdyFile;
import jg.proj.kms.RdyFileUtility;

public class KmonMain {

  public static void main(String[] args) {
    File scriptDir = new File(System.getProperty("user.home"), "kms/scripts/");

    Timer scheaduler = new Timer();

    System.out.println("----kmon initialized----");

    System.out.println("--> Querying for old files!");
    //Query all-ready present scripts:
    scheaduelEntries(scriptDir, scheaduler);

    //now query for new files
    System.out.println("--> Querying for new files!");

    try {
      ServerSocket serverSocket = new ServerSocket(9999, 10);
      serverSocket.setReuseAddress(true);
      serverSocket.setSoTimeout(Integer.MAX_VALUE);

      boolean stop = false;

      while (stop != true) {
        serverSocket.accept();
        System.out.println("----> Potential new script!");
        scheaduelEntries(scriptDir, scheaduler);
      }

      serverSocket.close();
    } catch (IOException e) {
      System.err.println("Unable to listen! Exiting....");
    }
  }

  private static void scheaduelEntries(File scriptDir, Timer scheaduler){
    for(File curFile : scriptDir.listFiles()){
      if (!curFile.getName().endsWith(".rdy")) {
        System.out.println("-> IGNORING: "+curFile.getName()+" isn't .rdy. Ignore...");
        continue;
      }

      try {
        final RdyFile rdyFile = RdyFileUtility.readRdyFile(curFile);
        Files.delete(curFile.toPath());
        DateTime trigger = rdyFile.getTriggerTime().getDateTime();

        scheaduler.schedule(new TimerTask() {
          public void run() {
            System.out.println("--> "+curFile.getName()+" triggered!");
            rdyFile.execute();
            System.out.println("--> "+curFile.getName()+" complete!");
          }
        }, trigger.toInstant().toDate());

        System.out.println("* SCHEADULED (OLD) FILE: "+curFile.getName());

      } catch (Exception e) {
        System.out.println("---ERROR READING (OLD): "+curFile.getName());
      }
    }
  }

}
