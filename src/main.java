import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dbroli001 on 2017/03/28.
 */
public class main {

    public static void main(String[] args) {
        System.out.print("testing: ");
        Scanner c = new Scanner(System.in);
        System.out.println(c.next());
        // check for new messages on queue?
        try {
            Socket sock = new Socket("test", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
