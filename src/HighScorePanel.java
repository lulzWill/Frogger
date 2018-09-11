import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

//this class shows the high scores and has an option to go back to the main menu
@SuppressWarnings("serial")
public class HighScorePanel extends JPanel implements Runnable, KeyListener{
	private JTextArea textArea; //the textArea to display the high scores
	private BufferedImage highScoresBlack; //a title for the panel
	private BufferedImage mainMenuYellow; //to let the user know they can go back to main menu
	private Sequencer selectionSeq; //to play the sound effects when the user hits enter
	private String topScoreList; //to store the top scores
	private int selection; //triggered when the user hits enter so the frame can know when to switch panels
	
	public HighScorePanel(){
		setFocusable(true);
		addKeyListener(this);
		
		textArea = new JTextArea();   //set up the text area
		textArea.setEditable(false);
		textArea.setSize(300, 300);
		textArea.setBackground(Color.gray);
		add(textArea);
		
		topScoreList = "";
		
		setBackground(Color.GRAY);
		
		try {
			mainMenuYellow = ImageIO.read(new File("../EndGamePics/Main Menu Yellow.png"));   //get the images for the
			highScoresBlack = ImageIO.read(new File("../MainMenuPics/High Scores Black.png"));//fancy text
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {			
			selectionSeq = MidiSystem.getSequencer(); //set up the sound to play when user hits enter
			selectionSeq.open();

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		selection = 0; //initialize selection to 0
	}
	
	//paint the "High Scores" text, the "Main Menu" text and the list of high scores (on the textArea)
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage(highScoresBlack, 150, 50, this);
		
		textArea.setLocation(225, 200);
		textArea.setText(topScoreList);
		
		g.drawImage(mainMenuYellow, 125, 500, this);
	}

	@Override
	public void keyPressed(KeyEvent e) { //when the user presses enter, play the sound and trigger selection
		InputStream in;

		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			try {
				in = new BufferedInputStream(new FileInputStream(new File("../Music/Selection.mid")));
				selectionSeq.setSequence(in);
			} catch (IOException | InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			
			selectionSeq.start();
			selection = 1;
		}
	}
	
	//this method is so the Frame can check if the user is still in the menu
	public boolean inMenu(){
		if(selection == 0){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void run() {          //the run method gives focus to this window so the KeyListener will work,
		requestFocusInWindow();  //then paints the images
		
		while(inMenu()){
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					repaint();
				}

			});

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//allows the Frame to reset selection before it swaps to this panel again
	public void resetVariables(){
		selection = 0;
	}
	
	//this method allows the panel to get the high scores from the client
	public void setHighScoreList(String scores){
		topScoreList = scores;
	}

	//mandatory method from implementing KeyListener
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	//mandatory method from implementing KeyListener
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
