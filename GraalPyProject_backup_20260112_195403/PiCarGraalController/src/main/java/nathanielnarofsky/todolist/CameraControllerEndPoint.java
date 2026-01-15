package nathanielnarofsky.todolist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraControllerEndPoint implements Runnable {
    // WebSocket endpoint implementation will go here
    // this class sends commands via Py4J to control the PiCar camera


    

    public synchronized void onMessage(String message) {
        // Code to handle incoming messages from clients
        switch (message) {
            case "CAMERA UP":
                sendCommandToPiCarCamera("UP");
                break;
            case "CAMERA DOWN":
                sendCommandToPiCarCamera("DOWN");
                break;
            case "CAMERA LEFT":
                sendCommandToPiCarCamera("LEFT");
                break;
            case "CAMERA RIGHT":
                sendCommandToPiCarCamera("RIGHT");
                break;
        }

        


    }

    private void sendCommandToPiCarCamera(String direction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCommandToPiCarCamera'");
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(59902);
            System.out.println("CameraControllerEndPoint listening on port 59902");
            boolean running = true;
            while (running == true) {
                    // Accept incoming connections and handle them
                    // This is where Socket server logic goes
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Connected to CameraControllerEndPoint");
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

