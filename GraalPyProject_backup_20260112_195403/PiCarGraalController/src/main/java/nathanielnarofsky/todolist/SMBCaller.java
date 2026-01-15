package nathanielnarofsky.todolist;

public class SMBCaller {

    
    public SMBCaller(int bus) {

    }

    public byte writeByte(byte[] data) {
        return 0;
    }

    public byte writeByteData(int command, byte data) {
        return 0;
    }

    public byte writeWordData(int command, int data) {
        return 0;
    }

    public byte writeBlockData(int command, byte[] data) {
        return 0;
    }

    public byte readByte() {
        return 0;
    }

    public byte readByteData(int command) {
        return 0;
    }

    public byte readWordData(int command) {
        return 0;
    }

    public byte readBlockData(int command) {
        return 0;
    }

    public boolean checkReady() {
        return true;
    }

    public String runCommand(String command) {
        return "";
    }

    public byte[] readI2CBlockData(int command, int length) {
        return new byte[0];
    }

   
    public int available() {
        return 0;
    }

    public void close() {
      // this is for closing the connection to the smbus device
    }


}
