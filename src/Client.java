import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Client {

    private Socket client_socket = null;
    private Scanner c = new Scanner(System.in);
    private DataInputStream input;
    private DataOutputStream output;
    private String name;

    public static void main(String[] args) {
        new Client();
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
            input = new DataInputStream(client_socket.getInputStream());
            output = new DataOutputStream(client_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.print("You: ");

            try {

                output.write(Util.serialize(c.nextLine(), name));

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                inp = input.read;
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Server: " + inp.toString());


        }
    }
}
