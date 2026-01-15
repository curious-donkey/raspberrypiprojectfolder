package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class BrainEndPoint implements Runnable {

    private static AutonomousMode autonomousMode;
    
    public void onMessage(String message) {
        // Code to handle incoming messages from clients
        // Implementation for brain commands will go here
        // stuff related to AI processing and decision making
        // and for switching modes between autonomous and manual control
        // this is where the "brain" of the Picar will be directed
            //I will use spring ai libraries for ai on this project
        System.out.println("BrainEndPoint received message: " + message);


        switch (message){
            case "START_AUTONOMOUS_MODE":
                System.out.println("Switching to autonomous mode...");
                // Code to switch to autonomous mode/
                startAutonomousMode();
                break;
            case "STOP_AUTONOMOUS_MODE":
                System.out.println("Switching to manual mode...");
                // Code to switch to manual mode
                stopAutonomousMode();
                break;

            case "PING":
                System.out.println("Received PING, sending PONG...");
                // Code to respond to ping
                break;

            case "STATUS_CHECK":
                System.out.println("Performing status check...");
                // Code to perform status check
                break;

            case "SHUTDOWN":
                System.out.println("Shutting down system...");
                // Code to safely shutdown the system
                break;
            
            case "RESTART":
                System.out.println("Restarting system...");
                // Code to safely restart the system
                break;

            case "EMERGENCY_STOP":
                System.out.println("Emergency stop activated!");
                // Code to immediately stop all operations
                break;

            case "PAUSE_OPERATIONS":
                System.out.println("Pausing all operations...");
                // Code to pause current operations
                break;

            case "RESUME_OPERATIONS":
                System.out.println("Resuming operations...");
                // Code to resume paused operations
                break;

            


            
            default:
                System.out.println("Unknown command received: " + message);
                break;
        } 
        

    }

    private synchronized void startAutonomousMode() {
        // Code to initiate autonomous driving mode
        System.out.println("Autonomous mode started.");
        // This could involve starting AI routines, sensor processing, etc.
        Main.setAutonomyState("AUTONOMOUS");
        autonomousMode = new AutonomousMode();
        ConcurrencyHandler.pool.submit(autonomousMode);

    }

    private synchronized void stopAutonomousMode() {
        // Code to stop autonomous driving mode
        System.out.println("Autonomous mode stopped.");
        // This could involve halting AI routines, returning control to user, etc.
        
        if (autonomousMode != null) {

            autonomousMode.processKill();
            // Implement logic to safely stop autonomous mode
            autonomousMode = null;
            Main.setAutonomyState("MANUAL");
        }
        else {
            System.out.println("Autonomous mode is not active.");
        }
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







