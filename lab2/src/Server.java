import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int port = 6789;

    public static void main(String[] args) {
        runServer();
    }

    private static void runServer() {
        try {
//            Create Server Socket
            ServerSocket welcomeSocket = new ServerSocket(port);

            while (true) {
//                Keep listening for clients connection requests, accept the connection when available
                Socket connectionSocket = welcomeSocket.accept();

//                Read received data, sent by client, through a buffer reader that is connected to the connection socket’s input stream
                BufferedReader inFromClient = new BufferedReader( new InputStreamReader(
                        connectionSocket.getInputStream() ) );
                String clientSentence = inFromClient.readLine();

//                Generate response
                String result = processData(clientSentence);

//                Send data to client, through a buffer writer connected to the connection socket’s output stream
                DataOutputStream outToClient = new DataOutputStream( connectionSocket.getOutputStream() );
                outToClient.writeBytes(result);
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    private static String processData(String data) {
        if (data != null) {
            String[] numbersString = data.split(",");
            Double[] numbers = new Double[numbersString.length];

            double sum = 0;
            double average = 0;
            double max = 0;
            double min = 0;

            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = Double.parseDouble(numbersString[i]);

                sum += numbers[i];

                if (i == 0) {
                    max = numbers[i];
                    min = max;
                }

                if (max < numbers[i]) max = numbers[i];
                if (min > numbers[i]) min = numbers[i];
            }

            average = sum / numbers.length;

            String result = "Sum = " + sum + ",Average = " + average + ",Max = " + max + ",Min = " + min + "\n";

            return result;
        }
        else return "";
    }
}