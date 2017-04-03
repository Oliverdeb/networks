import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by dbroli001 on 2017/03/28.
 */

public class Client {

    private Socket client_socket = null;
    private Scanner cInputScanner = new Scanner(System.in);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;
    private boolean writing = false;
    private Stack<String> mess =new Stack<String>();

    public static void main(String[] args) {
        new Client();
        
    }


    public void send_messsage(String message, String name, String time) throws IOException {
        output.writeObject(new Packet(message, name, time));
    }

    public Client(){

        try{
            client_socket = new Socket("localhost", 9600);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Connected to server at " + client_socket.getInetAddress());
        System.out.print("Please enter your name: ");
        name = cInputScanner.nextLine();


        try {
            output = new ObjectOutputStream(client_socket.getOutputStream());

            input = new ObjectInputStream(client_socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Send client name intially
            send_messsage("", name, Util.time_now());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread outputHandler = new Thread() {
            // Output thread
            public void run(){
                while(true){
                    Packet packet = null;

                    try {
                        packet = (Packet) input.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (packet.getUser().equals("server")){
                        // For User X has joined the room
                        System.out.println(packet.getMessage());
                    }else{
                        // For normal message output
                        String type = packet.getType() == null ? "" : "[" + packet.getType() + "]";
                        if (!writing ){
                           System.out.println(type + packet.getTime() + " " + packet.getUser()+ ": " + packet.getMessage());
                       }else{
                            mess.push(type + packet.getTime() + " " + packet.getUser()+ ": " + packet.getMessage());
                       }
                        
                    }
                }

            }
        };
        outputHandler.start();

        while (true) {
            System.out.println("Press enter to type message: ");
            cInputScanner.nextLine();
            writing = true;
            System.out.print("You: ");

            try {
                // Send message to server
                send_messsage(cInputScanner.nextLine(), name, Util.time_now());
                writing = false;
                if (mess.size() != 0){
                    while (mess.size() != 0){
                        System.out.println(mess.pop());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }
}
