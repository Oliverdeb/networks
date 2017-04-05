import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Packet implements Serializable{

    private String message;
    private String user;
    private String time;
    private String type;
    private byte[] imgBytes;
    private String fileName;


    public Packet(String message, String user, String time){
        this.message = message;
        this.user = user;
        this.time = time;
        this.type = null;
        this.imgBytes = null;
        this.fileName = null;
    }

    public Packet(String message, String user, String time, String type){
        this.message = message;
        this.user = user;
        this.time = time;
        this.type = type;
        this.imgBytes = null;
        this.fileName = null;
    }

    public Packet(String message, String user, String time, String type, byte[] imgBytes, String fileName){
        this.message = message;
        this.user = user;
        this.time = time;
        this.type = type;
        this.imgBytes = imgBytes;
        this.fileName = fileName;
    }

    public Packet(Packet p){
        this(p.getMessage(), p.getUser(), p.getTime(), p.getType(), p.getImgBytes(), p.getFileName());
    }

    public String getFileName(){
        return fileName;
    }

    public byte[] getImgBytes(){
        return this.imgBytes;
    }

    public String getTime(){
        return time;
    }

    public String getMessage() {
        return message;
    }
    public String getType(){
        return type;
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
