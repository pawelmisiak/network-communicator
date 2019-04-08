import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ConnectWindow extends JFrame {

    private JTextField ipAddress, port;
    private JButton submit, close;

    ConnectWindow(){
        super("Connect");
        setLayout(new FlowLayout());
        JLabel iplabel,portLabel;

        iplabel = new JLabel("IP address");
        ipAddress = new JTextField(20);
        ipAddress.setText("192.168.1.");
        portLabel = new JLabel("Port");
        port = new JTextField(8);
        port.setText("64000");
        submit = new JButton("Submit");
        close = new JButton("X");

        add(iplabel);
        add(ipAddress);
        add(portLabel);
        add(port);
        add(submit);
        add(close);
        thehandler handler = new thehandler();
        submit.addActionListener(handler);
        close.addActionListener(handler);
    }

    private class thehandler implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(event.getSource() ==  submit) {
                Driver.createNewMSGWindow(ipAddress.getText());
            }else if(event.getSource() == close) {
                Driver.closeAllWindows();
            }
        }
    }
}
