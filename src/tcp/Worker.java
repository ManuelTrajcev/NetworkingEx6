package tcp;

import java.io.File;
import java.net.Socket;

public class Worker extends Thread{
    private Socket socket;
    private File csvFile;

    public Worker(Socket socket, File csvFile) {
        this.socket = socket;
        this.csvFile = csvFile;
    }


}
