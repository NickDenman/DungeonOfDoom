import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * JPanel that contains the list of bots, the view for each bot and its respective command panel
 * @author Nick
 *
 */
public class BotGamePanel extends JPanel {
	CardLayout cardLayout = new CardLayout();
	BotListPanel buttonPanel;
	Image backgroundImage;
	BotClientGUI controller;
	HashMap<Integer, BotPanel> botPanelMap = new HashMap<Integer, BotPanel>();
	JPanel botPanel = new JPanel();
	
	/**
	 * Constructor. Sets up the initial size, layout and opacity for the panel
	 * @param controller: where all instructions will be sent to
	 */
	public BotGamePanel(BotClientGUI controller){
		this.controller = controller;
		backgroundImage();
		botPanel.setLayout(cardLayout);
		botPanel.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1254, 990));
		this.add(fillerPanel(), BorderLayout.NORTH);
		
		buttonPanel = new BotListPanel(controller);
		this.add(botPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates a new buttonPanel which allows user to change the bot currently seen in the GUI
	 */
	public void newButtonPanel(){
		this.remove(buttonPanel);
		buttonPanel = new BotListPanel(controller);
		this.add(buttonPanel, BorderLayout.WEST);
	}
	 /**
	  * A panel that spaces the components out so the title is visible
	  * @return
	  */
	private JPanel fillerPanel(){
		JPanel fillerPanel = new JPanel();
		fillerPanel.setPreferredSize(new Dimension(250, 250));
		fillerPanel.setOpaque(false);
		
		return fillerPanel;
	}
/**
 * Adds a new bot to the list of bots and changes the view to that of the new bot
 * @param botNumber
 */
	public synchronized void newBot(int botNumber){
		buttonPanel.addButton(botNumber);
		botPanelMap.put(botNumber, new BotPanel(controller, botNumber));
		botPanel.add(botPanelMap.get(botNumber), Integer.toString(botNumber));
		switchPane(botNumber);
	}
	
	/**
	 * Switches the bot currently being seen
	 * @param botNumber
	 */
	public void switchPane(int botNumber){
		cardLayout.show(botPanel, Integer.toString(botNumber));
	}
	
	/**
	 * Updates the goldpanel for a specific bot when gold has been collected
	 * @param botNumber: the bot whose gold panel that will be updated
	 * @param required: gold required to exit
	 * @param collected: gold that the bot has collected
	 */
	public void updateGoldPanel(int botNumber, int required, int collected){
		botPanelMap.get(botNumber).updateGold(required, collected);
	}
	 /**
	  * Overriden method that changes the background image
	  */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage,0,0,null);
	}
	
	/**
	 * Sets up the background image to a particular size
	 */
	private void backgroundImage(){
		try{
			BufferedImage background = ImageIO.read(new File("bot_main.jpg"));
			backgroundImage = background.getScaledInstance(-1, 1000, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.exit(1);
		}
		
	}
	/**
	 * Updats the view of the respective bot
	 * @param map: the new map that will be seen
	 * @param botNumber: the bot whose map will be updated
	 */
	public void updateView(char[][] map, int botNumber){
		botPanelMap.get(botNumber).updateView(map);
	}
}
