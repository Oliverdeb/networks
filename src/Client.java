import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dbroli001 on 2017/03/28.
 */

public class Client {

    private Socket client_socket = null;
    private Scanner cInputScanner = new Scanner(System.in);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

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
            send_messsage("", name, TimeUtil.time_now());
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
                        System.out.println(packet.getTime() + " " + packet.getUser()+ ": " + packet.getMessage());
                    }
                }

            }
        };
        outputHandler.start();

        while (true) {
            // input thread
            System.out.print("You: ");

            try {
                // Send message to server
                send_messsage(cInputScanner.nextLine(), name, TimeUtil.time_now());
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }
}
