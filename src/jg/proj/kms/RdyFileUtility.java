package jg.proj.kms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;

import jg.proj.kms.defcoms.DefaultCommands;

/**
 * A Utility class to parse, load, and write .rdy files
 * @author G Whisk
 *
 */
public class RdyFileUtility {

  /**
   * Parses the raw commands given to kms into an .rdy file representation.
   * @param workingDir - the working directory in which this .rdy is to be executed in
   * @param lines - the commands. The first line should be the raw trigger time
   * @throws Exception - if an eror occurs while parsing
   * @return the RdyFile representation, or null if an error occurs.
   *         If an error does occur, it is printed to standard error.
   */
  public static RdyFile parse(String workingDir, String ... lines) throws Exception{

    if (lines.length <= 1) {
      throw new Exception("Parse Error: Missing comamnds!");
    }

    String triggerTime = lines[0];

    String [] actualCommands = new String[lines.length - 1];
    System.arraycopy(lines, 1, actualCommands, 0, actualCommands.length);

    try {
      return new RdyFile(triggerTime, workingDir, parseToExecs(actualCommands));
    } catch (Exception e) {
      throw new Exception("Parse Error: Trigger time is malformed. Reason: "+e.getMessage());
    }
  }

  /**
   * Parses the list of commands into an array of ExecComponents
   * @param lines - the array of commands
   * @return an array of ExecComponents
   * @throws Exception - if an error occurs during parsing.
   */
  public static ExecComponent [] parseToExecs(String ... lines) throws Exception{

    if (lines.length < 1) {
      throw new Exception("Parse Error: Missing comamnds!");
    }

    //combine the rest of the commands as one string for parsing
    String allCommands = "";
    for(int i = 0; i < lines.length ; i++){
      allCommands += lines[i];
    }

    int currentCommandCount = 1;

    ArrayList<ExecComponent> commndList = new ArrayList<>();

    //start parsing commands
    for(int i = 0; i < allCommands.length(); i++){
      if (allCommands.charAt(i) == '{') {


        //the enclosed whole command: COMMAND <arg1> <arg2>
        String enclosedCommand = "";
        i++;
        while (i < allCommands.length()) {
          if ( allCommands.charAt(i) != '}' ) {
            enclosedCommand += allCommands.charAt(i);
          }
          else {
            break;
          }
          i++;
        }

        //System.out.println("PARSED WHOLE COMMAND: "+enclosedCommand);

        //parse command name
        String commandName = "";
        int argStart = 0;
        for ( ; argStart < enclosedCommand.length(); argStart++) {
          if (enclosedCommand.charAt(argStart) != '<') {
            commandName += enclosedCommand.charAt(argStart);
          }
          else {
            break;
          }
        }

        commandName = commandName.trim();
        //System.out.println("---PARSED COMMAND NAME: |"+commandName+"|");

        //check if command name is supported
        Command command = null;
        if ( (command = isSupportedDefCommand(commandName)) == null ) {
          throw new Exception("Parse Error: '"+commandName+"' is an unsupported command!");
        }

        //parse arguments
        ArrayList<String> argumentList = new ArrayList<>();
        String currentArg = "";
        for( ; argStart < enclosedCommand.length(); argStart++){
          if (enclosedCommand.charAt(argStart) == '<') {
            continue;
          }
          else if (enclosedCommand.charAt(argStart) == '>') {
            if (currentArg.isEmpty()) {
              continue;
            }
            argumentList.add(currentArg.trim());
            currentArg = "";
          }
          else {
            currentArg += enclosedCommand.charAt(argStart);
          }
        }

        //check if command argument requirement mmatches with given arguments
        if (command.requiredArgs() < 0) {
          int minArgReq = Math.abs(command.requiredArgs());
          if (argumentList.size() < minArgReq) {
            throw new Exception("Parse Error: '"+commandName+"' requires at least "+minArgReq+" argument , "+currentCommandCount+" arg");
          }
        }
        else if (command.requiredArgs() > 0) {
          if (command.requiredArgs() != argumentList.size()) {
            throw new Exception("Parse Error: '"+commandName+"' requires "+command.requiredArgs()+" , "+currentCommandCount+" arg");
          }
        }

        commndList.add(new ExecComponent(command, argumentList.toArray(new String[argumentList.size()])));

        currentCommandCount++;
      }
    }

    try {
      return commndList.toArray(new ExecComponent[commndList.size()]);
    } catch (Exception e) {
      throw new Exception("Parse Error: Trigger time is malformed. Reason: "+e.getMessage());
    }
  }

  /**
   * Checks if the given string corresponds to a supported command
   * @param commandName - the command name to check
   * @return the corresponding Command, or null if none was found
   */
  public static Command isSupportedDefCommand(String commandName){
    for(DefaultCommands command : DefaultCommands.values()){
      if (commandName.equalsIgnoreCase(command.RELATED.getName())) {
        return command.RELATED;
      }
    }
    return null;
  }

  /**
   * Trims all strings in the string array
   * @param strings - the strign array
   */
  public static void trimStrings(String [] strings){
    for (int i = 0; i < strings.length; i++) {
      strings[i] = strings[i].trim();
    }
  }

  /**
   * Writes an RdyFile object into an actual file
   * @param rdyFile - the RdyFile object
   * @param destination - the File to write it to
   * @return true if write was successful, false if else.
   */
  public static boolean writeRdyFile(RdyFile rdyFile, File destination){
    if (!destination.exists()) {
      try {
        if (!destination.createNewFile()) {
          return false;
        }
      } catch (IOException e) {
        return false;
      }
    }

    try {
      PrintWriter printWriter = new PrintWriter(destination);

      //first line is working directory
      printWriter.println(rdyFile.getWorkingDirectory());

      //second line is trigger
      String exactTrigger = rdyFile.getTriggerTime().getDateTime().toString();
      printWriter.println(exactTrigger);

      for(ExecComponent component : rdyFile.getExecComponents()){
        String wholeCommand = "{";
        wholeCommand += component.getCommand().getName();


        for(String arg : component.getArguments()){
          String encryptArg = "<"+arg+">";
          wholeCommand += encryptArg;
        }

        wholeCommand += "}";

        printWriter.println(wholeCommand);
      }

      printWriter.flush();
      printWriter.close();

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Parses an actual .rdy file into an RdyFile object
   * @param file - the .rdy file
   * @return the RdyFile representation
   * @throws Exception - if an error occurs while parsing
   */
  public static RdyFile readRdyFile(File file) throws IOException, IllegalArgumentException{
    if (file.exists() && file.canRead()) {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        //first line is working directory
        String currentWorkingDirectory = reader.readLine();

        //second line is trigger
        String triggerTime = reader.readLine();

        String commands = "";
        String current = null;
        while ((current = reader.readLine()) != null) {
          commands += current;
        }

        reader.close();

        try {
          ExecComponent[] execs = parseToExecs(commands);
          return new RdyFile(new DateTime(triggerTime), currentWorkingDirectory, execs);
        } catch (Exception e) {
          throw new IllegalArgumentException(e.getMessage());
        }
    }
    else {
      return null;
    }
  }
}
