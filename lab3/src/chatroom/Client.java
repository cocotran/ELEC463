package chatroom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private static Socket clientSocket;
    private static JTextArea receivedTextArea;
    private static JTextArea sendTextArea;
    private static String clientName = "";

    public static void main(String[] args) throws Exception {

    // CONNECTION ROW STARTS
        // Create the GUI frame and components
        JFrame frame = new JFrame ("Chatting Client");
        frame.setLayout(null);
        frame.setBounds(100, 100, 480, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Client Name label
        JLabel clientNameLabel = new JLabel("Client Name");
        clientNameLabel.setBounds(20, 40, 150, 30);
        frame.getContentPane().add(clientNameLabel);

        // Client name input field
        JTextField clientNameTextField = new JTextField();
        clientNameTextField.setBounds(130, 40, 150, 30);
        frame.getContentPane().add(clientNameTextField);

        // Connect/Disconnect button
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(330, 40, 100, 30);
        frame.getContentPane().add(connectButton);
    // CONNECTION ROW ENDS


    // CHAT ROOM STARTS
        receivedTextArea = new JTextArea();
        receivedTextArea.setBounds(20, 100, 420, 300);
        receivedTextArea.setEditable(false);
        frame.getContentPane().add(receivedTextArea);
        receivedTextArea.setVisible(false);

        JScrollPane receivedTextAreaScroll = new JScrollPane(receivedTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        receivedTextAreaScroll.setBounds(20,  100,  420,  300);
        frame.getContentPane().add(receivedTextAreaScroll);
        receivedTextAreaScroll.setVisible(false);
    // CHAT ROOM ENDS


    // SEND TO ROW STARTS
        // Send To label
        JLabel sendToLabel = new JLabel("Send to");
        sendToLabel.setBounds(20, 430, 150, 30);
        frame.getContentPane().add(sendToLabel);
        sendToLabel.setVisible(false);

        // Send To input field
        JTextField sendToTextField = new JTextField();
        sendToTextField.setBounds(130, 430, 150, 30);
        frame.getContentPane().add(sendToTextField);
        sendToTextField.setVisible(false);
    // SEND TO ROW ENDS

    // SEND TEXT STARTS
        // send text area
        sendTextArea = new JTextArea();
        sendTextArea.setBounds(20, 480, 300, 300);
        frame.getContentPane().add(sendTextArea);
        sendTextArea.setVisible(false);

        // Send button
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(330, 500, 80, 30);
        frame.getContentPane().add(sendButton);
        sendButton.setVisible(false);
    // SEND TEXT ENDS


    // BUTTONS ACTION LISTENER FUNCTIONS STARTS
        // Action listener when connect button is pressed
        connectButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try {

                    if (connectButton.getText().equals("Connect")) { //if pressed to connect

                        clientName = clientNameTextField.getText();

                        if (!clientName.equals("")) {
                            // create a new socket to connect with the server application
                            clientSocket = new Socket ("localhost", 6789);

                            // call function StartThread
                            StartThread();

                            //make the GUI components visible, so the client can send and receive messages
                            clientNameTextField.setEditable(false);
                            receivedTextArea.setVisible(true);
                            receivedTextAreaScroll.setVisible(true);
                            sendToLabel.setVisible(true);
                            sendToTextField.setVisible(true);
                            sendTextArea.setVisible(true);
                            sendButton.setVisible(true);

                            // change the Connect button text to disconnect
                            connectButton.setText("Disconnect");
                        }

                    } else { // if pressed to disconnect

                        // create an output stream and send a Remove message to disconnect from the server
                        DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
                        outToServer.writeBytes("-Remove\n");

                        // close the client's socket
                        clientSocket.close();

                        clientName = "";

                        // make the GUI components invisible
                        clientNameTextField.setEditable(true);
                        receivedTextArea.setVisible(false);
                        receivedTextAreaScroll.setVisible(false);
                        sendToLabel.setVisible(false);
                        sendToTextField.setVisible(false);
                        sendTextArea.setVisible(false);
                        sendButton.setVisible(false);

                        // change the Connect button text to connect
                        connectButton.setText("Connect");
                    }

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }});


        // Disconnect on close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {

                try {

                    // create an output stream and send a Remove message to disconnect from the server
                    DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
                    outToServer.writeBytes("-Remove\n");

                    // close the client's socket
                    clientSocket.close();

                    clientName = "";

                    // make the GUI components invisible
                    clientNameTextField.setEditable(true);
                    receivedTextArea.setVisible(false);
                    receivedTextAreaScroll.setVisible(false);
                    sendToLabel.setVisible(false);
                    sendToTextField.setVisible(false);
                    sendTextArea.setVisible(false);
                    sendButton.setVisible(false);

                    // change the Connect button text to connect
                    connectButton.setText("Connect");

                    System.exit(0);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }


            }
        });

        // send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String toClient;
                    if (sendToTextField.getText().equals(""))
                        toClient = "ALL_CLIENTS";
                    else
                        toClient = sendToTextField.getText();

                    DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
                    outToServer.writeBytes("-Message," + sendTextArea.getText() + "," + toClient + "," + clientName + "\n");

                    sendTextArea.setText("");

                } catch (Exception er) {}
            }
        });

        frame.setVisible(true);

    }

    // BUTTONS ACTION LISTENER FUNCTIONS ENDS



    // Thread to always read messages from the server and print them in the textArea
    private static void StartThread() {

        new Thread (new Runnable(){ @Override
        public void run() {

            try {

                // create a buffer reader and connect it to the socket's input stream
                BufferedReader inFromServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
                String receivedSentence;

                //create an output stream
                DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());

                // always read received messages and append them to the textArea
                while (true) {

                    receivedSentence = inFromServer.readLine();
                     System.out.println(receivedSentence);

                    if (receivedSentence.startsWith("-Connected")) {
                        outToServer.writeBytes("-Name," + clientName + "\n");
                    }

                    else if (receivedSentence.startsWith("-Joined")) {
                        String []strings = receivedSentence.split(",");

                        if (strings[1].equals(clientName)) {
                            receivedTextArea.setText("You are connected." + "\n");
                        }

                        else {
                            receivedTextArea.append(strings[1] + " is connected." + "\n");
                        }
                    }

                    else if (receivedSentence.startsWith("-Message")) {
                        String []strings = receivedSentence.split(",");

                        if (strings[2].equals(clientName)) {
                            receivedTextArea.append("You: " + strings[1] + "\n");
                        }
                        else
                            receivedTextArea.append(strings[2] + ": " + strings[1] + "\n");
                    }

                }

            }
            catch(Exception ex) {

            }


        }}).start();
    }


}
