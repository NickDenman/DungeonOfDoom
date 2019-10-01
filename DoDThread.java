import java.net.*;
import java.io.*;

/**
 * Contains the code for each thread that is created when a client joins
 *
 * @author: nd430
**/
public class DoDThread implements Runnable {
	Thread dodThread;
	Socket socket;
	GameLogic gameLogic;
	char playerSymbol;
	PrintWriter out;
	BufferedReader in;
	int clientID;
	String userInput;
	ObjectOutputStream outStream;
	
	/**
	 * Constructor. assigns the inputs to variables stored in the class and creates the thread
	 *
	 * @param socket: 
	 * 		the socket which the client is reading from and writing to
	 * @param gameLogic:
	 *		the logic behind the game shared by all clients
	 **/
	public DoDThread(Socket socket, GameLogic gameLogic, int clientID){
		this.socket = socket;
		this.gameLogic = gameLogic;
		this.clientID = clientID;
		dodThread = new Thread(DoDThread.this);
		
	}
	//starts the thread
	public void start(){
		dodThread.start();
	}
	/**
	 * Method from interface Runnable. Contains the code which determines if a human or bot has joined and runs
	 * the game accordingly
	**/
	public void run(){
		//try catch which attempts to create a new printWriter and BufferedReader on the given socket
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new ObjectOutputStream(socket.getOutputStream());
			
			//makes sure input does not equal null
			
			userInput = in.readLine();
			if(userInput != null){
				playerSymbol = userInput.charAt(0);
			}
			else{
				return;
			}
			outStream.writeObject(gameLogic.getGoldRequired());
			gameLogic.newPlayer(playerSymbol, outStream, clientID);
			this.runHuman();
		}
		catch(IOException e){
			//e.printStackTrace();
		}
	}
	
	/**
	 * Method which contains the code for listening to and printing to the client
	**/
	private void runHuman() throws IOException{
				
		//while loop that continues to execute until the game is over or the client disconnects
		while(gameLogic.gameRunning() && (userInput = in.readLine()) != null){
			System.out.println(clientID + ": " + userInput);
			gameLogic.processCommand(clientID, userInput);
		}
		if(!gameLogic.gameRunning()){
			gameLogic.removePlayer(clientID);
		}
	}
	/**
	 * Method that closes all the sockets once the game has ended releasing the blocking IOException
	**/
	public void closeSocket() throws IOException{	
		socket.shutdownInput();
		socket.shutdownOutput();
	}
}