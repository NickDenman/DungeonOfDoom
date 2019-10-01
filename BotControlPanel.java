
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ChangeListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Contains the code allowing the user to add bots, change icon and speed
 *
 * @author: nd430
**/
public class BotControlPanel extends JPanel {
	int sliderValue;
	BotClientGUI controller = null;
	CoinPanel coinPanel;
	int botNumber;
	
	/**
	 * Constructor. Sets size, opacity and adds elements to the panel
	 * @param controller: controller that instructions will be sent to
	 * @param botNumber: the new number of the bot to be added
	 */
	public BotControlPanel(BotClientGUI controller, int botNumber){
		this.botNumber = botNumber;
		this.controller = controller;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(434, 200));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		coinPanel = new CoinPanel();
		this.add(new ButtonPanel());
		this.add(coinPanel);
		this.add(new SpeedPanel());
	}
	 /**
	  * Resizes image so it fits on the panel
	  * @param location: where the image is stored
	  * @param size: size of the new image
	  * @return the new resized image
	  */
	protected Image resizeImage(String location, int size){
		try{
			BufferedImage fullCharacterImage = ImageIO.read(new File(location));
			Image characterImage = fullCharacterImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
			return characterImage;
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.exit(1);
			return null;
		}
	}
	 /**
	  * Updates the coin panel when a bot collects a coin
	  * @param required: the gold required for the bot to win
	  * @param collected: the amount of gold the bot has collected
	  */
	public void updateCoinPanel(int required, int collected){
		coinPanel.updateCoinPanel(required, collected);
	}
	
	/**
	 * Class that extends JPanel containing all the buttons for the control panel
	 * @author nd430
	 *
	 */
	private class ButtonPanel extends JPanel{
		/**
		 * Constructor. Sets size, opacity and adds elements to the panel
		 */
		private ButtonPanel(){
			this.setPreferredSize(new Dimension(434, 125));
			this.setLayout(new GridLayout(1, 4, 10, 10));
			
			setBorder(new EmptyBorder(20, 10, 10, 10));
			setOpaque(false);
			//setBackground(Color.GREEN);
			
			this.add(newBotButton());
			this.add(quitButton());
			this.add(characterOne());
			this.add(characterTwo());
		}
		/**
		 * Creates a new Button that allows bots to be added to the game
		 * @return the button that will add new bots
		 */
		private JButton newBotButton(){
			JButton newBotButton = new JButton("NEW BOT");
			
			newBotButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("NEW BOT");
					controller.newBot();
				}
			});
			
			return newBotButton;
		}
		
		/**
		 * Creates a new Button that quits the game
		 * @return the button that will quit the game
		 */
		private JButton quitButton(){
			JButton quitButton = new JButton("QUIT");
			
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("QUIT");
					controller.closeResources();
					controller.quit();
				}
			});
			
			return quitButton;
		}
		
		/**
		 * Creates a new Button that allows bots symbol to be changed
		 * @return the button that will change bot symbol
		 */
		private JButton characterOne(){
			JButton characterOneButton = new JButton(new ImageIcon(resizeImage("Brown_thing.png", 96)));
			
			characterOneButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					controller.sendToServer(botNumber, 'B');
				}
			});
			
			return characterOneButton;
		}
		
		/**
		 * Creates a new Button that allows bots symbol to be changed
		 * @return the button that will change bot symbol
		 */
		private JButton characterTwo(){
			JButton characterOneButton = new JButton(new ImageIcon(resizeImage("koopa.png", 96)));
			
			characterOneButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					controller.sendToServer(botNumber, 'K');
				}
			});
			
			return characterOneButton;
		}
	}
	/**
	 * Class the extends JPanel and allows user to control speed
	 * @author nd430
	 *
	 */
	private class SpeedPanel extends JPanel{
		
		/**
		 * Constructor creates slider allowing user to change speed
		 */
		private SpeedPanel(){
			//this.setBackground(Color.BLUE);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(434, 224));
			this.setOpaque(false);
			final JSlider slider = new JSlider(JSlider.HORIZONTAL, 500, 5500, 3000);
			slider.setOpaque(false);
			slider.setMajorTickSpacing(1000);
			slider.setMinorTickSpacing(100);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent arg0) {
					sliderValue = (int)slider.getValue();
					changeSpeed();
				}
				
			});
			
			this.add(slider, BorderLayout.CENTER);
		}
		
		private void changeSpeed(){
			controller.changeBotSpeed(sliderValue, botNumber);
		}
	}
}
