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
    private static int fixedPort;


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
                }else{
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

    static void createNewMSGWindow(String ad){
        MessagingWindow msWindow = new MessagingWindow(ad);
        msWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        msWindow.setSize(400,300);
        msWindow.setVisible(true);
        msWindow.setTitle(ad);
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
