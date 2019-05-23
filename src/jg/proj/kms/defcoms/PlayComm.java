package jg.proj.kms.defcoms;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import jg.proj.kms.Command;
import jg.proj.kms.VerificationException;

public class PlayComm implements Command{


	//dummy object to wake JavaFX up
  private static JFXPanel panel = new JFXPanel();


	protected static MediaPlayer player;

	@Override
	public int exec(String workingDir, String ... args) {
		File songFile = new File(workingDir, args[0]);

		//if duration = -1, play full song
		long duration = Long.parseLong(args[1]);
		if (songFile.exists() == false) {
			return - 1;
		}

		try {
			Media song = new  Media(songFile.toURI().toString());
			player = new MediaPlayer(song);

			if (duration < 0) {
				player.play();
			}
			else {
				player.play();
				while (player.getCurrentTime().lessThanOrEqualTo(Duration.seconds(duration)));
				player.stop();
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "play";
	}

	@Override
	public int requiredArgs() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void verifyCommands(String... args) throws VerificationException {
	  try {
      Long.parseLong(args[1]);
    } catch (NumberFormatException e) {
      throw new VerificationException(this, "2nd argument needs to be a valid integer");
    }
	}

}
