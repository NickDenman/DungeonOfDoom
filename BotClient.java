import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Starts the game with a Bot Player. Contains code for bot's decision making.
 *
 * @author : The unnamed tutor.
 */
public class BotClient{
	Socket socket;
	ObjectInputStream inStream;
	BotView view;
	int sleep = 3000;
	
	private int botNumber;
	private PrintWriter toServer;
	private Random random;
	private int goldRequired;
	private static final char [] DIRECTIONS = {'N','S','E','W'};
	
	/**
	 * Constructor. Creates sockets, and adds bots to the view
	 * @param view: the gui for the bots
	 * @param hostName: hostName for the sockets to connect to
	 * @param portNumber: portNumber for the sockets to connect to
	 * @param botNumber: the unique id of the bot
	 */
	public BotClient(BotView view, String hostName, int portNumber, int botNumber){
		this.botNumber = botNumber;
		this.view = view;
		
		try{
			socket = new Socket(hostName, portNumber);
			toServer = new PrintWriter(socket.getOutputStream(), true);
			inStream = new ObjectInputStream(socket.getInputStream());
			view.newBot(botNumber);
			random = new Random();
			toServer.println("B");
			getGoldRequired();
			botToServer();
			new ServerToUser();
			view.updateGoldPanel(botNumber, goldRequired, 0);
			view.successfulOptionPane();
			view.switchPane("2");
		}catch(IOException e){
			view.unableToConnectPane("Unable to create bot");
		}
	}
	
	/**
	 * A new thread that sends directions from the bot to the server
	 */
	private void botToServer(){
		new Thread(new Runnable(){
			public void run(){
				while(inStream != null){
					toServer.println(this.getNextAction());
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			public char getNextAction() {
		    	return DIRECTIONS[random.nextInt(4)];
		    }
		}).start();
	}
	
	/**
	 * Sends the character symbol to the server allowing bot symbol to be changed
	 * @param symbol: the new bot symbol
	 */
	public void sendToServer(char symbol){
		toServer.println(symbol);
	}
	
	/**
	 * Class that creates a new allows server to constantly receive data from server
	 * @author nd430
	 *
	 */
	private class ServerToUser implements Runnable{
		Thread stuThread;
		
		/**
		 * Constructor. Creates and startes the thread stuThread
		**/
		public ServerToUser(){
			stuThread = new Thread(this);
			stuThread.start();
		}
		
		/**
		 * Run method from implemented Interface Runnable.
		**/
		public void run(){
			this.receiveFromServer();
		}
		
		/**
		 * Method that continuously reads data from the server and processes the command
		**/
		private void receiveFromServer(){
			//try catch block that attemts to read from the server and print to console
			try{
				//while loop that continues to run until the connection to the server breaks
				while(inStream != null){
					processCommand(inStream.readObject());
				}
			}
			catch (IOException e) {
				System.err.println("Couldn't establish I/O connection");
				closeResources();
			}
			catch(ClassNotFoundException cnfe){
				System.err.println("Couldn't find class");
			}
			finally{
				view.switchPane("1");
			}
		}
		/**
		 * Processes the data coming from the server determining what to do with it
		 * @param command
		 */
		private void processCommand(Object command){
			
			if(command instanceof char[][]){
				char[][] map = (char[][]) command;
				view.updateView(map, botNumber);
			}
			else if(command instanceof Integer){
				view.updateGoldPanel(botNumber, goldRequired, (int) command);
			}
			else if(command instanceof String){
				if(command.equals("WIN")){
					view.winMessage();
				}
				else if(command.equals("LOSE")){
					view.loseMessage();
				}
				closeResources();
			}
		}
	}
	
	
	
	
	/**
	 * Changes the speed that the bot moves at
	 * @param speed
	 */
	public void updateSpeed(int speed){
		sleep = speed;
	}

	/**
	 * Gets the gold required to exit the map at the beginning of the game
	 * @throws IOException
	 */
	private void getGoldRequired() throws IOException{
		try {
			goldRequired = (int) inStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the bot currently being viewed in the GUI
	 * @param botNumber
	 */
	public void changeBotView(int botNumber){
		view.changeBotView(botNumber);
	}
	
    // Selects the next action the bot will perform. Simple implementation - just picks a random direction
    public char getNextAction() {
    	return DIRECTIONS[random.nextInt(4)];
    }

    /**
     * Closes the printWriter, ObjectInputStream and socket
     */
    public void closeResources() {
		try{
			if(toServer != null){
				toServer.println('Q');
				toServer.close();
			}
			if(inStream != null){

				inStream.close();
			}
			if(socket != null){

				socket.close();
			}
			
		} catch(IOException e){
			//e.printStackTrace();
		}
	}
}