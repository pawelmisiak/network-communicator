import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Driver {


    private static Socket mySocket;
    private static InetAddress convertedAdderss, twoFiveFiveAddress;
    private static ArrayList<MessagingWindow> currentConnectionArray;
    private static int index;
    private static ConnectWindow newWindow;
    private static String myName = "Pawel";
    private static int fixedPort = 64000;


    public static void main(String[] args) {
        mySocket = new Socket(fixedPort, Socket.SocketType.Broadcast);

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


                if (checkIfConnected(senderAddress.toString())){
                    currentConnectionArray.get(index).insertMsg(inMessage.trim());
                }else if (checkIfForMe(inMessage.trim())){
                    createNewMSGWindow(senderAddress.toString());
                    currentConnectionArray.get(currentConnectionArray.size()-1).insertMsg(inMessage.trim());
                }

                System.out.println("Received Message = " + inMessage);
                System.out.println("Sender Address = " + senderAddress.getHostAddress());
            }
        }
        System.out.println("Loop Thread has exited");
        System.out.println("finished execution");
    }

    private static boolean checkIfForMe(String msg){
        if (msg == "Pawel" || msg == "pawel"){
            return true;
        }
        System.out.println("Got the message!!");
        return false;
    }

    private static boolean checkIfConnected(String addr){
        String txt = addr.replace("/", "");

        for (int i = 0; i < currentConnectionArray.size(); i++){
            String txt2 = currentConnectionArray.get(i).address;
            if (txt2.equals(txt)){

                System.out.println("returned true");
                index = i;
                return true;
            }
        }

        System.out.println("returned false");
        return false;
    }

    public static void sendMessage(String ip, String msg){
        try {
            convertedAdderss = InetAddress.getByName(ip);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(-1);
        }
        mySocket.send(msg, convertedAdderss, fixedPort);
    }

    static void createNewMSGWindow(String name){
        MessagingWindow msWindow = new MessagingWindow(name);
        msWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        msWindow.setSize(400,300);
        msWindow.setVisible(true);
        msWindow.setTitle(name);
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

    static void connectRequest(String name){
        String strToConnect1stPart = "????? ";
        String strToConnect2ndPart = " ##### ";
        String msg = strToConnect1stPart + name + strToConnect2ndPart + myName;
        try {
            twoFiveFiveAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(-1);
        }
        mySocket.send(msg, twoFiveFiveAddress, fixedPort);
    }
}
