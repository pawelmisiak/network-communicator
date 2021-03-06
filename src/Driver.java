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


        boolean gotMessage = false;
        while (gotMessage == false) {

            DatagramPacket inPacket = mySocket.receive();

            if (inPacket != null) {
                byte[] inBuffer = inPacket.getData();
                String inMessage = new String(inBuffer);
                InetAddress senderAddress = inPacket.getAddress();

                if (checkIfConnected(senderAddress.toString())){
                    System.out.println("if statement We are already Connected");
                    currentConnectionArray.get(index).insertMsg(inMessage.trim());
                }else if (checkIfRequestForMe(inMessage.trim(), senderAddress)){
                    System.out.println("if statement Request is For Me");
                    respondToTheirRequest(senderAddress);
                    String[] strings = inMessage.trim().split(" ");
                    createNewMSGWindow(strings[3], senderAddress.toString().replace("/", ""));
                    currentConnectionArray.get(currentConnectionArray.size()-1).insertMsg(" !!! STARTING NEW CHAT !!!");
                }else if (inMessage.trim().substring(0,5).equals("#####")) {
                    System.out.println("if statement Response to Request");
                    ResponseFromMyRequest(inMessage, senderAddress);
                }

            }
        }
        System.out.println("Loop Thread has exited");
        System.out.println("finished execution");
    }

/////////////////////////                            /////////////////////////
///////////////////////// Connection Request Methods /////////////////////////
/////////////////////////                            /////////////////////////


    //Todo: working on this now!!
    static void ResponseFromMyRequest(String msg, InetAddress senderAddress){
        System.out.println("got a response " + msg + "with address" + " " + senderAddress);
        String[] strings = msg.split(" ");
        createNewMSGWindow(strings[1], strings[3]);
    }


    private static boolean checkIfConnected(String addr){
        String txt = addr.replace("/", "");

        for (int i = 0; i < currentConnectionArray.size(); i++){
            String txt2 = currentConnectionArray.get(i).address;
            if (txt2.equals(txt)){
                index = i;
                return true;
            }
        }
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

    static void createNewMSGWindow(String name, String address){
        MessagingWindow msWindow = new MessagingWindow(name, address);
        msWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        msWindow.setSize(400,300);
        msWindow.setVisible(true);
        msWindow.setTitle(name + " - " + address);
        currentConnectionArray.add(msWindow);
    }

    ////////////////                            ////////////////
    //////////////// Foreign connection request ////////////////
    ////////////////                            ////////////////
    private static boolean checkIfRequestForMe(String msg, InetAddress senderAddress){
        String[] strings = msg.split(" ");

        if (strings[0].equals("?????") && (strings[1].toLowerCase().equals(myName.toLowerCase()))){
            System.out.println("This message is for me: " + msg);
            return true;
        }
        System.out.println("not for me!");
        return false;
    }
    private static void respondToTheirRequest(InetAddress senderAddress){
        String strToConnect1stPart = "##### ";
        String strToConnect2ndPart = " ##### ";
        String msg = strToConnect1stPart + myName + strToConnect2ndPart + mySocket.getAddress().getHostAddress();
        mySocket.send(msg, senderAddress, fixedPort);
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
//        System.out.println("sending connection Request!");
        mySocket.send(msg, twoFiveFiveAddress, fixedPort);
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
