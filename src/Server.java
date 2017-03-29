import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Server {

    private ServerSocket server_socket = null;
    private ArrayList<Listener> clients = new ArrayList<>(5);
    private DataInputStream input;
    private DataOutputStream output;


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
                newclient = server_socket.accept();
                System.out.println("NEW CONNECTION FROM " + newclient.getInetAddress());
                System.out.println("Starting listener for new client");
                Listener e = new Listener(newclient);
                e.start();
                clients.add(e);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


//        close_connections();
    }



}
