import java.awt.Color;
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
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel where the user can input portNumber and start/quit game
 * @author Nick
 *
 */
public class ServerStartPanel extends JPanel{
	String portNumber;
	DODServerGUI controller;
	
	public ServerStartPanel(String portNumber, DODServerGUI controller){
		this.portNumber = portNumber;
		this.controller = controller;
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		add(new InputPanel(), gbc);
	}
	
	/**
	 * Adds background image
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
			g.drawImage(resizeImage("server_start.jpg", 1000),0,0,this);
	}
	
	/**
	 * Resizes image to the specified size
	 * @param imageLocation: location of image in files
	 * @param height, height of image
	 * @return
	 */
	private Image resizeImage(String imageLocation, int height){
		try{
			BufferedImage background = ImageIO.read(new File(imageLocation));
			Image backgroundImage = background.getScaledInstance(-1, height, Image.SCALE_SMOOTH);
			return backgroundImage;
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.exit(1);
			return null;
		}
		
		
	}
	
	/**
	 * Panel where user inputs portNumber and can start/quit
	 * @author Nick
	 *
	 */
	private class InputPanel extends JPanel{
		JTextField portNumberField;
		
		private InputPanel(){
			this.setLayout(new GridLayout(4, 1, 0, 10));
			this.initPanel();
			this.setOpaque(false);
		}
		
		/**
		 * Adds components to the panel
		 */
		private void initPanel(){
			add(IPTextField());
			add(portTextField());
			add(startButton());
			add(quitButton());
		}
		
		/**
		 * Panel containing the IP text field
		 * @return
		 */
		private JPanel IPTextField(){
			JPanel labelPanel = new JPanel();
			labelPanel.setBackground(Color.WHITE);
			try {
				labelPanel.add(new JLabel(InetAddress.getLocalHost().getHostAddress()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
				labelPanel.add(new JLabel("IP Unknown."));
			}
		
			return labelPanel;
		}
		
		/**
		 * Text Field where port Number can be changed
		 * 		 * @return
		 */
		public JTextField portTextField(){
			portNumberField = new JTextField(portNumber);
			
			return portNumberField;
		}
		
		/**
		 * Button which starts the game
		 * @return
		 */
		public JButton startButton(){
			JButton startButton = new JButton("START");
			
			startButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					controller.newGame(getPortTextField());
				}	
			});
			
			return startButton;
		}
		
		/**
		 * Button which quits the game
		 * @return
		 */
		public JButton quitButton(){
			JButton quitButton = new JButton("QUIT");
			quitButton.setMinimumSize(new Dimension(300, 40));
			
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					controller.quit();
				}	
			});
			
			return quitButton;
		}
		
		/**
		 * Helper method to get the ip address
		 * @return
		 */
		private int getPortTextField(){
			return Integer.parseInt(portNumberField.getText());
		}
	}	
}
