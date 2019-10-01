import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Class that contains the view and the control panel for the human player
 * @author Nick
 *
 */
public class ServerGamePanel extends JPanel {
	DODServerGUI controller;
	GodsEyeView godsEyeView;
	Image backgroundImage;
	ButtonPanel buttonPanel;
	boolean blankView = false;
	
	/**
	 * Constructor that sets up the panel that contains the view and control panel
	 * @param controller
	 */
	public ServerGamePanel(DODServerGUI controller){
		this.controller = controller;
		backgroundImage();
		godsEyeView = new GodsEyeView();
		buttonPanel = new ButtonPanel();
		setLayout(new BorderLayout());
		this.add(fillerPanel(), BorderLayout.NORTH);
		setPreferredSize(new Dimension(1274, 990));
		setOpaque(false);
		add(godsEyeView, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	
	}
	
	/**
	 * Filler panel that allows the title in the background image to be seen
	 * @return
	 */
	private JPanel fillerPanel(){
		JPanel fillerPanel = new JPanel();
		fillerPanel.setPreferredSize(new Dimension(220, 220));
		fillerPanel.setOpaque(false);
		
		return fillerPanel;
	}
	
	/**
	 * Sets up the background image to the specified size
	 */
	private void backgroundImage(){
		try{
			BufferedImage background = ImageIO.read(new File("server_main.jpg"));
			backgroundImage = background.getScaledInstance(-1, 1000, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.exit(1);
		}
		
	}
	
	/**
	 * Adds background image to the panel
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage,0,0,null);
	}
	
	/**
	 * Updates the server view to the new map 
	 * @param map
	 */
	public void updateView(char[][] map){
		godsEyeView.updateView(map);
	}
	
	/**
	 * Sets up servers view
	 * @param mapWidth: width of the map needed for the view
	 * @param mapHeight: height of the map needed for the view
	 * @param portNumber: number that server will accept connections on
	 */
	public void setupView(int mapWidth, int mapHeight, int portNumber){
		godsEyeView.setupView(mapWidth, mapHeight);
		buttonPanel.setupView(portNumber);
	}
	
	/**
	 * Class that shows the full view of the map
	 * @author Nick
	 *
	 */
	private class GodsEyeView extends JPanel{
		int mapWidth;
		int mapHeight;
		int pictureSize;
		int panelWidth = 1274;
		int panelHeight = 680;
		GridBagConstraints gbc = new GridBagConstraints();
		ImageIcon wall;
		ImageIcon coin;
		ImageIcon bot;
		ImageIcon exit;
		ImageIcon ground;
		ImageIcon mario;
		ImageIcon luigi;
		ImageIcon toadBlue;
		ImageIcon toadYellow;
		ImageIcon mushroom;
		ImageIcon koopa;
		
		/**
		 * Constructor. Adds the images to the map
		 */
		private GodsEyeView(){
			setLayout(new GridBagLayout());
			setOpaque(false);
			setPreferredSize(new Dimension(panelWidth, panelHeight));
			gbc.insets = new Insets(1,1,0,0);
		}
		
		protected void setupView(int mapWidth, int mapHeight){
			this.mapWidth = mapWidth;
			this.mapHeight = mapHeight;
			calculateImageSize();
			
			koopa = resizeImage("koopa.png", pictureSize);
			wall = resizeImage("Wall.jpg", pictureSize);
			coin = resizeImage("CoinBackground.png", pictureSize);
			bot  = resizeImage("Brown_thing.png", pictureSize);
			exit  = resizeImage("Pipe.jpg", pictureSize);
			ground  = resizeImage("Ground.png", pictureSize);
			mario = resizeImage("Mario.jpg", pictureSize);
			luigi = resizeImage("Luigi.jpg", pictureSize);
			toadBlue = resizeImage("Toad_Blue.jpg", pictureSize);
			toadYellow = resizeImage("Toad_Yellow.jpg", pictureSize);
			mushroom = resizeImage("Mushroom.png", pictureSize);
		}
		
		/**
		 * Calculates the size of the images so they will fit on the map
		 */
		private void calculateImageSize(){
			int width = panelWidth-mapWidth;
			int height = panelHeight-mapHeight;
			
			pictureSize = (width)/mapWidth;
			
			if(pictureSize*mapHeight > height){
				pictureSize = (height)/mapHeight;
			}
			
		}
		
		/**
		 * Replaces all the panels with an image so the server user wont know what is going on
		 */
		private void blankView(){
			removeAll();
			
			for(int i=0; i<mapWidth; i++){
				for(int j=0; j<mapHeight; j++){
					gbc.gridx = i;
					gbc.gridy = j;
					add(new JLabel(mushroom), gbc);
				}
			}
			revalidate();
			repaint();
		}
		
		/**
		 * Updates the server view to the new map
		 * @param map
		 */
		public void updateView(char[][] map){
			if(!blankView){
				removeAll();
				for(int i=0; i<mapWidth; i++){
					for(int j=0; j<mapHeight; j++){
						gbc.gridx = i;
						gbc.gridy = j;
						switch(map[j][i]){
						case '#' :
							add(new JLabel(wall), gbc);
							break;
						case '.' :
							add(new JLabel(ground), gbc);
							break;
						case 'G' :
							add(new JLabel(coin), gbc);
							break;
						case 'E' :
							add(new JLabel(exit), gbc);
							break;
						case 'B' :
							add(new JLabel(bot), gbc);
							break;
						case 'M' :
							add(new JLabel(mario), gbc);
							break;
						case 'L' :
							add(new JLabel(luigi), gbc);
							break;
						case 'T' :
							add(new JLabel(toadBlue), gbc);
							break;
						case 'Y' :
							add(new JLabel(toadYellow), gbc);
							break;
						case 'K' :
							add(new JLabel(koopa), gbc);
							break;
						}
					}
				}
				revalidate();
				repaint();
			}
		}
	}
	
	/**
	 * Resizes the image so it will fit on the map
	 * @param location: image location in files
	 * @param size: new image size
	 * @return
	 */
	private ImageIcon resizeImage(String location, int size){
		try{
			BufferedImage fullCharacterImage = ImageIO.read(new File(location));
			Image characterImage = fullCharacterImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
			return new ImageIcon(characterImage);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.exit(1);
			return null;
		}
	}
	/**
	 * Panel containing buttons so server user can input commands
	 * @author Nick
	 *
	 */
	private class ButtonPanel extends JPanel{
		
		private ButtonPanel(){
			setPreferredSize(new Dimension(90,90));
			setOpaque(false);
			setLayout(new GridBagLayout());
			//this.setBorder(new EmptyBorder(20, 20, 20, 20));
		}
		
		/**
		 * Sets up the view by adding the buttons to specified locations
		 * @param portNumber
		 */
		private void setupView(int portNumber){
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,5,5,5);
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(IPPanel(), gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(portPanel(portNumber), gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridheight = 2;
			gbc.fill = GridBagConstraints.VERTICAL;
			add(blankButton(), gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridheight = 2;
			gbc.fill = GridBagConstraints.VERTICAL;
			add(quitButton(), gbc);
		}
		
		/**
		 * Button that blanks the view
		 * @return
		 */
		private JButton blankButton(){
			JButton blank = new JButton(resizeImage("Mushroom.png", 60));
			blank.setPreferredSize(new Dimension(60, 60));
			
			blank.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(blankView){
						blankView = false;
						controller.updateView();
					}
					else{
						blankView = true;
						godsEyeView.blankView();
					}
				}	
			});
			
			return blank;
		}
		
		/**
		 * Button that quits the game
		 * @return
		 */
		private JButton quitButton(){
			JButton quitButton = new JButton("QUIT");
			quitButton.setPreferredSize(new Dimension(65, 60));
			
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					controller.quit();
				}
			});
			
			return quitButton;
		}
		
		/**
		 * Panel displaying the current IP
		 * @return
		 */
		private JPanel IPPanel(){
			JPanel ipPanel = new JPanel();
			JLabel ipLabel;
			ipPanel.setLayout(new BorderLayout());
			ipPanel.setBackground(Color.WHITE);
			
			try {
				ipLabel = new JLabel("IP Address: " + InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				e.printStackTrace();
				ipLabel = new JLabel("IP Unknown.");
			}

			ipPanel.setPreferredSize(new Dimension(250, 25));
			ipLabel.setFont(new Font(ipLabel.getFont().getName(), Font.PLAIN, 18));
			ipPanel.add(ipLabel, BorderLayout.CENTER);
			
			return ipPanel;
		}
		
		/**
		 * Panel showing the current port Number
		 * @param portNumber
		 * @return
		 */
		private JPanel portPanel(int portNumber){
			JPanel portPanel = new JPanel();
			JLabel portLabel = new JLabel("Port Number: " + Integer.toString(portNumber));
			portPanel.setLayout(new BorderLayout());
			
			portPanel.setPreferredSize(new Dimension(250, 25));
			portLabel.setFont(new Font(portLabel.getFont().getName(), Font.PLAIN, 18));
			portPanel.add(portLabel, BorderLayout.CENTER);
			portPanel.setBackground(Color.WHITE);
			return portPanel;
		}
	}
	
}
