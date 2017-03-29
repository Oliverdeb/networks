import java.awt.*;
import java.io.Serializable;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Packet implements Serializable{

    private String message;
    private String user;
    private String time;
    private Image img;


    public Packet(String message, String user, String time){
        this.message = message;
        this.user = user;
        this.time = time;
    }

    public void setImg(Image img){
        this.img = img;
    }

    public Image getImg(){
        return this.img;
    }

    public String getTime(){
        return this.time;
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
