import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 * Panel that contains the control panel and the view panel for the cllient
 * @author Nick
 *
 */
public class GamePanel extends JPanel{
	
	Image backgroundImage;
	HumanClientGUI controller;
	ControlPanel controlPanel;
	ViewPanel viewPanel;
	
	/**
	 * Constructor that sets up the panel and adds components
	 * @param controller
	 */
	public GamePanel(HumanClientGUI controller){
		this.controller = controller;
		backgroundImage();
		this.setLayout(new BorderLayout());
		this.add(fillerPanel(), BorderLayout.NORTH);
		
		controlPanel = new ControlPanel(controller);
		viewPanel = new ViewPanel();
		
		this.add(viewPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.WEST);
	}
	
	/**
	 * Panel that is added so title in backing image can be seen
	 * @return
	 */
	private JPanel fillerPanel(){
		JPanel fillerPanel = new JPanel();
		fillerPanel.setPreferredSize(new Dimension(250, 250));
		fillerPanel.setBackground(Color.BLUE);
		fillerPanel.setOpaque(false);
		
		return fillerPanel;
	}
	
	/**
	 * Upadates the clients coin panel when gold has been collected
	 * @param goldRequired: gold required to win the game
	 * @param goldCollected: gold currently collected
	 */
	public void updateCoinPanel(int goldRequired, int goldCollected){
		controlPanel.updateCoinPanel(goldRequired, goldCollected);
	}
	/**
	 * Updates the view to the new map
	 * @param map: map to be shown in the view
	 */
	public void updateView(char[][] map){
		viewPanel.updateLook(map);
	}
	
	/**
	 * Adds the background image
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage,0,0,null);
	}
	
	/**
	 * Updates the size of the background image to fit the panel
	 */
	private void backgroundImage(){
		try{
			BufferedImage background = ImageIO.read(new File("client_main.jpg"));
			backgroundImage = background.getScaledInstance(-1, 1000, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.out.println("Game");
			System.exit(1);
		}
		
	}
}
