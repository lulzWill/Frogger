import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel implements Runnable, KeyListener{
	private BufferedImage splatHole;       //all the images that will be used
	private BufferedImage startBlack;
	private BufferedImage startYellow;
	private BufferedImage highScoresBlack;
	private BufferedImage highScoresYellow;
	private BufferedImage quitBlack;
	private BufferedImage quitYellow;
	private BufferedImage optionsBlack;
	private BufferedImage optionsYellow;
	private int selection; //what the user is hovering over
	private int finalChoice; //what the user finally selected
	private Sequencer moveSelectSeq; //the music that plays
	private Sequencer selectionSeq;
	
	public MainMenuPanel(){
		setFocusable(true);
		this.addKeyListener(this);
		try
		{   //set up the images
			splatHole = ImageIO.read(new File("../IntroPics/SplatHole.png"));
			startBlack = ImageIO.read(new File("../MainMenuPics/Start Game Black.png"));
			startYellow = ImageIO.read(new File("../MainMenuPics/Start Game Yellow.png"));
			highScoresBlack = ImageIO.read(new File("../MainMenuPics/High Scores Black.png"));
			highScoresYellow = ImageIO.read(new File("../MainMenuPics/High Scores Yellow.png"));
			quitBlack = ImageIO.read(new File("../MainMenuPics/Quit Black.png"));
			quitYellow = ImageIO.read(new File("../MainMenuPics/Quit Yellow.png"));
			optionsBlack = ImageIO.read(new File("../MainMenuPics/Options Black.png"));
			optionsYellow = ImageIO.read(new File("../MainMenuPics/Options Yellow.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		     //set up the songs
		try {
			moveSelectSeq = MidiSystem.getSequencer();
			moveSelectSeq.open();
			
			selectionSeq = MidiSystem.getSequencer();
			selectionSeq.open();

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		//initialize variables
		selection = 1;
		finalChoice = 0;
		setBackground(Color.gray);
	}
	
	//paints the background splat and all the menu options, it will highlight whatever the user is hovered over
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(splatHole, 0, 0, this);
		if(selection == 1){                          //paint the Start Game image, paint it yellow if user has
			g.drawImage(startYellow, 140, 30, this); //it currently selected
		}
		else{
			g.drawImage(startBlack, 140, 30, this);
		}

		if(selection == 2){
			g.drawImage(highScoresYellow, 140, 105, this); //paint the High Scores image, paint it yellow if user has
		}												   //it currently selected
		else{
			g.drawImage(highScoresBlack, 140, 105, this);
		}
		if(selection == 3){
			g.drawImage(quitYellow, 140, 600, this); //paint the Quit image, paint it yellow if user has it
		}											 //currently selected
		else{
			g.drawImage(quitBlack, 140, 600, this);
		}
	}
	
	//the run method just waits until the user finally selects something, and continues to call repaint until then
	public void run() {
		requestFocusInWindow();
		
		while(finalChoice == 0){	
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					repaint();
				}

			});
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) { //the KeyListener plays a sound if the user moves their selection or
		InputStream in;					 //hits enter, and repaints the appropriate images
		InputStream in2;
		try {
			in = new BufferedInputStream(new FileInputStream(new File("../Music/MoveSelect.mid")));
			moveSelectSeq.setSequence(in);

			in2 = new BufferedInputStream(new FileInputStream(new File("../Music/Selection.mid")));
			selectionSeq.setSequence(in2);
		} catch (IOException | InvalidMidiDataException e1) {
			e1.printStackTrace();
		}

		if(e.getKeyCode() == KeyEvent.VK_UP)  //if they hit up, play sound and change selection
		{
			moveSelectSeq.start();
			selection -= 1;
			if(selection == 0){
				selection = 3;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) //if they hit down, play sound and change selection
		{
			moveSelectSeq.start();
			selection += 1;
			if(selection == 4){
				selection = 1;
			}
		}

		else if(e.getKeyCode() == KeyEvent.VK_ENTER){	//if they hit enter, play sound and trigger finalChoice		
			selectionSeq.start();
			finalChoice = selection;
		}
	}
	
	//this method is so the Frame can know when the user wants to switch panels
	public int getChoice(){
		return finalChoice;
	}
	
	//this method allows the Frame to reset the variables before it swaps in the mainMenu panel
	public void resetVariables(){
		finalChoice = 0;
		selection = 1;
	}
	
	public void setChoice(int num){
		finalChoice = num;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {	
	}
}

