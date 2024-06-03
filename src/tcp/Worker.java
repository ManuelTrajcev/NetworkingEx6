package tcp;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

public class Worker extends Thread {
    private Socket socket;
    private File csvFile;

    public Worker(Socket socket, File csvFile) {
        this.socket = socket;
        this.csvFile = csvFile;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute() throws IOException {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        BufferedWriter fileWriter = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, true)));

            writer.write("Hello " + socket.getInetAddress() + "\n");
            writer.flush();

            String response = reader.readLine();
            if (response.contains("Hello")) {
                writer.write("SEND DAILY DATA\n");
                writer.flush();

                response = reader.readLine();
                String[] parts = response.split(",");
                if (parts.length == 3) {
                    writeToFile(fileWriter, response);
                    writer.write("OK\n");
                    writer.flush();
                } else {
                    throw new RuntimeException("Invalid input");
                }
                if ((response = reader.readLine()).equals("QUIT")) {
                    socket.close();
                }

            } else {
                throw new RuntimeException("Connection canceled");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.flush();
            writer.close();
            fileWriter.flush();
            fileWriter.close();
            reader.close();
            socket.close();
        }
    }

    public synchronized void writeToFile(BufferedWriter fileWriter, String response) throws IOException {
        String[] parts = response.split(",");
        fileWriter.append(String.format("%s\t\t%s\t\t\t\t\t%s\t\t\t\t\t%s\n", LocalDate.now(), parts[0], parts[1], parts[2]));
        fileWriter.flush();
    }
}
