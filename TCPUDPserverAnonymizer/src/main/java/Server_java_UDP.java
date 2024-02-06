import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple UDP server that receives a message and a keyword from a client
 * Anonymizes the message and sends it back to the client, with the number of "Socket Programming"'s
 * equal to the number of substitutions made in the message.
 * If the String sent is larger than the buffer size, it will be divided into parts and sent separately,
 * and the server will reassemble it. This system uses an 'ACK' message to ensure the data is sent completely. (order not guaranteed)
 * It only communicates with one client at a time.
 * <p>References:
 * <ul>
 * <li>[1]{@code @source} lmn@isep.ipp.pt - 27/12/2023 -"UDPEchoCServer.java" (unknown version) Type: source code
 * <li>[2]{@code @source} Oracle (2023) "Class DatagramSocket" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
 * <li>[3]{@code @source} Oracle (2023) "Class DatagramPacket" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
 * <li>[4]{@code @source} Oracle (2023) "Class InetAddress" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
 * <li>[5]{@code @source} Oracle (2023) "Class SocketException" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
 * <li>[6]{@code @source} Oracle (2023) "Class IOException" (N/A) [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
 * </ul>
 */
public class Server_java_UDP {
    /**
     * The maximum time to wait for a response from the server
     */
    private static final int MAX_TIMEOUT = 1000;
    /**
     * The size of the buffer used to send and receive data
     */

    private static final int BUFFER_SIZE = 20;
    /**
     * The acknowledgment message sent by the server
     */
    private static final String ACK = "ACK";
    /**
     * The Socket Programming String
     */
    private static final String SOCKET_PROGRAMMING = "\nSocket Programming";
    /**
     * The DatagramSocket used to send and receive data
     */
    private DatagramSocket socket;
    /**
     * The port number of the client that the server is communicating with
     */
    private int port;
    /**
     * The IP address of the client that the server is communicating with
     */

    private InetAddress address;

    /**
     * The constructor of the server
     * It initializes the server with the given port number
     *
     * @param port The port number of the server
     */
    public Server_java_UDP(int port) {
        if (port < 1024 || port > 49151) {
            System.out.println("Invalid port number");
            System.exit(0);
        }// Better error handling
        try {
            // Initialize the socket with the given port
            this.socket = new DatagramSocket(port);
            System.out.println("Server initialized on port " + port);
        } catch (SocketException e) {
            System.out.println("Error initializing server: " + e.getMessage());
        }
    }

    /**
     * The main method of the server
     * It reads the port number from the command line arguments and initializes the server
     * It then waits for a message from the client and processes it
     *
     * @param args The command line arguments, inputting the port number only
     */
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        Server_java_UDP server = new Server_java_UDP(port);
        server.receiveData();
    }

    /**
     * The method that receives the message and keyword from the client
     * It then anonymizes the message and sends it back to the client
     * if something is incorrect, the server will print an error message to the console and exit
     */
    public void receiveData() {
        try {
            receiveAndProcessData();
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * coordinator method for receiving and processing the data
     * receives the number of parts of the message and the message itself
     * receives the number of parts of the keyword and the keyword itself
     * anonymizes the message and sends it back to the client
     * if something is incorrect, the server will print an error message to the console and exit
     */
    private void receiveAndProcessData() {
        try {
            int lengthOfString = receiveNumberOfParts();
            int numberOfParts = receiveNumberOfParts();
            String message = receivePartsOfMessage(numberOfParts);
            if (message.length() == 0) {
                System.out.println("Did not receive valid String from Client. Terminating.");
                System.exit(1);
            }
            if (lengthOfString != message.length()) {
                System.out.println("Error: Length of the message does not match the length of the message 3");
                System.exit(0);
            }// Better error handling
            int numberOfPartsKeyword = receiveNumberOfParts();
            String keyword = receivePartsOfMessage(numberOfPartsKeyword);
            if (keyword.length() == 0) {
                System.out.println("Did not receive valid keyword from Client. Terminating.");
                System.exit(1);
            }
            String anonymizedMessage = Anonymizer.anonymizeKeyword(message, keyword);
            int numberOfSocketProgrammings = Anonymizer.startingPositions(message, keyword).size();
            String[] dividedAnonymizedMessage = divideString(anonymizedMessage);
            sendData(numberOfSocketProgrammings, dividedAnonymizedMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Receives the number of parts of the message/keyword from the client
     *
     * @return The number of parts of the message/keyword
     * @throws IOException in case of an error with the socket
     */

    private int receiveNumberOfParts() throws IOException {
        byte[] lengthNumberofPartsBuffer = new byte[BUFFER_SIZE];
        DatagramPacket lengthNumberOfPartsPacket = new DatagramPacket(lengthNumberofPartsBuffer, lengthNumberofPartsBuffer.length);
        socket.receive(lengthNumberOfPartsPacket);
        if (this.address == null || this.port == 0) {
            this.address = lengthNumberOfPartsPacket.getAddress();
            this.port = lengthNumberOfPartsPacket.getPort();
        }
        sendACK(lengthNumberOfPartsPacket.getAddress(), lengthNumberOfPartsPacket.getPort());
        int lengthNumberOfParts = Integer.parseInt(new String(lengthNumberOfPartsPacket.getData()).trim());
        byte[] numberofPartsBuffer = new byte[BUFFER_SIZE];
        DatagramPacket numberofPartsPacket = new DatagramPacket(numberofPartsBuffer, numberofPartsBuffer.length);
        socket.receive(numberofPartsPacket);
        if (lengthNumberOfParts != numberofPartsPacket.getLength()) {
            System.out.println("Error: Number of parts does not match the length of the message");
            System.exit(0);
        }
        sendACK(numberofPartsPacket.getAddress(), numberofPartsPacket.getPort());
        return Integer.parseInt(new String(numberofPartsPacket.getData()).trim());
    }

    /**
     * Receives the parts of the message/keyword from the client (the message itself, not the number of parts it splits into
     *
     * @param numberOfParts The number of parts of the message/keyword
     * @return The message/keyword
     * @throws IOException in case of an error with the socket
     */
    private String receivePartsOfMessage(int numberOfParts) throws IOException {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < numberOfParts; i++) {
            byte[] lengthBuffer = new byte[BUFFER_SIZE];
            DatagramPacket lengthBufferPacket = new DatagramPacket(lengthBuffer, lengthBuffer.length);
            socket.receive(lengthBufferPacket);
            sendACK(lengthBufferPacket.getAddress(), lengthBufferPacket.getPort());
            int length = Integer.parseInt(new String(lengthBufferPacket.getData()).trim());
            byte[] partBuffer = new byte[BUFFER_SIZE];
            DatagramPacket partBufferPacket = new DatagramPacket(partBuffer, partBuffer.length);
            socket.receive(partBufferPacket);
            if (length != partBufferPacket.getLength()) {
                System.out.println("Error: Length of the part does not match the length of the message");
                System.exit(0);
            }
            sendACK(partBufferPacket.getAddress(), partBufferPacket.getPort());
            message.append(new String(partBufferPacket.getData()));
        }
        return message.toString().trim();
    }

    /**
     * Sends an acknowledgment message to the client
     *
     * @param address The IP address of the client
     * @param port    The port number of the client
     * @throws IOException in case of an error with the socket
     */
    private void sendACK(InetAddress address, int port) throws IOException {
        byte[] ackBuffer = ACK.getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length, address, port);
        socket.send(ackPacket);
    }

    /**
     * Divides a given String into parts of a given size. The parts are returned in an array of Strings
     * starts at 0 and start is moved according to the byte size of the characters in the string  until it reaches the end of the string
     * the while loop line and the regex expressions are used to ensure that the string is not cut in the middle of a multi-byte character
     *
     * @param input The String to be divided
     * @return The array of divided Strings
     * @throws IOException in case of an error with the encoding
     */
    private String[] divideString(String input) {
        try {
            byte[] inputBytes = input.getBytes("UTF-8");
            List<String> result = new ArrayList<>();
            int start = 0;
            while (start < inputBytes.length) {
                int end = Math.min(inputBytes.length, start + BUFFER_SIZE);
                // Ensure not to cut in the middle of a multi-byte character
                while (end < inputBytes.length && (inputBytes[end] & 0xC0) == 0x80) {
                    end--;
                }
                result.add(new String(inputBytes, start, end - start, "UTF-8"));
                start = end;
            }
            return result.toArray(new String[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Sends the anonymized message back to the client, in this order: 1. the number of socket programmings, 2. the number of parts of the message, 3. the parts of the message
     *
     * @param numberOfSocketProgrammings The number of "Socket Programming"'s in the message
     * @param dividedAnonymizedMessage   The parts of the anonymized message to be sent back to the client (in an array of Strings)
     */
    public void sendData(int numberOfSocketProgrammings, String[] dividedAnonymizedMessage) {
        try {
            //send number of socket programmings
            sendNumberPacket(numberOfSocketProgrammings);
            //send number of parts and the parts of the message to receive
            sendNumberPacket(dividedAnonymizedMessage.length);
            //send the actual Data
            sendDataPacket(dividedAnonymizedMessage);
            //send Socket Programming count
            sendSocketProgrammings(numberOfSocketProgrammings);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends the number of "Socket Programming"'s in the message to the client and the number of parts of the message.
     * First sends the length of the packet, then the actual data.
     * Waits for an ACK after sending each packet
     *
     * @param numberOf The number of "Socket Programming"'s or parts of message
     */
    private void sendNumberPacket(int numberOf) {
        String numberOfSocketProgrammingsString = String.valueOf(numberOf);
        byte[] numberOfSocketProgrammingsBuffer = numberOfSocketProgrammingsString.getBytes();
        String numberOfSocketProgrammingsBufferLength = String.valueOf(numberOfSocketProgrammingsBuffer.length);
        byte[] numberOfSocketProgrammingsBufferLengthBuffer = numberOfSocketProgrammingsBufferLength.getBytes();
        DatagramPacket lengthPacket = new DatagramPacket(numberOfSocketProgrammingsBufferLengthBuffer, numberOfSocketProgrammingsBufferLengthBuffer.length, address, port);
        DatagramPacket dataPacket = new DatagramPacket(numberOfSocketProgrammingsBuffer, numberOfSocketProgrammingsBuffer.length, address, port);
        try {
            socket.send(lengthPacket);
            if (!waitForAck()) {
                System.out.println("ACK not received");
                System.exit(0);
            }
            socket.send(dataPacket);
            if (!waitForAck()) {
                System.out.println("ACK not received");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends the parts of the anonymized message to the client. First sends the length of the packet, then the actual data.
     *
     * @param dividedAnonymizedMessage The parts of the anonymized message to be sent back to the client (in an array of Strings)
     *                                 Waits for an ACK after sending each packet
     */
    private void sendDataPacket(String[] dividedAnonymizedMessage) {
        for (String part : dividedAnonymizedMessage) {
            byte[] partBuffer = part.getBytes();
            String partBufferLength = String.valueOf(partBuffer.length);
            byte[] partBufferLengthBuffer = partBufferLength.getBytes();
            DatagramPacket lengthPacket = new DatagramPacket(partBufferLengthBuffer, partBufferLengthBuffer.length, address, port);
            DatagramPacket dataPacket = new DatagramPacket(partBuffer, partBuffer.length, address, port);
            try {
                socket.send(lengthPacket);
                if (!waitForAck()) {
                    System.out.println("ACK not received");
                    System.exit(0);
                }
                socket.send(dataPacket);
                if (!waitForAck()) {
                    System.out.println("ACK not received");
                    System.exit(0);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void sendSocketProgrammings(int numberOfSocketProgrammings) {
        for (int i = 0; i < numberOfSocketProgrammings; i++) {
            byte[] databuffer = SOCKET_PROGRAMMING.getBytes();
            String dataBufferLength = String.valueOf(databuffer.length);
            byte[] dataBufferLengthBuffer = dataBufferLength.getBytes();
            DatagramPacket lengthPacket = new DatagramPacket(dataBufferLengthBuffer, dataBufferLengthBuffer.length, address, port);
            DatagramPacket dataPacket = new DatagramPacket(databuffer, databuffer.length, address, port);
            try {
                socket.send(lengthPacket);
                if (!waitForAck()) {
                    System.out.println("ACK not received");
                    System.exit(0);
                }
                socket.send(dataPacket);
                if (!waitForAck()) {
                    System.out.println("ACK not received");
                    System.exit(0);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Waits for an acknowledgment message from the client
     *
     * @return true if the acknowledgment message is received, false otherwise
     */
    private boolean waitForAck() {
        byte[] buffer = new byte[5];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.setSoTimeout(MAX_TIMEOUT);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            return received.equalsIgnoreCase(ACK);
        } catch (IOException e) {
            return false;
        }
    }
}
