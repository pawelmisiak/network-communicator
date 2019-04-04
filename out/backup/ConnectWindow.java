import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectWindow extends JFrame {

    private JLabel iplabel,portLabel;
    private JTextField ipAddress, port;
    private JButton submit;

    public ConnectWindow(){
        super("Connect");
        setLayout(new FlowLayout());

        iplabel = new JLabel("IP address");
        ipAddress = new JTextField(20);
        ipAddress.setText("192.168.1.");
        portLabel = new JLabel("Port");
        port = new JTextField(8);
        port.setText("64000");
        submit = new JButton("Submit");

        add(iplabel);
        add(ipAddress);
        add(portLabel);
        add(port);
        add(submit);
        thehandler handler = new thehandler();
        submit.addActionListener(handler);
    }

    private class thehandler implements ActionListener{
        public void actionPerformed(ActionEvent event){
            String str = "";
            if(event.getSource() ==  submit) {

//                Driver.message = port.getText();
//                Driver.address = ipAddress.getText();
//
                Driver.createNewMSGWindow("", ipAddress.getText(), port.getText());
            }
        }
    }
}
