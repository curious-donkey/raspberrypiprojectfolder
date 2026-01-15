package nathanielnarofsky.todolist.picarcontroller;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint_turning")
public class TurningEndPoint {

    @OnOpen
    public void onOpen() {
        // Code to handle new WebSocket connection
    }


    @OnMessage
    public synchronized void onMessage(Session session,String message) {
        // Code to handle incoming messages from clients
        // Implementation for turning commands will go here
        switch (message) {
            case "TURN LEFT":
                sendCommandToPiCarTurning("FORWARD");
                break;
            case "TURN RIGHT":
                sendCommandToPiCarTurning("BACKWARD");
                break;
            default:
                sendCommandToPiCarTurning("STOP");
                // Handle unknown commands
                break;
        }
    }


    private void sendCommandToPiCarTurning(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCommandToPiCarTurning'");
    }

}
