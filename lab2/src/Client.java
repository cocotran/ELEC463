import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client {

    private static JFrame frame = new JFrame();
    static boolean isConnected = false;
    private static Socket clientSocket;
    static int step;

    public static void main(String[] args) {
        Client client = new Client();
        step = 0;

//		create frame
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("TCP Calculator");
        frame.getContentPane().setLayout(null); // NOTE: this might cause bugs, textareas and buttons might not appear until mouse over.
        frame.setVisible(true);

        client.displayConnectStatus();

//		Connect button
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(330, 25, 100, 30);
        frame.getContentPane().add(connectButton);
        connectButton.addActionListener(new ConnectListener());

        if (isConnected && step >= 1) {
            client.displayCountOfNumber();
        }

        if (step >= 2) {
            client.displayAddNumbers();
        }
    }

    public void displayConnectStatus() {
        JLabel label = new JLabel("");
        label.setBounds(50, 30, 200, 20);
        label.setFont(new Font("Times", Font.PLAIN, 12));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(label);
        if (!this.isConnected) {
            label.setForeground(Color.red);
            label.setText("Connection Status: Not Connected");
        }
        else {
            label.setForeground(Color.blue);
            label.setText("Connection Status: Connected");
        }
    }

    public static void connect() {
        try {
            clientSocket = new Socket ("localhost", Server.port);
            isConnected = true;
            step = 1;
        } catch (IOException e) {
            System.out.println("Error: " + e);;
        }
    }

    public static void disconnect() {
        try {
            clientSocket.close();
            isConnected = false;
            step = 0;
        } catch (IOException e) {
            System.out.println("Error: " + e);;
        }
    }

    public void displayCountOfNumber() {
        if (isConnected) {
//		    count numbers
            JLabel labelCountNumber = new JLabel("Counts of Numbers:");
            labelCountNumber.setBounds(50, 100, 200, 20);
            labelCountNumber.setHorizontalAlignment(SwingConstants.LEFT);
            labelCountNumber.setVerticalAlignment(SwingConstants.CENTER);
            frame.getContentPane().add(labelCountNumber);

//		    count of numbers input
            JTextField countOfNumbers = new JTextField("");
            countOfNumbers.setBounds(200, 100, 100, 30);
            frame.getContentPane().add(countOfNumbers);

//		    set button
            JButton setButton = new JButton("Set");
            setButton.setBounds(330, 100, 100, 30);
            frame.getContentPane().add(setButton);

            if (step > 1) {
                // TODO: disable it
            }
            else {
                labelCountNumber.setFont(new Font("Times", Font.BOLD, 12));
                countOfNumbers.setFont(new Font("Times", Font.PLAIN, 12));
            }
        }
    }

    public void displayAddNumbers() {
//		add numbers
        JLabel labelAddNumber = new JLabel("Add Numbers to Message:");
        labelAddNumber.setBounds(50, 150, 200, 20);
        labelAddNumber.setFont(new Font("Times", Font.PLAIN, 12));
        labelAddNumber.setHorizontalAlignment(SwingConstants.LEFT);
        labelAddNumber.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(labelAddNumber);

//		add numbers input
        JTextField addNumber = new JTextField("");
        addNumber.setFont(new Font("Times", Font.PLAIN, 12));
        addNumber.setBounds(200, 150, 100, 30);
        frame.getContentPane().add(addNumber);

//		add button
        JButton addButton = new JButton("Add");
        addButton.setBounds(330, 150, 100, 30);
        frame.getContentPane().add(addButton);;
    }
}

class ConnectListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Client.isConnected)
            Client.disconnect();
        else
            Client.connect();
    }
}
