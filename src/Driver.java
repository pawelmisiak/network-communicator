import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Driver {


    private static Socket mySocket;
    private static InetAddress convertedAdderss;
    private static ArrayList<MessagingWindow> currentConnectionArray;
    private static int index;
    private static ConnectWindow newWindow;
    private static String myName = "Pawel";


    public static void main(String[] args) {
        mySocket = new Socket(64000, Socket.SocketType.Broadcast);

        currentConnectionArray = new ArrayList<>();

        newWindow = new ConnectWindow();
        newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newWindow.setSize(350,100);
        newWindow.setVisible(true);

        System.out.println("My Address = " + mySocket.getAddress().getHostAddress());
        System.out.println("My Port = " + mySocket.getPortNumber());

        boolean gotMessage = false;
        while (gotMessage == false) {

            DatagramPacket inPacket = mySocket.receive();

            if (inPacket != null) {
                byte[] inBuffer = inPacket.getData();
                String inMessage = new String(inBuffer);
                InetAddress senderAddress = inPacket.getAddress();
                int senderPort = inPacket.getPort();


                if (checkIfConnected(senderAddress.toString(), senderPort)){
                    currentConnectionArray.get(index).insertMsg(inMessage.trim());
                }else{
                    createNewMSGWindow(senderAddress.toString(), Integer.toString(senderPort));
                    currentConnectionArray.get(currentConnectionArray.size()-1).insertMsg(inMessage.trim());
                }

                System.out.println("Received Message = " + inMessage);
                System.out.println("Sender Address = " + senderAddress.getHostAddress());
                System.out.println("Sender Port = " + senderPort);
            }
        }
        System.out.println("Loop Thread has exited");
        System.out.println("finished execution");
//        mySocket.close(); // TODO: ADD THIS LATER TO CLOSING WINDOW OPTION
    }
    private static boolean checkIfConnected(String addr, int por){
        String txt = addr.replace("/", "") + por;

        for (int i = 0; i < currentConnectionArray.size(); i++){
            String txt2 = currentConnectionArray.get(i).address + currentConnectionArray.get(i).port;
            if (txt2.equals(txt)){

                System.out.println("returned true");
                index = i;
                return true;
            }
        }

        System.out.println("returned false");
        return false;
    }

    public static void sendMessage(String ip, int pt, String msg){
        try {
            convertedAdderss = InetAddress.getByName(ip);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(-1);
        }
        mySocket.send(msg, convertedAdderss, pt);
    }

    static void createNewMSGWindow(String ad, String pt){
        MessagingWindow msWindow = new MessagingWindow(ad,pt);
        msWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        msWindow.setSize(400,300);
        msWindow.setVisible(true);
        msWindow.setTitle(ad + " on port " + pt);
        currentConnectionArray.add(msWindow);
    }

    static void closeAllWindows(){
        currentConnectionArray.forEach((o) -> {
            o.dispatchEvent(new WindowEvent(o, WindowEvent.WINDOW_CLOSING));
        });
        mySocket.close();
        newWindow.dispatchEvent(new WindowEvent(newWindow, WindowEvent.WINDOW_CLOSING));
        System.exit(0);
    }
}
