import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class MessagingWindow extends JFrame {

    private JTextArea convoField;
    private JTextField messageField;
    private JScrollPane sp;
    private JButton submit;
    String address, name;

     MessagingWindow(String name, String address){
        this.address = address;
        this.name = name;

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
//        messageField.addActionListener(handler);
        submit.addActionListener(handler);
    }


    private class thehandler implements ActionListener {
        public void actionPerformed(ActionEvent event){
            String str = messageField.getText();
            if(event.getSource() ==  submit) {
                convoField.append("Me: " + str + "\n");
//                String[] strings = address.split(" ");
//                System.out.println(" I am sending this " + strings[1]);
//                Driver.sendMessage(strings[1], str);

                Driver.sendMessage(address, str);
            }
            messageField.setText("");
        }
    }
    public void insertMsg(String s){
        convoField.append(name + ": " + s + "\n");
    }
}

