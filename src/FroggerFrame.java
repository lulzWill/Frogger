import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.JFrame;

//the FroggerFrame class is a JFrame that holds all the panels that serve as the interfaces of the game
@SuppressWarnings("serial")
public class FroggerFrame extends JFrame{
	private FroggerPanel fP; //this is the panel that plays the game
	private MainMenuPanel mainMenu; //this panel is the main menu
	private IntroAnimation intro; //this panel runs the intro
	private Client client; //this is the client that communicates with the server
	private HighScorePanel highScorePanel; //this panel displays the high scores
	private SendScorePanel sendScorePanel; //this panel prompts the user to send their score
	private GameOverPanel gameOverPanel; //this panel says the game is over
	private ExecutorService ex = Executors.newCachedThreadPool(); //executor is used to run runnables
	private Sequencer mainSongSeq; //main menu song
	private Sequencer gameSongSeq; //game song
	
	//the constructor creates an instance of all the necessary panels and sets up music/images
	public FroggerFrame()
	{
		super("Frogger");
		
		try {
			mainSongSeq = MidiSystem.getSequencer(); //get the music ready
			mainSongSeq.open();
			
			gameSongSeq = MidiSystem.getSequencer();
			gameSongSeq.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		setFocusable(false); //frame should never have focus for keyListeners
		intro = new IntroAnimation();
		intro.setSize(600, 740);
		intro.setVisible(false);
		add(intro);
		
		client = new Client();
		
		mainMenu = new MainMenuPanel();
		mainMenu.setSize(600, 740);
		
		fP = new FroggerPanel();
		fP.setSize(600,740);
		fP.setBackground(Color.black);
	
		highScorePanel = new HighScorePanel();
		highScorePanel.setSize(600, 740);
		
		sendScorePanel = new SendScorePanel();
		sendScorePanel.setSize(600, 740);
		
		gameOverPanel = new GameOverPanel();
		gameOverPanel.setSize(600, 740);
	}
	
	public void startGame(){
		ex.execute(client); //run the client to set up communication with server
		
		client.setHighScores(); //get the high scores list ready
		
		try {
			Thread.sleep(500);               //give the client time to get the high scores
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		intro.setHighScoreList(client.getTopScores()); //give the high score list to the intro panel so it can
		intro.setVisible(true);						   //display them
		ex.execute(intro); //start the intro animation
		
		//while the intro animation is running, finalize all the other panels and music
		InputStream in;
		InputStream in2;
		setBackground(Color.GRAY);
		try {
			in = new BufferedInputStream(new FileInputStream(new File("Music/Frogger.mid")));
			mainSongSeq.setSequence(in);
			mainSongSeq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			
			in2 = new BufferedInputStream(new FileInputStream(new File("Music/Game Song.mid")));
			gameSongSeq.setSequence(in2);
			gameSongSeq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		} catch (IOException | InvalidMidiDataException e) {
			e.printStackTrace();
		}
		
		mainMenu.setVisible(false);
		add(mainMenu);
		
		fP.setVisible(false);
		add(fP);
		
		highScorePanel.setVisible(false);
		add(highScorePanel);
		
		sendScorePanel.setVisible(false);
		add(sendScorePanel);
		
		gameOverPanel.setVisible(false);
		add(gameOverPanel);
		
		while(!intro.introComplete()){ //wait for intro to finish animation
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		intro.setVisible(false);
		
		while(isVisible())
		{
			mainSongSeq.start(); //play the main menu song
			
			mainMenu.setVisible(true); //run the main menu for the first time
			ex.execute(mainMenu);
			
			//this is the game loop, it waits for the user to choose something from the main menu,
			//then it displays the appropriate panels
			while(true){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(mainMenu.getChoice() == 1){ //if the user chooses Start Game, display the FroggerPanel;
					mainSongSeq.stop();
					
					gameSongSeq.start(); //start game music
					
					mainMenu.setVisible(false);  //switch out the main menu for the FroggerPanel, and allow it
					mainMenu.setFocusable(false);//to have focus for the KeyListener
				
					fP.setVisible(true);
					fP.resetGameState(); //reinitialize all the variables for the panel
					ex.execute(fP); //run the game
					
					while(fP.isPlaying()){                 //the frame now waits for the game to finish running,
						try {							   //the use of multi-threading here significantly improves
							Thread.sleep(10);			   //performance
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					fP.setVisible(false);   //game is now over, so swap the FroggerPanel with the gameOverPanel
					fP.setFocusable(false);
					gameSongSeq.stop();
					
					gameOverPanel.resetVariables();
					gameOverPanel.setVisible(true);
					ex.execute(gameOverPanel);
					
					while(!gameOverPanel.gameOverComplete()){ //wait for gameOverPanel to run its animation
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					gameOverPanel.setVisible(false); //gameOverPanel animation is complete, so switch panels with
													 //the sendScore panel and start the main menu music
					mainSongSeq.setMicrosecondPosition(0);
					mainSongSeq.start();
					
					client.setGameOver(); //let the client know it needs to send user's score and make a new
										  //high score list
					sendScorePanel.resetVariables();
					sendScorePanel.setScore(fP.getScore());
					sendScorePanel.setVisible(true);
					ex.execute(sendScorePanel);
					while(sendScorePanel.getName().equals("")){ //wait for the user to enter their name
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					client.setName(sendScorePanel.getName()); //give the client the user's name
					client.setScore(fP.getScore()); //give the client the user's score
					
					try {
						Thread.sleep(100);              //give the client enough time to get the high scores
					} catch (InterruptedException e1) { //from the server
						e1.printStackTrace();
					}
					sendScorePanel.setVisible(false); //switch the sendScorePanel with the highScorePanel
					
					highScorePanel.setHighScoreList(client.getTopScores());
					highScorePanel.resetVariables();    //give the highScorePanel the high score list, and
					highScorePanel.setVisible(true);    //allow it take focus for the KeyListener
					highScorePanel.setFocusable(true);
					ex.execute(highScorePanel);
					
					while(highScorePanel.inMenu()){        //wait while the user is looking at high scores
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					highScorePanel.setVisible(false);   //user left high scores, so switch it out with the
					highScorePanel.setFocusable(false); //main menu panel, then restart the game loop
					
					mainMenu.resetVariables();
					mainMenu.setVisible(true);
					mainMenu.setFocusable(true);
					ex.execute(mainMenu);
				}
				
				if(mainMenu.getChoice() == 2){ //if the user chooses to see high scores, go straight to the
					mainMenu.setVisible(false);//high score page
					mainMenu.setFocusable(false);
					
					client.setHighScores(); //let the client know it needs to grab the high score list without
											//sending a user's name/score
					try {
						Thread.sleep(500);              //give the client time to get the list from the server
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					highScorePanel.setHighScoreList(client.getTopScores()); //give the highScorePanel the high scores
					highScorePanel.resetVariables();
					highScorePanel.setVisible(true);   //display the high score panel and let it take the focus
					highScorePanel.setFocusable(true);
					ex.execute(highScorePanel);
					
					while(highScorePanel.inMenu()){        //wait while the user is looking at high scores
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					highScorePanel.setVisible(false);   //switch the highScorePanel with the mainMenu panel
					highScorePanel.setFocusable(false);
					
					mainMenu.resetVariables();
					mainMenu.setVisible(true);
					mainMenu.setFocusable(true);
					ex.execute(mainMenu);
				}
				
				if(mainMenu.getChoice() == 3){ //if the user chooses quit, then close the program
					System.exit(0);
				}
			}
		}
	}
}
