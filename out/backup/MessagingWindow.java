import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagingWindow extends JFrame {

    public JTextArea convoField;
    private JTextField messageField;
    private JButton submit;
    public String message, address, port;

    public MessagingWindow(String message, String address, String port){
//        super("Messaging with" + address);
        this.message = message;
        this.address = address;
        this.port = port;

        setLayout(new FlowLayout());

        convoField = new JTextArea(12,30);
        messageField = new JTextField(20);
        submit = new JButton("Submit");

        add(convoField);
        add(messageField);
        add(submit);
        thehandler handler = new thehandler();
        submit.addActionListener(handler);
    }

    private class thehandler implements ActionListener {
        public void actionPerformed(ActionEvent event){
            String str = messageField.getText();
            if(event.getSource() ==  submit) {
                convoField.append("me: " + str + "\n");
                int pt = Integer.parseInt(port);

                Driver.sendMessage(address,pt, str);
            }

            messageField.setText("");
        }
    }
    public void insertMsg(String s){
        convoField.append("them: " + s + "\n");
    }
}
