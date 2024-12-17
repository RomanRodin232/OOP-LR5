package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private static final String host = "localhost";
    private static final int port = 3443;

    private Server server;
    private PrintWriter outMessage;
    private Scanner inMessage;
    private Socket clientSocket = null;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                server.sendMessageToAllClients(clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " вошёл в чат!");
                break;
            }

            while (true) {
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.equals("quit")) {
                        server.sendMessageToClient(this);
                        server.sendMessageToAllClients(clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " вышел из чата");
                        break;
                    }
                    server.sendMessageToAllClients(clientMessage);
                }
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();

        }
    }
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void close() {
        server.removeClient(this);
    }
}