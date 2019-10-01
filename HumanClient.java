import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Contains the code which allows a human to connect to the server and play the game against other 
 * humans and bots
 *
 * @author: nd430
**/
public class HumanClient{
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	private BufferedReader input;
	Boolean gameRunning = true;
	private UserToServer uts;
	private ServerToUser stu;
	
	public static void main(String[] args){
		//checks to make sure a hostname and portnumber were enterred when the client was run
		if (args.length != 2) {
            System.err.println("Usage: java HumanClient <host name> <port number>");
            System.exit(1);
        }
		
		String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
		
		HumanClient humanClient = new HumanClient(hostName, portNumber);
	}
	
	/**
	 * Constructor. Creates new socket, PrintWriter, and two BufferedReaders for the server and username
	 *
	 * @param hostName:
	 * 		The hostname the client will connect to
	 * @param portNumber:
	 *		The portnumber the client will connect to
	**/
	public HumanClient(String hostName, int portNumber){
		//try catch to create socket, printwriter and two bufferedreaders
		try{
			socket = new Socket(hostName, portNumber);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			input = new BufferedReader(new InputStreamReader(System.in));
		}
		//catch if an UnknownHostException is thrown
		catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        } 
		//catch if the client couldn't establis a connection to the given hostname
		catch (IOException e) {
			e.printStackTrace();
            System.err.println("Couldn't establish I/O for the connection to " + hostName);
            System.exit(1);
        }
		
		//creates new instances of the inner classes, one to listen to the server and one to write to the server
		stu = new ServerToUser();
		uts = new UserToServer();
	}
	
	/**
	 * An innner class that implements the Runnable interface and sends data from the user to the server
	 */
	private class UserToServer implements Runnable{
		Thread utsThread;
		/**
		 * Constructor. creates and starts the thread utsThread
		 */
		public UserToServer(){
			utsThread = new Thread(this);
			utsThread.start();
		}
		/**
		 * Run method from interface Runnable. contains the code that runs the thread
		*/
		public void run(){
			try{
				this.sendCommandToServer();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		/**
		 * Method that reads input from the user and sends it to the server to be processes
		**/
		private void sendCommandToServer()throws IOException{
			String fromUser = "";
		
			//while loop that continues to read user inputt until the game ends
			while(gameRunning){
				fromUser = input.readLine();
				out.println(fromUser);
			}
		}
	}
	/**
	 * An inner class that reads data from the server and prints it to the clients console
	**/
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
		 * Method that continuously reads data from the server and prints it to the clients console
		**/
		private void receiveFromServer(){
			String fromServer;
			//try catch block that attemts to read from the server and print to console
			try{
				//while loop that continues to run until the connection to the server breaks
				while((fromServer = in.readLine()) != null){
					System.out.println("" + fromServer);
				}
				gameRunning = false;
				
				//close the sockets input and output to free resources
				socket.shutdownOutput();
				socket.shutdownInput();
			}
			catch (IOException e) {
				System.err.println("Couldn't establish I/O connection");
				gameRunning = false;
				System.exit(1);
			}
		}
	}
}