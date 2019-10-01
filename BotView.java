import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * The main view panel of the bot which contains the start panel and the game panel
 * @author Nick
 *
 */
public class BotView extends MessagePanes {
	BotGamePanel gamePanel;
	BotClientGUI controller;
	
	/**
	 * Constructor. Creates the frame and adds the main components
	 * @param hostName
	 * @param portNumber
	 * @param controller
	 */
	public BotView(String hostName, int portNumber, BotClientGUI controller){
		frame = new JFrame("Dungeon of Doom Bot");
		this.controller = controller;
		gamePanel = new BotGamePanel(controller);
		
		quitOnClose();
		frame.setLayout(cardLayout);
		frame.add(new StartPanel(hostName, portNumber, controller, "bot_start.jpg"), "1");
		frame.add(gamePanel, "2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * Closes all resources when the pane is closed
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
	 * Changes the view to the specified bot
	 * @param botNumber: unique id of the bot to be viewed
	 */
	public void changeBotView(int botNumber){
		gamePanel.switchPane(botNumber);
	}
	/**
	 * Adds a new bot to the game panel with the specific unique id
	 * @param botNumber: unique id of the new bot
	 */
	public void newBot(int botNumber){
		gamePanel.newBot(botNumber);
	}
	
	/**
	 * updates the gold panel of the specified bot
	 * @param botNumber: which bot's panel to be updated
	 * @param required: amount required to exit
	 * @param collected: amount collected
	 */
	public void updateGoldPanel(int botNumber, int required, int collected){
		gamePanel.updateGoldPanel(botNumber, required, collected);
	}
	
	/**
	 * updates the view of the specified bot to the new map
	 * @param map: new map to be shown
	 * @param botNumber: the bot view to be updated
	 */
	public void updateView(char[][] map, int botNumber){
		System.out.println();
		gamePanel.updateView(map, botNumber);
	}
	
	/**
	 * Adds a new button panel to the gamePanel view
	 */
	public void newButtonPanel(){
		gamePanel.newButtonPanel();
	}
}