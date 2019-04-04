import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 class MessagingWindow extends JFrame {

    private JTextArea convoField;
    private JTextField messageField;
    private JScrollPane sp;
    private JButton submit;
    String address;
    String port;

     MessagingWindow(String address, String port){
        this.address = address;
        this.port = port;

        setLayout(new FlowLayout());

        convoField = new JTextArea(12,30);
        convoField.setEditable(false);
        sp = new JScrollPane(convoField);

        messageField = new JTextField(20);
        submit = new JButton("Submit");

        add(sp);
//        add(convoField);
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
                Driver.sendMessage(address.replace("/",""),pt, str);
            }
            messageField.setText("");
        }
    }
    public void insertMsg(String s){
        convoField.append("them: " + s + "\n");
    }
}

