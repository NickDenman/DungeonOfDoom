import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Class containing the JPanes to show success or failure on certain commands
 * @author Nick
 *
 */
public class MessagePanes {
	protected JFrame frame;
	protected CardLayout cardLayout = new CardLayout();
	
	/**
	 * Shows that connection to the server was successful
	 */
	public void successfulOptionPane(){
		JOptionPane.showMessageDialog(frame, "Connection successful!", "Connection status",  JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Shows that connection to the server was unsuccessful
	 */
	public void unableToConnectPane(String message){
		JOptionPane.showMessageDialog(frame, message, "Connection status",  JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Shows the game has been won
	 */
	public void winMessage(){
		JOptionPane.showMessageDialog(frame, "Congratulations! You escaped the dungeon.", "Game Over",  JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * shows that the player has lost the game
	 */
	public void loseMessage(){
		JOptionPane.showMessageDialog(frame, "Someone escaped the dungeon before you.. Better luck next time!", "Game Over",  JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * switches the currently viewed pane
	 * @param panel
	 */
	public void switchPane(String panel){
		cardLayout.show(frame.getContentPane(), panel);
	}
	
	/**
	 * Closes the jframe
	 */
	public void dispose(){
		frame.dispose();
	}
}
