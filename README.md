<h1>Technical Report</h1>
<h2>Overview</h2>
    <p>This Java-based project comprises five main components: a TCP server (server_java_TCP), a TCP client (client_java_TCP), an UDP server (server_java_UDP), an UDP client (client_java_UDP), and a utility class for anonymization (Anonymizer). This system primarily intends to manipulate a String, searching inside it for a Substring (String keyword), both strings given by the client. If any occurrence of the keyword (case-insensitive) exists, it will be replaced it with a String of capital 'X's with the length of the keyword String, returning the final result to the client.</p>

<h2>Code Organization</h2>
<h3>The codebase is organized into the following components:</h3>
<ul>
    <li><code>src/Server_java_TCP.java:</code> TCP server implementation.</li>
    <li><code>src/Client_java_TCP.java:</code> TCP client implementation.</li>
    <li><code>src/Server_java_UDP.java:</code> UDP server implementation.</li>
    <li><code>src/Client_java_UDP.java:</code> UDP client implementation.</li>
    <li><code>src/Anonymizer.java:</code> Utility class for anonymization.</li>
</ul>

<h2>Client TCP Component (Client_java_TCP)</h2>
<h3>Features</h3>
<ul>
    <li>Establishes a TCP connection with a server.</li>
    <li>Sends a user-provided string and keyword to the server.</li>
    <li>Receives and prints the server's response.</li>
    <li>Handles various exceptions for robust error management.</li>
</ul>

<h3>Usage</h3>
<pre><code>
   Client_java_TCP client = new Client_java_TCP();   
   client.sendData(serverName, port, userString, keyword);
</code></pre>

<h3>Key Components</h3>
<ul>
    <li>Socket: Used for communication with the server.</li>
    <li>BufferedReader: Reads user input and server responses.</li>
    <li>PrintWriter: Sends data to the server.</li>
</ul>


<h3>Constructor</h3>
<pre><code> public Client_java_TCP() {
        socket = null;}
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
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
            System.err.println("I/O error: " + e.getMessage());}}
</code></pre>

<h3>Methods</h3>
<p>
    <code>sendData(String host, String port, String userString, String keyword)</code>: Sends data to the server, expecting input from the user (Strings: IP address message and keyword, int: port) using that information to send the data to the server.
</p>

<h3>Exception Handling</h3>
<ul>
    <li><strong>UnknownHostException:</strong> Handles an unknown server or unresolved host. Displays an error message and terminates the program.</li>
    <li><strong>SocketTimeoutException:</strong> Handles a connection timeout. Displays an error message and terminates the program.</li>
    <li><strong>ConnectException:</strong> Handles inability to establish a connection to the server. Displays an error message and terminates the program.</li>
    <li><strong>IOException:</strong> Handles general I/O errors during socket communication. Displays an error message and terminates the program.</li>
    <li><strong>NumberFormatException:</strong> Handles an invalid port format (non-integer). Displays an error message and terminates the program.</li>
</ul>

<h3>Functionality</h3>
<p>
    Establishes a TCP connection, sends user data, receives server response, and handles exceptions gracefully.
</p>

<h2>Server TCP component (Server_java_TCP)</h2>

<p>The <code>Server_java_TCP</code> class implements a TCP server in Java, handling client connections, reading input, and sending responses. It uses TCP/IP for communication, ensuring that data is transmitted reliably across the network. This class is structured to listen on a specified port, accept incoming connections, and process data according to the server's logic, such as anonymizing keywords in received text.</p>

<h3>Features</h3>
<ul>
    <li><strong>TCP/IP Communication</strong>: Utilizes TCP/IP protocol for reliable data transmission.</li>
    <li><strong>Keyword Anonymization</strong>: Anonymizes specified keywords in received strings by replacing them with uppercase "X".</li>
    <li><strong>Multi-Client Handling</strong>: Capable of handling multiple client connections sequentially.</li>
    <li><strong>Keyword Count</strong>: Returns the number of times a keyword was replaced in each string.</li>
    <li><strong>Socket Programming Acknowledgment</strong>: Sends a "Socket Programming" message for each keyword replacement.
</li>
</ul>

<h3>Usage</h3>
<pre><code>Server_java_TCP server = new Server_java_TCP(port);
server.startListening();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>ServerSocket</strong>: Manages incoming connections, listening for requests on a specified port.</li>
        <li><strong>BufferedReader and PrintWriter</strong>: Facilitate reading from and writing to the client, respectively, over the established TCP connection.</li>
        <li><strong>Client Connection Handling</strong>: Accepts and processes each client connection, enabling the anonymization and counting of keywords.</li>
</ul>

<h3>Constructor</h3>
    <pre><code>public Server_java_TCP(int port) throws IOException {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }</code></pre>

<h3>Main Method</h3>
    <pre><code>public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java server_java_TCP <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]); //converts String to Integer
        Server_java_TCP server = new Server_java_TCP(port); //creates server
        server.waitConnections();
    }</code></pre>

<h3>Methods</h3>
    <ul>
        <li><code>waitConnections()</code>: Waits for and accepts incoming client connections. For each connection, it reads a string and a keyword from the client, anonymizes the keyword in the string, sends back the anonymized string along with the count of keyword substitutions, and for each substitution, sends a "Socket Programming" acknowledgment message back to the client.</li>        
    </ul>

<h3>Exception Handling</h3>
    <ul>
        <li><strong>IOException</strong>: Handles input/output errors related to network and stream operations.</li>
    </ul>

<h3>Functionality</h3>
    <ul>
        <li>Provides utility methods for the TCP server component, facilitating key server operations.</li>
        <li>Handles common tasks related to the TCP server, such as establishing ServerSocket, accepting client connections, and managing data flow between the server and clients.</li>
    </ul>

<h2>Utility Class(Anonymizer)</h2>
<p>The <code>Anonymizer</code> class is a Java implementation designed to anonymize specific keywords in a given string. Below is an overview of the class, including its functionality, methods, and exception handling.</p>

<h3>Functionality</h3>
<ul>
  <li><strong>Keyword Anonymization</strong>: Replaces all occurrences of a specified keyword in a string with the character 'X'.</li>
  <li><strong>Case Sensitivity Handling</strong>: Converts both the input string and the keyword to lower case to ensure case-insensitive matching.</li>
  <li><strong>Position Tracking</strong>: Identifies and tracks the starting positions of the keyword within the input string.</li>
</ul>

<h3>Key Components</h3>
<ul>
  <li><strong>Anonymize Keyword</strong>: replaces occurrences of the keyword with 'X'.</li>
  <li><strong>Starting Positions Method</strong>: Identifies the starting positions of the keyword in the input string.</li>
  <li><strong>Anonymize Positions Method</strong>: Replaces characters at specified positions with 'X'.</li>
  <li><strong>String Builders</strong>: Utilized for efficient string manipulation during the anonymization process.</li>
  <li><strong>List Data Structure</strong>: Stores the positions of the keyword occurrences.</li>
</ul>

<h3>Methods</h3>
<ol>
  <li><code>public static String anonymizeKeyword(String input, String keyword):</code>Anonymizes the given string by replacing the keyword with 'X'.Returns the modified string after anonymization.
  </li>
  <li><code>public static List&lt;Integer&gt; startingPositions(String input, String keyword):</code>Identifies the start positions of the keyword in the input string. Returns a list of integer positions.
  </li>
  <li><code>private static String anonymizePositions(String input, List&lt;Integer&gt; positions, String keyword):</code>Anonymizes specific positions in the string based on the provided list. Returns the modified string after replacement. </li>
</ol>

<h3>Exception Handling</h3>
<ul>
  <li><code>NullPointerException</code>: Thrown if either the input string or the keyword is null. This ensures that the method does not operate on invalid data.</li>
</ul>

<h2>Client UDP component (Cliente_java_UDP)</h2>
<h3>Features</h3>
<ul>
    <li><strong>Message Transmission</strong>: Sends messages to a server using UDP communication.</li>
    <li><strong>Keyword Anonymization</strong>: Anonymizes keywords for secure data transfer.</li>
    <li><strong>Data Segmentation</strong>: Handles large messages by dividing them into manageable parts.</li>
    <li><strong>ACK Mechanism</strong>: Implements an acknowledgment system for reliable data transfer.</li>
    <li><strong>Socket Programming Recognition</strong>: Prints occurrences of the phrase "Socket Programming" in received messages.</li>
</ul>

<h3>Usage</h3>
<pre><code>Client_java_UDP client = new Client_java_UDP();
client.sendData(message, keyword, address, port);
client.receiveData();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>DatagramSocket</strong>: The DatagramSocket used to send and receive data.</li>
    <li><strong>Max Timeout</strong>: Maximum time to wait for a response from the server.</li>
    <li><strong>Buffer Size</strong>: The size of the buffer used for data transmission.</li>
    <li><strong>Acknowledgment Message</strong>: The acknowledgment message sent by the server ("ACK").</li>
    <li><strong>IP Address</strong>: The IP address of the server.</li>
    <li><strong>Port Number</strong>: The port number of the server.</li>
</ul>

<h3>Constructor</h3>
<pre><code>public Client_java_UDP() throws IOException {
    try {
        socket = new DatagramSocket();
        socket.setSoTimeout(MAX_TIMEOUT);
    } catch (IOException e) {
        System.out.println("Socket error: " + e.getMessage());
    }
}
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
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
</code></pre>

<h3>Methods</h3>
 <li><code>createUDPSocket()</code>: Initializes a DatagramSocket for UDP communication.</li>
    <li><code>sendData(String message, String keyword, InetAddress address, int port)</code>: Sends a message and a keyword to the server. Divides the message into parts if it exceeds the buffer size.</li>
    <li><code>sendPartsOfMessage(InetAddress address, int port, String[] dividedMessage, byte[] bufferData, byte[] bufferLength)</code>: Sends parts of the message, handling acknowledgments for each part.</li>
    <li><code>divideString(String input)</code>: Divides a given string into parts based on a specified buffer size, considering multi-byte characters.</li>
    <li><code>sendPacket(byte[] buffer, InetAddress address, int port)</code>: Sends a packet to the server and waits for an acknowledgment ("ACK").</li>
    <li><code>waitForAck()</code>: Waits for an acknowledgment message from the server.</li>
    <li><code>receiveData()</code>: Receives the answer from the server, including the message and occurrences of "Socket Programming."</li>
    <li><code>receiveNumberOfParts()</code>: Receives the number of parts of the message from the server, handling acknowledgments.</li>
    <li><code>sendACK(InetAddress address, int port)</code>: Sends an acknowledgment message to the server.</li>
    <li><code>receiveMessage(int numberOfParts)</code>: Receives the message from the server in pieces, handling acknowledgments for each part.</li>

<h3>Exception Handling</h3>
<p>The UDP client component handles various exceptions to ensure robust communication. The following exceptions are managed:</p>

<ul>
    <li><code>IOException</code>: Handles general I/O errors during socket communication.</li>
    <li><code>NumberFormatException</code>: Manages errors related to invalid port number formatting.</li>
    <li><code>UnknownHostException</code>: Deals with issues when the server's host address cannot be determined.</li>
    <li><code>SocketTimeoutException</code>: Addresses situations where the connection times out.</li>
</ul>

<p>For each exception, appropriate error messages are displayed, and the program is terminated in case of critical failures.</p>

<h3>Functionality</h3>
<ul>
    <li>Provides utility methods for the UDP client component.</li>
    <li>Handles common tasks related to the UDP client, such as creating a DatagramSocket, sending data, and handling acknowledgments.</li>
</ul>


<h2>Server UDP component (Server_java_UDP)</h2>
<h3>Features</h3>
<ul>
    <li><strong>Message Anonymization</strong>: Anonymizes messages based on a given keyword (using Anonymizer.class methods).</li>
    <li><strong>Segmented Data Transfer</strong>: Handles messages larger than the buffer size by dividing them into parts.</li>
    <li><strong>Acknowledgement System</strong>: Uses 'ACK' messages to ensure complete data transfer.</li>
    <li><strong>Single Client Communication</strong>: Communicates with one client at a time.</li>
</ul>

<h3>Usage</h3>
<pre><code>Server_java_UDP server = new Server_java_UDP(port);
server.receiveData();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>Max Timeout</strong>: Maximum time to wait for a response from the server.</li>
    <li><strong>Buffer Size</strong>: The size of the buffer used for data transmission.</li>
    <li><strong>Acknowledgment Message</strong>: The message sent by the server to acknowledge receipt.</li>
    <li><strong>Socket Programming String</strong>: Used to indicate substitutions made in the message.</li>
</ul>

<h3>Constructor</h3>
<pre><code>public Server_java_UDP(int port) {
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
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        Server_java_UDP server = new Server_java_UDP(port);
        server.receiveData();
    }
</code></pre>

<h3>Methods</h3>
<ul>
    <li><code>receiveData()</code>: connects the initialization of the server to processing the message and keyword from the client.</li>
    <li><code>receiveAndProcessData()</code>: Coordinates the reception and processing of data. Receives the length of the message, the number of parts in which it is fragmented, the string itself, the number of parts in which the keyword will be fragmented and the keyword itself.Checks to see if any String is received (regarding keyword and message) and, if so if it is in the correct length</li>
    <li><code>receiveNumberOfParts()</code>: receives the DatagramPackets indicating the length of the message String, then the message itself, checking to see if their length matches. Sends 2 Ack Packets tp the client if everything goes well</li>
    <li><code>receivePartsOfMessage(int numberOfParts):</code> similar process to receiveNumberOfParts, but for the message in itself. repeats the loop for the amount of fragments that will be sent from the client.</li>
    <li><code>sendACK(InetAddress address, int port):</code> sends an 'ACK' DatagramPacket back to the client side everytime it is required by the receive functions/methods.</li>
    <li><code>divideString(String string):</code> receives a String and divides it in slices according to the maximum buffer size</li>
    <li><code>sendData(int numberOfSocketProgrammings, String[] dividedAnonymizedMessage):</code> coordinator of sending of DatagramPackets back to the client. sends first the number of "\nSocketProgramming" Strings that will be sent to the client, the number of portions in which the String is divided, sends the actual String (in portions) and then the Socket Programmings</li>
    <li><code>sendNumberPacket(int numberOf):</code> sends the packets with the count of anonymized Strings fragments that will be sent to the client. Waits for 2 Acks from client</li>
    <li><code>sendDataPacket(String[] dividedAnonymizedMessage):</code> sends 2 DatagramPacket per each
    fragment, one with the information about the length of the 2nd packet, and the actual data packet. Waits for 2 ACK DatagramPackets from the client, one after the length packet and one after the data packet.</li>
    <li><code>sendSocketProgamming(int numberOfSocketProgrammings):</code> similar to the previous one but only for the amount of "\nSocket Programming" Strings</li>
    <li><code>waitForAck():</code> method to coordinate all the send methods, requiring them to wait after each DatagramPacket sending for a corresponding ACK DatagramPacket. After the pre-established timeout, throws annd exception</li>
</ul>

<h3>Exception Handling</h3>
<ul>
    <li><code>IOException</code>: Handles general I/O errors during socket communication.</li>
    <li><code>SocketTimeoutException</code>: Addresses situations where the connection times out.</li>
    <li><code>SocketException</code>: Addresses issues with the Socket itself.</li>
</ul>


<h3>Functionality</h3>
<ul>
    <li>Provides utility methods for the UDP client component.</li>
    <li>Handles common tasks related to the UDP client, such as creating a DatagramSocket, sending data, and handling acknowledgments.</li>
</ul>

<h2>Challenges and Solutions</h2>
<ul>
    <li><strong>String Anonymization:</strong> Implemented case-insensitive search and efficient string manipulation using String.toLowerCase(), indexOf(), and StringBuilder.</li>
    <li><strong>Network Communication both in TCP and UDP:</strong>s Addressed TCP connection handling, data integrity, and network exceptions through robust exception handling and socket management. Addressed with quality and security the communication with UDP, using 'ACK' confirmations and DatagramPacket's to send the length of other DatagramPackets.</li>
    <li><strong>Reliable UDP implementation:</strong> in order to implemente a reliable UDP that can handle String fragmentation, per every packet that needs to be sent, a first one with the information about its buffer’s length needs to be sent and adequately received by the other side. Also from the other side needs to come na ACK String attesting that they received one length packet and another ACK attesting that they received the actual packet and it has the length sent in the length packet. It’s a lot of crossed information and is very mistake-prone.</li>
    
</ul>

<h2>Future Enhancements</h2>
<ul>
    <li><strong>Concurrency Handling:</strong> Enable the server to handle multiple client connections simultaneously.</li>
    <li><strong>User Interface:</strong> Develop a GUI for the client application for easier interaction.</li>
    <li><strong>Extended Functionality:</strong> Expand the Anonymizer class to include more sophisticated text processing features.</li>
    <li><strong>Keyword Context Sensitivity:</strong> The current system might anonymize keywords regardless of their context. Developing a more context-aware system can be challenging but would enhance the tool's usefulness.</li>
</ul>

<h2>References</h2>
<ul>
    <li>lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoServer.java" (N/A) Type: source code</li>
    <li>lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoClient.java" (N/A) Type: source code</li>
    <li>lmn@isep.ipp.pt - 06/01/2024 -"UDPEchoCServer.java" (unknown version) Type: source code</li>
    <li>lmn@isep.ipp.pt - 06/01/2024 -"UDPEchoCClient.java" (unknown version) Type: source code</li>
    <li>Stack Overflow (2023) "How to replace a substring in Java" [Community Discussion].</li>
    <li>Oracle (2023) "Class String" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class NullPointerException" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Interface List<E>" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class ArrayList<E>" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class StringBuilder" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class ServerSocket" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class BufferedReader" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class InputStreamReader" [Official Documentation]. Java Platform Standard Edition 8 Documentation</li>
    <li>Oracle (2023) "Class PrintWriter" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
<li>Oracle(2023)"Class DatagramSocket" [Official Java Documentation]. Java Platform Standard Edition 8 Documentation.</li>
        <li>Oracle(2023)"Class InetAddress" [Official Java Documentation],  Java Platform Standard Edition 8 Documentation.</li>
        <li>Oracle(2023)"Class InetAddress" [Official Java Documentation], Java Platform Standard Edition 8 Documentation</li>
</ul>
    <h1>Technical Report</h1>
<h2>Overview</h2>
    <p>This Java-based project comprises five main components: a TCP server (server_java_TCP), a TCP client (client_java_TCP), an UDP server (server_java_UDP), an UDP client (client_java_UDP), and a utility class for anonymization (Anonymizer). This system primarily intends to manipulate a String, searching inside it for a Substring (String keyword), both strings given by the client. If any occurrence of the keyword (case-insensitive) exists, it will be replaced it with a String of capital 'X's with the length of the keyword String, returning the final result to the client.</p>

<h2>Code Organization</h2>
<h3>The codebase is organized into the following components:</h3>
<ul>
    <li><code>src/Server_java_TCP.java:</code> TCP server implementation.</li>
    <li><code>src/Client_java_TCP.java:</code> TCP client implementation.</li>
    <li><code>src/Server_java_UDP.java:</code> UDP server implementation.</li>
    <li><code>src/Client_java_UDP.java:</code> UDP client implementation.</li>
    <li><code>src/Anonymizer.java:</code> Utility class for anonymization.</li>
</ul>

<h2>Client TCP Component (Client_java_TCP)</h2>
<h3>Features</h3>
<ul>
    <li>Establishes a TCP connection with a server.</li>
    <li>Sends a user-provided string and keyword to the server.</li>
    <li>Receives and prints the server's response.</li>
    <li>Handles various exceptions for robust error management.</li>
</ul>

<h3>Usage</h3>
<pre><code>
   Client_java_TCP client = new Client_java_TCP();   
   client.sendData(serverName, port, userString, keyword);
</code></pre>

<h3>Key Components</h3>
<ul>
    <li>Socket: Used for communication with the server.</li>
    <li>BufferedReader: Reads user input and server responses.</li>
    <li>PrintWriter: Sends data to the server.</li>
</ul>


<h3>Constructor</h3>
<pre><code> public Client_java_TCP() {
        socket = null;}
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
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
            System.err.println("I/O error: " + e.getMessage());}}
</code></pre>

<h3>Methods</h3>
<p>
    <code>sendData(String host, String port, String userString, String keyword)</code>: Sends data to the server, expecting input from the user (Strings: IP address message and keyword, int: port) using that information to send the data to the server.
</p>

<h3>Exception Handling</h3>
<ul>
    <li><strong>UnknownHostException:</strong> Handles an unknown server or unresolved host. Displays an error message and terminates the program.</li>
    <li><strong>SocketTimeoutException:</strong> Handles a connection timeout. Displays an error message and terminates the program.</li>
    <li><strong>ConnectException:</strong> Handles inability to establish a connection to the server. Displays an error message and terminates the program.</li>
    <li><strong>IOException:</strong> Handles general I/O errors during socket communication. Displays an error message and terminates the program.</li>
    <li><strong>NumberFormatException:</strong> Handles an invalid port format (non-integer). Displays an error message and terminates the program.</li>
</ul>

<h3>Functionality</h3>
<p>
    Establishes a TCP connection, sends user data, receives server response, and handles exceptions gracefully.
</p>

<h2>Server TCP component (Server_java_TCP)</h2>

<p>The <code>Server_java_TCP</code> class implements a TCP server in Java, handling client connections, reading input, and sending responses. It uses TCP/IP for communication, ensuring that data is transmitted reliably across the network. This class is structured to listen on a specified port, accept incoming connections, and process data according to the server's logic, such as anonymizing keywords in received text.</p>

<h3>Features</h3>
<ul>
    <li><strong>TCP/IP Communication</strong>: Utilizes TCP/IP protocol for reliable data transmission.</li>
    <li><strong>Keyword Anonymization</strong>: Anonymizes specified keywords in received strings by replacing them with uppercase "X".</li>
    <li><strong>Multi-Client Handling</strong>: Capable of handling multiple client connections sequentially.</li>
    <li><strong>Keyword Count</strong>: Returns the number of times a keyword was replaced in each string.</li>
    <li><strong>Socket Programming Acknowledgment</strong>: Sends a "Socket Programming" message for each keyword replacement.
</li>
</ul>

<h3>Usage</h3>
<pre><code>Server_java_TCP server = new Server_java_TCP(port);
server.startListening();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>ServerSocket</strong>: Manages incoming connections, listening for requests on a specified port.</li>
        <li><strong>BufferedReader and PrintWriter</strong>: Facilitate reading from and writing to the client, respectively, over the established TCP connection.</li>
        <li><strong>Client Connection Handling</strong>: Accepts and processes each client connection, enabling the anonymization and counting of keywords.</li>
</ul>

<h3>Constructor</h3>
    <pre><code>public Server_java_TCP(int port) throws IOException {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }</code></pre>

<h3>Main Method</h3>
    <pre><code>public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java server_java_TCP <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]); //converts String to Integer
        Server_java_TCP server = new Server_java_TCP(port); //creates server
        server.waitConnections();
    }</code></pre>

<h3>Methods</h3>
    <ul>
        <li><code>waitConnections()</code>: Waits for and accepts incoming client connections. For each connection, it reads a string and a keyword from the client, anonymizes the keyword in the string, sends back the anonymized string along with the count of keyword substitutions, and for each substitution, sends a "Socket Programming" acknowledgment message back to the client.</li>        
    </ul>

<h3>Exception Handling</h3>
    <ul>
        <li><strong>IOException</strong>: Handles input/output errors related to network and stream operations.</li>
    </ul>

<h3>Functionality</h3>
    <ul>
        <li>Provides utility methods for the TCP server component, facilitating key server operations.</li>
        <li>Handles common tasks related to the TCP server, such as establishing ServerSocket, accepting client connections, and managing data flow between the server and clients.</li>
    </ul>

<h2>Utility Class(Anonymizer)</h2>
<p>The <code>Anonymizer</code> class is a Java implementation designed to anonymize specific keywords in a given string. Below is an overview of the class, including its functionality, methods, and exception handling.</p>

<h3>Functionality</h3>
<ul>
  <li><strong>Keyword Anonymization</strong>: Replaces all occurrences of a specified keyword in a string with the character 'X'.</li>
  <li><strong>Case Sensitivity Handling</strong>: Converts both the input string and the keyword to lower case to ensure case-insensitive matching.</li>
  <li><strong>Position Tracking</strong>: Identifies and tracks the starting positions of the keyword within the input string.</li>
</ul>

<h3>Key Components</h3>
<ul>
  <li><strong>Anonymize Keyword</strong>: replaces occurrences of the keyword with 'X'.</li>
  <li><strong>Starting Positions Method</strong>: Identifies the starting positions of the keyword in the input string.</li>
  <li><strong>Anonymize Positions Method</strong>: Replaces characters at specified positions with 'X'.</li>
  <li><strong>String Builders</strong>: Utilized for efficient string manipulation during the anonymization process.</li>
  <li><strong>List Data Structure</strong>: Stores the positions of the keyword occurrences.</li>
</ul>

<h3>Methods</h3>
<ol>
  <li><code>public static String anonymizeKeyword(String input, String keyword):</code>Anonymizes the given string by replacing the keyword with 'X'.Returns the modified string after anonymization.
  </li>
  <li><code>public static List&lt;Integer&gt; startingPositions(String input, String keyword):</code>Identifies the start positions of the keyword in the input string. Returns a list of integer positions.
  </li>
  <li><code>private static String anonymizePositions(String input, List&lt;Integer&gt; positions, String keyword):</code>Anonymizes specific positions in the string based on the provided list. Returns the modified string after replacement. </li>
</ol>

<h3>Exception Handling</h3>
<ul>
  <li><code>NullPointerException</code>: Thrown if either the input string or the keyword is null. This ensures that the method does not operate on invalid data.</li>
</ul>

<h2>Client UDP component (Cliente_java_UDP)</h2>
<h3>Features</h3>
<ul>
    <li><strong>Message Transmission</strong>: Sends messages to a server using UDP communication.</li>
    <li><strong>Keyword Anonymization</strong>: Anonymizes keywords for secure data transfer.</li>
    <li><strong>Data Segmentation</strong>: Handles large messages by dividing them into manageable parts.</li>
    <li><strong>ACK Mechanism</strong>: Implements an acknowledgment system for reliable data transfer.</li>
    <li><strong>Socket Programming Recognition</strong>: Prints occurrences of the phrase "Socket Programming" in received messages.</li>
</ul>

<h3>Usage</h3>
<pre><code>Client_java_UDP client = new Client_java_UDP();
client.sendData(message, keyword, address, port);
client.receiveData();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>DatagramSocket</strong>: The DatagramSocket used to send and receive data.</li>
    <li><strong>Max Timeout</strong>: Maximum time to wait for a response from the server.</li>
    <li><strong>Buffer Size</strong>: The size of the buffer used for data transmission.</li>
    <li><strong>Acknowledgment Message</strong>: The acknowledgment message sent by the server ("ACK").</li>
    <li><strong>IP Address</strong>: The IP address of the server.</li>
    <li><strong>Port Number</strong>: The port number of the server.</li>
</ul>

<h3>Constructor</h3>
<pre><code>public Client_java_UDP() throws IOException {
    try {
        socket = new DatagramSocket();
        socket.setSoTimeout(MAX_TIMEOUT);
    } catch (IOException e) {
        System.out.println("Socket error: " + e.getMessage());
    }
}
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
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
</code></pre>

<h3>Methods</h3>
 <li><code>createUDPSocket()</code>: Initializes a DatagramSocket for UDP communication.</li>
    <li><code>sendData(String message, String keyword, InetAddress address, int port)</code>: Sends a message and a keyword to the server. Divides the message into parts if it exceeds the buffer size.</li>
    <li><code>sendPartsOfMessage(InetAddress address, int port, String[] dividedMessage, byte[] bufferData, byte[] bufferLength)</code>: Sends parts of the message, handling acknowledgments for each part.</li>
    <li><code>divideString(String input)</code>: Divides a given string into parts based on a specified buffer size, considering multi-byte characters.</li>
    <li><code>sendPacket(byte[] buffer, InetAddress address, int port)</code>: Sends a packet to the server and waits for an acknowledgment ("ACK").</li>
    <li><code>waitForAck()</code>: Waits for an acknowledgment message from the server.</li>
    <li><code>receiveData()</code>: Receives the answer from the server, including the message and occurrences of "Socket Programming."</li>
    <li><code>receiveNumberOfParts()</code>: Receives the number of parts of the message from the server, handling acknowledgments.</li>
    <li><code>sendACK(InetAddress address, int port)</code>: Sends an acknowledgment message to the server.</li>
    <li><code>receiveMessage(int numberOfParts)</code>: Receives the message from the server in pieces, handling acknowledgments for each part.</li>

<h3>Exception Handling</h3>
<p>The UDP client component handles various exceptions to ensure robust communication. The following exceptions are managed:</p>

<ul>
    <li><code>IOException</code>: Handles general I/O errors during socket communication.</li>
    <li><code>NumberFormatException</code>: Manages errors related to invalid port number formatting.</li>
    <li><code>UnknownHostException</code>: Deals with issues when the server's host address cannot be determined.</li>
    <li><code>SocketTimeoutException</code>: Addresses situations where the connection times out.</li>
</ul>

<p>For each exception, appropriate error messages are displayed, and the program is terminated in case of critical failures.</p>

<h3>Functionality</h3>
<ul>
    <li>Provides utility methods for the UDP client component.</li>
    <li>Handles common tasks related to the UDP client, such as creating a DatagramSocket, sending data, and handling acknowledgments.</li>
</ul>


<h2>Server UDP component (Server_java_UDP)</h2>
<h3>Features</h3>
<ul>
    <li><strong>Message Anonymization</strong>: Anonymizes messages based on a given keyword (using Anonymizer.class methods).</li>
    <li><strong>Segmented Data Transfer</strong>: Handles messages larger than the buffer size by dividing them into parts.</li>
    <li><strong>Acknowledgement System</strong>: Uses 'ACK' messages to ensure complete data transfer.</li>
    <li><strong>Single Client Communication</strong>: Communicates with one client at a time.</li>
</ul>

<h3>Usage</h3>
<pre><code>Server_java_UDP server = new Server_java_UDP(port);
server.receiveData();
</code></pre>

<h3>Key Components</h3>
<ul>
    <li><strong>Max Timeout</strong>: Maximum time to wait for a response from the server.</li>
    <li><strong>Buffer Size</strong>: The size of the buffer used for data transmission.</li>
    <li><strong>Acknowledgment Message</strong>: The message sent by the server to acknowledge receipt.</li>
    <li><strong>Socket Programming String</strong>: Used to indicate substitutions made in the message.</li>
</ul>

<h3>Constructor</h3>
<pre><code>public Server_java_UDP(int port) {
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
</code></pre>

<h3>Main Method</h3>
<pre><code>public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        Server_java_UDP server = new Server_java_UDP(port);
        server.receiveData();
    }
</code></pre>

<h3>Methods</h3>
<ul>
    <li><code>receiveData()</code>: connects the initialization of the server to processing the message and keyword from the client.</li>
    <li><code>receiveAndProcessData()</code>: Coordinates the reception and processing of data. Receives the length of the message, the number of parts in which it is fragmented, the string itself, the number of parts in which the keyword will be fragmented and the keyword itself.Checks to see if any String is received (regarding keyword and message) and, if so if it is in the correct length</li>
    <li><code>receiveNumberOfParts()</code>: receives the DatagramPackets indicating the length of the message String, then the message itself, checking to see if their length matches. Sends 2 Ack Packets tp the client if everything goes well</li>
    <li><code>receivePartsOfMessage(int numberOfParts):</code> similar process to receiveNumberOfParts, but for the message in itself. repeats the loop for the amount of fragments that will be sent from the client.</li>
    <li><code>sendACK(InetAddress address, int port):</code> sends an 'ACK' DatagramPacket back to the client side everytime it is required by the receive functions/methods.</li>
    <li><code>divideString(String string):</code> receives a String and divides it in slices according to the maximum buffer size</li>
    <li><code>sendData(int numberOfSocketProgrammings, String[] dividedAnonymizedMessage):</code> coordinator of sending of DatagramPackets back to the client. sends first the number of "\nSocketProgramming" Strings that will be sent to the client, the number of portions in which the String is divided, sends the actual String (in portions) and then the Socket Programmings</li>
    <li><code>sendNumberPacket(int numberOf):</code> sends the packets with the count of anonymized Strings fragments that will be sent to the client. Waits for 2 Acks from client</li>
    <li><code>sendDataPacket(String[] dividedAnonymizedMessage):</code> sends 2 DatagramPacket per each
    fragment, one with the information about the length of the 2nd packet, and the actual data packet. Waits for 2 ACK DatagramPackets from the client, one after the length packet and one after the data packet.</li>
    <li><code>sendSocketProgamming(int numberOfSocketProgrammings):</code> similar to the previous one but only for the amount of "\nSocket Programming" Strings</li>
    <li><code>waitForAck():</code> method to coordinate all the send methods, requiring them to wait after each DatagramPacket sending for a corresponding ACK DatagramPacket. After the pre-established timeout, throws annd exception</li>
</ul>

<h3>Exception Handling</h3>
<ul>
    <li><code>IOException</code>: Handles general I/O errors during socket communication.</li>
    <li><code>SocketTimeoutException</code>: Addresses situations where the connection times out.</li>
    <li><code>SocketException</code>: Addresses issues with the Socket itself.</li>
</ul>


<h3>Functionality</h3>
<ul>
    <li>Provides utility methods for the UDP client component.</li>
    <li>Handles common tasks related to the UDP client, such as creating a DatagramSocket, sending data, and handling acknowledgments.</li>
</ul>

<h2>Challenges and Solutions</h2>
<ul>
    <li><strong>String Anonymization:</strong> Implemented case-insensitive search and efficient string manipulation using String.toLowerCase(), indexOf(), and StringBuilder.</li>
    <li><strong>Network Communication both in TCP and UDP:</strong>s Addressed TCP connection handling, data integrity, and network exceptions through robust exception handling and socket management. Addressed with quality and security the communication with UDP, using 'ACK' confirmations and DatagramPacket's to send the length of other DatagramPackets.</li>
    <li><strong>Reliable UDP implementation:</strong> in order to implemente a reliable UDP that can handle String fragmentation, per every packet that needs to be sent, a first one with the information about its buffer’s length needs to be sent and adequately received by the other side. Also from the other side needs to come na ACK String attesting that they received one length packet and another ACK attesting that they received the actual packet and it has the length sent in the length packet. It’s a lot of crossed information and is very mistake-prone.</li>
    
</ul>

<h2>Future Enhancements</h2>
<ul>
    <li><strong>Concurrency Handling:</strong> Enable the server to handle multiple client connections simultaneously.</li>
    <li><strong>User Interface:</strong> Develop a GUI for the client application for easier interaction.</li>
    <li><strong>Extended Functionality:</strong> Expand the Anonymizer class to include more sophisticated text processing features.</li>
    <li><strong>Keyword Context Sensitivity:</strong> The current system might anonymize keywords regardless of their context. Developing a more context-aware system can be challenging but would enhance the tool's usefulness.</li>
</ul>

<h2>References</h2>
<ul>
    <li>lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoServer.java" (N/A) Type: source code</li>
    <li>lmn@isep.ipp.pt - 27/12/2023 -"TCPEchoClient.java" (N/A) Type: source code</li>
    <li>lmn@isep.ipp.pt - 06/01/2024 -"UDPEchoCServer.java" (unknown version) Type: source code</li>
    <li>lmn@isep.ipp.pt - 06/01/2024 -"UDPEchoCClient.java" (unknown version) Type: source code</li>
    <li>Stack Overflow (2023) "How to replace a substring in Java" [Community Discussion].</li>
    <li>Oracle (2023) "Class String" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class NullPointerException" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Interface List<E>" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class ArrayList<E>" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class StringBuilder" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class ServerSocket" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class BufferedReader" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
    <li>Oracle (2023) "Class InputStreamReader" [Official Documentation]. Java Platform Standard Edition 8 Documentation</li>
    <li>Oracle (2023) "Class PrintWriter" [Official Documentation]. Java Platform Standard Edition 8 Documentation.</li>
<li>Oracle(2023)"Class DatagramSocket" [Official Java Documentation]. Java Platform Standard Edition 8 Documentation.</li>
        <li>Oracle(2023)"Class InetAddress" [Official Java Documentation],  Java Platform Standard Edition 8 Documentation.</li>
        <li>Oracle(2023)"Class InetAddress" [Official Java Documentation], Java Platform Standard Edition 8 Documentation</li>
</ul>
    