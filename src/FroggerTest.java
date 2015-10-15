import javax.swing.JFrame;

//create an instance of FroggerFrame and run it.
public class FroggerTest {

	public static void main(String[] args) {
		FroggerFrame fT = new FroggerFrame();
		fT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fT.setSize(600,740);
		fT.setResizable(false);
		fT.setVisible(true);
		
		fT.startGame();

	}

}
