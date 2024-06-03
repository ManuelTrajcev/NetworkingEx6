package tcp;

import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
    private int port;
    private String filePath;

    private static File csvFile;

    public Server(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
        csvFile = new File(filePath);
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            Worker worker = new Worker(socket, csvFile);
            worker.start();
        }
    }

    public static void main(String[] args) {
        String filePath = "./data.csv";
        Server server = new Server(8888, filePath);
        server.start();
    }
}
