package jg.proj.kms;

/**
 * Represents a kms Command
 * @author G Whsik
 *
 */
public interface Command {

	/**
	 * Executes this command.
	 *
	 * @param args - the necessary arguments for this command
	 * @param workingDir - the working directory to execute this command on
	 * @return the exit code of this command. Return value may indicate failure
	 */
	public int exec(String workingDir, String ... args);

	/**
	 * Returns the name of this argument
	 * @return the string name of this argument
	 */
	public String getName();


	/**
	 * Verifies the types of arguments for this command
	 * @throws VerificationException if verification fails
	 */
	public void verifyCommands(String ... args) throws VerificationException;


	/**
	 * The amount of arguments this command requires.
	 *
	 * If the requiredArgs is a negative integer 'n', then that means that
	 * this command takes at least n-arguments
	 *
	 * @return the amount of arguments this command requires
	 */
	public int requiredArgs();

}
