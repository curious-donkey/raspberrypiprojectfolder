package nathanielnarofsky.todolist.picarcontroller;

import jakarta.jms.Session;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint_brain")
public class BrainEndPoint {


    @OnOpen
    public void onOpen() {
        // Code to handle new WebSocket connection
    }


    @OnMessage
    public synchronized void onMessage(Session session, String message) {
        // Code to handle incoming messages from clients
        // Implementation for brain commands will go here
        // stuff related to AI processing and decision making
        // and for switching modes between autonomous and manual control
        // this is where the "brain" of the Picar will be directed
            // this is the hard part
            // aside from rewriting the entire python code into Java
            // it talks to the python code via Py4J


    }






}
