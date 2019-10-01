import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Class that creates the main frame for the panels to be added to
 * @author Nick
 *
 */
public class ClientGUI extends MessagePanes{
	
	ImageIcon backgroundImage;
	HumanClientGUI controller;
	GamePanel gamePanel;
	
	/**
	 * Constructor that sets up the frame
	 * @param hostName: default hostname to be displayed
	 * @param portNumber: default portNumber to be displayed
	 * @param controller: where data will be processed
	 */
	public ClientGUI(String hostName, int portNumber, HumanClientGUI controller){
		frame = new JFrame("Dungeon of Doom");
		gamePanel = new GamePanel(controller);
		this.controller = controller;
		quitOnClose();
		frame.setLayout(cardLayout);
		frame.add(new StartPanel(hostName, portNumber, controller, "client_start.jpg"), "1");
		frame.add(gamePanel, "2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * Closes resources once frame has been closed
	 */
	private void quitOnClose(){
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				controller.closeResources();
				controller.quit();
			}
			
		});
	}
	
	/**
	 * updates the gold on the panel
	 * @param goldRequired: amount required to exit the dungeon
	 * @param goldCollected: amount of gold collected
	 */
	public void updateGold(int goldRequired, int goldCollected){
		gamePanel.updateCoinPanel(goldRequired, goldCollected); 
	}
	
	/**
	 * Updates the view to the new specified map
	 * @param map
	 */
	public void updateView(char[][] map){
		gamePanel.updateView(map);
	}
}
