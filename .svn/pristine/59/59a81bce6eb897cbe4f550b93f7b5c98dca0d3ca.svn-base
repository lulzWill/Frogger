import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

//this panel appears after the GameOver panel, it makes the user enter their name so it can send the
//name and score to the server to generate a high score list
@SuppressWarnings("serial")
public class SendScorePanel extends JPanel implements Runnable{
	private JTextField textField; //textField for user to enter their name
	private int score; //the user's score
	private BufferedImage sendScores; //image of text that says "Send Your Score"
	private String nameString; //holds the user's name
	private String nameHolder; //gets the user's name from the textField
	
	public SendScorePanel(){
		textField = new JTextField(10);
		textField.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){ //when user enters text, put it in the
						nameHolder = event.getActionCommand();		//nameHolder String
					}
				});
		add(textField);
		
		score = -1;
		nameString = "";
		nameHolder = "";
		
		try {
			sendScores = ImageIO.read(new File("EndGamePics/SendScore.png")); //set up image
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setBackground(Color.gray);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage(sendScores, 75, -50, this); //draw the "Send Your Score" text at the top
		
		g.drawString("Score: " + Integer.toString(score), 250, 250); //display the user's score
		g.drawString("Enter Name: ", 200, 300); //prompt user to enter name
		textField.setLocation(280, 287); //show text box for user to enter their name
	}
	
	public void run(){
		
		while(nameHolder.equals("")){ //paint the panel and wait until the user enters their name
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
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		nameString = nameHolder;
		nameHolder = "";
	}
	
	//allows the Frame to set the score from the FroggerPanel
	public void setScore(int num){
		score = num;
	}
	
	//allows the Frame to check when the user entered their name
	public String getName(){
		return nameString;
	}
	
	//allows the Frame to reset the variables before it swaps out the panels
	public void resetVariables(){
		nameString = "";
		score = -1;
	}
}
