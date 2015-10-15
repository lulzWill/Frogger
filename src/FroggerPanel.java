import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class FroggerPanel extends JPanel implements Runnable{
	//initialize local vars
	private MovingGameObject[] trucks = new MovingGameObject[18];
	private MovingGameObject[] waterObjects = new MovingGameObject[17];
	private MovingGameObject[] lives = new MovingGameObject[3];
	private ExecutorService ex = Executors.newCachedThreadPool();
	private static final boolean LEFT = true;
	private static final boolean RIGHT = false;
	private Frog player;
	private int lifeCount;
	private int logIndex;
	private boolean playing;
	private BufferedImage img;
	private int rpDelay;
	private boolean[] cubbiesDone = new boolean[5];
	
	//default constructor with no inputs
	public FroggerPanel()
	{	
		//set local vars
		playing = true;
		rpDelay = 100;
		lifeCount = 3;
		for(int i = 0; i < 5; i++)
		{
			cubbiesDone[i] = false;
		}
		
		//Populate Road Row 1
		trucks[3] = new MovingGameObject(600, 550, 10, "GamePics/betterTruck.png", RIGHT, rpDelay);
		ex.execute(trucks[3]);
		add(trucks[3]);
		trucks[4] = new MovingGameObject(328, 550, 10, "GamePics/betterTruck.png", RIGHT, rpDelay);
		ex.execute(trucks[4]);
		add(trucks[4]);
		trucks[5] = new MovingGameObject(56, 550, 10, "GamePics/betterTruck.png", RIGHT, rpDelay);
		ex.execute(trucks[5]);
		add(trucks[5]);
		//Populate Road Row 2
		trucks[8] = new MovingGameObject(600, 500, 20, "GamePics/lambo.png", RIGHT, rpDelay);
		ex.execute(trucks[8]);
		add(trucks[8]);
		trucks[9] = new MovingGameObject(328, 500, 20, "GamePics/lambo.png", RIGHT, rpDelay);
		ex.execute(trucks[9]);
		add(trucks[9]);
		trucks[10] = new MovingGameObject(150, 500, 20, "GamePics/lambo.png", RIGHT, rpDelay);
		ex.execute(trucks[10]);
		add(trucks[10]);
		//PopulateRoad Row 3
		trucks[6] = new MovingGameObject(428, 450, 20, "GamePics/betterTruck.png", RIGHT, rpDelay);
		ex.execute(trucks[6]);
		add(trucks[6]);
		trucks[7] = new MovingGameObject(56, 450, 20, "GamePics/betterTruck.png", RIGHT, rpDelay);
		ex.execute(trucks[7]);
		add(trucks[7]);
		//Populate Road Row 4
		trucks[11] = new MovingGameObject(0, 400, 15, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[11]);
		add(trucks[11]);
		trucks[12] = new MovingGameObject(175, 400, 15, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[12]);
		add(trucks[12]);
		trucks[13] = new MovingGameObject(350, 400, 15, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[13]);
		add(trucks[13]);
		trucks[14] = new MovingGameObject(525, 400, 15, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[14]);
		add(trucks[14]);
		//Populate Road Row 5
		trucks[15] = new MovingGameObject(428, 350, 17, "GamePics/betterTruck.png", LEFT, rpDelay);
		ex.execute(trucks[15]);
		add(trucks[15]);
		trucks[16] = new MovingGameObject(56, 350, 17, "GamePics/betterTruck.png", LEFT, rpDelay);
		ex.execute(trucks[16]);
		add(trucks[16]);
		trucks[17] = new MovingGameObject(56, 350, 17, "GamePics/betterTruck.png", LEFT, rpDelay);
		ex.execute(trucks[17]);
		add(trucks[17]);
		//Populate Road Row 6
		trucks[0] = new MovingGameObject(600, 300, 20, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[0]);
		add(trucks[0]);
		trucks[1] = new MovingGameObject(328, 300, 20, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[1]);
		add(trucks[1]);
		trucks[2] = new MovingGameObject(56, 300, 20, "GamePics/lambo.png", LEFT, rpDelay);
		ex.execute(trucks[2]);
		add(trucks[2]);
		
		//create player
		player = new Frog(300, 600, 5);
		ex.execute(player);
		add(player);
			
		//create logs
		waterObjects[0] = new MovingGameObject(56, 200, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[0]);
		add(waterObjects[0]);
		waterObjects[1] = new MovingGameObject(306, 200, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[1]);
		add(waterObjects[1]);
		waterObjects[2] = new MovingGameObject(556, 200, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[2]);
		add(waterObjects[2]);
		waterObjects[3] = new MovingGameObject(140, 100, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[3]);
		add(waterObjects[3]);
		waterObjects[4] = new MovingGameObject(56, 100, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[4]);
		add(waterObjects[4]);
		waterObjects[5] = new MovingGameObject(440, 100, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[5]);
		add(waterObjects[5]);
		waterObjects[6] = new MovingGameObject(356, 100, 10, "GamePics/log.png", RIGHT, rpDelay);
		ex.execute(waterObjects[6]);
		add(waterObjects[6]);
		
		//create turtles
		waterObjects[7] = new DissapearingGameObject(356, 150, 10, "GamePics/turtle.png", LEFT, 20, rpDelay);
		ex.execute(waterObjects[7]);
		add(waterObjects[7]);
		waterObjects[8] = new DissapearingGameObject(406, 150, 10, "GamePics/turtle.png", LEFT, 20, rpDelay);
		ex.execute(waterObjects[8]);
		add(waterObjects[8]);
		waterObjects[9] = new DissapearingGameObject(56, 150, 10, "GamePics/turtle.png", LEFT, 20, rpDelay);
		ex.execute(waterObjects[9]);
		add(waterObjects[9]);
		waterObjects[10] = new DissapearingGameObject(106, 150, 10, "GamePics/turtle.png", LEFT, 20, rpDelay);
		ex.execute(waterObjects[10]);
		add(waterObjects[10]);
		waterObjects[11] = new DissapearingGameObject(156, 150, 10, "GamePics/turtle.png", LEFT, 20, rpDelay);
		ex.execute(waterObjects[11]);
		add(waterObjects[11]);
		waterObjects[12] = new DissapearingGameObject(356, 50, 10, "GamePics/turtle.png", LEFT, 40, rpDelay);
		ex.execute(waterObjects[12]);
		add(waterObjects[12]);
		waterObjects[13] = new DissapearingGameObject(86, 50, 10, "GamePics/turtle.png", LEFT, 40, rpDelay);
		ex.execute(waterObjects[13]);
		add(waterObjects[13]);
		waterObjects[14] = new DissapearingGameObject(136, 50, 10, "GamePics/turtle.png", LEFT, 40, rpDelay);
		ex.execute(waterObjects[14]);
		add(waterObjects[14]);
		waterObjects[15] = new DissapearingGameObject(556, 50, 10, "GamePics/turtle.png", LEFT, 40, rpDelay);
		ex.execute(waterObjects[15]);
		add(waterObjects[15]);
		waterObjects[16] = new DissapearingGameObject(606, 50, 10, "GamePics/turtle.png", LEFT, 40, rpDelay);
		ex.execute(waterObjects[16]);
		add(waterObjects[16]);
		
		//create life icons
		for(int i = 1; i < 4; i++)
		{
			lives[i - 1] = new MovingGameObject(50*i, 650, "GamePics/frogFrame1.png", 40, 40, rpDelay);
			ex.execute(lives[i - 1]);
			add(lives[i - 1]);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		
		//set up background textures
		setImage("GamePics/grassBar.png");
		g2D.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), this);
		g2D.drawImage(img, 0, 250, img.getWidth(), img.getHeight(), this);
		g2D.drawImage(img, 0, 600, img.getWidth(), img.getHeight(), this);
		setImage("GamePics/waterBlock.png");
		g2D.drawImage(img, 0, 50, img.getWidth(), img.getHeight(), this);
		g2D.setColor(Color.black);
		g2D.fillRect(0,  650, 600, 50);
		g2D.setColor(Color.white);
		g2D.drawString("Lives", 0, 675);
		g2D.drawString("Score: " + player.getScore(), 400, 675);
		
		//draw road lines
		setImage("GamePics/roadTile.png");
		for(int i = 6; i < 12; i++)
		{
			g2D.drawImage(img, 0, 50*i, img.getWidth(), img.getHeight(), this);
			if(i != 6)
			{
				for(int j = 0; j < 10; j++)
				{
					if(i == 9)
					{
						g2D.setColor(Color.yellow);
						g2D.fillRect(0, 50*i, 600, 5);
					}
					else
					{
						g2D.setColor(Color.white);
						g2D.fillRect(60*j, 50*i, 40, 5);
					}
				}
			}
		}
		//set used cubby images
		g2D.setColor(Color.black);
		for(int i = 0; i < 5; i++)
		{
			setImage("GamePics/marsh.png");
			g2D.drawImage(img, 40 + (i * 110), 10, 80, 40, this);
			if(cubbiesDone[i])
			{
				setImage(player.getCurIdlePic());
				g2D.drawImage(img, 60 + (i * 110), 10, 40, 40, this);
			}
		}
	}
	@Override
	public void run() {
		//request focus
		player.requestFocusInWindow();
		while(playing)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(checkForCollisions())
			{
				if(lifeCount > 0)
				{
					lifeCount = lifeCount - 1;
				}
				
				player.setAlive(false);
				player.playDeath();
				lives[lifeCount].setPic("GamePics/splatSmall.png");
				player.repaint();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(lifeCount > 0)
				{
					player.setAlive(true);
					player.setFrogPos(300, 600);
					player.repaint();
				}
				else
				{
					playing = false;
				}
			}
			else if(atEnd())
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				player.setFrogPos(300, 600);
				repaint();
				player.addToScore(100);
				player.resetMaxHeight();
				if(checkWin())
				{
					for(int i = 0; i < 5; i++)
					{
						cubbiesDone[i] = false;
					}
					player.addToScore(1000);
					player.playVictory();
				}
				
			}
			if(onWater() && !player.isGod())
			{
				if(!waterObjects[logIndex].getFace())
				{
					player.setXSpeed(waterObjects[logIndex].getXSpeed()/10);
				}
				else
				{
					player.setXSpeed(-waterObjects[logIndex].getXSpeed()/10);
				}
			}
			else
			{
				player.setXSpeed(0);
			}
		}
		
	}
	
	private boolean checkForCollisions()
	{
		if(onRoad() && !player.isGod())
		{
			for(int i = 0; i < trucks.length; i++)
			{
				if((trucks[i].getXPos() + trucks[i].getWidth() >= player.getXPos() + 5 && trucks[i].getXPos() <= player.getXPos() + 40) && trucks[i].getYPos() == player.getYPos())
				{
					return true;
				}
			}
		}
		else if(onWater() && !player.isGod())
		{
			boolean onLog = false;
			for(int i = 0; i < waterObjects.length; i++)
			{
				if((waterObjects[i].getXPos() + waterObjects[i].getWidth() >= player.getXPos() + 5 && waterObjects[i].getXPos() <= player.getXPos() + player.getWidth()/2) && (player.getYPos() >= waterObjects[i].getYPos() && player.getYPos() <= waterObjects[i].getYPos() + 45))
				{
					if(waterObjects[i].isInteractable())
					{
						logIndex = i;
						onLog = true;
					}
				}
			}
			if(!onLog)
			{
				return true;
			}
		}
		else if(atEnd())
		{
			if(!cubbiesDone[0] && (player.getXPos() >= 40 && player.getXPos() + player.getWidth() <= 120))
			{
				cubbiesDone[0] = true;
			}
			else if(!cubbiesDone[1] && (player.getXPos() >= 150 && player.getXPos() + player.getWidth() <= 230))
			{
				cubbiesDone[1] = true;
			}
			else if(!cubbiesDone[2] && (player.getXPos() >= 260 && player.getXPos() + player.getWidth() <= 340))
			{
				cubbiesDone[2] = true;
			}
			else if(!cubbiesDone[3] && (player.getXPos() >= 370 && player.getXPos() + player.getWidth() <= 450))
			{
				cubbiesDone[3] = true;
			}
			else if(!cubbiesDone[4] && (player.getXPos() >= 480 && player.getXPos() + player.getWidth() <= 560))
			{
				cubbiesDone[4] = true;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean onWater()
	{
		if(player.getYPos() >= 50 && player.getYPos() < 250)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean atEnd()
	{
		if(player.getYPos() >= 0 && player.getYPos() < 50)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean onRoad()
	{
		if(player.getYPos() >= 300 && player.getYPos() <= 600)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkWin()
	{
		for(int i = 0; i < 5; i++)
		{
			if(!cubbiesDone[i])
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
	public void setImage(String s)
	{
		try
		{
			img = ImageIO.read(new File(s));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void resetGameState()
	{
		rpDelay = 100;
		lifeCount = 3;
		player.setScore(0);
		player.resetMaxHeight();
		player.resetFrogPic();
		player.resetFrog();
		playing = true;
		player.setAlive(true);
		player.setFrogPos(300, 600);
		player.repaint();
		for(int i = 0; i < 3; i++)
		{
			lives[i].setPic("GamePics/frogFrame1.png");
		}
		for(int i = 0; i < 5; i++)
		{
			cubbiesDone[i] = false;
		}
	}
	
	public int getScore()
	{
		return player.getScore();
	}
}
