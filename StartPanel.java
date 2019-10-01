import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Panel showing the start screen
 * @author Nick
 *
 */
public class StartPanel extends JPanel {
	Image backgroundImage;
	String hostName;
	int portNumber;
	Client controller;
	
	/**
	 * Constructor. 
	 * @param hostName: default hostname
	 * @param portNumber: default portnumber
	 * @param controller: controller that will determine what to do with commands from view
	 * @param backgroundLocation: where the background image is stored
	 */
	public StartPanel(String hostName, int portNumber, Client controller, String backgroundLocation){
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.controller = controller;
		
		backgroundImage(backgroundLocation);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		this.add(new InputPanel(), gbc);
		setVisible(true);
	}
	//adds the background image
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
			g.drawImage(backgroundImage,0,0,this);
	}
	/**
	 * Resizes the background image to fit
	 * @param imageLocation
	 */
	private void backgroundImage(String imageLocation){
		try{
			BufferedImage background = ImageIO.read(new File(imageLocation));
			backgroundImage = background.getScaledInstance(-1, 1000, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image...");
			System.exit(1);
		}
		
	}
	
	/**
	 * Class that contains the panel to input portnumber, hostname and whether to quit/start
	 * @author Nick
	 *
	 */
	private class InputPanel extends JPanel{
		JButton startButton;
		JButton quitButton;
		JTextField IPAddressField;
		JTextField portNumberField;
		
		/**
		 * Constructor sets up panel
		 */
		private InputPanel(){
			this.setLayout(new GridLayout(4, 1, 0, 10));
			this.initPanel();
			this.setOpaque(false);
			this.setVisible(true);
		}
		
		/**
		 * Adds elements to the panel
		 */
		public void initPanel(){
			this.setupIPTextField();
			this.setupPortTextField();
			this.setupStartButton();
			this.setupQuitButton();
		}
		
		/**
		 * Adds the start button with the specified dimensions 
		 */
		public void setupStartButton(){
			startButton = new JButton("START");
			startButton.setPreferredSize(new Dimension(150, 40));
			this.add(startButton);
			
			startButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					controller.connectToServer(getIPTextField(), getPortTextField());
				}	
			});
		}
		
		/**
		 * Adds the quit button with the specified dimensions 
		 */
		public void setupQuitButton(){
			quitButton = new JButton("QUIT");
			quitButton.setMinimumSize(new Dimension(150, 40));
			this.add(quitButton);
			
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					controller.closeResources();
					controller.quit();
				}	
			});
		}
		
		
		/**
		 * sets up the text field for inputting ip address
		 */
		public void setupIPTextField(){
			IPAddressField = new JTextField(hostName);
			IPAddressField.setPreferredSize(new Dimension(150, 40));
			this.add(IPAddressField);
		}
		
		/**
		 * sets up the port number for taking the port number
		 */
		public void setupPortTextField(){
			portNumberField = new JTextField(Integer.toString(portNumber));
			portNumberField.setPreferredSize(new Dimension(150, 40));
			this.add(portNumberField);
		}
		
		/**
		 * gets the contents from the ip text field
		 * @return contents of ip text field
		 */
		private String getIPTextField(){
			return IPAddressField.getText();
		}
		
		/**
		 * gets the contents from the portnumber text field
		 * @return contents of portNumber text field
		 */ 
		private int getPortTextField(){
			return Integer.parseInt(portNumberField.getText());
		}
		
	}
}
