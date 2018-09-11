import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//this class runs the GameOver animation when the user loses all their lives
@SuppressWarnings("serial")
public class GameOverPanel extends JPanel implements Runnable{
	private BufferedImage gameOverText; //the "Game Over" text
	private Sequencer gameOverSongSeq; //the song to play
	private boolean complete; //triggered when the animation is complete
	
	public GameOverPanel(){
		try
		{
			gameOverText = ImageIO.read(new File("../GameOverPics/Game Over.png"));//set up the file image
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			gameOverSongSeq = MidiSystem.getSequencer(); //set up the music
			gameOverSongSeq.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		complete = false; //initialize complete to false since animation not done yet
		setBackground(Color.black);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(gameOverText, 75, 125, this); //just draw the image of the "Game Over" text in
	}											  //the same place
	
	public void run(){
		InputStream in; //set up the music
		
		try {
			in = new BufferedInputStream(new FileInputStream(new File("../Music/Game Over.mid")));
			gameOverSongSeq.setSequence(in);
		} catch (IOException | InvalidMidiDataException e) {

			e.printStackTrace();
		}
		gameOverSongSeq.start(); //play the game over song
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				repaint(); //paint the "Game Over" text
			}

		});
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while(gameOverSongSeq.isRunning()){    //wait for the song to finish playing before
			try {							   //we trigger 'complete'
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		complete = true;
	}
	
	//before we rerun the animation, we need a way to reset complete to false
	public void resetVariables(){
		complete = false;
	}
	
	//need a way for the frame to know when complete is triggered
	public boolean gameOverComplete(){
		return complete;
	}
}
