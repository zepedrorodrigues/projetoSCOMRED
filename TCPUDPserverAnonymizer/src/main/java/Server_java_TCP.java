import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple TCP/IP server that receives Strings and keywords
 * and returns the String with the keyword anonymized
 * turned to uppercase "X"
 * <p>References:
 *  <ul>
 * <li>[1]{@code @source} lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoServer.java" (unknown version) Type: source code
 * <li>[2]{@code @source} Oracle (2023) "Class ServerSocket" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[3]{@code @source} Oracle (2023) "Class BufferedReader" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[4]{@code @source} Oracle (2023) "Class InputStreamReader" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * <li>[5]{@code @source} Oracle (2023) "Class PrintWriter" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.
 * </ul>ki
 */
public class Server_java_TCP {
    /**
     * privately saves ServerSocket
     * {@code @source}[#1]
     */
    private ServerSocket serverSocket;

    /**
     * Starts the server Anonymizer, binding it to the specified port
     *
     * @param port The port binded to the server at localhost
     *             {@code @source}[#1]
     *             {@code @source}[#2]
     */

    public Server_java_TCP(int port) throws IOException {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Creates ServerSocket and waits for connections
     *
     * @param args String[] port must be in space [0], or will exit with code 1
     *             Turns port in an Integer and Creates server in port
     *             Waits for connections
     *             {@code @source} lmn@isep.ipp.pt - 27/1/2023 -"TCPEchoServer.java" (unknown version) Type: source code
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java server_java_TCP <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]); //converts String to Integer
        Server_java_TCP server = new Server_java_TCP(port); //creates server
        server.waitConnections();
    }

    /**
     * ServerSocket waits Connection from clientSocket
     * when achieved, start
     * {@code @source} lmn@isep.ipp.pt - 27/1/2023 -"TCPEchoServer.java" (unknown version) Type: source code
     */
    public void waitConnections() {
        Socket clientSocket;
        try {
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String stringtoBeAnonymized = in.readLine();
            String keyWord = in.readLine();

            if (stringtoBeAnonymized == null || stringtoBeAnonymized.isEmpty()) {
                System.err.println("Did not received valid string from client. Terminating!");
                return; // terminate the method execution
            }

            if (keyWord == null || keyWord.isEmpty()) {
                System.err.println("Did not received valid keyword from client. Terminating!");
                return; // terminate the method execution
            }

            while (stringtoBeAnonymized != null) {
                String anonymizedString = Anonymizer.anonymizeKeyword(stringtoBeAnonymized, keyWord);
                int countSubstitutions = Anonymizer.startingPositions(stringtoBeAnonymized, keyWord).size();
                out.println(anonymizedString);
                out.println(countSubstitutions);
                for (int i = 0; i < countSubstitutions; i++)
                    out.println("Socket Programming");
                stringtoBeAnonymized = in.readLine();
                if (stringtoBeAnonymized == null) {
                    out.println("");
                }
            }
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Did not receive valid String from Client. Terminating.");
        }
    }
}

