package jg.proj.kms.defcoms;

import java.io.IOException;

import jg.proj.kms.Command;
import jg.proj.kms.VerificationException;

public class ExecComm implements Command{

	@Override
	public int exec(String workingDir, String ... args) {
		String wholeCommad = args[1];
		workingDir = '"'+workingDir+'"';
		try {
			String wdChangComm = "\" cd  "+workingDir+" && "+wholeCommad+" \" ";
			Process process = Runtime.getRuntime().exec("cmd /c "+wdChangComm);
			return process.waitFor();
		} catch (IOException | InterruptedException e) {
			return -1;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "exec";
	}

	@Override
	public int requiredArgs() {
		return 1;
	}

	@Override
	public void verifyCommands(String... args) throws VerificationException {
	    //this command will take all string arguments by default
		return;
	}

}
