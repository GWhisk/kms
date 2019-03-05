package jg.proj.kmsterm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javafx.application.Platform;
import jg.proj.kms.RdyFile;
import jg.proj.kms.RdyFileUtility;

/**
 * Main driver class for kms - the command line program.
 * @author G Whisk
 *
 */
public class TermMain {

  private static final double CURRENT_VERSION = 1.0;

	private static final String WORKING_DIR = new File("").getAbsolutePath();

	private static final File INSTALL_DIR = new File(System.getProperty("user.home"), "kms/"); // the install directory
	private static final File SCRIPTS_DIR = new File(System.getProperty("user.home"), "kms/scripts/"); // the scripts directory

	private static final Options OPTIONS;
	static{
	  OPTIONS = new Options();

    Option helpOption = new Option("h", "help", false, "Prints helpful general and usage information");
    OPTIONS.addOption(helpOption);

    Option currentVersion = new Option("v", "vers", false, "Prints the current kms version being installed");
    OPTIONS.addOption(currentVersion);

    Option deleteOption = new Option("r", "remove", false, "Removes an active kms script, by the numerical ID. If there's no argument, "
                                      + "it removes the most recent script.");
    deleteOption.setOptionalArg(true);
    deleteOption.setArgs(1);
    OPTIONS.addOption(deleteOption);

    Option clearAllOption = new Option("c", "clear", false, "Clears ALL active kms script");
    clearAllOption.setArgs(0);
    OPTIONS.addOption(clearAllOption);

    Option viewActives = new Option("a", "active", false, "Prints all active scripts, along with their trigger times");
    OPTIONS.addOption(viewActives);
	}

	/**
	 * Starting program method
	 * @param args - command args
	 */
	public static void main(String[] args){
		if (!verifyInstallFiles()) {
			System.err.println("Error! kms wasn't correctly installed. Please re-install");
			System.exit(-1);
		}

		if (args == null || args.length == 0) {
      printUsage();
      System.exit(0);
    }

		args = parseForOptions(args);

		long count = 1;
		try {
			count = getCurrentCount();
		} catch (Exception e) {
			System.err.println("Error! Unexpected exception when parsing count value.");
			System.exit(-1);
		}


		RdyFile rdyFile = null;
    try {
      rdyFile = parseArgs(args);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.err.println("Exiting...");
      System.exit(-1);
    }

		try {
			rdyFile.verifyCommands();
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
			System.err.println("Exiting...");
			System.exit(-1);
		}

		File rdyFileDir = new File(System.getProperty("user.home"), "kms/scripts/"+count+".rdy");
		RdyFileUtility.writeRdyFile(rdyFile, rdyFileDir);

		count++; //increment count
		updateCount(count); //update count on install directory

		//Add concluding print statements
		if (alertKmonitor()) {
	    System.out.println("* Script set for: "+rdyFile.getTriggerTime().getDateTime()+" | ID: "+(count - 1));
    }

		//This is due to PlayComm's use of JavaFX to play songs
		//Because we initialize JFXPanel to wake up JavaFX, our application can't exit normally
		//as JavaFX has opened it's own thread.
		Platform.exit();
	}

	private static boolean alertKmonitor(){
	  int portNumber = 9999;
	  try {
      Socket socket = new Socket();
      socket.setSoTimeout(5000);
      socket.connect(new InetSocketAddress(InetAddress.getByName("localhost"), portNumber));
      socket.close();
      return true;
    } catch (IOException e) {
      System.err.println("Unexpected IO Error: kmon not alive!");
      return false;
    }
	}

	/**
	 * Reads and returns the current rdy file count
	 * @return returns the current rdy file count
	 * @throws Exception - if any error occurs while reading and parsing the count
	 */
	private static long getCurrentCount() throws Exception{
		File infoFile = new File(System.getProperty("user.home"), "kms/info.txt");
		BufferedReader reader = new BufferedReader(new FileReader(infoFile));
		String countString = reader.readLine();
		reader.close();

		return Long.parseLong(countString);
	}

	/**
	 * Writes the new rdy file count
	 * @param newCount - the new count
	 * @return true if value is successfully written. False, if else.
	 */
	private static boolean updateCount(long newCount){
		File infoFile = new File(System.getProperty("user.home"), "kms/info.txt");

		try {
			FileWriter writer = new FileWriter(infoFile, false);
			writer.write(String.valueOf(newCount));
			writer.flush();

			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Verifies the existence of crucial kms files
	 * @return true if all crucial files exists. False if file(s) are missing.
	 */
	private static boolean verifyInstallFiles(){
		File [] files = {
				INSTALL_DIR,
				new File(INSTALL_DIR, "info.txt"),
				SCRIPTS_DIR
		};

		for(File file : files){
			if (!file.exists()) {
				return false;
			}
		}

		return true;
	}

	private static String [] parseForOptions(String ... args){
	  ArrayList<String> realRDYArguments = new ArrayList<>();

	  CommandLineParser parser = new DefaultParser();
    try {
      CommandLine commandLine = parser.parse(OPTIONS, args);
      if (commandLine.hasOption('h') || commandLine.hasOption("help")) {
        printUsage();
        System.exit(0);
      }
      else if (commandLine.hasOption('v') || commandLine.hasOption("vers")) {
        System.out.println("Version: "+CURRENT_VERSION);
        System.exit(0);
      }
      else if (commandLine.hasOption('r') || commandLine.hasOption("remove")) {
        String value = commandLine.getOptionValue("remove");
        if (value == null) {
          //remove most recent script
          Set<File> fileSet = FileUtils.getSortedRdyFiles(SCRIPTS_DIR);
          if (fileSet.isEmpty()) {
            System.out.println("No active scripts found!");
          }
          else {
            File recent = fileSet.toArray(new File[fileSet.size()])[0];
            if (!recent.delete()) {
              System.err.println("Unexpected Error: Failed to delete script!");
            }
            else {
              System.out.println("Script "+FileUtils.parseFileName(recent)+" has been deleted!");
            }
          }
        }
        else {
          //remove file by the given value
          try {
            long longValue = Long.parseLong(value);
            File target = FileUtils.getRdyFiles(SCRIPTS_DIR).get(String.valueOf(longValue));
            if (target != null) {
              if (!target.delete()) {
                System.err.println("Unexpected Error: Failed to delete script!");
              }
              else {
                System.out.println("Script "+longValue+" has been deleted!");
              }
            }
            else {
              System.err.println("Couldn't find script "+longValue+". Please use option -a, or -active for active scripts.");
            }
          } catch (NumberFormatException e) {
            System.err.println("Value for remove option is non-numerical!");
            printUsage();
            System.exit(-1);
          }
        }
        System.exit(0);
      }
      else if (commandLine.hasOption('c') || commandLine.hasOption("clear")) {
        try {
          FileUtils.deleteScripts(SCRIPTS_DIR);
          System.exit(0);
        } catch (Exception e) {
          System.out.println("Unexpeted Error: Failed to delete script "+e.getMessage()+"!");
          System.exit(-1);
        }
      }
      else if (commandLine.hasOption('a') || commandLine.hasOption("active")) {
        Set<File> fileSet = FileUtils.getSortedRdyFiles(SCRIPTS_DIR);
        System.out.println("------------ACTIVE SCRIPTS------------");
        for(File curFile : fileSet){
          String triggerTime = FileUtils.getTriggerTime(curFile);
          if (triggerTime == null) {
            triggerTime = "<ERROR RETRIEVING>";
          }

          System.out.println("  "+FileUtils.parseFileName(curFile)+"           TIME: "+triggerTime);
        }
        System.exit(0);
      }


      realRDYArguments.addAll(commandLine.getArgList());

    } catch (ParseException e) {
      System.err.println("Error parsing arguments!");
      printUsage();
    }

	  return realRDYArguments.toArray(new String[realRDYArguments.size()]);
	}

	/**
	 * Parses the command arguments into a RdyFile
	 * @param args - the trigger time and commands to parse
	 * @return the RdyFile equivalent
	 * @throws Exception
	 */
	private static RdyFile parseArgs(String ... args) throws Exception {
		if (args == null || args.length == 0) {
			printUsage();
			return null;
		}
		else {
			//first argument is either date_time or countdown
			//We're going to compiled all arguments as an array and pass it to RdyFileUtility
			return RdyFileUtility.parse(WORKING_DIR,args);
		}
	}

	private static void printUsage() {
	  String headerString = "kms supports two ways to formate date_time: "+System.lineSeparator()+
	                 "-> year:month:day:hour:minute "+System.lineSeparator()+
	                 "-> hour:minute , which will be based on the current day"+System.lineSeparator()+
	                 "The format for countdown is hour:minutes:seconds (all components must be valid, positive 32-bit integers)";

	  HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("kms [-h | -v | -c | -a | -r [ID] ] | ( ( (date_time) | countdown) {command1} [{command2} , ...] ) ",
        "Version: "+CURRENT_VERSION+System.lineSeparator()+headerString,
        OPTIONS,
        "");
	}
}
