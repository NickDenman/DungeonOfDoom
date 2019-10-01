import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
/**
 * The panel which will show each individual bots map and control panel
 * @author Nick
 *
 */


public class BotPanel extends JPanel {
	ViewPanel viewPanel;
	BotClientGUI controller = null;
	BotControlPanel botControlPanel;
	int botNumber;
	
	/**
	 * Constructor. Sets up the layout and adds the components to the panel
	 * @param controller
	 * @param botNumber
	 */
	public BotPanel(BotClientGUI controller, int botNumber){
		this.botNumber = botNumber;
		this.setBackground(Color.ORANGE);
		this.controller = controller;
		//this.setPreferredSize(new Dimension(1274, 990));
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
		viewPanel = new ViewPanel();
		botControlPanel = new BotControlPanel(controller, botNumber);
		
		//this.add(fillerPanel(), BorderLayout.NORTH);
		this.add(viewPanel, BorderLayout.CENTER);
		this.add(botControlPanel, BorderLayout.WEST);
	}
	
	/**
	 * Updates the gold of the specific bot
	 * @param required
	 * @param collected
	 */
	public void updateGold(int required, int collected){
		botControlPanel.updateCoinPanel(required, collected);
	}
	/**
	 * Updates the view of this bot
	 * @param map
	 */
	public void updateView(char[][] map){
		viewPanel.updateLook(map);
	}
}
