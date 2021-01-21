// Quang Anh Tran - 40075748


package elec463.lab1.server;

import java.net.DatagramSocket;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class CalculatorServer {

	public static void main(String[] args) throws IOException {
		int p = 9876;
		
		
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Calculator Server");
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel label = new JLabel("");
		label.setBounds(20, 20, 500, 50);
		label.setFont(new Font("Times", Font.BOLD, 16));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label);
		label.setText("UDP server is listening on port " + p);
		
		
//		Create UDP Socket:
		DatagramSocket serverSocket = new DatagramSocket(p);
		
		while (true) {
	//		Read received data, sent by client, through an array of bytes and a Datagram packet:
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			serverSocket.receive(packet);
			String receivedSentence = new String(packet.getData());
			
			String sendingSentence = null;
			
			CalculatorServer calculatorServer = new CalculatorServer();
			
			// https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java
			if (receivedSentence.contains("+")) {
				String[] parts = receivedSentence.split("\\+"); // https://www.yawintutor.com/java-util-regex-patternsyntaxexception-dangling-meta-character-near-index-0/
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.add(num1, num2);
			} else if (receivedSentence.contains("-")) {
				String[] parts = receivedSentence.split("-");
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.minus(num1, num2);
			} else if (receivedSentence.contains("x") && !receivedSentence.contains("a")) {
				String[] parts = receivedSentence.split("x");
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.multiply(num1, num2);
			} else if (receivedSentence.contains("÷")) {
				String[] parts = receivedSentence.split("÷");
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.divide(num1, num2);
			} else if (receivedSentence.contains("Max")) {
				String[] parts = receivedSentence.replace("Max", "a").split("a");
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.max(num1, num2);
			} else if (receivedSentence.contains("Min")) {
				String[] parts = receivedSentence.replace("Min", "i").split("i");
				double num1 = Float.parseFloat(parts[0]);
				double num2 = Float.parseFloat(parts[1]);
				sendingSentence = calculatorServer.min(num1, num2);
			}
//			System.out.println(sendingSentence);
			
//			Get the IP address and Port number of the received packet
			InetAddress IPAddress = packet.getAddress();
			int port = packet.getPort();
			
	//		Send data to client, through an array of bytes and a Datagram packet:
			calculatorServer.sendData(sendingSentence, serverSocket, IPAddress, port);
			
//			server terminates after 60s
			serverSocket.setSoTimeout(60000);
			
		}
	}
	
	
	private void sendData(String sendingSentence, DatagramSocket serverSocket, InetAddress IPAddress, int port) throws IOException {
//		Send data to client, through an array of bytes and a Datagram packet:
		byte[] data = new byte[1024];
		data = sendingSentence.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, port);
		serverSocket.send(packet);
	}
	
	private String add(double num1, double num2) {
		String result = String.valueOf(num1 + num2);
		return result;
	}
	
	private String minus(double num1, double num2) {
		String result = String.valueOf(num1 - num2);
		return result;
	}
	
	private String multiply(double num1, double num2) {
		String result = String.valueOf(num1 * num2);
		return result;
	}
	
	private String divide(double num1, double num2) {
		String result = String.valueOf(num1 / num2);
		return result;
	}
	
	private String max(double num1, double num2) {
		String result = String.valueOf(Math.max(num1, num2));
		return result;
	}
	
	private String min(double num1, double num2) {
		String result = String.valueOf(Math.min(num1, num2));
		return result;
	}
}
