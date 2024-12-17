package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    static final int port = 12345;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }
    public void sendMessageToClient(ClientHandler client) {
        for (ClientHandler o : clients) {
            if (o == client) {o.sendMsg("Good bye");}
        }
    }
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }


}