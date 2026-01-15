package nathanielnarofsky.todolist;


import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

public class Main {

    
    public static String autonomyState;
    private static Context pythonContext;

    public static void main(String[] args) {
        System.out.println("PiCar GraalPy Controller Starting...");

        try {
            // Set up the GraalPy context with Java interop
            setupPythonContext();
            
            // Set up the _lgpio module bridge
            setupLgpioBridge();
            
            // Test basic GPIO operations
            testGpioOperations();
            
            // If we get here, everything is working!
            System.out.println("PiCar GraalPy Controller ready!");
            
            // You can now import and use robot-hat or other GPIO libraries
            runPythonGpioCode();
            
        } catch (Exception e) {
            System.err.println("Error setting up PiCar controller: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pythonContext != null) {
                pythonContext.close();
            }
        }
    

        System.out.println("Hello and welcome to PiCar Graal Controller!");

        ConcurrencyHandler concurrencyHandler = new ConcurrencyHandler();

        concurrencyHandler.handleConcurrency();
        
        
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



    }


    

    private static void setupPythonContext() {
        System.out.println("Setting up GraalPy context...");
        
        pythonContext = Context.newBuilder("python")
            .allowPolyglotAccess(PolyglotAccess.ALL)
            .allowHostAccess(HostAccess.ALL)
            .allowHostClassLookup(s -> true)
            .allowCreateThread(true)
            .option("python.ForceImportSite", "false")
            .build();
            
        System.out.println("GraalPy context created");
    }


    private static void setupLgpioBridge() {
        System.out.println("Setting up _lgpio bridge...");
        
        try {
            // Load our _lgpio bridge module
            pythonContext.eval("python", """
                                         import sys
                                         sys.path.insert(0, 'src/main/resources')
                                         import _lgpio
                                         print('\u2705 _lgpio bridge imported successfully')""");
            
        } catch (Exception e) {
            System.err.println("Failed to setup _lgpio bridge: " + e.getMessage());
            throw new RuntimeException("Could not setup lgpio bridge", e);
        }
    }

    private static void testGpioOperations() {
        System.out.println("Testing basic GPIO operations...");
        
        try {
            Value result = pythonContext.eval("python", """
                                                        # Test that we can call lgpio functions
                                                        import _lgpio
                                                        print('Testing _gpiochip_open...')
                                                        try:
                                                            handle = _lgpio._gpiochip_open(0)
                                                            print(f'GPIO chip 0 handle: {handle}')
                                                            if handle >= 0:
                                                                _lgpio._gpiochip_close(handle)
                                                                print('GPIO chip closed successfully')
                                                            'GPIO_TEST_PASSED'
                                                        except Exception as e:
                                                            print(f'Note: GPIO test failed (expected on non-Pi): {e}')
                                                            'GPIO_TEST_SKIPPED'
                                                        """);
            
            System.out.println("GPIO operations test completed: " + result.asString());
            
        } catch (Exception e) {
            System.err.println("GPIO test failed (this is normal if not running on Raspberry Pi): " + e.getMessage());
        }
    }

    private static void runPythonGpioCode() {
        System.out.println("🐍 Running Python GPIO example...");
        
        try {
            pythonContext.eval("python", """
                                         # Example of how to use the bridge with existing GPIO libraries
                                         print('\\n\ud83d\udccb Python GPIO Environment:')
                                         print('- _lgpio module: Available')
                                         print('- Native library: Loaded')
                                         print('- Java bridge: Active')
                                         print()
                                         print('\ud83d\udd27 You can now import robot-hat or gpiozero:')
                                         print('  import lgpio')
                                         print('  from robot_hat import Pin, LED, Motor')
                                         print('  # ... your robot code here ...')
                                         print()
                                         print('\ud83d\udca1 The lgpio_extra.py wrapper will use our Java bridge')
                                         print('   transparently for all GPIO operations!')
                                         """);
            
        } catch (Exception e) {
            System.err.println("Error running Python GPIO code: " + e.getMessage());
        }
    }

    /**
     * Utility method to execute Python code in the context
     */
    public static Value executePython(String pythonCode) {
        if (pythonContext == null) {
            throw new RuntimeException("Python context not initialized");
        }
        return pythonContext.eval("python", pythonCode);
    }
    
    /**
     * Utility method to get a Python value
     */
    public static Value getPythonValue(String variable) {
        return pythonContext.getBindings("python").getMember(variable);
    }

    public static String getAutonomyState() {
        return autonomyState;
    }

    public static void setAutonomyState(String autonomyState) {
        Main.autonomyState = autonomyState;
    }



}