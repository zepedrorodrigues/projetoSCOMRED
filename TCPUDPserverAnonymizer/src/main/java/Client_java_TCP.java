import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 * A simple TCP/IP client that sends a String and a keyword
 * to a server and prints the server's response.
 * <p>References:
 *  <ul>
 * <li>[1]{@code @source} lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoClient.java" (unknown version) Type: source code
 * <li>[2]{@code @source} Oracle (2023) "Class Socket" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[3]{@code @source} Oracle (2023) "Class PrintWriter" [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[4]{@code @source} Oracle (2023) "Class BufferedReader" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[5]{@code @source} Oracle (2023) "Class InputStreamReader" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 *  </ul>
 */

/**
 * This class represents a TCP client in Java.
 * It is used to send data to a server using a specified host and port.
 */
public class Client_java_TCP {
    /**
     * The socket used to send data to the server.
     */
    private Socket socket;

    /**
     * Constructor for the client_java_TCP class.
     * Initializes the socket to null.
     */
    public Client_java_TCP() {
        socket = null;
    }

    /**
     * The main method of the client_java_TCP class.
     * It prompts the user for the server name, port, string, and keyword,
     * and then sends the data to the server.
     *
     * @param args Command line arguments.
     */

    public static void main(String[] args) {
        try (
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter server name or IP address: ");
            String serverName = stdIn.readLine();
            System.out.print("Enter port: ");
            String port = stdIn.readLine();
            System.out.print("Enter string: ");
            String userString = stdIn.readLine();
            System.out.print("Enter keyword: ");
            String keyword = stdIn.readLine();

            Client_java_TCP client = new Client_java_TCP();
            client.sendData(serverName, port, userString, keyword);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * This method is used to send data to a server.
     * It opens a socket to the specified host and port, sends the user string and keyword,
     * and then prints the server's response.
     *
     * @param host       The host name or IP address of the server.
     * @param port       The port number of the server.
     * @param userString The string to be sent to the server.
     * @param keyword    The keyword to be sent to the server.
     */
    public void sendData(String host, String port, String userString, String keyword) {
        try {
            int portNum = Integer.valueOf(port);
            if (portNum < 1024 || portNum > 49151) {
                System.err.println("Invalid port number. Terminating!");
                return;
            }
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, portNum), 5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(userString);
            out.println(keyword);

            String serverSentence = in.readLine();
            if (serverSentence != null) {
                System.out.println(serverSentence);
            }
            String numberRepeats = in.readLine();
            if (numberRepeats == null) {
                return;
            }
            int repeats = Integer.parseInt(numberRepeats);
            for (int i = 0; i < repeats; i++) {
                String socketsProg = in.readLine();
                System.out.println(socketsProg);
            }
            out.close();
            in.close();
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Unknown server. Terminating!");
            System.exit(1);
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out. Terminating!");
            System.exit(1);
        } catch (ConnectException e) {
            System.err.println("Could not connect to server. Terminating!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Failed to send expression. Terminating!");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port format. Terminating!");
            System.exit(1);
        }
    }
}