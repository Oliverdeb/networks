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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (packet.getMessage().startsWith("/w ")){
                // handle whisper
            } else {
                for (Listener client : Server.clients){
                    if (client.equals(this)) {
                        continue;
                    }else{
                        try {
                            client.send_message(packet.getMessage(), packet.getUser(), TimeUtil.time_now());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println(packet.getTime() + " " +packet.getUser() + ": " + packet.getMessage());
            System.out.flush();

        }
    }

    public void send_message(String message, String client_name, String time) throws IOException {
        output.writeObject(new Packet(message, client_name, time));
    }


    public Listener(Socket client){
        this.client = client;
    }
}
