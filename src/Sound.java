import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

//public class Sound {
//	
//	  public static void main(String[] args) throws Exception {
//	        URL url = new URL(
//	                          "http://pscode.org/media/leftright.wav");
//	        Clip clip = AudioSystem.getClip();
//	        // getAudioInputStream() also accepts a File or InputStream
//	        AudioInputStream ais = AudioSystem.getAudioInputStream( url );
//	        clip.open(ais);
//	        clip.loop(Clip.LOOP_CONTINUOUSLY);
//	        SwingUtilities.invokeLater(new Runnable() {
//	            public void run() {
//	                // A GUI element to prevent the Clip's daemon Thread 
//	                // from terminating at the end of the main()
//	                JOptionPane.showMessageDialog(null, "Close to exit!");
//	            }
//	        });
//	    }
//	}