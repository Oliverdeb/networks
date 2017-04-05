import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
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
    private volatile boolean flag = false;
    private volatile boolean recv_list = false;
    private volatile boolean waitingForLoc = false;
    private String confirmation;
    private String location;

    public static void main(String[] args) {
        new Client();
        
    }


    public void send_messsage(String message, String name, String time, String type) throws IOException {
        output.writeObject(new Packet(message, name, time, type));
    }

    public void send_messsage(String message, String name, String time, String type, byte[] imgBytes, String fileName) throws IOException {
        output.writeObject(new Packet(message, name, time, type, imgBytes, fileName));
    }

    public void save_file(Packet packet){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(packet.getFileName());
            fos.write(packet.getImgBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_help(){
        String cmds = new String(
                        "\nType any message and press enter to send a message to everyone connected to the server (in the chatroom)\n"+
                        "\n/w\t<user_name> \t\t\t- to send a private message to a user. e.g /w Oliver hello, how are you?\n" +
                        "/help \t\t\t\t\t- to get this list of commands back\n" +
                        "/file\t<path/to/file> \t\t\t- sends a file to everyone in the chatroom.\n" +
                        "/wfile\t<user_name> <path/to/file> \t- sends a file to someone privately\n" +
                        "/quit \t\t\t\t\t- to quit\n"
        );
        System.out.println(cmds);
    }


    public Client(){
        System.out.println("Enter server port (enter nothing for 9600 default): ");
        String str_port = cInputScanner.nextLine();
        System.out.println("Enter server IP address (enter nothing for default localhost):");
        String ip = cInputScanner.nextLine();

        int port = 0;
        if (str_port.equals("")){
            port = 9600;
        }else{
            port = Integer.parseInt(str_port);
        }
        if (ip.equalsIgnoreCase("")){
            ip = "localhost";
        }
        try{
            client_socket = new Socket(ip, port);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Connected to server at " + client_socket.getInetAddress());

        print_help();

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
            send_messsage("", name, Util.time_now(), "client_name");
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
                        if (packet.getMessage().contains("Connected")){
                            recv_list = true;
                        }
                        String type = !flag ? "" : "\n" ;

                        if (packet.getType() != null)
                        {
                            System.out.println("?/");
                            if (packet.getType().equalsIgnoreCase("file_request")){
                                System.out.println(type+packet.getMessage());
                                while (flag){}
                                String resp = confirmation;
                                String response = resp.equalsIgnoreCase("Y") ? "accept_transfer" : "decline_transfer";
                                try{
                                    send_messsage(response, name, "", response);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            System.out.println(type+packet.getMessage());
                        }

                    }else{
                        // For normal message output
                        boolean out = false;
                        if (packet.getType() != null){
                            if(packet.getType().equalsIgnoreCase("file_transfer") || packet.getType().equalsIgnoreCase("file_transfer_pm")){
                                // code to ask for location.
                                out = true;
                                FileOutputStream fos = null;
                                waitingForLoc = true;
                                while(waitingForLoc){}
                                String fileLoc = location;

                                try {
                                    fos = new FileOutputStream(location);
                                    fos.write(packet.getImgBytes());
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (!out){
                            String type = packet.getType() == null ? "" : "[" + packet.getType() + "]";
                            type = !flag ? type : "\n" + type;
                            System.out.println(type + packet.getTime() + " " + packet.getUser()+ ": " + packet.getMessage());
                        }

                    }
                }

            }
        };
        outputHandler.start();

        while (true) {
            while (!recv_list){}
            if (waitingForLoc){
                System.out.print("Enter a path and filename e.g (/path/to/file.jpeg): ");
            }else{
                System.out.print("You: ");
            }
            flag = true;

            try {
                // Send message to server
                String msg = cInputScanner.nextLine();
                if (waitingForLoc){
                    location = msg;
                    waitingForLoc = false;
                    continue;
                }

                if (msg.equalsIgnoreCase("Y") || msg.equalsIgnoreCase("N")){
                    if (msg.equalsIgnoreCase("Y")){
                        waitingForLoc = true;

                    }
                    confirmation = msg;
                    flag = false;

                }else {
                    flag = false;

                    if (msg.equalsIgnoreCase("/help")) {
                        print_help();
                    } else if (msg.startsWith("/wfile")) {
                        String[] split = msg.split(" ");
                        File f = new File(split[2]);
                        System.out.println("Attempting to send " + f.getName() + " to " + split[1]);
                        byte[] bytes = null;
                        boolean correctInput = f.exists();

                        while (!correctInput) {
                            System.out.println("File not found, please enter the correct path. \n/wfile <user> <path/to/file>");
                            System.out.print("You: ");
                            msg = cInputScanner.nextLine();
                            f = new File(msg.split(" ")[2]);
                            correctInput = f.exists();
                        }
                        bytes = Files.readAllBytes(f.toPath());
                        send_messsage(split[1], name, null, "file_transfer_pm", bytes, f.getName());


                    } else if (msg.startsWith("/file")) {
                        File f = new File(Util.parse_location(msg));
                        byte[] bytes = null;
                        boolean correctInput = f.exists();

                        while (!correctInput) {
                            System.out.println("File not found, please enter the correct path. \n/file <path/to/file>");
                            System.out.print("You: ");
                            msg = cInputScanner.nextLine();
                            f = new File(msg.split(" ")[1]);
                            correctInput = f.exists();
                        }
                        bytes = Files.readAllBytes(f.toPath());


                        send_messsage("", name, null, "file_transfer", bytes, f.getName());
                    } else if (msg.startsWith("/quit")) {
                        send_messsage(msg, name, Util.time_now(), "message");
                        System.exit(0);

                    } else {
                        send_messsage(msg, name, Util.time_now(), "message");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
