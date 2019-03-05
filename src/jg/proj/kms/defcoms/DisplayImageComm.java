package jg.proj.kms.defcoms;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import jg.proj.kms.Command;
import jg.proj.kms.VerificationException;

public class DisplayImageComm implements Command{

	@Override
	public int exec(String workingDir, String ... args) {
	  // TODO Auto-generated method stub
	  File picFile = new File(args[0]);
	  if (!picFile.isAbsolute()) {
	    picFile = new File(workingDir, args[0]);
	  }

	  System.out.println("---FULL PATH-----: "+picFile.toString());

	  if (!picFile.exists() && !picFile.canRead()) {
	    System.out.println(picFile.exists()+" | "+picFile.canRead());
	    return -1;
	  }
	  long seconds = Long.parseLong(args[1]);

	  GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	  GraphicsDevice device = env.getDefaultScreenDevice();

	  JFrame jf = new JFrame();
	  jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
	  jf.setUndecorated(true);
	  jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  Image resized = new ImageIcon(picFile.getAbsolutePath()).getImage().getScaledInstance(device.getDisplayMode().getWidth(),
	      device.getDisplayMode().getHeight(),
	      Image.SCALE_FAST);

	  ImageIcon icon = new ImageIcon(resized);
	  jf.add(new JLabel(icon));


	  jf.setVisible(true);
	  device.setFullScreenWindow(jf);


	  if (seconds >= 0) {
	    try {
	      Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
	      jf.dispose();
	    } catch (InterruptedException e) {
	      jf.dispose();
	    }
	  }
	  return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "display";
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
