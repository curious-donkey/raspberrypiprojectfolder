package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BrainEndPoint implements Runnable {

    public void onMessage(String message) {
        // Code to handle incoming messages from clients
        // Implementation for brain commands will go here
        // stuff related to AI processing and decision making
        // and for switching modes between autonomous and manual control
        // this is where the "brain" of the Picar will be directed
            // this is the hard part
            // aside from rewriting the entire python code into Java
            // which I will be doing
            // I will use Pi4J to rewrite the GPIO control code
            // I will use graalvm's python interoperability to run AI code in python
            // and have it communicate with the Java code
            // the hardware calls will have to be entirely rewritten
            // but the AI logic can remain in python
            // the result will be a faster and more efficient PiCar controller
            // even though that will be a decent amount of work
            throw new UnsupportedOperationException("Unimplemented method 'onMessage'");

    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(59903);
            System.out.println("BrainEndPoint listening on port 59903");
            boolean running = true;
            while (running == true) {
                    // Accept incoming connections and handle them
                    // This is where Socket server logic goes
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Connected to BrainEndPoint");
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    String message = in.readUTF();
                    onMessage(message);
                    // Further handling of clientSocket will go here
                    running = false; // Placeholder to exit loop after one connection for now
                    serverSocket.close();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
     

    }
    }







