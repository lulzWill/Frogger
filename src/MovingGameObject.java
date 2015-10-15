import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class MovingGameObject extends JPanel implements Runnable{
	private final int xSpeed;
	private final int width;
	private final int height;
	private int xPos;
	private int yPos;
	private boolean face;
	private BufferedImage image;
	private boolean interactable = true;
	private int rpDelay;
	//constructor for stationary object. This is used for life icons
	public MovingGameObject(int x, int y, String img, int w, int h, int rp)
	{
		rpDelay = rp;
		face = true;
		xSpeed = 0;
		xPos = x;
		yPos = y;
		setOpaque(false);
		try
		{
			image = ImageIO.read(new File(img));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		width = w;
		height = h;
	}
	
	//constructor for moving object
	public MovingGameObject(int x, int y, int xs, String img, boolean f, int rp)
	{
		rpDelay = rp;
		face = f;
		xSpeed = xs;
		xPos = x;
		yPos = y;
		setOpaque(false);
		try
		{
			image = ImageIO.read(new File(img));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		width = image.getWidth() * 2;
		height = image.getHeight() * 2;
		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//paints the image based on what direction it is facing
		if(face)
		{
			g.drawImage(image, 0, 5, width, height, this);
		}
		else
		{
			g.drawImage(image, width, 5, -width, height, this);
		}
	}
	@Override
	public void run() {
		while(isVisible())
		{
			//delay based on the set repaint delay
			try {
				Thread.sleep(rpDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//move and repaint the object
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					moveObject();
					repaint();
				}
			});
		}
	}
	
	public void moveObject()
	{
		//logic to move the object
		if(xPos > -width && face)
		{
			xPos = xPos - xSpeed;
		}
		else if(face)
		{
			xPos = 600 + width;
		}
		else if(xPos < 600 + width && !face)
		{
			xPos = xPos + xSpeed;
		}
		else
		{
			xPos = 0 - width;
		}
		setSize(getPreferredSize());
		setLocation(xPos, yPos);
	}
	
	//accessor methods below this point
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getXPos()
	{
		return xPos;
	}
	public int getYPos()
	{
		return yPos;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(108, 40);
	}
	
	public void setPic(String s)
	{
		try
		{
			image = ImageIO.read(new File(s));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int getXSpeed()
	{
		return xSpeed;
	}
	
	public boolean getFace()
	{
		return face;
	}
	
	public boolean isInteractable()
	{
		return interactable;
	}
}
