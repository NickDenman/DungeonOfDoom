import java.net.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * Contains the code for the server allowing users to join and play the game
 *
 * @author: nd430
**/

public class DODServerGUI{
	ServerView serverView;
	int counter = 0;
	ServerSocket serverSocket;
	GameLogic gameLogic;
	DODServerGUI dodServer;
	String portNumber;
	
	public static void main(String[] args){
		//checks to see if a valid portNumber to listen on was enterred
		if(args.length != 1){
			System.err.println("Usage: java DoDServer <port number>");
			System.exit(1);
		}
		
		DODServerGUI dodServer = new DODServerGUI(args[0]);
	}
	
	/**
     * Constructor. creates the gui
	 *
     * @param portNumber :The portNumber that the serverSocket will listen on.
     */
	public DODServerGUI(String portNumber){
		this.portNumber = portNumber;
		dodServer = this;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				newServerView();
			}
		});
	}
	
	private void newServerView(){
		serverView = new ServerView(portNumber, dodServer);
	}
	
	/**
     * Asks if a new game should be started and processes the result accordingly
     */
	public void newGame(int portNumber){
		try{
			serverSocket = new ServerSocket(portNumber);
			gameLogic = new GameLogic(serverSocket.getLocalPort(), this);
			serverView.setupView(gameLogic.getMapWidth(), gameLogic.getMapHeight(), portNumber);
			serverView.updateView(gameLogic.getMap());
			serverView.switchPane("2");
			serverView.successfulOptionPane();
			this.runGame();	
			//serverSocket.close();
		}
		catch(IOException e){
			serverView.unableToConnectPane();
			//e.printStackTrace();
		}
	}
	
	/**
	 * Updates the server view
	 */
	protected void updateView(){
		serverView.updateView(gameLogic.getServerMap());
	}
	/**
     * Continuously allows clients to join until the someone wins the game
     */
	private void runGame(){
		new Thread(new Runnable(){
			public void run(){
				try{
					//while the game is running accept any new connections
					while(gameLogic.gameRunning()){
						newPlayer(serverSocket.accept());
					}
					closeResources();
				}
				catch(IOException e){
					//e.printStackTrace();
				}
			}
		}).start();

	}
	
	/**
     * Synchronized method that is called when a client joins. It starts a new thread and adds the 
	 * thread to an arraylist
     *
     * @param socket : The socket that the user will read and write to
     */
	private synchronized void newPlayer(Socket socket){
		new DoDThread(socket, gameLogic, getNewID()).start();;
	}
	
	/**
     * Synchronized method that is called when the game endes. It closes all the sockets and empties the arraylist
	 * of threads
     *
     * @param socket : The socket that the user will read and write to
     */
	protected synchronized void closeResources(){
		if(serverSocket != null){
			try {
				//gameLogic.quitGame();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Prints this message when someone has won
	 */
	protected void endMessage(){
		serverView.winningMessage();
		serverView.switchPane("1");
	}
	/**
	 * Quits the game, closing resources and closing the frame
	 */
	protected void quit(){
		gameLogic.quitGame();
		System.out.println("QUIT");
		closeResources();
		serverView.dispose();
	}
	
	/**
	 * Creates a new ID for each client joining
	 * @return
	 */
	private int getNewID(){
		int id = counter;
		counter++;
		return id;
	}
}