import java.awt.Dimension;
import java.awt.FlowLayout;
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
 * Panel containing the buttons to allow user to send commands
 * @author Nick
 *
 */
public class ControlPanel extends JPanel{
	HumanClientGUI controller;
	CharacterPanel characterPanel;
	CoinPanel coinPanel;
	ButtonPanel buttonPanel;
	QuitPanel quitPanel;
	
	
	/**
	 * Constructor sets up the panel 
	 * @param controller: controller to send commands to
	 */
	public ControlPanel(HumanClientGUI controller){
		this.controller = controller;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(534, 200));
		setOpaque(false);
		
		characterPanel = new CharacterPanel();
		coinPanel = new CoinPanel();
		buttonPanel = new ButtonPanel();
		quitPanel = new QuitPanel();
		
		setupUI();
	}
	 /**
	  * Adds the buttons to the panel
	  */
	private void setupUI(){
		add(characterPanel);
		add(coinPanel);
		add(buttonPanel);
		add(quitPanel);
	}
	
	/**
	 * Resizes the image to the specified size to fit
	 * @param location: image location in files
	 * @param size: size of new image
	 * @return
	 */
	private Image resizeImage(String location, int size){
		try{
			BufferedImage fullCharacterImage = ImageIO.read(new File(location));
			Image characterImage = fullCharacterImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
			return characterImage;
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.out.println("CONTROL: "+ location);
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Updates panel to the new amount of gold collected
	 * @param goldRequired: gold Required to win
	 * @param goldCollected: gold collected
	 */
	public void updateCoinPanel(int goldRequired, int goldCollected){
		coinPanel.updateCoinPanel(goldRequired, goldCollected);
	}
	 
	/**
	 * Panel containing the possible choices of characters
	 * @author nd430
	 *
	 */
	private class CharacterPanel extends JPanel{
		private CharacterPanel(){
			setLayout(new GridLayout(1, 4, 10, 10));
			setPreferredSize(new Dimension(534, 141));
			setBorder(new EmptyBorder(20, 10, 10, 10));
			setOpaque(false);
			
			add(marioButton());
			add(luigiButton());
			add(toadBlueButton());
			add(toadYellowButton());
		}
		
		/**
		 * Button allowing user to change character to mario
		 * @return
		 */
		private JButton marioButton(){
			JButton marioButton = new JButton(new ImageIcon(resizeImage("Mario.jpg", 121)));
			
			marioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('M');
				}
			});
			
			return marioButton;
		}
		
		/**
		 * Button allowing user to change character to luigi
		 * @return
		 */
		private JButton luigiButton(){
			JButton marioButton = new JButton(new ImageIcon(resizeImage("Luigi.jpg", 121)));
			
			marioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('L');
				}
			});
			
			return marioButton;
		}
		
		/**
		 * Button allowing user to change character to blue toad
		 * @return
		 */
		private JButton toadBlueButton(){
			JButton marioButton = new JButton(new ImageIcon(resizeImage("Toad_Blue.jpg", 121)));
			
			marioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('T');
				}
			});
			
			return marioButton;
		}
		
		/**
		 * Button allowing user to change character to yellow toad
		 * @return
		 */
		private JButton toadYellowButton(){
			JButton marioButton = new JButton(new ImageIcon(resizeImage("Toad_Yellow.jpg", 121)));
			
			marioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('Y');
				}
			});
			
			return marioButton;
		}
	}
	
	/**
	 * Panel containing the button for the user to quit the game
	 * @author nd430
	 *
	 */
	private class QuitPanel extends JPanel{
		private QuitPanel(){
			setPreferredSize(new Dimension(534, 70));
			setBorder(new EmptyBorder(0,0,15,0));
			setLayout(new GridBagLayout());
			setOpaque(false);
			GridBagConstraints gbc = new GridBagConstraints();
			
			add(quitButton());
		}
		
		/**
		 * Creates the button that will quit the game when pressed
		 * @return
		 */
		private JButton quitButton(){
			JButton quitButton = new JButton("QUIT");
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					controller.sendToServer('Q');
					controller.closeResources();
					controller.quit();
				}
			});
			
			return quitButton;
		}	
	}
	
	/**
	 * Creates the button panel where user will choose which direction to move in
	 * @author Nick
	 *
	 */
	private class ButtonPanel extends JPanel{
		private ButtonPanel(){
			setPreferredSize(new Dimension(534, 270));
			setLayout(new GridBagLayout());
			setOpaque(false);
			
			setupUI();
		}
		
		/**
		 * Adds the specified buttons to their specified locations on the panel
		 */
		private void setupUI(){
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			add(northButton(), gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(westButton(), gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			add(pickupButton(), gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 1;
			add(eastButton(), gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 2;
			add(southButton(), gbc);
		}
		/**
		 * Button that allows the player to move North
		 * @return
		 */
		private JButton northButton(){
			JButton northButton = new JButton(new ImageIcon(resizeImage("North.png", 75)));
			northButton.setPreferredSize(new Dimension(75, 75));
			
			northButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('N');
				}
			});
			
			return northButton;
		}
		/**
		 * Button that allows the player to move WESt
		 * @return
		 */
		private JButton westButton(){
			JButton westButton = new JButton(new ImageIcon(resizeImage("West.png", 75)));
			westButton.setPreferredSize(new Dimension(75, 75));
			
			westButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('W');
				}
			});
			
			return westButton;
		}
		/**
		 * Button that allows the player to pickup
		 * @return
		 */
		private JButton pickupButton(){
			JButton pickupButton = new JButton(new ImageIcon(resizeImage("Coin.png", 75)));
			pickupButton.setPreferredSize(new Dimension(75, 75));
			
			pickupButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('P');
				}
			});
			
			return pickupButton;
		}
		
		/**
		 * Button that allows the player to move east
		 * @return
		 */
		private JButton eastButton(){
			JButton eastButton = new JButton(new ImageIcon(resizeImage("East.png", 75)));
			eastButton.setPreferredSize(new Dimension(75, 75));
			
			eastButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('E');
				}
			});
			
			return eastButton;
		}
		
		/**
		 * Button that allows the player to move south
		 * @return
		 */
		private JButton southButton(){
			JButton southButton = new JButton(new ImageIcon(resizeImage("South.png", 75)));
			southButton.setPreferredSize(new Dimension(75, 75));
			
			southButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					controller.sendToServer('S');	
				}
			});
			
			return southButton;
		}
	}
}
