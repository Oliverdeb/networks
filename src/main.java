import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class main {

    /**
     * Implement stack for new messages
     * @param args
     */
    public static void main(String[] args) {

        System.out.print("Client starting up");

        try {
            Socket sock = new Socket("test", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Socket OK.");
    }



}
