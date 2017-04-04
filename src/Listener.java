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
                String[] message_segments = packet.getMessage().split(" ");

                try {
                    if (message_segments.length < 2) {
                        send_message("Incorrect usage.\n/w <user> <msg>", "server", Util.time_now());
                    } else if (findClient(message_segments[1]) == null) {
                        send_message("Couldn't find user " + message_segments[1], "server", Util.time_now());
                    }else{
                    /*
                    Sending the actual whisper message
                     */
                    Listener client = findClient(message_segments[1]);
                    client.send_message(
                            Util.parse_message(packet.getMessage(), message_segments[1]),
                            message_segments[1],
                            Util.time_now(),
                            "PM"
                    );
                    }
                }catch (IOException io){
                    io.printStackTrace();
                }



            } else if (packet.getMessage().startsWith("/file ")){

                imageHandler(packet);

            } else if (packet.getMessage().startsWith("/quit")) {

            }else{
                for (Listener client : Server.clients){
                    if (client.equals(this)) {
                        continue;
                    }else{
                        try {
                            client.send_message(packet.getMessage(), packet.getUser(), Util.time_now());
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

    public void imageHandler(Packet packet){
        File file = new File(Util.parse_location(packet.getMessage()));


    }

    public void send_connected_users() throws IOException {
        /*
        Sends a list of the currently connected users.
         */
        String msg =    "Connected users\n" +
                        "---------------";
        for (int i = 0; i < Server.clients.size(); i++) {
            msg += "\n" + i + ": " + Server.clients.get(i).getClientName();
        }
        send_message(msg, "server", Util.time_now());
    }

    public void send_message(String message, String client_name, String time) throws IOException {
        output.writeObject(new Packet(message, client_name, time));
    }

    public void send_message(String message, String client_name, String time, String type) throws IOException {
        output.writeObject(new Packet(message, client_name, time, type));
    }


    public Listener findClient(String user_name){
        for (Listener client : Server.clients){
            if (user_name.equalsIgnoreCase(client.getClientName())){
                return client;
            }
        }
        return null;
    }

    public Listener(Socket client){
        this.client = client;
    }
}
