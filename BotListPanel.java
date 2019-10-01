import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.GridBagConstraints;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
/**
 * The panel containing all the buttons of each individual bot
 * @author Nick
 *
 */
public class BotListPanel extends JPanel {
	ArrayList<JButton> buttonArray = new ArrayList<JButton>();
	JPanel contentPanel;
	GridBagConstraints gbc;
	BotClientGUI controller;
	int count = 0;
	int botNumber;
	
	/**
	 * Constructor. sets up panel initially.
	 * @param controller
	 */
	public BotListPanel(BotClientGUI controller){
		this.controller = controller;
		gbc = new GridBagConstraints();
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setOpaque(false);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(100, 600));
		this.add(contentPanel, BorderLayout.CENTER);
	}
	 /**
	  * Adds a new button for each new bot that joins
	  * @param botNumber: the unique id of the new bot that joined
	  */
	public synchronized void addButton(int botNumber){
		this.botNumber = botNumber;
		JButton botButton = new JButton("BOT: "+botNumber);
		gbc.gridy = count++;
		gbc.gridx = 0;
		//contentPanel.add(new JButton("Bot: " + botNumber));
		
		botButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				changeBotView();
			}
		});
		contentPanel.add(botButton, gbc);
		this.revalidate();
		this.repaint();
	}
	
	private void changeBotView(){
		controller.changeBotView(botNumber);
	}
}
