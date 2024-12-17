import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             Scanner scanner = new Scanner(System.in);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Вы подключились к серверу");

            Thread listenerThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("Сообщение: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Соединение с сервером потеряно");
                }
            });
            listenerThread.start();

            System.out.println("Введите сообщения (для выхода введите 'quit'):");
            String userInput;
            while (scanner.hasNextLine()) {
                userInput = scanner.nextLine();
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }}
