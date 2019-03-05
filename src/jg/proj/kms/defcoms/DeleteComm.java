package jg.proj.kms.defcoms;

import java.io.File;
import java.nio.file.Files;

import jg.proj.kms.Command;
import jg.proj.kms.VerificationException;

public class DeleteComm implements Command{

	private int filesFailedToDelete = 0;

	@Override
	public int exec(String workingDir, String ... args) {
		for(String arg : args) {
			File file = new File(workingDir, arg);
			if (file.exists()) {
				recurseDelete(file);
			}
		}

		filesFailedToDelete = 0;

		int failed = filesFailedToDelete;
		return -1 * failed;
	}

	private void recurseDelete(File file) {
		if (file.isFile()) {
			if (!file.delete()){
				filesFailedToDelete ++;
			}
		}
		else {
			for(File nested : file.listFiles()) {
				recurseDelete(nested);
			}
			file.delete();
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "delete";
	}

	@Override
	public int requiredArgs() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public void verifyCommands(String... args) throws VerificationException {
       //this command will take all string arguments by default
		return;
	}

}
