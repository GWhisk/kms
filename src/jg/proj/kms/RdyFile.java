package jg.proj.kms;

import org.joda.time.DateTime;

/**
 * Represents an .rdy file - the intermediate representation of a list of commands passed to kms
 * @author G Whisk
 *
 */
public class RdyFile {
  private String workingDir;
  private TriggerTime triggerTime;
  private ExecComponent [] components;

  /**
   * Constructs an RdyFile
   * @param triggerTime - the raw trigger time, in the supported format
   * @param workingDir - the working directory when this RdyFile was created
   * @param components - the ExecComponents held in this RdyFile
   * @throws Exception - if an exception is raised while parsing the trigger time
   */
  public RdyFile(String triggerTime, String workingDir, ExecComponent [] components) throws Exception {
    this.triggerTime = parseTriggerTime(triggerTime);
    this.components = components;
    this.workingDir = workingDir;
  }

  /**
   * Constructs an RdyFile
   * @param dateTime - the DateTime representation to trigger
   * @param workingDir - the working directory when this RdyFile was created
   * @param components - the ExecComponents held in this RdyFile
   */
  public RdyFile(DateTime dateTime , String workingDir, ExecComponent [] components){
    this.triggerTime = new TriggerTime(dateTime);
    this.components = components;
    this.workingDir = workingDir;
  }

  /**
   * Parses the raw trigger time into a proper TriggerTime object
   * @param triggerTime - the raw trigger time
   * @return the proper TriggerTime representation
   * @throws Exception - if the raw trigger time is malformed or formatted incorrectly
   */
  private TriggerTime parseTriggerTime(String triggerTime) throws Exception{
    String [] comps = triggerTime.split(":");

    if (comps.length == 2) {
      //By hours:minute format
      try {
        int hours = Integer.parseInt(comps[0]);
        int minutes = Integer.parseInt(comps[1]);

        if (hours < 0 || minutes < 0) {
          //no negative values allowed
          throw new Exception("Countdown time component not a valid, positive 32-bit integer!");
        }
        else {
          DateTime time = DateTime.now().withTime(hours, minutes, 0, 0);
          return new TriggerTime(time);
        }
      } catch (NumberFormatException e) {
        throw new Exception("Countdown time component not a valid, positive 32-bit integer!");
      }
    }
    else if (comps.length == 5) {
      //By year:month:day:hour:minute format
      try {
        int year = Integer.parseInt(comps[0]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[2]);
        int hour = Integer.parseInt(comps[3]);
        int minute = Integer.parseInt(comps[4]);

        if (year < 0 || month < 0 || day < 0 || hour < 0 || minute < 0) {
          //no negative values allowed
          throw new Exception("Invalid date-time component(s)! (negative values)");
        }
        else {
          DateTime time = new DateTime(year, month, day, hour, minute);
          return new TriggerTime(time);
        }
      } catch (NumberFormatException e) {
        throw new Exception("Countdown time component not a valid, positive 32-bit integer!");
      }
    }
    else if (comps.length == 3) {
      //By hour:minutes:seconds format
      try {
        int hours = Integer.parseInt(comps[0]);
        int minutes = Integer.parseInt(comps[1]);
        int seconds = Integer.parseInt(comps[2]);

        if (hours < 0 || minutes < 0 || seconds < 0) {
          //no negative values allowed
          throw new Exception("Countdown time component not a valid, positive 32-bit integer!");
        }
        else {
          return new TriggerTime(hours, minutes, seconds);
        }
      } catch (NumberFormatException e) {
        throw new Exception("Countdown time component not a valid, positive 32-bit integer!");
      }
    }
    else {
      throw new Exception("Invalid trigger time format!");
    }
  }

  /**
   * Returns the working directory this .rdy file was meant for
   * @return the String path of the working directory
   */
  public String getWorkingDirectory(){
    return workingDir;
  }

  /**
   * Returns the TriggerTime of this .rdy file
   * @return the TriggerTime of this .rdy file
   */
  public TriggerTime getTriggerTime() {
    return triggerTime;
  }

  /**
   * Verifies the .rdy file.
   *
   * Verification is mainly checking whether arguments match the types of command parameters. If not,
   * an IllegalStateException is thrown
   *
   * For example, the default command 'play' accepts two arguments. The first being a String and the second being a Long.
   * When verifying, this method will check whether the 2nd argument to 'play' is a Long. If not, an IllehalStateException
   * is thrown
   *
   * @throws IllegalStateException - if a mismatch of type is encountered
   */
  public void verifyCommands() throws IllegalStateException{
    for(int i = 0; i < components.length; i++){
      ExecComponent currentComponent = components[i];
      try {
        currentComponent.getCommand().verifyCommands(currentComponent.getArguments());
      } catch (VerificationException e) {
        throw new IllegalStateException("Error! Command "+(i+1)+" has an error: "+e.getMessage());
      }
    }
  }

  /**
   * Executes the ExecComponents of this RdyFile
   */
  public void execute() {
    for(ExecComponent component : components) {
      component.execute(workingDir);
    }
  }

  /**
   * Returns the ExecComponents of this RdyFile
   * @return the ExecComponents of this RdyFile
   */
  public ExecComponent[] getExecComponents(){
    return components;
  }

  /**
   * Represents a trigger time
   * @author G Whisk
   */
  public static class TriggerTime{
    private DateTime dateTime;

    /**
     * Constructs a TriggerTime
     * @param hours - the amount of hours for this countdown
     * @param minutes - the amount of minutes for this countdown
     * @param seconds - the amount of seconds for this countdown
     * @param rawTriggerTime - the raw, String trigger time
     */
    public TriggerTime(int hours, int minutes, int seconds) {
      this(DateTime.now().plusHours(hours).plusMinutes(minutes).plusSeconds(seconds));
    }

    /**
     * Constructs a TriggerTime
     * @param dateTime - the DateTime object representing the trigger time
     * @param rawTriggerTime - the raw, String trigger time
     */
    public TriggerTime(DateTime dateTime) {
      this.dateTime = dateTime;
    }

    /**
     * Returns the DateTime representation of this trigger time
     * @return the DateTime representation of this trigger time
     */
    public DateTime getDateTime() {
      return dateTime;
    }
  }
}
