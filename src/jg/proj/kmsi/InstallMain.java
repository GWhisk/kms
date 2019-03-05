package jg.proj.kmsi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Installation Class for the kms program
 * @author Jose Guaro
 *
 */
public class InstallMain {

	public static final double CURRENT_VERSION = 1.0; //the version of kms being installed

	private static final Options OPTIONS;
	static{
		OPTIONS = new Options();

		Option helpOption = new Option("h", "help", false, "Prints helpful general and usage information");
		OPTIONS.addOption(helpOption);

		Option currentVersion = new Option("v", "vers", false, "Prints the current kms version being installed");
		OPTIONS.addOption(currentVersion);
	}

	private static String installDir = System.getProperty("user.home");

	/**
	 * Main driver method for this program
	 * @param args - program arguments.
	 *
	 */
	public static void main(String [] args){
		parseArguments(args);

		//Sets the install directory
		File targetDir = new File(installDir, "kms/");
		System.out.println("* Installing in directory "+targetDir.getAbsolutePath());

		if (targetDir.exists()) {
			System.err.println("Error! Directory is taken. Stopping installation...");
			System.exit(-1);
		}

		if (!targetDir.mkdir()) {
			System.err.println("Error! Cannot create directory. Stopping installation...");
			System.exit(-1);
		}

		//Information file for kms and associated program to check current naming scheme
		File infoFile = new File(targetDir, "info.txt");

		try {
			if (!infoFile.createNewFile() && !infoFile.canWrite()) {
				System.err.println("Error! Couldn't create or write to info file. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create info file. Stopping installation...");
			System.exit(-1);
		}

		System.out.println("* Created info file: "+infoFile.getAbsolutePath());

		//Initialize the info file
		try {
			PrintWriter infoWrite = new PrintWriter(infoFile);
			infoWrite.println(0);
			infoWrite.flush();

			System.out.println(" > Initialized!");

			infoWrite.close();
		} catch (FileNotFoundException e) {
			System.err.println("Unexpected IO error. Stopping installation...");
			System.exit(-1);
		}

		File scriptDirectory = new File(targetDir, "scripts/");
		System.out.println("* Creating script directory...");

		if (scriptDirectory.exists()) {
			System.err.println("Error! Directory is taken. Stopping installation...");
			System.exit(-1);
		}

		if (!scriptDirectory.mkdir()) {
			System.err.println("Error! Cannot create directory. Stopping installation...");
			System.exit(-1);
		}

		System.out.println(" > Created!");

		//Link kms stuff in starup folder
		Path startupDir = Paths.get("C:/users/All Users/Start Menu/Programs/Startup");

		System.out.println("* Creating startup file...");

		File batchFile = new File(startupDir.toFile(), "kmsmonitor.bat");
		try {
			if (!batchFile.createNewFile() && !batchFile.canWrite()) {
				System.err.println("Error! Couldn't create or write to startup file. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create startup file. Stopping installation...");
			System.exit(-1);
		}

		try {
			PrintWriter writer = new PrintWriter(batchFile);
			writer.println("javaw.exe -jar \""+new File(targetDir, "kmon.jar").getAbsolutePath()+"\"");
			writer.flush();
			writer.close();
			System.out.println("* Startup file created. ");
		} catch (FileNotFoundException e) {
			System.err.println("Unexpected IO error. Stopping installation...");
			System.exit(-1);
		}

		//kmon.jar copying section
		System.out.println("* Copying over kmon.jar...");
		URL kmonJar = InstallMain.class.getResource("kmon.jar");

		File kmonJarDest = new File(targetDir, "kmon.jar");
		try {
			if (!kmonJarDest.createNewFile() && !kmonJarDest.canWrite()) {
				System.err.println("Error! Couldn't create or write kmon.jar. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create kmon.jar. Stopping installation...");
			System.exit(-1);
		}

		try {
			InputStream inputStream = kmonJar.openStream();
			FileOutputStream stream = new FileOutputStream(kmonJarDest);

			byte [] buffer = new byte[512];
			int written = 0;
			while ( ( written = inputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, written);
			}

			stream.close();
			System.out.println("* kmon.jar finished!");
		} catch (IOException e) {
			System.err.println("Error! Couldn't create kmon.jar. Stopping installation...");
			System.exit(-1);
		}

		//kms.jar installs section
		System.out.println("* Copying over kms.jar...");
		URL kmsJar = InstallMain.class.getResource("kms.jar");

		File kmsJarDest = new File(targetDir, "kms.jar");
		try {
			if (!kmonJarDest.createNewFile() && !kmonJarDest.canWrite()) {
				System.err.println("Error! Couldn't create or write kms.jar. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create kms.jar. Stopping installation...");
			System.exit(-1);
		}

		try {
			InputStream inputStream = kmsJar.openStream();
			FileOutputStream stream = new FileOutputStream(kmsJarDest);

			byte [] buffer = new byte[512];
			int written = 0;
			while ( ( written = inputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, written);
			}

			stream.close();
			System.out.println("* kms.jar finished!");
		} catch (IOException e) {
			System.err.println("Error! Couldn't create kmon.jar. Stopping installation...");
			System.exit(-1);
		}

		//Create kms bat and add to Windows Path
		System.out.println("* Creating kms executable...");

		File kmsExec = new File(targetDir, "kms.bat");
		try {
			if (!kmsExec.createNewFile() && !kmsExec.canWrite()) {
				System.err.println("Error! Couldn't create or write to kms exec. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create kms exec. Stopping installation...");
			System.exit(-1);
		}

		try {
			PrintWriter writer = new PrintWriter(kmsExec);
			writer.println("@echo off");
			writer.println("java.exe -jar \""+new File(targetDir, "kms.jar").getAbsolutePath()+"\" %*");
			writer.flush();
			writer.close();
			System.out.println("* Startup file created. ");
		} catch (FileNotFoundException e) {
			System.err.println("Unexpected IO error. Stopping installation...");
			System.exit(-1);
		}


		//Add kms to Path
		System.out.println("* Adding kms to path...");
		URL patheditorURL = InstallMain.class.getResource("patheditor.ps1");

		File patheditorDest = new File(targetDir, "patheditor.ps1");
		try {
			if (!patheditorDest.createNewFile() && !patheditorDest.canWrite()) {
				System.err.println("Error! Couldn't create or write script. Stopping installation...");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.err.println("Error! Couldn't create script. Stopping installation...");
			System.exit(-1);
		}

		try {
			InputStream inputStream = patheditorURL.openStream();
			FileOutputStream stream = new FileOutputStream(patheditorDest);

			byte [] buffer = new byte[512];
			int written = 0;
			while ( ( written = inputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, written);
			}

			stream.close();
			System.out.println("* script copying finished!");
		} catch (IOException e) {
			System.err.println("Error! Couldn't create script. Stopping installation...");
			System.exit(-1);
		}

		//Execute Powerscript adding kms to path

		String powerShellScriptQuoted = "'"+patheditorDest.getAbsolutePath()+"'";
		String kmsDirectoryQuoted = "'"+targetDir.getAbsolutePath()+"'";
		String fullCommand =
				"PowerShell -NoProfile -ExecutionPolicy Bypass -Command \"& "+powerShellScriptQuoted+" "+kmsDirectoryQuoted+"\"";

		try {
			Process process = Runtime.getRuntime().exec(fullCommand);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("* Installation finished!");

		System.out.println("---------> PLEASE REBOOT YOUR MACHINE FOR KMS TO FUNCTION CORRECTLY <---------");
	}

	private static void parseArguments(String ... args){
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

			if (!commandLine.getArgList().isEmpty()) {
				System.err.println("Unnecessary arguments passed!");
				printUsage();
				System.exit(-1);
			}

		} catch (ParseException e) {
			System.err.println("Error parsing arguments!");
			printUsage();
		}
	}

	private static void printUsage(){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("kmsi [-h | -v ]",
				            "the installation program for kms (VERS. "+CURRENT_VERSION+")",
				            OPTIONS,
				            "");
	}
}
