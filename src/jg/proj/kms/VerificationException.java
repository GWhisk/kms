package jg.proj.kms;

/**
 * Represents an error with command verification
 * @author G Whisk
 *
 */
public class VerificationException extends Exception{

	public VerificationException(Command command , String error){
		super("The '"+command.getName()+"' has an error: "+error);
	}

}
