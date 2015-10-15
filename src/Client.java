import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Client implements Runnable{
	private Socket client; //socket to set up connection
	private ObjectOutputStream output; //output to server
	private ObjectInputStream input; //input from server
	private int score; //variable to hold scores
	private boolean gameOver = false; //sends if game is over to client
	private boolean getHighScores = false; //sends if should show high scores screen to server
	private boolean on = true; //game is still running variable
	private String nameHold; //holds the name that is retrieved from user
	private String topScores;//the high scores string

	public Client() {
		nameHold = "";
		
		try {
			//connect to server
			client = new Socket(InetAddress.getByName("127.0.0.1"), 12345);
			
			//set up output stream to server
			output = new ObjectOutputStream(client.getOutputStream());
			output.flush();
			//set up input stream to server
			input = new ObjectInputStream(client.getInputStream());
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while(on)//while game is still going
			{
				
				//if game over send "Game Over to server
				if (gameOver)
				{
					sendData("Game Over");
					
					//while nothing has been inputed into the name field after game
					while (nameHold == "")
					{
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					//send name and score to server
					sendData(nameHold);
					sendData(Integer.toString(score));
					
					//name hold back to null
					nameHold = "";
					
					try {
						topScores = (String) input.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					//set game over back to false
					gameOver = false;
					
				}
				//if the user said high score then send high score
				if(getHighScores)
				{
					sendData("High Scores");
			
					try {
						topScores = (String) input.readObject();
					} catch (ClassNotFoundException e) {
						
						e.printStackTrace();
					}
					
					getHighScores = false;
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} catch (EOFException e) {
			System.out.print("\n client terminated");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		try {
			//close everything once done
			client.close();
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send message to server
	private void sendData(String message) {
		try // send object to server
		{
			output.writeObject(message);
			output.flush(); // flush data to output
		} catch (IOException i) {
			System.out.print("Error writing object");
		}
	}
	
	public void setGameOver()
	{
		gameOver = true;
	}
	
	public void setHighScores()
	{
		getHighScores = true;
	}
	
	public void end()
	{
		on = false;
	}
	
	public String getTopScores(){
		return topScores;
	}
	
	public void setScore(int num){
		score = num;
	}
	
	public void setName(String name){
		nameHold = name;
	}
	
	public void resetVariables(){
		gameOver = false;
		getHighScores = false;
		score = -10;
		nameHold = "";
	}
}
