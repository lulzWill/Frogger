import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This class provides the introduction animation. This thread runs while the other panels are (invisibly)
 * being added to the frame.
 * @author Eric
 *
 */
@SuppressWarnings("serial")
public class IntroAnimation extends JPanel implements Runnable {
	/**
	 * whiteText is the white text that initially moves up the screen
	 */
	private BufferedImage whiteText;
	/**
	 * greenText is the green text that falls out of the splat where whiteText was
	 */
	private BufferedImage greenText;
	/**
	 * splatHole is the green splat that says "Frogger" in the middle
	 */
	private BufferedImage splatHole;
	/**
	 * greenSplat is the green splat without "Frogger" in the middle
	 */
	private BufferedImage greenSplat;
	/**
	 * introSongSeq will play the introductory song
	 */
	private Sequencer introSongSeq;
	/**
	 * y is the y position of the images that are being drawn
	 */
	private int y;
	/**
	 * speed controls how fast the images move across the screen
	 */
	private int speed;
	/**
	 * firstMoveComplete triggers when the white text has reached the middle of the screen
	 */
	private boolean firstMoveComplete;
	/**
	 * secondMoveComplete triggers after the splat has been on the screen for a set amount of time
	 */
	private boolean secondMoveComplete;
	/**
	 * thirdMoveComplete triggers when the animation has fully run
	 */
	private boolean thirdMoveComplete;
	/**
	 * textArea is used to display the high scores
	 */
	private JTextArea textArea;
	/**
	 * topScores is the formatted string of all the top scores
	 */
	private String topScores;
	
	/**
	 * the constructor sets up the images/music, creates the text area, and initializes the variables.
	 * the variables are hard-coded for the animation to display properly.
	 */
	public IntroAnimation(){	
		textArea = new JTextArea(); //set up text area
		textArea.setEditable(false);
		textArea.setSize(300, 300);
		textArea.setBackground(Color.black);
		textArea.setForeground(Color.white);
		add(textArea);
		
		y = 450; //initialize variables
		speed = 1;
		firstMoveComplete = false;
		secondMoveComplete = false;
		thirdMoveComplete = false;
		String topScores = "";
		
		try
		{
			whiteText = ImageIO.read(new File("../IntroPics/Frogger1.png")); //set up images
			greenText = ImageIO.read(new File("../IntroPics/Frogger2.png"));
			greenSplat = ImageIO.read(new File("../IntroPics/GreenSplat.png"));
			splatHole = ImageIO.read(new File("../IntroPics/SplatHole.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			introSongSeq = MidiSystem.getSequencer(); //set up sound
			introSongSeq.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		setBackground(Color.black);
	}//end constructor
	
	/**
	 * the paintComponent method draws all the images in the appropriate spot. using the firstMoveComplete,
	 * secondMoveComplete, and thirdMoveComplete triggers it knows which images to draw.
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!firstMoveComplete){                 //if we're on the first move, draw the high scores and draw
			textArea.setLocation(250, 10);      //the white text as it scrolls upward
			textArea.setText(topScores);
			g.drawImage(whiteText, 0, y, this);
		}
		
		else if(!secondMoveComplete){           //once the white text hits the appropriate spot, display the
			textArea.setVisible(false);         //green splat
			g.drawImage(greenSplat, 0, 0, this);
		}
		
		else if(!thirdMoveComplete){            //after the green splat, make the green text fall, and leave
			g.drawImage(splatHole, 0, 0, this); //the hole
			g.drawImage(greenText, 0, y, this);
		}
	}//end paintComponent
	
	/**
	 * the run method sets up the music, then calls the appropriate methods to draw the animation. for the
	 * first move, it calls repaint() and move() in order to make the white text scroll upward. for the second
	 * move, it draws the green splat in the middle of the screen and leaves it there for a bit. for the third
	 * move, the green text falls out of the splat.
	 */
	public void run() {
		InputStream in;  //first set up the music and play it
		
		try {
			in = new BufferedInputStream(new FileInputStream(new File("../Music/Intro Song.mid")));
			introSongSeq.setSequence(in);
		} catch (IOException | InvalidMidiDataException e) {

			e.printStackTrace();
		}
		introSongSeq.start();

		while(y >= 0)   //until the white text hits a set point, keep calling move() to calculate its new
		{				//position, and then repaint it.
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					move();
					repaint();
				}

			});
		}
		try {
			Thread.sleep(2500);             //this wait is implemented so the animation will line up
		} catch (InterruptedException e1) { //with the music
			e1.printStackTrace();
		}

		firstMoveComplete = true; //the white text has hit the middle, and the green splat should now be drawn

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				repaint(); //draw the green splat
			}

		});

		try {
			Thread.sleep(2750);            //this wait is also set up in order to line up with the music
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		secondMoveComplete = true;

		while(y < 500) //this section will call move and repaint to make the green text fall from the middle
		{			   //of the screen
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					move();
					repaint();
				}

			});
		}

		try {
			Thread.sleep(2000);             //this wait is just so the screen doesn't immediately move to the
		} catch (InterruptedException e1) { //main menu
			e1.printStackTrace();
		}

		thirdMoveComplete = true;
		
		introSongSeq.close();
	}
	
	//this method is used to calculate positions of the text images for the white text that needs to
	//steadily scroll up the screen, and for the green text that should 'fall' off the screen
	public void move() {
		if(!firstMoveComplete){
			y -= speed;
		}
		else if(!thirdMoveComplete){
			y+= speed;
			if(speed < 20){
				speed++;
			}
		}
	}
	
	//this method is so the frame can know when the animation is done
	public boolean introComplete(){
		return thirdMoveComplete;
	}
	
	//this method sets the topScores to the formatted string from the client
	public void setHighScoreList(String scores){
		topScores = scores;
	}
}
