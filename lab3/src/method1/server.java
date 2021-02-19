package method1;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Mohammad Altahat
 */

public class server {
    
	//Array of Sockets for all connected clients
	static ArrayList<Socket> ClientsSocketes = new ArrayList<Socket>();
		
	public static void main(String[] args) throws Exception {
		
		//Create the GUI frame and components
		JFrame frame = new JFrame ("Chatting Server");
		frame.setLayout(null);
		frame.setBounds(100, 100, 300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
   
		JLabel connectionStatusLabel = new JLabel("No Clients Connected");
		connectionStatusLabel.setBounds(80, 30, 200, 30);
		connectionStatusLabel.setForeground(Color.red);
		frame.getContentPane().add(connectionStatusLabel);
 	   	
		//create the welcoming server's socket
 	   	ServerSocket welcomeSocket = new ServerSocket(6789);
  	    
 	   	//thread to always listen for new connections from clients
 	   	new Thread (new Runnable(){ @Override
 	   		public void run() {

 	   		Socket connectionSocket;
	 	   	DataOutputStream outToClient;
	
	 	   	while (!welcomeSocket.isClosed()) {

 	   		try {

 	   			//when a new client connect, accept this connection and assign it to a new connection socket
 	   			connectionSocket = welcomeSocket.accept();

 	   			//create a new output stream and send the message "You are connected" to the client
 	   			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				outToClient.writeBytes("-Connected\n");
 	   			
 	   			ClientsSocketes.add(connectionSocket);
 	   			StartNewClientThread(connectionSocket);

 	   		}
 	   		catch (Exception ex) {
 	   		
 	   		}
	                  
 	    } 	 
 	   	
 	  }}).start();
 	   
 	   	
 	   	
 	  //thread to always get the count of connected clients and update the label and send to clients
 	  new Thread (new Runnable(){ @Override
	   		public void run() {
 		  
 		  		try {
 		  			
 		  			DataOutputStream outToClient;
 		  			
 		  			while (true) {
 		  				
 		  				if (ClientsSocketes.size() > 0) //if there are one or more clients print their number
 		 	   			{
 		 	   				if (ClientsSocketes.size() == 1)
 		 	   					connectionStatusLabel.setText("1 Client Connected");
 		 	   				else
 		 	   					connectionStatusLabel.setText(ClientsSocketes.size() + " Clients Connected");
 		 	   				 	   				
 		 	   				connectionStatusLabel.setForeground(Color.blue);
 		 	   			}
 		 	   			else { //if there are no clients connected, print "No Clients Connected"
 		 	   				
 		 	   				connectionStatusLabel.setText("No Clients Connected");
 			   				connectionStatusLabel.setForeground(Color.red);
 		 	   			}
 		  				
 		  				
 		  				for (int i = 0; i < ClientsSocketes.size(); i++) { 	    					

 		  					outToClient = new DataOutputStream(ClientsSocketes.get(i).getOutputStream());
    						outToClient.writeBytes("-Count, " + ClientsSocketes.size() + "\n");
 	    				}
 		  			
 		  				Thread.sleep(1000);
 		  				
 		  			}
 		  			
 		  		} catch (Exception ex) {
 		  			
 		  		}
 		  		
 	 }}).start();
 	  
 	  
 	//thread to always get the date and send to clients
 	  new Thread (new Runnable(){ @Override
	   		public void run() {
 		  
 		  		try {
 		  			
 		  			DataOutputStream outToClient;
 		  			
 		  			while (true) {
 		  				
 		  				Date now = new Date();
 		  				SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy  h:m:s a z");
 		  				
 		  				//System.out.println(dateFormatter.format(now));
 		  				
 		  				for (int i = 0; i < ClientsSocketes.size(); i++) { 	    					

 		  					outToClient = new DataOutputStream(ClientsSocketes.get(i).getOutputStream()); 		  					
    						outToClient.writeBytes("-Date;" + dateFormatter.format(now) + "\n");    						
    						
 	    				}
 		  			 	
 		  				Thread.sleep(1000);
 		  				
 		  			}
 		  			
 		  		} catch (Exception ex) {
 		  			
 		  		}
 		  		
 	 }}).start();
 	  

 	 frame.setVisible(true);
 	  
    }
	
	
	static void StartNewClientThread(Socket connectionSocket)
	{
		
		//thread to receive and reply to specific client
		new Thread (new Runnable(){ @Override
		public void run() {
		
			try {
				
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());;
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));;
				String clientSentence;
				boolean isRun = true;
				
				while (isRun) {
				
					clientSentence = inFromClient.readLine();
	    			
	    			//ckeck the start of the message
	    			
	    			if (clientSentence.startsWith("-Remove")) { //Remove Client
	    				
	    				for (int i = 0; i < ClientsSocketes.size(); i++) {
	    					
	    					if (ClientsSocketes.get(i).getPort() == connectionSocket.getPort()) {
	    						ClientsSocketes.remove(i);
	    						isRun = false;
	    					}
	    				}
	    			}else if (clientSentence.startsWith("-Compute")) { //compute the sum and send the result back
				    	
						String []numbers = clientSentence.split(",");
						
						double sum = Double.parseDouble(numbers[1]);
						double min = Double.parseDouble(numbers[1]);
						double max = Double.parseDouble(numbers[1]);
						
						for (int i = 2; i < numbers.length; i++) {
							
							sum += Double.parseDouble(numbers[i]);
							
							if (Double.parseDouble(numbers[i]) < min)
								min = Double.parseDouble(numbers[i]);
							
							if (Double.parseDouble(numbers[i]) > max)
								max = Double.parseDouble(numbers[i]);
							
						}
						
						double average = sum / (numbers.length - 1);

						outToClient.writeBytes("-Results," + sum + "," 
												+ average + ","
												+ min + ","
												+ max + "\n");	    					
		
					} 
	    			
				  				
				}
				  			
			} catch (Exception ex) {
				
			}
			  		
		}}).start();
		  
	  
	}
    
}
