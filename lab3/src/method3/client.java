package method3;

import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;

/**
 *
 * @author Mohammad Altahat
 */

public class client {
	
	static Socket clientSocket;
	static JTextArea receivedTextArea;
	static JLabel countLabel;
	static JLabel dateLabel;
	
	public static void main(String[] args) throws Exception {
	      
	    //Create the GUI frame and components
		JFrame frame = new JFrame ("Chatting Client");
		frame.setLayout(null);
		frame.setBounds(100, 100, 500, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	    	  	    	 
	    JLabel statusLabel = new JLabel("Not Connected");
	    statusLabel.setBounds(20, 40, 150, 30);
	    statusLabel.setForeground(Color.RED);
	    frame.getContentPane().add(statusLabel);
	    
	    JButton connectButton = new JButton("Connect");
	    connectButton.setBounds(290, 40, 100, 30);
	    frame.getContentPane().add(connectButton);
	    
	    countLabel = new JLabel("");
	    countLabel.setBounds(20, 100, 300, 30);
	    frame.getContentPane().add(countLabel);
	    countLabel.setVisible(false);
	    
	    dateLabel = new JLabel("Server's Date");
	    dateLabel.setBounds(20, 140, 400, 30);
	    frame.getContentPane().add(dateLabel);
	    dateLabel.setVisible(false);
	    
	    JLabel sendLabel = new JLabel("Send Numbers");
	    sendLabel.setBounds(20, 180, 100, 30);
	    frame.getContentPane().add(sendLabel);
	    sendLabel.setVisible(false);
	    
	    JTextField sendTextField = new JTextField();
	    sendTextField.setBounds(130, 180, 150, 30);
	    frame.getContentPane().add(sendTextField);
	    sendTextField.setVisible(false);
	    
	    JButton sendButton = new JButton("Send");
	    sendButton.setBounds(300, 180, 80, 30);
	    frame.getContentPane().add(sendButton);
	    sendButton.setVisible(false);
	    
	    receivedTextArea = new JTextArea();
	    receivedTextArea.setBounds(20, 220, 460, 300);
	    receivedTextArea.setEditable(false);
	    frame.getContentPane().add(receivedTextArea);
	    receivedTextArea.setVisible(false);
	    
	    JScrollPane receivedTextAreaScroll = new JScrollPane(receivedTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    receivedTextAreaScroll.setBounds(20,  220,  460,  300);
	    frame.getContentPane().add(receivedTextAreaScroll);
	    receivedTextAreaScroll.setVisible(false);
	    
	    //Action listener when connect button is pressed
	    connectButton.addActionListener( new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		try {
	    			
	    			if (connectButton.getText().equals("Connect")) { //if pressed to connect
				    	    		  
	    				//create a new socket to connect with the server application 
	    				clientSocket = new Socket ("localhost", 6789);		    				
		    			
		    			//call function StartThread
		    			StartThread();
		    			
		    			//make the GUI components visible, so the client can send and receive messages
						sendButton.setVisible(true);
						sendLabel.setVisible(true);
						sendTextField.setVisible(true);
						dateLabel.setVisible(true);
						countLabel.setVisible(true);
						receivedTextArea.setVisible(true);
						receivedTextAreaScroll.setVisible(true);
						
						statusLabel.setText("Connected");
						statusLabel.setForeground(Color.BLUE);
						
						//change the Connect button text to disconnect
						connectButton.setText("Disconnect");
				    	    		  
	    			} else { //if pressed to disconnect
	    				
	    				//create an output stream and send a Remove message to disconnect from the server 
	    				DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());		    				
		    			outToServer.writeBytes("-Remove\n");
		    			
		    			//close the client's socket
	    				clientSocket.close();
	    				
	    				//make the GUI components invisible
	    				sendButton.setVisible(false);
						sendLabel.setVisible(false);
						sendTextField.setVisible(false);
						dateLabel.setVisible(false);
						countLabel.setVisible(false);
						receivedTextArea.setVisible(false);
						receivedTextAreaScroll.setVisible(false);
						
						//change the Connect button text to connect
	    				connectButton.setText("Connect");
	    				statusLabel.setText("Not Connected");
	    				statusLabel.setForeground(Color.RED);
	    				
	    			}
	    			
	    		} catch (Exception ex) {
	    			System.out.println(ex.toString());
	    		}
	    	}});

	    //Action listener when send button is pressed
	    sendButton.addActionListener( new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		try {
		    			//create an output stream
	    				DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());		    					    		
		    					    			
		    			if (!sendTextField.getText().equals("")) { //if the send to textfield has a name then add "@sendTo name:" to the beginning of the message and send it
		    						    				
		    				String sendingSentence = "-Compute," + sendTextField.getText() + "\n";
			    			outToServer.writeBytes(sendingSentence);
			    			
		    			}		    				    		
					
	    		} catch (Exception ex) {
	    			System.out.println(ex.toString());
	    		}
	    	}});
	    
	    //Disconnect on close
	    frame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent we) {
	    		  
	    		try {
	
	    			//create an output stream and send a Remove message to disconnect from the server 
    				DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());		    				
  	    			outToServer.writeBytes("-Remove\n");
  	    			
  	    			//close the client's socket
    				clientSocket.close();
    				
    				//make the GUI components invisible
    				sendButton.setVisible(false);
  					sendLabel.setVisible(false);
  					sendTextField.setVisible(false);
  					dateLabel.setVisible(false);
  					countLabel.setVisible(false);
  					receivedTextArea.setVisible(false);
  					receivedTextAreaScroll.setVisible(false);
  					
  					//change the Connect button text to connect
    				connectButton.setText("Connect");
    				statusLabel.setText("Not Connected");
    				statusLabel.setForeground(Color.RED);
    				
    				System.exit(0);
    				
	    		  } catch (Exception ex) {
		    			System.out.println(ex.toString());
		    		}
	    		
	    	    
	    	  }
	    	});
	    	   
	    frame.setVisible(true);   
	    	   
	    }
	
	//Thread to always read messages from the server and print them in the textArea
	private static void StartThread() {
		
		new Thread (new Runnable(){ @Override
 	   		public void run() {
						
			try {

				//create a buffer reader and connect it to the socket's input stream
				BufferedReader inFromServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

				String receivedSentence;
				
				//always read received messages and append them to the textArea
				while (true) {
										
					receivedSentence = inFromServer.readLine();	
					//System.out.println(receivedSentence);
					
					if (receivedSentence.startsWith("-Date")) {
	    			
						String []strings = receivedSentence.split(";");
						dateLabel.setText("Server's Date: " + strings[1]);
	    				
	    			} else if (receivedSentence.startsWith("-Results")) {
	    				    	
	    				String []strings = receivedSentence.split(",");
	    				receivedTextArea.setText("Sum is: " + strings[1] + "\n");
	    				receivedTextArea.append("Average is: " + strings[2] + "\n");
	    				receivedTextArea.append("Minimum is: " + strings[3] + "\n");
	    				receivedTextArea.append("Maximum is: " + strings[4]);

	    			} else if (receivedSentence.startsWith("-Count")) {
	    				
	    				String []strings = receivedSentence.split(",");
	    				countLabel.setText("Number of connected clients to the server: " + strings[1] + "\n");
	    				
	    			}
				}
				
			}
			catch(Exception ex) {
				
			}
			
			
		}}).start();
		
	}
    
}