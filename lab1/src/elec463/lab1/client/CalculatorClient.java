// Quang Anh Tran - 40075748


package elec463.lab1.client;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;


public class CalculatorClient {
	
	public static void main(String[] args) throws IOException {
//		create frame
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 520, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Two Numbers Calculator");
		frame.getContentPane().setLayout(null); // NOTE: this might cause bugs, textareas and buttons might not appear until mouse over.
		frame.setVisible(true);
		
//		title
		JLabel label = new JLabel("");
		label.setBounds(20, 20, 200, 20);
		label.setFont(new Font("Times", Font.BOLD, 16));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label);
		label.setText("Two Numbers Calculator");
		
//		1st number
		JLabel firstNumber = new JLabel("");
		firstNumber.setBounds(20, 80, 200, 20);
		firstNumber.setFont(new Font("Times", Font.PLAIN, 12));
		firstNumber.setHorizontalAlignment(SwingConstants.LEFT);
		firstNumber.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(firstNumber);
		firstNumber.setText("First Number");
		
//		1st number input
		JTextField firstNumTextField = new JTextField("");
		firstNumTextField.setFont(new Font("Times", Font.PLAIN, 14));
		firstNumTextField.setBounds(120, 81, 150, 20);
		frame.getContentPane().add(firstNumTextField);
		
//		2nd number
		JLabel secondNumber = new JLabel("");
		secondNumber.setBounds(20, 120, 200, 20);
		secondNumber.setFont(new Font("Times", Font.PLAIN, 12));
		secondNumber.setHorizontalAlignment(SwingConstants.LEFT);
		secondNumber.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(secondNumber);
		secondNumber.setText("Second Number");
		
//		2nd number input
		JTextField secondNumTextField = new JTextField("");
		secondNumTextField.setFont(new Font("Times", Font.PLAIN, 14));
		secondNumTextField.setBounds(120, 121, 150, 20);
		frame.getContentPane().add(secondNumTextField);
		
//		display result
		JLabel result = new JLabel("");
		result.setBounds(20, 160, 200, 20);
		result.setFont(new Font("Times", Font.BOLD, 12));
		result.setHorizontalAlignment(SwingConstants.LEFT);
		result.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(result);
		
		
		
//		Buttons onClick Listener
//		https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
		class MyActionListener implements ActionListener {
			private String operationStr;
			
			public MyActionListener(String button) {
				this.operationStr = button;
			}
			
            public void actionPerformed(ActionEvent e) {
            	try { // check to see inputs are int or double
            		// https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
            		Double.parseDouble(firstNumTextField.getText());
            		Double.parseDouble(secondNumTextField.getText());
            		
            		// if two numbers are in correct format
            		try {
    					String answer = this.calculate();
    					
    					if (this.operationStr != "Max" && this.operationStr != "Min") {
    	            		result.setText("Answer:  " + firstNumTextField.getText() + "  " + this.operationStr + "  " + secondNumTextField.getText() + "  =  " + answer);
    	            	} else {
    	            		result.setText("Answer:  " + this.operationStr + "imum" + " is:  " + answer);
    					}
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
            	
            	// invalid inputs
				} catch (NumberFormatException e2) {
					result.setText("Please enter a valid number.");
				}
            }
            
            public void sendData(DatagramSocket clientSocket) throws IOException {
//        		Create an IP address and Port number
        		InetAddress IPAddress = InetAddress.getByName("localhost");
        		int port = 9876;
        		
//        		Send data to the server through an array of bytes and a Datagram packet:
        		byte[] data = new byte[1024];
        		String sendingSentence = firstNumTextField.getText() + this.operationStr + secondNumTextField.getText();
        		data = sendingSentence.getBytes();
        		DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, port);
        		clientSocket.send(packet);
			}
            
            public String receiveData(DatagramSocket clientSocket) throws IOException {
//        		Read received data, sent by the server, through an array of bytes and a Datagram packet:
        		byte[] data = new byte[1024];
        		DatagramPacket packet = new DatagramPacket(data, data.length);
        		clientSocket.receive(packet);
        		String receivedSentence = new String(packet.getData());
        		return receivedSentence;
			}
            
            public String calculate() throws IOException {
            	DatagramSocket clientSocket = new DatagramSocket();
            	this.sendData(clientSocket);
            	String resultString = this.receiveData(clientSocket);
            	clientSocket.close();
				return resultString;
			}
        }
		
		
//		plus button
		JButton plusButton = new JButton("+");
		plusButton.setBounds(20, 200, 60, 30);
		frame.getContentPane().add(plusButton);
		plusButton.addActionListener(new MyActionListener("+"));
		
//		minus button
		JButton minusButton = new JButton("-");
		minusButton.setBounds(100, 200, 60, 30);
		frame.getContentPane().add(minusButton);
		minusButton.addActionListener(new MyActionListener("-"));
		
//		multiply button
		JButton mulbButton = new JButton("x");
		mulbButton.setBounds(180, 200, 60, 30);
		frame.getContentPane().add(mulbButton);
		mulbButton.addActionListener(new MyActionListener("x"));
		
//		divide button
		JButton divideButton = new JButton("÷");
		divideButton.setBounds(260, 200, 60, 30);
		frame.getContentPane().add(divideButton);
		divideButton.addActionListener(new MyActionListener("÷"));
		
//		max button
		JButton maxButton = new JButton("Max");
		maxButton.setBounds(340, 200, 60, 30);
		frame.getContentPane().add(maxButton);
		maxButton.addActionListener(new MyActionListener("Max"));
		
//		minimum button
		JButton minButton = new JButton("Min");
		minButton.setBounds(420, 200, 60, 30);
		frame.getContentPane().add(minButton);
		minButton.addActionListener(new MyActionListener("Min"));
	  }
}































