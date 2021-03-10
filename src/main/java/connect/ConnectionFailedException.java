package connect;

public class ConnectionFailedException extends Exception{
    public ConnectionFailedException(int responseCode, String message){
        super(responseCode + " " + message);
    }
}
