import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Client {

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();
        frame.setBounds(600, 200, 600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("TCP Client");
        frame.setLayout(null);
        frame.setVisible(true);

        //connection status label
        JLabel statusLabel = new JLabel("Connection Status: Not Connected");
        statusLabel.setForeground(Color.red);
        statusLabel.setBounds(20, 20, 500, 20);
        statusLabel.setFont(new Font("Times", Font.BOLD, 14));
        frame.getContentPane().add(statusLabel);

        //received messages from server in textarea
        JTextArea recMessages = new JTextArea();
        recMessages.setBounds(20, 100, 540, 430);
        frame.getContentPane().add(recMessages);
        recMessages.setText("Messages received from Server:\n");

        //button to connect or disconnect
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(150, 50, 300, 30);
        connectButton.setFont(new Font("Times", Font.BOLD, 26));
        frame.getContentPane().add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(connectButton.getText().equals("Disconnect")) { //if disconnecting
                    connectButton.setText("Connect");
                    statusLabel.setText("Connection Status: Not Connected");
                    statusLabel.setForeground(Color.red);
                }
                else { //if connecting
                    connectButton.setText("Disconnect");
                    connect(statusLabel, recMessages);
                }
            }
        });

    }

    public static void connect(JLabel statusLabel, JTextArea recMessages) {
        try {

            //ip address 127.0.0.1 or local address can be used //or on mac/linux loopbackip address
            Socket clientSocket = new Socket("localhost", 6789);

            statusLabel.setText("Connection Status: Connected");
            statusLabel.setForeground(Color.blue);

            //send msg to server
            DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());

            String sentence = "Connection Request, "
                    + "Client Port Number: " + clientSocket.getLocalPort()
                    + ", Client IP Address: " + clientSocket.getLocalAddress()
                    + ", Server Port: " + clientSocket.getPort() + '\n'; //have to add \n since using readLine in recSentence

            outputToServer.writeBytes(sentence);

            //read msg from server
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String recSentence = inputFromServer.readLine();
            System.out.println("From Server: " + recSentence);
            recMessages.append(recSentence + '\n');
        }
        catch(Exception ex) {

        }
    }

}