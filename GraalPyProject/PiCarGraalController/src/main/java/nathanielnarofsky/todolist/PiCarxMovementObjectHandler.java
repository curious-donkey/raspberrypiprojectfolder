package nathanielnarofsky.todolist;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

public class PiCarxMovementObjectHandler {

    //this class will handle interactions with the PiCarx object via GraalPy

    private Context pythonContext;
    private Value picarxObject;

    public PiCarxMovementObjectHandler() {
        createPiCarxObject();
    }

    private void createPiCarxObject() {
        // Setup Python context
        pythonContext = Context.newBuilder("python")
            .allowPolyglotAccess(PolyglotAccess.ALL)
            .allowHostAccess(HostAccess.ALL)
            .allowHostClassLookup(s -> true)
            .option("python.ForceImportSite", "false")
            .build();
        
        // Create PiCarx Python object
        pythonContext.eval("python", "import sys");
        pythonContext.eval("python", "sys.path.append('./src/main/java/nathanielnarofsky/todolist')");
        pythonContext.eval("python", "from picarx import picarx");
        Value picarxClass = pythonContext.eval("python", "picarx.Picarx");
        picarxObject = picarxClass.newInstance();
        
        System.out.println("PiCarx Python object created successfully");
    }

    public Value getPiCarxObject() {
        return picarxObject;
    }

    public void close() {
        if (pythonContext != null) {
            pythonContext.close();
        }
    }

    public void moveForward() {
        System.out.println("Move Forward Invoked");
    }
    public void moveBackward() {
        System.out.println("Move Backward Invoked");
    }


}
