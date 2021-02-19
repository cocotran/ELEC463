package method2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSession {

	public int clientID; //client name
	public Socket connectionSocket; //client connection socket
	public DataOutputStream outToClient;
	BufferedReader inFromClient;
	ArrayList<ClientSession> ClientSessions; //list of all clients connected to the server
	
	ClientSession(int id, Socket socket, ArrayList<ClientSession> clientSessions) {
		try {
			
			clientID = id;
			connectionSocket = socket;
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			ClientSessions = clientSessions;
			
		} catch (Exception e) {}
		
		
		//thread to receive and reply to specific client
		new Thread (new Runnable(){ @Override
		public void run() {
		
			try {
				
				String clientSentence;
				
				while (true) {
				
					clientSentence = inFromClient.readLine();
	    			
	    			//ckeck the start of the message
	    			
	    			if (clientSentence.startsWith("-Remove")) { //Remove Client
	    				
	    				for (int i = 0; i < ClientSessions.size(); i++) {
	    					
	    					if (ClientSessions.get(i).clientID == clientID) {
	    						ClientSessions.remove(i);
	    					}
	    				}
	    			}
	    			else if (clientSentence.startsWith("-Compute")) { //compute the sum and send the result back
				    	
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
	
	public void SendMessage(String message)
	{
		try {
			
			outToClient.writeBytes(message);
			
		} catch (IOException e) {}
		
	}

}
