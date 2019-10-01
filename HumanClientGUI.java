import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that determines what to do with the data received from the client GUI
 * @author Nick
 *
 */
public class HumanClientGUI extends Client{
	
	ClientGUI view;
	String hostName;
	int portNumber;
	Socket socket;
	PrintWriter out;
	ObjectInputStream inStream;
	int goldRequired = 0;
	boolean gameRunning = false;
	ServerToUser stu;
	
	public static void main(String[] args){
		if (args.length != 2) {
            System.err.println("Usage: java HumanClient <host name> <port number>");
            System.exit(1);
        }
		
		String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
		
        HumanClientGUI HumanClientGUI = new HumanClientGUI(hostName, portNumber);
	}
	
	/**
	 * Constructor. creates new view
	 * @param hostName: default host name
	 * @param portNumber: default portnumber
	 */
	public HumanClientGUI(String hostName, int portNumber){
		this.hostName = hostName;
		this.portNumber = portNumber;
		
		view = new ClientGUI(hostName, portNumber, this);
	}
	
	/**
	 * Connects to the server with the specified hostName and portNumber
	 * @param hostName: the hostname the socket will connect on
	 * @param portNumber the portnumber the socket will connect on
	 */
	public void connectToServer(String hostName, int portNumber){
		try{
			socket = new Socket(hostName, portNumber);
			out = new PrintWriter(socket.getOutputStream(), true);
			inStream = new ObjectInputStream(socket.getInputStream());
			
			gameRunning = true;
			
			out.println("M");
			getGoldRequired();
			view.updateGold(goldRequired, 0);

			stu = new ServerToUser();
					
			view.switchPane("2");
			view.successfulOptionPane();
		}
		catch (UnknownHostException e) {
            view.unableToConnectPane("Unknown host: " + hostName);
            closeResources();	
        } 
		//catch if the client couldn't establish a connection to the given hostname
		catch (IOException e) {
			closeResources();	
			view.unableToConnectPane("Couldn't establish I/O for the connection to " + hostName);
            //System.exit(1);
        } 
	}

	/**
	 * Gets the gold required to exit the dungeon when the game begins
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
	 * 
	 * Sends commands received from the GUI to the server
	 * @param command
	 */
	public void sendToServer(char command){
		out.println(command);
	}
	
	/**
	 * Closes all the resources when the game has ended/user has quit
	 */
	public void closeResources(){
		try{
			if(out != null){
				//out.println('Q');
				out.close();
			}
			if(inStream != null){
				inStream.close();
			}
			if(socket != null){
				socket.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Disposes the view
	 */
	public void quit(){
		view.dispose();
	}
	
	/**
	 * Class that creates new thread to constantly receive data from the server
	 * @author Nick
	 *
	 */
	private class ServerToUser implements Runnable{
		Thread stuThread;
		int id = -1;
		
		/**
		 * Constructor. Creates and startes the thread stuThread
		**/
		public ServerToUser(){
			stuThread = new Thread(this);
			stuThread.start();
		}
		
		private ServerToUser(int id){
			this.id = id;
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
		 * Method that continuously reads data from the server and prints it to the clients console
		**/
		private void receiveFromServer(){
			//try catch block that attemts to read from the server and print to console
			try{
				//while loop that continues to run until the connection to the server breaks
				while(gameRunning){
					processCommand(inStream.readObject());
				}
			}
			catch (IOException e) {
				System.err.println("Couldn't establish I/O connection");
			}
			catch(ClassNotFoundException cnfe){
				System.err.println("Couldn't find class");
			}
			finally{
				view.switchPane("1");
			}
		}
		
		/**
		 * Determines what to do with the data received form the server
		 * @param command: the data received from the server
		 */
		private void processCommand(Object command){
			
			if(command instanceof char[][]){
				char[][] map = (char[][]) command;
				view.updateView(map);
			}
			else if(command instanceof Integer){
				view.updateGold(goldRequired, (int) command);
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
}
