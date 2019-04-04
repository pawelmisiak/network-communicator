import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Driver {


    public static Socket mySocket;
    private static InetAddress convertedAdderss;
    private static ArrayList<MessagingWindow> currentConnectionArray;
    public static boolean flag = false;
    public static String message, address;


    public static void main(String[] args) {
        mySocket = new Socket(64000, Socket.SocketType.NoBroadcast);

        currentConnectionArray = new ArrayList<>();

        ConnectWindow newWindow = new ConnectWindow();
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setSize(350,100);
        newWindow.setVisible(true);

//        MessagingWindow msWindow = new MessagingWindow();
//        msWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        msWindow.setSize(400,500);
//        msWindow.setVisible(true);

        System.out.println("My Address = " + mySocket.getAddress().getHostAddress());
        System.out.println("My Port = " + mySocket.getPortNumber());

//        mySocket.send("hello there", fixedAddress, 64000);
//        try { // to slow down the loop
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException ie) {
//            ie.printStackTrace();
//            System.exit(-1);
//        }

        boolean gotMessage = false;
        while (gotMessage == false) {

            DatagramPacket inPacket = mySocket.receive();

            if (inPacket != null) {
                        try { // to slow down the loop
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(-1);
        }

                byte[] inBuffer = inPacket.getData();
                String inMessage = new String(inBuffer);
                InetAddress senderAddress = inPacket.getAddress();
                int senderPort = inPacket.getPort();

                if (currentConnectionArray.contains(MessagingWindow(senderAddress,senderPort,)))

//                msWindow.insertMsg(inMessage.trim());

                System.out.println("Received Message = " + inMessage);
                System.out.println("Sender Address = " + senderAddress.getHostAddress());
                System.out.println("Sender Port = " + senderPort);
            }
        }
        System.out.println("Loop Thread has exited");



        System.out.println("finished execution");
//        mySocket.close(); // TODO: ADD THIS LATER TO CLOSING WINDOW OPTION
    }
    public static void sendMessage(String ip, int pt, String msg){
        System.out.println("The message is " + msg);
        try {
            convertedAdderss = InetAddress.getByName(ip);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(-1);
        }
        mySocket.send(msg, convertedAdderss, pt);
        System.out.println("Message was sent to: " + ip + " with port " + pt);
    }

    public static void createNewMSGWindow(String msg, String ad, String pt){
        MessagingWindow msWindow = new MessagingWindow(msg,ad,pt);
        msWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        msWindow.setSize(400,300);
        msWindow.setVisible(true);
        msWindow.setTitle(ad + " on port " + pt);
        currentConnectionArray.add(msWindow);
        System.out.println(currentConnectionArray.size());
    }
}
