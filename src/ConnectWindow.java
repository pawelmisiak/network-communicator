import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ConnectWindow extends JFrame {

    private JTextField name;
    private JButton connect, close;

    ConnectWindow(){
        super("Connect");
        setLayout(new FlowLayout());
        JLabel iplabel,portLabel;

        iplabel = new JLabel("Name");
        name = new JTextField(20);
        name.setText("Sameh");
        connect = new JButton("connect");
        close = new JButton("X");

        add(iplabel);
        add(name);
        add(connect);
        add(close);
        thehandler handler = new thehandler();
        connect.addActionListener(handler);
        close.addActionListener(handler);
    }

    private class thehandler implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(event.getSource() ==  connect) {
                Driver.connectRequest(name.getText());
            }else if(event.getSource() == close) {
                Driver.closeAllWindows();
            }
        }
    }
}
