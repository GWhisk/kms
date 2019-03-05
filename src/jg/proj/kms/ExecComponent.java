package jg.proj.kms;

import java.util.Arrays;

/**
 * Represents a command to be executed.
 *
 * NOTE: ExecComponents are different from Commands.
 *       ExecComponents are Commands to be executed, along with their String arguments
 * @author G Whisk
 *
 */
public class ExecComponent {

	private Command command;
	private String [] arguments;

	private int result = 0;

	/**
	 * Constructs an ExecComponent
	 * @param command - the Command to execute
	 * @param arguments - the argument to pass to the given Command
	 */
	public ExecComponent(Command command, String ... arguments) {
		this.command = command;
		this.arguments = arguments;
	}

	/**
	 * Executes the command given to this ExecComponent
	 */
	public void execute(String workingDir) {
		result = command.exec(workingDir, arguments);
	}

	/**
	 * Returns the latest execution result
	 * @return the latest execution result
	 */
	public int latestResult() {
		return result;
	}

	/**
	 * Returns the arguments of this components
	 * @return the arguments of this components
	 */
	public String [] getArguments(){
		return arguments;
	}

	public Command getCommand(){
		return command;
	}

	public String toString(){
		return command.getName()+" | ARGS: "+Arrays.toString(arguments);
	}
}
