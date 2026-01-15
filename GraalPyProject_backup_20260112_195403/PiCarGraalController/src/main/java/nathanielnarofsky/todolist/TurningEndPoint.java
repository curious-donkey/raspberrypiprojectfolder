package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TurningEndPoint implements Runnable {

    
    public void onMessage(String message) {
        // Code to handle incoming messages from clients
        // Implementation for turning commands will go here
        if (message.equals("TURN LEFT")) {
            sendCommandToPiCarTurning("LEFT");
        }
        else if (message.equals("TURN RIGHT")) {
            sendCommandToPiCarTurning("RIGHT");
        }
        
    }
    


    private void sendCommandToPiCarTurning(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCommandToPiCarTurning'");
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(59900);
            System.out.println("MovementEndPoint listening on port 59900");
            boolean running = true;
            while (running == true) {
                    // Accept incoming connections and handle them
                    // This is where Socket server logic goes
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Connected to MoveMentEndPoint");
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

    
