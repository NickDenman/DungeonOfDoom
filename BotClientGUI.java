import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;
/**
 * Contains the code for running the bot, connecting to server
 * @author: nd430
**/

public class BotClientGUI extends Client{
	HashMap<Integer, BotClient> botMap = new HashMap<Integer, BotClient>();
	BotClientGUI botClientGUI;
	BotView view;
	String hostName;
	int portNumber;
	int count = 0;
	boolean gameRunning = false;
	
	//main method taking in two parameters, hostname and portnumber
	public static void main(String[] args){
		if (args.length != 2) {
			System.err.println("Usage: java BotClientGUI <host name> <port number>");
			System.exit(1);
		}
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		new BotClientGUI(hostName, portNumber);
	}
	/**
     * Constructor. creates the GUI
	 *
     * @param portNumber :The portNumber that the socket will join on.
     * @param hostname :The hostname that the socket will join on.
     */
	public BotClientGUI(String hostName, int portNumber){
		this.hostName = hostName;
		this.portNumber = portNumber;
		botClientGUI = this;
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				newView();
			}
		});
		
		
	}
	
	private void newView(){
		view = new BotView(hostName, portNumber, botClientGUI);
	}
	
	/**
	 * Counts how many bots have joined giving each a unique id
	 */
	public int botNumber(){
		System.out.println("COUNT: " + count);
		return count++;
	}

	/**
	 * Method implemented from abstract class. Closes all sockets once the game has ended/bots have quit
	 */
	@Override
	public void closeResources() {
		for(int key : botMap.keySet()){
			botMap.get(key).closeResources();
		}
		
		//iterates through bots removing all from the hashmap
		Iterator<Entry<Integer,BotClient>> iterator = botMap.entrySet().iterator();
		while (iterator.hasNext()) {
		    Entry<Integer, BotClient> entry = iterator.next();	
		    iterator.remove();
		}
		count = 0;
	}
	
	/**
	 * Sends symbol to server 
	 * 
	 * @param botNumber: which bot is sending
	 * @param symbol: what will be sent to the server
	 */
	public void sendToServer(int botNumber, char symbol){
		botMap.get(botNumber).sendToServer(symbol);
	}

	/**
	 * Method implemented from abstract class. Creates a new game by initially connecting to the server
	 * @param: hostName, hostname for socket to join on
	 * @param portNumber: portnumber for the socket to join to
	 */
	@Override
	public void connectToServer(String hostName, int portNumber){
		this.hostName = hostName;
		this.portNumber = portNumber;
		view.newButtonPanel();
		this.newBot();
	}
	
	/**
	 * Changes the current bot being viewed in the gui
	 * @param botNumber: number of bot to be viewed
	 */
	public void changeBotView(int botNumber){
		view.changeBotView(botNumber);
	}
	
	/**
	 * Changes the speed of the bot making the game harder/easier
	 * @param speed: the new speed 
	 * @param botNumber: the bot whose speed will be changed
	 */
	public void changeBotSpeed(int speed, int botNumber){
		botMap.get(botNumber).updateSpeed(speed);
	}
	
	/**
	 * Creates a new bot with a unique number
	 */
	public synchronized void newBot(){
		int id = botNumber();
		gameRunning = true;
		botMap.put(id, new BotClient(view, hostName, portNumber, id));
	}
	
	/**
	 * Method implemented from abstract class closing the view.
	 */
	@Override
	public void quit() {
		view.dispose();	
	}
}