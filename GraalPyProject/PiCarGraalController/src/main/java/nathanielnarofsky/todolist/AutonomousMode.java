package nathanielnarofsky.todolist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutonomousMode implements Runnable {

    protected static String autonomyState;

    public static String getAutonomyState() {
        return autonomyState;
    }

    @Override
    public void run() {
        System.out.println("Autonomous mode activated");
        // Implement autonomous mode logic here
        autonomyState = Main.getAutonomyState();
        while (autonomyState.equals("AUTONOMOUS")) {
            // Autonomous driving logic
                //I need to set these actions to run concurrently
                //that way the car can move, process sensor data, and play music at the same time
                //so I will need to use a thread pool to manage these concurrent tasks

                ExecutorService pool = Executors.newFixedThreadPool(6);
                AutonomousMovement autonomousMovement = new AutonomousMovement();
                AutonomousCVProcessing autonomousCVProcessing = new AutonomousCVProcessing();
                AutonomousMusic autonomousMusic = new AutonomousMusic();
                CheckAutonomyState checkAutonomyState = new CheckAutonomyState();
                pool.submit(autonomousMovement);
                pool.submit(autonomousCVProcessing);
                pool.submit(autonomousMusic);
                pool.submit(checkAutonomyState);

                
        }
        if (autonomyState.equals("MANUAL")) {
            System.out.println("Exiting autonomous mode.");
        }
    }

    public static void setAutonomyState(String state) {
        AutonomousMode.autonomyState = state;
    }



}
