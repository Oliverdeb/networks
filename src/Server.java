import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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


        Thread exit = new Thread(){
          public void run(){
              Scanner c = new Scanner(System.in);
              String inp = c.nextLine();
              while(!inp.equalsIgnoreCase("/quit")){
                  inp = c.nextLine();
              }
              try {
                  server_socket.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              System.exit(0);
          }
        };
        exit.start();

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

                broadcast_new_client(e, " joined");
                e.send_connected_users();
                e.start();

                clients.add(e);


            }catch(Exception e){
                System.out.println("Many problems");
            }
        }


    }

    public static void send_files(Packet packet){
        for(Listener client: clients){
            if(packet.getUser().equalsIgnoreCase(client.getClientName())) continue;
            try {
                client.handle_file_sending(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void broadcast_new_client(Listener listener, String action){

        /*
        Broadcast to all connected clients when a new client joins the room
         */
        try {

            Packet p = (Packet)listener.input.readObject();
            listener.setClientName(p.getUser());
            relay_message(listener, p.getUser() + action + " the room.", "server");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void relay_message(Listener listener, String message, String client_name) throws IOException {

        /*
        Relay new message to all connected clients.
         */

        System.out.println(message);
        for(Listener client : clients) {
            if (client.equals(listener)) continue;
            // Loop through list of clients and send the message to them
            System.out.println("relaying to " + client.getClientName());
            client.send_message(message, client_name, "");
        }
    }

}
