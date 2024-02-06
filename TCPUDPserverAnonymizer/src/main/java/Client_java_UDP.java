import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple UDP client that sends a message and a keyword to a server
 * Receiving also the answer from the server, with the Anonymized keyword (sending an "ACK" message back)
 * Printing it to the console, in addition to a number o "Socket Programming"'s equal to the number of
 * substitutions made in the message (anonymizations).
 * If the String sent is larger than the buffer size, it will be divided into parts and sent separately,
 * and the server will reassemble it.
 * <p>References:</p>
 * <ul>
 * <li>[1]{@code @source} lmn@isep.ipp.pt - 27/12/2023 -"UDPEchoClient.java" (unknown version) Type: source code
 * <li>[2]{@code @source} "Class DatagramSocket" - Official Java Documentation, Oracle. Provides detailed information on using DatagramSockets for UDP communication.</li>
 * <li>[3]{@code @source} "Class InetAddress" - Official Java Documentation, Oracle. Describes methods for converting hostnames to InetAddress objects, essential for UDP socket communication.</li>
 * <li>[4]{@code @source} "UDP Communication" - Oracle Java Tutorials. Offers a comprehensive guide on implementing UDP server and client in Java.</li>
 * </ul>
 */
public class Client_java_UDP {

    /**
     * The DatagramSocket used to send and receive data
     */
    private DatagramSocket socket;
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
     * The IP address of the server
     */
    private InetAddress address;
    /**
     * The port number of the server
     */
    private int port;

    /**
     * The main method of the client
     * It reads the IP address, port number, message and keyword from the user (console)
     * and sends the message to the server
     *
     * @param args The command line arguments, inputting the port number only
     * @throws IOException in case of an error with the socket
     */
    public static void main(String[] args) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter server name or IP address: ");
            String address1 = in.readLine();
            System.out.print("Enter port: ");
            String port1 = in.readLine();
            System.out.print("Enter string: ");
            String message = in.readLine();
            System.out.print("Enter keyword: ");
            String keyword = in.readLine();
            InetAddress address = InetAddress.getByName(address1);
            int port = Integer.parseInt(port1);
            if (port < 1024 || port > 49151) {
                System.out.println("Invalid port number. Terminating!");
                System.exit(1);
            }
            Client_java_UDP client = new Client_java_UDP();
            client.sendData(message, keyword, address, port);
            client.receiveData();
        } catch (NumberFormatException e) {
            System.out.println("Invalid port format. Terminating!");
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("Could not connect to the server. Terminating!");
            System.exit(1);
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out. Terminating!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Failed to send expression. Terminating!");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The constructor of the client
     * It initializes the DatagramSocket used to send and receive data
     * defines the maximum time to wait for a response from the server
     *
     * @throws IOException in case of an error with the socket
     *                     printing the error message to the console
     */
    public Client_java_UDP() throws IOException {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(MAX_TIMEOUT);
        } catch (IOException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }

    /**
     * Sends the message and the keyword to the server
     * If the message is larger than the buffer size, it will be divided into parts and sent separately
     *
     * @param message The message to be sent
     * @param keyword The keyword to be sent
     * @param address The IP address of the server
     * @param port    The port number of the server
     * @throws IOException in case of an error with the socket
     */
    public void sendData(String message, String keyword, InetAddress address, int port) throws IOException {
        try {
            String lengthMessage = String.valueOf(message.length());
            byte[] bufferLengthMessage = lengthMessage.getBytes();
            String bufferLengthMessageLength = String.valueOf(bufferLengthMessage.length);
            byte[] bufferLengthMessageLengthBytes = bufferLengthMessageLength.getBytes();
            sendPacket(bufferLengthMessageLengthBytes, address, port);
            sendPacket(bufferLengthMessage, address, port);
            String[] dividedMessage = divideString(message);
            String[] dividedKeyword = divideString(keyword);
            String numberOfPartsMessage = String.valueOf(dividedMessage.length);
            String numberOfPartsKeyword = String.valueOf(dividedKeyword.length);
            byte[] bufferNumberOfPartsMessage = numberOfPartsMessage.getBytes();
            byte[] bufferNumberOfPartsKeyword = numberOfPartsKeyword.getBytes();
            String bufferMessageLength = String.valueOf(bufferNumberOfPartsMessage.length);
            String bufferKeywordLength = String.valueOf(bufferNumberOfPartsKeyword.length);
            byte[] bufferMessageLengthBytes = bufferMessageLength.getBytes();
            byte[] bufferKeywordLengthBytes = bufferKeywordLength.getBytes();
            sendPartsOfMessage(address, port, dividedMessage, bufferNumberOfPartsMessage, bufferMessageLengthBytes);
            sendPartsOfMessage(address, port, dividedKeyword, bufferNumberOfPartsKeyword, bufferKeywordLengthBytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends the whole message divided in Parts.
     * First sends 2 packets, one with the length of the Packet with the Number of Parts, receiving an ACK
     * then one with the actual number of parts the server will receive, if its length matches the one sent before, receives an ACK
     * Then, per each divided substring, sends 2 packets, one with the length of the Packet with the substring, receiving an ACK
     * then one with the actual substring, if its length matches the one sent before, receives an ACK
     * in the server size, buffer sizes are compared between the lengthbuffer and the actual length of the buffer and if they match, receives an ACK
     *
     * @param address        The IP address of the server
     * @param port           The port number of the server
     * @param dividedMessage The message divided into parts
     * @param bufferData     The buffer containing the data to be sent
     * @param bufferLength   The buffer containing the length of the data to be sent
     * @throws IOException in case of an error with the socket
     */
    private void sendPartsOfMessage(InetAddress address, int port, String[] dividedMessage, byte[] bufferData, byte[] bufferLength) throws IOException {
        sendPacket(bufferLength, address, port);
        sendPacket(bufferData, address, port);
        for (int i = 0; i < dividedMessage.length; i++) {
            byte[] buffer = dividedMessage[i].getBytes();
            String bufferLength1 = String.valueOf(buffer.length);
            byte[] bufferLengthBytes = bufferLength1.getBytes();
            sendPacket(bufferLengthBytes, address, port);
            sendPacket(buffer, address, port);
        }
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
    private String[] divideString(String input) throws IOException {
        try {
            byte[] inputBytes = input.getBytes("UTF-8");
            List<String> result = new ArrayList<>();
            int start = 0;
            while (start < inputBytes.length) {
                int end = Math.min(inputBytes.length, start + BUFFER_SIZE);
                // Ensure not to cut in the middle of a multibyte character
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
     * Sends a packet itself to the server (mechanism detailed in the sendPartsOfMessage method)
     * sends the packet and waits for an ACK (first message always sent is the length of the packet, gets 100% an ACK
     * then sends the actual data, and waits for an ACK, if it does not receive it, prints an error message to the console)
     *
     * @param buffer  The buffer containing the data to be sent
     * @param address The IP address of the server
     * @param port    The port number of the server
     * @throws IOException in case of an error with the socket
     */
    private void sendPacket(byte[] buffer, InetAddress address, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        if (!waitForAck()) {
            System.out.println("Communication failure: ACK not received. Terminating!");
            System.exit(0);
        }
    }

    /**
     * Waits for an acknowledgment message from the server
     * creates a 5 byte sized buffer (enough to fit 'ACK')
     * receives the packet and checks if the message is 'ACK'
     *
     * @return true if the acknowledgment message is received, false otherwise
     */
    private boolean waitForAck() {
        byte[] buffer = new byte[5];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            return received.equalsIgnoreCase(ACK);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Receives the answer from the server. First receives the number of Socket Programming strings it will receive,
     * then it receives the number of parts of the message, then the message itself, and then the Socket Programming strings
     * printing both the message and the Socket Programming strings to the console
     *
     * @throws IOException in case of an error with the socket
     */
    private void receiveData() throws IOException {
        int numberOfSocketProgrammings = receiveNumberOfParts();
        int numberOfPartsMessage = receiveNumberOfParts();
        String message = receiveMessage(numberOfPartsMessage);
        String socketProgrammings = receiveMessage(numberOfSocketProgrammings);
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(socketProgrammings);
        System.out.println(sb);
    }

    /**
     * Receives the number of parts of the message from the server
     * receives the length of the number of parts, then the number of parts itself
     * saves the IPAddress and the port of the server to parameters of the object to be used in other methods
     * without direct access to these parameters
     * if the length of the number of parts does not match the length of the message, prints an error message to the console
     *
     * @return The number of parts of the message
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
     * Sends an acknowledgment message to the server
     *
     * @param address The IP address of the server
     * @param port    The port number of the server
     * @throws IOException in case of an error with the socket
     */
    private void sendACK(InetAddress address, int port) throws IOException {
        byte[] ackBuffer = ACK.getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length, address, port);
        socket.send(ackPacket);
    }

    /**
     * Receives the message from the server, in pieces
     * for each piece receives the length of the message, then the message itself, comparing the length of the message
     * with the length of the buffer, if they do not match, prints an error message to the console and does not send ACK,
     * ending the program
     *
     * @param numberOfParts The number of parts of the message
     * @return The message received from the server, put together (hopefully)
     * @throws IOException in case of an error with the socket
     */

    private String receiveMessage(int numberOfParts) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfParts; i++) {
            byte[] bufferLength = new byte[BUFFER_SIZE];
            DatagramPacket lengthPacket = new DatagramPacket(bufferLength, bufferLength.length);
            socket.receive(lengthPacket);
            if (this.address == null || this.port == 0) {
                this.address = lengthPacket.getAddress();
                this.port = lengthPacket.getPort();
            }
            sendACK(lengthPacket.getAddress(), lengthPacket.getPort());
            int length = Integer.parseInt(new String(lengthPacket.getData()).trim());
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            if (length != packet.getLength()) {
                System.out.println("Error: Length does not match the length of the message");
                System.exit(0);
            }
            sendACK(packet.getAddress(), packet.getPort());
            sb.append(new String(packet.getData(), 0, packet.getLength()));
        }
        return sb.toString();
    }
}
