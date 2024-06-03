package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
    private int serverPort;
    private String serverName;

    public Client(int serverPort, String serverName) {
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute() throws IOException {
        Socket socket = new Socket(InetAddress.getByName(serverName), serverPort);
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;


            while (true){
                response = reader.readLine();

                if (response.contains("Hello")) {
                    writer.write("Hello " + serverPort +"\n");
                } else if (response.equals("SEND DAILY DATA")) {
                    writer.write("123, 32, 12\n");
                } else if (response.equals("OK")) {
                    writer.write("QUIT\n");
                    writer.flush();
                    break;
                }
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
            reader.close();
            socket.close();
        }
    }

    public static void main(String[] args) {
        String serverName = "localhost";
        int serverPort = 8888;
        Client client = new Client(serverPort, serverName);
        client.start();
    }
}
