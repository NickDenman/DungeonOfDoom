/**
 * Contains the main logic part of the game, as it processes.
 *
 * @author : The unnamed tutor.
 */
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

//import javax.swing.text.html.HTMLDocument.Iterator;
import java.util.Iterator;
//import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

public class GameLogic {
	HashMap<Integer, Player> playerHash = new HashMap<Integer, Player>();
	private Map map;
	int clientID;
	int portNumber;
	DODServerGUI server;
	private boolean active = true;
	
	/**
	 * Constructor. creates a new map and chatroom object
	**/
	public GameLogic(int portNumber, DODServerGUI server){
		this.server = server;
		this.portNumber = portNumber;
		map = new Map();
		map.readMap("maps/example_map.txt");
	}
	
	/**
	 * Sets the players initial position to a random place on the map that does not contain another player or wall
	 *
	 * @return: the x and y coordinates that the player was initially placed in
	 */
	public synchronized int[] setInitialPosition(){
		int[] coordinates = new int[2];
		int width = map.getMapWidth();
		int height = map.getMapHeight();
		Random rand = new Random();
		int randRow;
		int randCol;
		
		//chooses a random number less than the map height
		randRow = rand.nextInt(height-2)+1;
		//chooses a random number less than the map width
		randCol = rand.nextInt(width-2)+1;
		boolean validPosition = false;
		
		//While loop that checks that the the tile at the random coordinates chosen is not a wall or Gold or another player
		while(!validPosition){
			System.out.println("WHILE LOOP");
			if(map.getTile(randCol, randRow) != '#' || map.getTile(randCol, randRow) != 'G'){
				if(playerHash.size()>0){
					//iterates through all the players in the hashmap to check the new position does not equal one of theirs
					for(int key : playerHash.keySet()){
						if(randCol == playerHash.get(key).getXCoordinate() && randRow == playerHash.get(key).getYCoordinate()){
							randRow = rand.nextInt(height-2)+1;
							randCol = rand.nextInt(width-2)+1;
							break;
						}
						else{
							validPosition = true;
						}
					}
				}
				else{
					validPosition = true;
				}
			}
			else{
				randRow = rand.nextInt(height-2)+1;
				randCol = rand.nextInt(width-2)+1;
			}
		}
		
		coordinates[0] = (randCol);
		coordinates[1] = (randRow);
		System.out.println("END OF POSITION");
		return coordinates;
	}
	/*
	 *   Helper methods for when switching between human player and bot player
	 */
	
    // get current players total of collected gold
    private int getPlayersCollectedGold(){
    	return playerHash.get(clientID).getGoldCollected();
    }
    
    // increment current players total of collected gold
    private void incrementPlayersCollectedGold(){
		playerHash.get(clientID).incrementCollectedGold();
    }
    
    // get current players x coordinate
    private int getPlayersXCoordinate(){
    	return playerHash.get(clientID).getXCoordinate();
    }
    
    // set current players x coordinate
    private void setPlayersXCoordinate(int newX){
		playerHash.get(clientID).setXCoordinate(newX);
    }
    
    // get current players y coordinate
    private int getPlayersYCoordinate(){
    	return playerHash.get(clientID).getYCoordinate();
    }
 
    // set current players y coordinate
    private void setPlayersYCoordinate(int newY){
    	playerHash.get(clientID).setYCoordinate(newY);
    }
	
	/**
	 * Syncronized method that is called when a new player joins. Checks their username isn't already taken then adds them to the hashmap
	 *
	 * @return: boolean false if the  username has already been taken and true if it hasn'taken
	 */
	public synchronized void newPlayer(char playerChar, ObjectOutputStream out, int clientID){
			playerHash.put(clientID, new Player(playerChar, setInitialPosition(), clientID, out));
			server.updateView();
			look();
	}
	
	/**
	 * Syncronized method that removes a player from the hashmap if they leave during the game
	 */
	public synchronized void removePlayer(int clientID){
		try {
			System.out.println(clientID);
			System.out.println(playerHash.size());
			playerHash.get(clientID).getPlayersObjectStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		playerHash.remove(clientID);
		//look();
		server.updateView();
	}
	/**
     * Processes the command. It should return a reply in form of a String, as the protocol dictates.
     * Otherwise it should return the string "Invalid".
     *
     * @param command : Input entered by the user.
     * @return : Processed output or Invalid if the @param command is wrong.
     */
    public synchronized void processCommand(int clientID, String action) {
		this.clientID = clientID;
		
		//System.out.println("" + playerHash.size());
		switch (action.toUpperCase()){
		case "N":
		case "S":
		case "W":
		case "E":
			move(action.charAt(0));
			server.updateView();
			look();
			break;
		case "P":
			pickup();
			break;
		case "Q":
			removePlayer(clientID);
			break;
//		case "G":
//			sendCollectedGold();
//			break;
		case "M":
		case "L":
		case "Y":
		case "T":
		case "B":
		case "K":
			changeSymbol(action.charAt(0));
		}
    }
    
    /**
     * Changes the symbol of the specified client
     * @param playerSymbol
     */
    public void changeSymbol(char playerSymbol){
    	playerHash.get(clientID).setPlayerSymbol(playerSymbol);
    	look();
    	server.updateView();
    }

    /**
     * Gets the full map to be viewed by the server
     * @return: the map for the server
     */
    protected char[][] getServerMap(){
    	char[][] fullMap = map.getMap();
    	char[][] serverMap = new char[map.getMapHeight()][map.getMapWidth()];
    	
    	for (int i = 0; i < map.getMapHeight(); i++) {
			for (int j = 0; j < map.getMapWidth(); j++) {
				serverMap[i][j] = fullMap[i][j];
			}
    	}
    	for(int key : playerHash.keySet()){
    		serverMap[playerHash.get(key).getYCoordinate()][playerHash.get(key).getXCoordinate()] = playerHash.get(key).getPlayerSymbol();
    	}
    	
    	return serverMap;
    }
    /**
     * @return if the game is running.
     */
    public boolean gameRunning() {
        return active;
    }

    /**
     * Checks if movement is legal and updates player's location on the map.
     *
     * @param direction : The direction of the movement.
     * @return : Protocol if success or not.
     */
    protected void move(char direction) {
    	int newX = getPlayersXCoordinate();
    	int newY = getPlayersYCoordinate();
		switch (direction){
		case 'N':
			newY -=1;
			break;
		case 'E':
			newX +=1;
			break;
		case 'S':
			newY +=1;
			break;
		case 'W':
			newX -=1;
			break;
		default:
			break;
		}
		
		// check if the player can move to that tile on the map
		//int xDistance = newX - getOpponentXCoordinate();
    	//int yDistance = newY - getOpponentYCoordinate();
		
		int xDistance;
		int yDistance;
		for(int key : playerHash.keySet()){
			xDistance = newX - playerHash.get(key).getXCoordinate();
			yDistance = newY - playerHash.get(key).getYCoordinate();
			if(xDistance == 0 && yDistance == 0){
				return;
			}
		}
		
		if(map.getTile(newX, newY) != '#'){
			//System.out.println("moved from " + getPlayersXCoordinate() + ", " + getPlayersYCoordinate());
			setPlayersXCoordinate(newX);
			setPlayersYCoordinate(newY);
			//System.out.println("moved to " + getPlayersXCoordinate() + ", " + getPlayersYCoordinate());
			if (checkWin()){
				try{
					look();
					server.updateView();
					winningMessage();
					server.endMessage();
					quitGame();
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
		}
    }
    
    /**
     * Sends the message to clients when game is over
     * @throws IOException
     */
    private void winningMessage() throws IOException{
    	for(int key : playerHash.keySet()){
    		if(key == clientID){
    			playerHash.get(key).getPlayersObjectStream().writeObject("WIN");
    		}
    		else{
    			playerHash.get(key).getPlayersObjectStream().writeObject("LOSE");
    		}
    	}
    }

    /**
     * Converts the map from a 2D char array to a single string.
     *
     * @return : A String representation of the game map.
     */
    private void look(){
    	// get look window for current player
    	char[][] look = new char[5][5];
    	
    	// is opponent visible? if they are then add them to the look window
    	//int xDistance =  getPlayersXCoordinate() - getOpponentXCoordinate();
    	//int yDistance = getPlayersYCoordinate() - getOpponentYCoordinate();
    	
		int xDistance;
		int yDistance;
		
		//System.out.println("" + playerLocation.size());
		
		
		
		for(int key : playerHash.keySet()){
			look = map.look(playerHash.get(key).getXCoordinate(), playerHash.get(key).getYCoordinate());
			for(int key1 : playerHash.keySet()){
				xDistance = playerHash.get(key).getXCoordinate() - playerHash.get(key1).getXCoordinate();
				yDistance = playerHash.get(key).getYCoordinate() - playerHash.get(key1).getYCoordinate();
				if(xDistance <= 2 && xDistance >= -2 && yDistance <= 2 && yDistance >= -2){
					look[2-xDistance][2-yDistance] = playerHash.get(key1).getPlayerSymbol();
				}
			}
			
			try {
				playerHash.get(key).getPlayersObjectStream().writeObject(look);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
    	
    	
    }

    
    /**
     * Processes the player's pickup command, updating the map and the player's gold amount.
     *
     * @return If the player successfully picked-up gold or not.
     */
    protected void pickup() {
    	if (map.getTile(getPlayersXCoordinate(), getPlayersYCoordinate()) == 'G') {
    		incrementPlayersCollectedGold();
			map.replaceTile(getPlayersXCoordinate(), getPlayersYCoordinate(), '.');
			
			try {
				playerHash.get(clientID).getPlayersObjectStream().writeObject(getPlayersCollectedGold());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

    /**
	 * checks if the player collected all GOLD and is on the exit tile
	 * @return True if all conditions are met, false otherwise
	 */
	protected boolean checkWin() {
		if (getPlayersCollectedGold() >= map.getGoldToWin() && 
			map.getTile(getPlayersXCoordinate(), getPlayersYCoordinate()) == 'E') {
			
			return true;
		}
		return false;
	}

	/**
	 * Quits the game when called
	 */
	public void quitGame(){
		System.out.println("QG START");
		for(int key : playerHash.keySet()){
			try {
				playerHash.get(key).getPlayersObjectStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		active = false;
		
		Iterator<Entry<Integer,Player>> iterator = playerHash.entrySet().iterator();
		while (iterator.hasNext()) {
		    Entry<Integer, Player> entry = iterator.next();		    
		    iterator.remove();
		}	
		Socket socket;
		try {
			socket = new Socket("localhost", portNumber);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the gold required to exit
	 * @return
	 */
	public synchronized int getGoldRequired(){
		System.out.println("MAP BEFORE");
		return map.getGoldToWin();
	}
	
	/**
	 * Gets the overall heightt of the map
	 * @return
	 */
	public int getMapHeight(){
		return map.getMapHeight();
	}
	/**
	 * gets the overall width of the map
	 * @return
	 */
	public int getMapWidth(){
		return map.getMapWidth();
	}
	/**
	 * Gets the full map from the server
	 * @return
	 */
	public synchronized char[][] getMap(){
		return map.getMap();
		
	}
}