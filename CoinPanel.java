import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel that contains the coins collected and required
 * @author Nick
 *
 */
public class CoinPanel extends JPanel{
	public CoinPanel(){
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(434, 750));
		setOpaque(false);
	}
	
	/**
	 * Updates the coin panel to the new number of coins collected
	 * @param goldRequired: the gold required to exit
	 * @param goldCollected: the gold collected
	 */
	protected void updateCoinPanel(int goldRequired, int goldCollected){
		removeAll();
		
		for(int i=0; i<goldCollected; i++){
			add(new JLabel(new ImageIcon(resizeImage("CoinL.png", 75))));
		}
		
		for(int j=0; j<goldRequired-goldCollected; j++){
			add(new JLabel(new ImageIcon(resizeImage("CoinOutline.png", 75))));
		}
		revalidate();
		repaint();
	}
	
	
	/**
	 * Resizes the images to fit
	 * @param location: location of the image
	 * @param size: size of the new image
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
			System.out.println("COIN");
			System.exit(1);
			return null;
		}
	}
}
