import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Server {

    private ServerSocket server_socket = null;
    public static ArrayList<Listener> clients = new ArrayList<>(5);


    public static void main(String[] args) {
        new Server();
    }

    public Server(){

       try{
            server_socket = new ServerSocket(9600);

        }catch (Exception e){
            e.printStackTrace();
        }

        Socket newclient = null;
        System.out.println("Server ready to accept connections");


        while(true){
            try{
                /*
                Accept new client connections and create a thread to handle them.
                 */
                newclient = server_socket.accept();
                System.out.println("NEW CONNECTION FROM " + newclient.getInetAddress());
                System.out.println("Starting listener for new client");
                Listener e = new Listener(newclient);
                e.input = new ObjectInputStream(newclient.getInputStream());
                e.output = new ObjectOutputStream(newclient.getOutputStream());

                broadcast_new_client(newclient, e);
                e.start();

                clients.add(e);


            }catch(Exception e){
                System.out.println("Many problems");
            }
        }


    }

    public void broadcast_new_client(Socket client, Listener listener){

        /*
        Broadcast to all connected clients when a new client joins the room
         */
        try {

            Packet p = (Packet)listener.input.readObject();
            listener.setClientName(p.getUser());
            relay_message( p.getUser()+ " joined the room.", "server", TimeUtil.time_now());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void relay_message(String message, String client_name, String time) throws IOException {

        /*
        Relay new message to all connected clients.
         */

        System.out.println(message);
        for(Listener client : clients) {
            // Loop through list of clients and send the message to them
            System.out.println("relaying to " + client.getClientName());
            client.send_message(message, client_name, time);
        }
    }

}
