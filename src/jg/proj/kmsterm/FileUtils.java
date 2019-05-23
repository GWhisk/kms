package jg.proj.kmsterm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class FileUtils {

  /**
   * Searches the directory for .rdy files
   * @param directory - the directory to search
   * @return a Map of String file names with File object values
   */
  public static Map<String, File> getRdyFiles(File directory){
    if (directory.isDirectory() && directory.exists()) {
      HashMap<String, File> map = new HashMap<>();
      for(File curFile : directory.listFiles()){
        if (curFile.isFile() && parseFileExtension(curFile).equals("rdy")) {
          map.put(parseFileName(curFile), curFile);
        }
      }
      return map;
    }
    return null;
  }

  /**
   * Searches the directory for .rdy file,
   * and sorts them from most recently created to least recently created
   * @param directory - the directory to search
   * @return a set of File objects, sorted.
   */
  public static Set<File> getSortedRdyFiles(File directory){
    if (directory.isDirectory() && directory.exists()) {
      TreeSet<File> set = new TreeSet<>(new Comparator<File>() {

        @Override
        public int compare(File o1, File o2) {
          String o1Name = parseFileName(o1);
          String o2Name = parseFileName(o2);

          long firstVal = Long.parseLong(o1Name);
          long secondVal = Long.parseLong(o2Name);

          if (firstVal < secondVal) {
            return -1;
          }
          else if (firstVal == secondVal) {
            return 0;
          }
          else {
            return 1;
          }
        }
      });

      for(File curFile : directory.listFiles()){
        if (curFile.isFile() && parseFileExtension(curFile).equals("rdy")) {
          set.add(curFile);
        }
      }

      return set;
    }
    return null;
  }

  /**
   * Parses the file name (without "." and the extension)
   * @param file - the File path to parse
   * @return the file extension (without the dot) from a file path
   */
  public static String parseFileName(File file){
    String fileName = file.getName();
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex < 0) {
      return null;
    }

    return fileName.substring(0, dotIndex);
  }

  /**
   * Parses the file extension (without the dot) from a file path
   * @param file - the File path to parse
   * @return the file extension (without the dot) from a file path
   */
  public static String parseFileExtension(File file){
    String fileName = file.getName();
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex < 0) {
      return null;
    }

    return fileName.substring(dotIndex+1);
  }

  /**
   * Returns the raw trigger time of the .rdy file
   * @param file - the rdy to read
   * @return the string raw trigger time
   */
  public static String getTriggerTime(File file){
    if (file.exists() && parseFileExtension(file).equals("rdy")) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        reader.readLine();
        String triggerString = reader.readLine();

        reader.close();
        return triggerString;
      } catch (IOException e) {
        return null;
      }
    }
    return null;
  }

  /**
   * Deletes the scripts in the given directory
   * @param file - the directory to clear out
   * @throws Exception - if a script fails to delete
   */
  public static void deleteScripts(File file) throws Exception{
    if (file.exists() && file.isDirectory()) {
      for(File curFile : file.listFiles()){
        if (parseFileExtension(curFile).equals("rdy")) {
          if (!curFile.delete()) {
            throw new Exception(parseFileName(curFile));
          }
        }
      }
    }
  }

}
