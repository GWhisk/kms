package jg.proj.kms.defcoms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jg.proj.kms.Command;
import jg.proj.kms.VerificationException;

public class MakeComm implements Command {

	@Override
	public int exec(String workingDir, String ... args) {
		String filePath = args[0];
		String message = args[1];

		File targetFile = new File(workingDir, filePath);
		if (!targetFile.exists()) {
			if (targetFile.mkdirs()) {
				try {
					targetFile.createNewFile();
				} catch (IOException e) {
					return -1;
				}
			}
			return -1;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
			writer.write(message);
			writer.close();
		} catch (IOException e) {
			return -1;
		}

		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "make";
	}

	@Override
	public int requiredArgs() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void verifyCommands(String... args) throws VerificationException {
		//this command will take all string arguments by default
		return;
	}

}
