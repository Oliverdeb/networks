import java.io.*;
import java.net.Socket;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class Listener extends Thread {
    private Socket client;

    private DataInputStream input;
    private DataOutputStream output;

    public void run(){
        while (true){
            byte[] inp = new byte[1000];

            try {
                input.read(inp);
                System.out.println("Read and received byte from client");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Packet packet = null;

            try {
                packet = Util.deserialize(inp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(packet.getUser() + ": " + packet.getMessage());

            try {
                output.writeBytes("RECEIVED");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Listener(Socket client){

        try {
            this.input = new DataInputStream(client.getInputStream());
            this.output = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client = client;

    }
}
