import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * class which holds relevent data for all players of the game, including bots
 *
 * @author: nd430
**/

public class Player{
	private int goldCollected = 0;
	private int[] coordinates;
	private char playerSymbol;
	int clientID;
	ObjectOutputStream out;	
	/**
	 * Construtor. Assigns the given values to variables stored in the class
	 *
	 * @param playerSymbol:
	 *			The player's symbol
	 * @param coordinates:
	 * 			The player's location on the map
	 * @param username:
	 *			The player's unique username
	 */
	public Player(char playerSymbol, int[] coordinates, int clientID, ObjectOutputStream out){
		this.playerSymbol = playerSymbol;
		this.coordinates = coordinates;
		this.clientID = clientID;
		this.out = out;
	}
	/**
	 * returns the player's symbol
	 */
	public char getPlayerSymbol(){
		return playerSymbol;
	}
	
	/**
	 * returns the player's PrintWriter
	 */
	public ObjectOutputStream getPlayersObjectStream(){
		return out;
	}
	
	 /**
	 * returns the amount of gold the player has collected
	 */
	public int getGoldCollected(){
		return goldCollected;
	}
	
	 /**
	 * increments the gold collected by the player by one
	 */
	public void incrementCollectedGold(){
		goldCollected++;
	}
	
	 /**
	 * returns the player's x Coordinate on the map
	 */
	public int getXCoordinate(){
		return coordinates[0];
	}
	
	/**
	 * returns the player's y Coordinate on the map
	 */
	public int getYCoordinate(){
		return coordinates[1];
	}
	
	/**
	 * sets the players x coordinate to a new location
	 */
	public void setXCoordinate(int newXCoordinate){
		coordinates[0] = newXCoordinate;
	}
	
	public void setPlayerSymbol(char playerSymbol){
		this.playerSymbol = playerSymbol;
	}
	/**
	 * sets the players y coordinate to a new location
	 */
	public void setYCoordinate(int newYCoordinate){
		coordinates[1] = newYCoordinate;
	}
	
	/**
	 * returns the players username
	 */
	public int getUsername(){
		return clientID;
	}
}