import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dbroli001 on 2017/03/28.
 */

public class Client {

    private Socket client_socket = null;
    private Scanner c = new Scanner(System.in);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

    public static void main(String[] args) {
        new Client();
    }


    public void send_messsage(String message, String name) throws IOException {
        //Util.serialize(message, name)
        output.writeObject(new Packet(message, name));
    }

    public Client(){

        try{
            client_socket = new Socket("localhost", 9600);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Connected to server at " + client_socket.getInetAddress());
        System.out.print("Please enter your name: ");
        name = c.nextLine();


        try {
            output = new ObjectOutputStream(client_socket.getOutputStream());

            input = new ObjectInputStream(client_socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Send client name intially
            send_messsage("", name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.print("You: ");

            try {
                send_messsage(c.nextLine(), name);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] inp = new byte[1000];
            Packet packet = null;
            System.out.println("stuck here?");
            try {

                packet = (Packet) input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("nope got passed");

//            Packet packet = null;
//            try {
//                packet = Util.deserialize(inp);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }

            if (packet.getUser().equals("server")){

                System.out.println(packet.getMessage());
            }else{
                System.out.println(packet.getUser()+ ": " + packet.getMessage());
            }


        }
    }
}
