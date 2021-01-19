import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

class ClientOne extends JFrame {
    private JButton sendButton;
    private JTextField fieldOne;
    private JTextArea areaOne;
    private Container c;

    String name = "Rashed";

    BufferedReader reader;
    BufferedWriter writer;

    ClientOne() {
        initComponent();
    }

    public void initComponent() {
        c = this.getContentPane();
        c.setLayout(null);


        areaOne = new JTextArea();
        areaOne.setBounds(0, 0, 400, 434);
        areaOne.setEditable(false);
        areaOne.setBackground(Color.orange);
        c.add(areaOne);


        fieldOne = new JTextField();
        fieldOne.setBounds(01, 434, 307, 30);
        c.add(fieldOne);

        sendButton = new JButton("Send");
        sendButton.setBounds(312, 434, 70, 30);
        c.add(sendButton);


        try {
            Socket socket = new Socket("127.0.0.1", 9090);

            InputStreamReader ireader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(ireader);

            OutputStreamWriter owriter = new OutputStreamWriter(socket.getOutputStream());
            writer = new BufferedWriter(owriter);


            writer.write(name + "\n" );
            writer.flush();

            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String msg = fieldOne.getText()+"\n";
                        fieldOne.setText("");
                        areaOne.setText(areaOne.getText());
                        writer.write(msg);
                        writer.flush();
                    } catch (IOException ea) {
                        ea.printStackTrace();
                    }
                }
            });



            Thread thread = new Thread(){
                public void run(){
                    try{
                        String line = reader.readLine() + "\n";
                        while (line != null){
                            areaOne.append(line);
                            line = reader.readLine() + "\n";
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}



public class MainOne{
    public static void main(String[] args) {
        ClientOne one = new ClientOne();
        one.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        one.setTitle("Chat Application");
        one.setResizable(false);
        one.setBounds(150,100,400,500);
        one.setVisible(true);
    }
}
