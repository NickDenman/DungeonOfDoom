/**
 * Abstract class containing methods to be implemented
 * @author Nick
 *
 */
public abstract class Client {
	public abstract void closeResources();
	public abstract void connectToServer(String IPAddress, int portNumber);
	public abstract void quit();
}
