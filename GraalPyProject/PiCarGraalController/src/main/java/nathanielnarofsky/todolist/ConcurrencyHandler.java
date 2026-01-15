package nathanielnarofsky.todolist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;





public class ConcurrencyHandler {

    private final BrainEndPoint brainEndPoint;
    private final MoveMentEndPoint moveMentEndPoint;
    private final CameraControllerEndPoint cameraControllerEndPoint;
    private final TurningEndPoint turningEndPoint;
    private final CameraReader cameraReader;
    public static final ExecutorService pool = Executors.newFixedThreadPool(6);


    public ConcurrencyHandler() {
        this.brainEndPoint = new BrainEndPoint();
        this.moveMentEndPoint = new MoveMentEndPoint();
        this.cameraControllerEndPoint = new CameraControllerEndPoint();
        this.turningEndPoint = new TurningEndPoint();
        this.cameraReader = new CameraReader();
        
            // starting the threads for each endpoint
            // each endpoint handles a different aspect of the PiCar's functionality
            // you obviously want to be able to move and turn at the same time
            // and the brain needs to be able to process input too
            // so multithreading is needed
            // a single user will connect to each of these endpoints simultaneously

            // I need to set each endpoint to listen on a different port
            // this means I can stream video directly with Java and be comfortable that the streaming and file handling will work
            // this works as the camera is not attached to the Hat so there is no need to reverse engineer the Hat's camera handling code

            
            pool.submit(brainEndPoint);
            pool.submit(moveMentEndPoint);
            pool.submit(cameraControllerEndPoint);
            pool.submit(turningEndPoint);
            pool.submit(cameraReader);
    }


    public void handleConcurrency() {
        // Additional concurrency handling logic can be implemented here if needed













    }



}
