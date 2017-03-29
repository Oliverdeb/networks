import java.awt.*;
import java.io.Serializable;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Packet implements Serializable{

    private String message;
    private String user;
    private Image img;


    public Packet(String message, String user){
        this.message = message;
        this.user = user;
    }

    public void setImg(Image img){
        this.img = img;
    }

    public Image getImg(){
        return this.img;
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
