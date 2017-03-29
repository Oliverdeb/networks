import java.io.Serializable;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Packet implements Serializable{

    private String message;
    private String user;


    public Packet(String message, String user){
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
