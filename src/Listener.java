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
    private Packet tosend;

    public void setClientName(String name){
        this.name = name;
    }

    public String getClientName(){
        return this.name;
    }

    public void handle_file_sending(Packet packet) throws IOException {
        try {
            send_message(packet.getUser() + " wants to send you a file. Accept? (Y/N):", "server", "", "file_request");
            tosend = new Packet(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void run(){
        boolean done = false;
        while (true){
            if (done){break;}

            Packet packet = null;

            try {
                packet = (Packet)input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            switch (packet.getType()){
                case "file_transfer":
                    System.out.println("File transfer request received");

                    /*
                    Send to all clients
                    check for individual pm file sending
                     */

                    Server.send_files(packet);
                    break;

                case "file_transfer_pm":
                    /*
                    Send to all clients
                    check for individual pm file sending

                     */

                    for (Listener client : Server.clients){
                        if (client.getClientName().equalsIgnoreCase(packet.getMessage())){
                            try {
                                client.handle_file_sending(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }


                    break;

                case "accept_transfer":
                    System.out.println("initiating send");
//                    System.out.println(tosend.getUser() + tosend.getMessage() + tosend.getFileName() + tosend.getType());
                    try {

                        send_message(tosend);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "decline_transfer":
                    System.out.println("notifying not sending");
                        for (Listener client : Server.clients){
//                            System.out.println(tosend.getUser() + client.getClientName());
                            if (client.getClientName().equalsIgnoreCase(tosend.getUser())){
                                System.out.println("SENDING");
                                try {
                                    client.send_message(packet.getUser() + " declined to view your file.", "server", "");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }

                        }
                        break;

                case "message":
                    if (packet.getMessage().startsWith("/w ")){
                        String[] message_segments = packet.getMessage().split(" ");

                        try {
                            if (message_segments.length < 2) {
                                send_message("Incorrect usage.-w <user> <msg>", "server", Util.time_now());
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
                    } else if (packet.getMessage().startsWith("/quit")) {
                        try {
                            Server.relay_message(this, getClientName() + " left the room.", "server");
                            System.out.println("Trying to close");
//                            this.input.close();
//                            this.output.close();
                            this.client.close();
                            System.out.println("closed socket");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Server.clients.remove(this);
                        done = true;
                        break;
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

                    break;
                default:
                    break;
            }



        }
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

    public void send_message(Packet packet) throws IOException{
        output.writeObject(packet);
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
