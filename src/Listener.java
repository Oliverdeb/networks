import java.io.*;
import java.net.Socket;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Listener extends Thread {
    private Socket client;
    private String name;

    public ObjectInputStream input;
    public ObjectOutputStream output;

    public void setClientName(String name){
        this.name = name;
    }

    public String getClientName(){
        return this.name;
    }

    public void run(){



        while (true){
            byte[] inp = new byte[1000];
            Packet packet = null;

            try {
                packet = (Packet)input.readObject();
//                packet = Util.deserialize(inp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(packet.getUser() + ": " + packet.getMessage());

        }
    }

    public void send_message(String message, String client_name) throws IOException {
        output.writeObject(new Packet(message, client_name));
    }


    public Listener(Socket client){
        this.client = client;
    }
}
