package nathanielnarofsky.todolist;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        

        System.out.println("Hello and welcome to PiCar Graal Controller!");

        BrainEndPoint brainEndPoint = new BrainEndPoint();
        MoveMentEndPoint moveMentEndPoint = new MoveMentEndPoint();
        CameraControllerEndPoint cameraControllerEndPoint = new CameraControllerEndPoint();
        TurningEndPoint turningEndPoint = new TurningEndPoint();
        CameraReader cameraReader = new CameraReader();
        
            // starting the threads for each endpoint
            // each endpoint handles a different aspect of the PiCar's functionality
            // you obviously want to be able to move and turn at the same time
            // and the brain needs to be able to process input too
            // so multithreading is needed
            // a single user will connect to each of these endpoints simultaneously

            // I need to set each endpoint to listen on a different port
            // I need to have them listen perpetually

            // I will need to extract the user input interfaces out of the python modules and rewire them in Java
            // That way the program has interactivity
            // I just need to learn how to extract data from the outputs of the python modules

            // looking into the camera module first... I can substitute the camera module with a Java based camera module as they target the same hardware
            // this means I can stream video directly with Java and be comfortable that the streaming and file handling will work
            // this works as the camera is not attached to the Hat so there is no need to reverse engineer the Hat's camera handling code

            ExecutorService pool = Executors.newFixedThreadPool(4);
            pool.submit(brainEndPoint);
            pool.submit(moveMentEndPoint);
            pool.submit(cameraControllerEndPoint);
            pool.submit(turningEndPoint);
            pool.submit(cameraReader);




        

    }
}