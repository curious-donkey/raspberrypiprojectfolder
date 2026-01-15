package nathanielnarofsky.todolist.picarcontroller;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/websocketendpoint_camera")
public class CameraControllerEndPoint {
    // WebSocket endpoint implementation will go here
    // this class sends commands via Py4J to control the PiCar camera


    @OnOpen    public void onOpen() {
        // Code to handle new WebSocket connection
    }

    @OnMessage
    public synchronized void onMessage(Session session, String message) {
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
            default:
                // Handle unknown commands
                break;
        }

        


    }

    private void sendCommandToPiCarCamera(String direction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCommandToPiCarCamera'");
    }

}