import javax.swing.JFrame;

public class ServerTest {
	
	public static void main(String[] args) {
		
		//set up JFrame and create a server object
		JFrame app = new JFrame("High Score");
		Server application = new Server();

		app.add(application); 
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(300, 300);
		app.setVisible(true);

		//run the server methods
		application.runServer();
	}
}
