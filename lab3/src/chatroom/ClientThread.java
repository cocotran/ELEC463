package chatroom;

import method3.clientThread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{

    // The ClientServiceThread class extends the Thread class and has the following parameters
    public String name;
    private int number; // client name
    public Socket connectionSocket; // client connection socket
    ArrayList<ClientThread> Clients; // list of all clients connected to the server

    // constructor function
    public ClientThread(int number, Socket connectionSocket, ArrayList<ClientThread> Clients) {

        this.number = number;
        this.connectionSocket = connectionSocket;
        this.Clients = Clients;
        this.name = "";

    }

    // thread's run function
    public void run() {

        try {

            // create a buffer reader and connect it to the client's connection socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            String clientSentence;
            DataOutputStream outToClient;

            // always read messages from client
            while (true) {

                clientSentence = inFromClient.readLine();
                System.out.println(clientSentence);

                // check the start of the message

                if (clientSentence.startsWith("-Remove")) { // Remove Client
                    for (int i = 0; i < Clients.size(); i++) {
                        if (Clients.get(i).number == number) {
                            Clients.remove(i);
                        }
                    }
                }

                else if (clientSentence.startsWith("-Name")) {
                    String []client = clientSentence.split(",");
                    name = client[1];

                    Server.sendJoinedNameMessage(name);
                }

            }


        } catch (Exception ex) {
        }

    }
}
