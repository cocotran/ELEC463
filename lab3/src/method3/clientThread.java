package method3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 *
 * @author Mohammad Altahat
 */

public class clientThread extends Thread{

	//the ClientServiceThread class extends the Thread class and has the following parameters
	public int number; //client name
	public Socket connectionSocket; //client connection socket
	ArrayList<clientThread> Clients; //list of all clients connected to the server
	
	//constructor function 
	public clientThread(int number, Socket connectionSocket, ArrayList<clientThread> Clients) {
		
		this.number = number;
		this.connectionSocket = connectionSocket;
		this.Clients = Clients;		
		
	}
	
	//thread's run function
	public void run() {
		
		try {
			
			//create a buffer reader and connect it to the client's connection socket
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));;
    		String clientSentence;
    		DataOutputStream outToClient;
			
    		//always read messages from client
    		while (true) {
    			
    			clientSentence = inFromClient.readLine();
    			
    			//ckeck the start of the message
    			
    			if (clientSentence.startsWith("-Remove")) { //Remove Client
    				
    				for (int i = 0; i < Clients.size(); i++) {
    					
    					if (Clients.get(i).number == number) {
    						Clients.remove(i);	    					
    					}
    					
    				}
    				
    			} else if (clientSentence.startsWith("-Compute")) { //compute the sum and send the result back
    				    	
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
    				
    				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    				outToClient.writeBytes("-Results," + sum + "," 
    										+ average + ","
    										+ min + ","
    										+ max + "\n");	    					

    			}
    		}
			
			
		} catch(Exception ex) {
			
		}
		
		
	}
	
}
