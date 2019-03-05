package jg.proj.kms.defcoms;

import jg.proj.kms.Command;

/**
 * Commands supported by kms
 * @author G Whisk
 *
 */
public enum DefaultCommands {

	DELETE(new DeleteComm()),
	DISP(new DisplayImageComm()),
	EXEC(new ExecComm()),
	MAKE(new MakeComm()),
	PLAY(new PlayComm());


	public final Command RELATED;

	DefaultCommands(Command related){
		this.RELATED = related;
	}


}
