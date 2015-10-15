import javax.swing.SwingUtilities;


public class DissapearingGameObject extends MovingGameObject{
	//local vars
	private int dissapearTimer = 0;
	private boolean interactable;
	private String initImage;
	private int dissapearDelay;
	private int rpDelay;
	
	//constructor that takes x position, y position, x speed, image name, face, and delay time 
	public DissapearingGameObject(int x, int y, int xs, String img, boolean f, int dT, int rp)
	{
		super(x,y,xs,img,f,rp);
		rpDelay = rp;
		dissapearDelay = dT;
		initImage = img;
	}
	
	@Override
	public void run()
	{
		while(isVisible())
		{
			try {
				Thread.sleep(rpDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() {
					if(dissapearTimer == dissapearDelay)
					{
						shadow();
					}
					else if(dissapearTimer == dissapearDelay + 2)
					{
						submerge();
					}
					else if(dissapearTimer == dissapearDelay + 6)
					{
						shadow();
					}
					else if(dissapearTimer == dissapearDelay + 8)
					{
						reappear();
						dissapearTimer = 0;
					}
					moveObject();
					repaint();
				}
				
			});
			dissapearTimer = dissapearTimer + 1;
		}
	}
	
	//allows the object to submerge
	public void submerge()
	{
		interactable = false;
		setPic("GamePics/empty.png");
		repaint();
	}
	//shows shadow before submerging
	public void shadow()
	{
		interactable = true;
		setPic("GamePics/turtleShadow.png");
		repaint();
	}
	//allows object to reappear
	public void reappear()
	{
		interactable = true;
		setPic(initImage);
		repaint();
	}
	
	@Override
	public boolean isInteractable()
	{
		return interactable;
	}
}
