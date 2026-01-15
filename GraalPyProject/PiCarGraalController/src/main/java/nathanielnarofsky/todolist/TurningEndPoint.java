package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class TurningEndPoint implements Runnable {

    private final PiCarxTurningObjectHandler keypad = new PiCarxTurningObjectHandler();
  

    private void sendCommandToPicar(String message) {
        // Code to handle incoming messages from clients
        // Implementation for turning commands will go here
        if (message.equals("TURN LEFT")) {
            keypad.turnLeft();
        }
        else if (message.equals("TURN RIGHT")) {
            keypad.turnRight();
        }
        
    }
    
    private void onMessage(String command) {
        // Code to send commands to the PiCar
        // This might involve serial communication or network commands
        System.out.println("Sending command to PiCar: " + command);
        if (Main.getAutonomyState().equals("AUTONOMOUS")) {
            System.out.println("Turning control disabled in autonomous mode.");
            return;
        }
        else{
            System.out.println("Executing turning command: " + command);
            // Here we will have the logic to send the command to the PiCar turning mechanism via GraalPy
            sendCommandToPicar(command);
        }
    }

    
        
    

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(59900);
            System.out.println("TurningEndPoint listening on port 59900");
            boolean running = true;
            while (running == true) {
                    // Accept incoming connections and handle them
                    // This is where Socket server logic goes
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Connected to TurningEndPoint");
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

    
