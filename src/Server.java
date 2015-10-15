import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Server extends JPanel {
	private ServerSocket server;
	/** server socket to connect with clients */
	private Socket connection;
	private ObjectOutputStream outputStream; //output stream to client
	private ObjectInputStream inputStream; //gets input from client
	private String[] names = new String[6]; //string array to hold the names of high score holders 
	private String[] scoresHold = new String[13];// array to hold the names and scores of high scorers
	private int[] scores = new int[6]; //array to hold high scores
	private JTextArea outputArea; //output area for high scores
	private BufferedReader inputFile; //for reading file
	private BufferedWriter outputFile; //for writing from file
	private String name; // name from client
	private int score; // score from client
	private JPanel panel; 
	private boolean connected; //check if connected

	/** Constructor */
	public Server() {
		
		connected = false;
		//set up panel
		panel = new JPanel();
		add(panel);
		panel.setPreferredSize(new Dimension(300, 300));

		// create output area
		outputArea = new JTextArea();
		panel.add(outputArea, BorderLayout.CENTER);
		outputArea.setPreferredSize(new Dimension(300, 300));
		
		// set size and show
		setSize(300, 300);
		setVisible(true);
	}

	public void runServer() {
		try {
			//create socket
			server = new ServerSocket(12345, 1);
			while(true){
				//set up connection to client
				connection = server.accept();

				outputArea.append("Connection established.\n");

				outputStream = new ObjectOutputStream(connection.getOutputStream());
				outputStream.flush();

				inputStream = new ObjectInputStream(connection.getInputStream());

				outputArea.append("Got streams.\n");
				connected = true;
				boolean gamePlayed = false;
				
				//keep looping while connected to client
				while (connected) {
					try {
						String choice = "";

						//while the input from the client isn't game over or high scores, keep checking for input
						while(!choice.equals("Game Over") && !choice.equals("High Scores")){
							choice = (String) inputStream.readObject();
						}
						
						//if game over
						if(choice.equals("Game Over")){
							
							//get name and score 
							name = (String) inputStream.readObject();
							score = Integer.parseInt((String) inputStream.readObject());
							
							//open txt file
							inputFile = new BufferedReader(new FileReader("HighScores/HighScores.txt"));

							String temp;
							//put info from file into a temp array
							temp = inputFile.readLine();

							// put info from file into the array **
							int i = 0;
							while(temp != null) {
								scoresHold[i] = temp;
								temp = inputFile.readLine();
								i++;
							}

							//put name and score into scores hold array
							scoresHold[i] = name;
							scoresHold[i+1] = Integer.toString(score);

							if (inputFile != null) {
								inputFile.close();
							}

							//sort array from highest to lowest score
							sortScoresHoldArray();

							//open file to write to
							outputFile = new BufferedWriter(new FileWriter("HighScores/HighScores.txt"));

							//add name and scores to array
							StringBuffer tempString = new StringBuffer("");
							for(int j = 0; j < 5; j++){
								tempString.append(names[j]);
								tempString.append("\n");
								tempString.append(scores[j]);
								tempString.append("\n");
							}

							//write the tempScreen array into the file
							outputFile.write(tempString.toString());

							if (outputFile != null) {
								outputFile.close();
							}

							//send top scores to client
							outputStream.writeObject(displayTopScores());
							outputStream.flush();
							gamePlayed = true;
						}

						//once client sends high scores
						else if(choice.equals("High Scores")){
							//set up input to read file
							inputFile = new BufferedReader(new FileReader("HighScores/HighScores.txt"));

							//read file
							String temp = inputFile.readLine();
							
							// put info into the word array **
							int i = 0;
							while(temp != null) {
								scoresHold[i] = temp;
								temp = inputFile.readLine();
								i++;
							}
							if (inputFile != null) {
								inputFile.close();
							}
							//sort the scores array if game not over
							if(!gamePlayed)
							{
								sortScoresHoldArray();
							}
							//write high scores to file 
							outputStream.writeObject(displayTopScores());
							outputStream.flush();
						}

					} catch (EOFException | ClassNotFoundException e) {
						System.out.print("\nServer terminated connection");
						connected = false;
					} 

				}
				closeConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//close connections
	private void closeConnection() {
		System.out.print("Terminating Connection");

		try {
			outputStream.close();
			inputStream.close();
			connection.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	/** display top scores */
	private String displayTopScores() {
		StringBuffer temp = new StringBuffer("");
		
		for(int i = 0; i < 5; i++){
			temp.append(Integer.toString(i+1));
			temp.append(". ");
			temp.append(names[i]);
			temp.append(": ");
			temp.append(Integer.toString(scores[i]));
			temp.append(" ");
			temp.append("\n");
			temp.append("\n");
		}

		return temp.toString();
	}

	/**
	 * puts the information from the input file into the correct array and then
	 * sorts and prints the high scores with corresponding names
	 */
	private void sortScoresHoldArray() {
		// counters
		int j = 0;
		int k = 1;
		int i = 0;

		// put the info from the file into the appropriate array
		while (scoresHold[i] != null) {

			//if even number slot it is a name
			if (i % 2 == 0) {
				names[i - j] = scoresHold[i];
				j++;

				//if odd number slot it is a score
			} else if (i % 2 == 1) {
				int num = Integer.parseInt(scoresHold[i]);
				scores[i - k] = num;
				k++;
			}

			i++;
		}

		int hold = 0;
		String hold2 = "";


		// sort the array using bubble sort-- puts highest score in first
		for (int pass = 1; pass < scores.length; pass++) {
			for (i = 0; i < scores.length - 1; i++) {
				if (scores[i] < scores[i + 1]) {
					hold = scores[i];
					scores[i] = scores[i + 1];
					scores[i + 1] = hold;

					hold2 = names[i];
					names[i] = names[i + 1];
					names[i + 1] = hold2;
				}
			}
		}
	}
}