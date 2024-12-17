import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (             Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {

            System.out.println("Подключение к серверу...");

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Подключение установлено.");

                // Поток для прослушивания сообщений от сервера
                Thread listenerThread = new Thread(() -> {
                    String serverMessage;
                    try {
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println("Сообщение: " + serverMessage);
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка при получении сообщения от сервера: " + e.getMessage());
                    } finally {
                        System.out.println("Соединение с сервером прервано.");
                    }
                });
                listenerThread.start();

                System.out.println("Введите сообщения (для выхода введите 'quit'):");
                String userInput;
                while (scanner.hasNextLine()) {
                    userInput = scanner.nextLine();
                    if (userInput.equalsIgnoreCase("quit")) {
                        break;
                    }
                    out.println(userInput);
                }

            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            }


        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Клиент завершен.");
        }
    }
}