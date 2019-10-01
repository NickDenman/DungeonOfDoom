import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 * Panel which contains the view of the map for the clients
 * @author Nick
 *
 */
public class ViewPanel extends JPanel{
	ImageIcon wall;
	ImageIcon coin;
	ImageIcon bot;
	ImageIcon exit;
	ImageIcon ground;
	ImageIcon mario;
	ImageIcon luigi;
	ImageIcon toadBlue;
	ImageIcon toadYellow;
	ImageIcon koopa;
	
	/**
	 * Constructor. Sets up the panel and images
	 */
	public ViewPanel(){
		setLayout(new GridLayout(5,5,2,2));
		setPreferredSize(new Dimension(740, 740));
		//setBackground(Color.PINK);
		setOpaque(false);
		setBorder(new EmptyBorder(20, 20, 20, 20));
		
		wall = resizeImage("Wall.jpg", 140);
		coin = resizeImage("CoinBackground.png", 140);
		bot  = resizeImage("Brown_thing.png", 140);
		exit  = resizeImage("Pipe.jpg", 140);
		ground  = resizeImage("Ground.png", 140);
		mario = resizeImage("Mario.jpg", 140);
		luigi = resizeImage("Luigi.jpg", 140);
		toadBlue = resizeImage("Toad_Blue.jpg", 140);
		toadYellow = resizeImage("Toad_Yellow.jpg", 140);
		koopa = resizeImage("koopa.png", 140);
	}
	
	/**
	 * Resizes the images to the new size 
	 * @param location: where images are stored
	 * @param size: new image size
	 * @return: new image
	 */
	private ImageIcon resizeImage(String location, int size){
		try{
			BufferedImage fullCharacterImage = ImageIO.read(new File(location));
			Image characterImage = fullCharacterImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
			return new ImageIcon(characterImage);
		}
		catch (IOException e) {
			System.err.println("Unable to locate background image");
			System.out.println("VIEW PANEL");
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Updates the view so map is constantly updated
	 * @param map: new map to be shown
	 */
	public void updateLook(char[][] map){
		removeAll();
		
		for(int i=0; i<25; i++){
			switch(map[i%5][i/5]){
			case '#' :
				add(new JLabel(wall));
				break;
			case '.' :
				add(new JLabel(ground));
				break;
			case 'G' :
				add(new JLabel(coin));
				break;
			case 'E' :
				add(new JLabel(exit));
				break;
			case 'B' :
				add(new JLabel(bot));
				break;
			case 'M' :
				add(new JLabel(mario));
				break;
			case 'L' :
				add(new JLabel(luigi));
				break;
			case 'T' :
				add(new JLabel(toadBlue));
				break;
			case 'Y' :
				add(new JLabel(toadYellow));
				break;
			case 'K' :
				add(new JLabel(koopa));
				break;
			}
		}
		
		revalidate();
		repaint();
	}
}

