import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * Class that sets up the view for the server
 * @author Nick
 *
 */
public class ServerView extends JFrame {
	ImageIcon backgroundImage;
	CardLayout cardLayout = new CardLayout();
	ServerGamePanel gamePanel;
	DODServerGUI controller;
	
	/**
	 * Constructor. Adds components to the specified card in card layout
	 * @param portNumber: default portNumber
	 * @param controller: controller to process commands
	 */
	public ServerView(String portNumber, DODServerGUI controller){
		super("Dungeon of Doom Server");
		this.controller = controller;
		gamePanel = new ServerGamePanel(controller);
		
		this.setLayout(cardLayout);
		this.add(new ServerStartPanel(portNumber, controller), "1"); 
		add(gamePanel, "2");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/**
	 * pane showing that connection was successful
	 */
	public void successfulOptionPane(){
		JOptionPane.showMessageDialog(this, "Connection successful!", "Connection status",  JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * pane showing that connection was successful
	 */
	public void unableToConnectPane(){
		JOptionPane.showMessageDialog(this, "Unable to create socket on specifiec port.", "Connection status",  JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Shows that someone has won
	 */
	public void winningMessage(){
		JOptionPane.showMessageDialog(this, "Someone has escaped the dungeon.", "Game over",  JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Switches to the specified panel
	 * @param panel
	 */
	public void switchPane(String panel){
		cardLayout.show(this.getContentPane(), panel);
	}
	/**
	 * Sets up the view at the beginning of the game
	 * @param mapWidth
	 * @param mapHeight
	 * @param portNumber
	 */
	public void setupView(int mapWidth, int mapHeight, int portNumber){
		gamePanel.setupView(mapWidth, mapHeight, portNumber);
	}
	/**
	 * Updates the view constantly when anyone moves
	 * @param map: new map with clients locations
	 */
	public void updateView(char[][] map){
		gamePanel.updateView(map);
	}
}
