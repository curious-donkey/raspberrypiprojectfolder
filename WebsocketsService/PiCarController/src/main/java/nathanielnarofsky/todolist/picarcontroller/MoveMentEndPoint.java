package nathanielnarofsky.todolist.picarcontroller;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/websocketendpoint_movement")
public class MoveMentEndPoint {
    // WebSocket endpoint implementation will go here
    // this class sends commands via Py4J to control the PiCar

    @OnOpen
    public void onOpen() {
        // Code to handle new WebSocket connection

    }



   



    @OnMessage
    public synchronized void onMessage(Session session, String message) {
        // Code to handle incoming messages from clients
    
        

        if (message.startsWith("TURN LEFT")) {
            String direction = message.split(" ")[1];
            // Use Py4J to send command to PiCar
            sendCommandToPiCar(direction);
        }
        else if (message.startsWith("TURN RIGHT")) {
            String direction = message.split(" ")[1];
            // Use Py4J to send command to PiCar
            sendCommandToPiCar(direction);
        }
    
    
    }



    private void sendCommandToPiCar(String direction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCommandToPiCar'");
    }







}
