package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class MoveMentEndPoint implements Runnable {
    // WebSocket endpoint implementation will go here
    // this class sends commands via Py4J to control the PiCar

    private final PiCarxMovementObjectHandler keypad = new PiCarxMovementObjectHandler();
    

    public void sendCommandToPiCar(String message) {
        // Code to handle incoming messages from clients
    
        if (message.startsWith("FORWARD")) {
            keypad.moveForward();
        }
        else if (message.startsWith("BACKWARD")) {
            keypad.moveBackward();
        }  
    }



    /**
     * ignore the error message from visual studio code about the Context class
     * this project uses Java 25 so text block feature is available
     * this command is for invoking actions in the native python code
     * 
     */
    private void onMessage(String direction) {
        if (Main.getAutonomyState().equals("AUTONOMOUS")) {
            System.out.println("Movement control disabled in autonomous mode.");
            return;
        }else{
            System.out.println("Sending movement command: " + direction);
            sendCommandToPiCar(direction);
            // Here we will have the logic to send the command to the PiCar movement mechanism via GraalPy
        }
        
        
        }
    

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run() {
        
        try {
            ServerSocket serverSocket = new ServerSocket(59901);
            System.out.println("MovementEndPoint listening on port 59901");
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
            System.out.println("IOException in MoveMentEndPoint:");
            e.printStackTrace();
        }
     

    }


}
