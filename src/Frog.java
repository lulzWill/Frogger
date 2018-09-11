import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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


public class Frog extends JPanel implements KeyListener, Runnable{
	//local vars
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	private BufferedImage frogPic;
	private Sequencer bounceSeq;
	private Sequencer deathSeq;
	private Sequencer victorySeq;
	private int lastMove;
	private int frame = 0;
	private final int speed;
	private int xSpeed;
	private boolean moving = false;
	private boolean isAlive;
	private int score;
	private int maxHeight;
	private String codes;
	private String idleFrog;
	private String movingFrog;
	private String movingSideFrog;
	private String sideFrog;
	private boolean godMode;
	
	public Frog(int x, int y, int s)
	{
		//initialize local vars
		idleFrog = "../GamePics/frogFrame1.png";
		movingFrog = "../GamePics/frogFrame2.png";
		sideFrog = "../GamePics/frogFrameSide1.png";
		movingSideFrog = "../GamePics/frogFrame2Side.png";
		codes = "";
		maxHeight = 600;
		score = 0;
		xSpeed = 0;
		isAlive = true;
		//set up midi files
		try
		{
			bounceSeq = MidiSystem.getSequencer();
			bounceSeq.open();
			deathSeq = MidiSystem.getSequencer();
			deathSeq.open();
			victorySeq = MidiSystem.getSequencer();
			victorySeq.open();
		}
		catch(MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		speed = s;
		xPos = x;
		yPos = y;
		setOpaque(false);
		this.setFocusable(true);
		this.addKeyListener(this);
		setFrogPic(idleFrog);
		width = frogPic.getWidth();
		height = frogPic.getHeight();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setLocation(xPos, yPos);
		setSize(getPreferredSize());
		Graphics2D g2D = (Graphics2D)g;
		//if frog is alive, paint the frame of motion it is in
		if(isAlive)
		{
			switch(lastMove)
			{
				case 0:
					if(frame == 0)
					{
						setFrogPic(idleFrog);
					}
					else
					{
						setFrogPic(movingFrog);
					}
					g2D.drawImage(frogPic, 0, 10, width, height, this);
					break;
				case 1:
					if(frame == 0)
					{
						setFrogPic(idleFrog);
					}
					else
					{
						setFrogPic(movingFrog);
					}
					g2D.drawImage(frogPic, 0, height, width, -height, this);
					break;
				case 2:
					if(frame == 0)
					{
						setFrogPic(sideFrog);
					}
					else
					{
						setFrogPic(movingSideFrog);
					}
					g2D.drawImage(frogPic, 0, 10, width, height, this);
					break;
				case 3:
					if(frame == 0)
					{
						setFrogPic(sideFrog);
					}
					else
					{
						setFrogPic(movingSideFrog);
					}
					g2D.drawImage(frogPic, width, 10, -width, height, this);
					break;
				default:
					break;
			}
		}
		//if frog is dead, sets its image to flat
		else
		{	
			setFrogPic("../GamePics/splatSmall.png");
			g2D.drawImage(frogPic, 0, 10, width, height, this);
		}
	}
	
	@Override
	public void run() {
		//runs this while the panel is visible
		while(isVisible())
		{
			//moves the frog if it has an x-speed
			if(xPos >= 0 && xPos < 560)
			{
				xPos = xPos + xSpeed;
			}
			//give some delay
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//if the frog is moving, this will set the animation frames and move the frog
			if(moving)
			{
				playBounce();
				for(int i = 0; i < 50/speed; i++)
				{
					if(i == 50/speed - 1)
					{
						frame = 0;
					}
					else
					{
						frame = 1;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run() {
								moveFrog();
								repaint();
						}
					});
				}
				//add ten points if the frog reaches a new height
				if(getYPos() < maxHeight)
				{
					addToScore(10);
					maxHeight = maxHeight - 50;
				}
				//no longer moving
				moving = false;
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height + 10);
	}
	
	//used to set the frog's picture, takes a string as input. This string should always be a file name
	public void setFrogPic(String s)
	{
		try
		{
			frogPic = ImageIO.read(new File(s));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		width = 40;
		height = 40;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//if the frog is not moving and alive, respond to user input
		if(!moving && isAlive)
		{
			//move frog up
			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				lastMove = 0;
				moving = true;
				codes = "";
			}
			//move frog down
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				lastMove = 1;
				moving = true;
				codes = "";
			}
			//move frog left
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				lastMove = 3;
				moving = true;
				codes = "";
			}
			//move frog right
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				lastMove = 2;
				moving = true;
				codes = "";
			}
			//else store input in case it is a cheat code
			else
			{
				codes = codes + e.getKeyChar();
			}
			//checks if a cheat code was entered
			checkForCodes();
		}
	}
	
	public void moveFrog()
	{
		//move the frog based on last registered user input
		switch(lastMove)
		{
			case 0:
				if(!(yPos - speed < 0))
				{
					yPos = yPos - speed;
				}
				break;
			case 1:
				if(!(yPos + speed > 600))
				{
					yPos = yPos + speed;
				}
				break;
			case 2:
				if(!(xPos + speed > 550))
				{
					xPos = xPos + speed;
				}
				break;
			case 3:
				if(!(xPos - speed < 0))
				{
					xPos = xPos - speed;
				}
				break;
			default:
				break;
		}
	}
	
	//checks for cheat codes
	private void checkForCodes()
	{
		//turns frog into casavant when "cas" is entered
		if(codes.equals("cas"))
		{
			idleFrog = "../GamePics/cas.png";
			movingFrog = "../GamePics/cas.png";
			sideFrog = "../GamePics/cas.png";
			movingSideFrog = "../GamePics/cas.png";
			repaint();
		}
		//turns frog into its default when "frog" is entered
		else if(codes.equals("frog"))
		{
			resetFrogPic();
			repaint();
		}
		//turns frog into johnson when "john" is entered
		else if(codes.equals("john"))
		{
			idleFrog = "../GamePics/john.png";
			movingFrog = "../GamePics/john.png";
			sideFrog = "../GamePics/john.png";
			movingSideFrog = "../GamePics/john.png";
			repaint();
		}
		//makes frog immune to collisions when "godfrog" is entered
		else if(codes.equals("godfrog"))
		{
			godMode = true;
		}
	}
	//used to set the frog back to default
	public void resetFrogPic()
	{
		idleFrog = "../GamePics/frogFrame1.png";
		movingFrog = "../GamePics/frogFrame2.png";
		sideFrog = "../GamePics/frogFrameSide1.png";
		movingSideFrog = "../GamePics/frogFrame2Side.png";
	}
		
	//plays the bounce sound
	public void playBounce()
	{
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(new File("../Music/Bounce.mid")));
			bounceSeq.setSequence(in);
			bounceSeq.start();
		}
		catch(IOException | InvalidMidiDataException e)
		{
			e.printStackTrace();
		}
	}
	//plays the death sound
	public void playDeath()
	{
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(new File("../Music/Death.mid")));
			deathSeq.setSequence(in);
			deathSeq.start();
		}
		catch(IOException | InvalidMidiDataException e)
		{
			e.printStackTrace();
		}
	}
	//plays the victory sound
	public void playVictory()
	{
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(new File("../Music/Victory.mid")));
			victorySeq.setSequence(in);
			victorySeq.start();
		}
		catch(IOException | InvalidMidiDataException e)
		{
			e.printStackTrace();
		}
	}
	//position based accessor methods
	public int getXPos()
	{
		return xPos;
	}
	public int getYPos()
	{
		return yPos;
	}
	public void setFrogPos(int x, int y)
	{
		xPos = x;
		yPos = y;
		lastMove = 0;
		frame = 0;
	}
	
	//used to set the direction that the frog is facing
	public void setFrame(int f)
	{
		frame = f;
	}

	//used to set if the frog is alive
	public void setAlive(boolean b)
	{
		isAlive = b;
	}
	
	//used to set x speed of the frog
	public void setXSpeed(int s)
	{
		xSpeed = s;
	}
	//used to increment the player's score
	public void addToScore(int as)
	{
		score = score + as;
	}
	//used to hard set the player's score
	public void setScore(int s)
	{
		score = s;
	}
	//gets the player's score
	public int getScore()
	{
		return score;
	}
	//reset's the max height that the frog has reached
	public void resetMaxHeight()
	{
		maxHeight = 600;
	}
	//gets the image that the frog is currently set to
	public String getCurIdlePic()
	{
		return idleFrog;
	}
	//resets the godmode status of the frog
	public void resetFrog()
	{
		godMode = false;
	}
	//checks to see if the frog is in godmode
	public boolean isGod()
	{
		return godMode;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {	
	}
	@Override
	public void keyTyped(KeyEvent e) {	
	}
}
