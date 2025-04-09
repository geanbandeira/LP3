import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite seu nome: ");
        String userName = scanner.nextLine();
        
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Thread para receber mensagens do servidor
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com o servidor perdida.");
                }
            }).start();
            
            System.out.println("Conectado ao servidor. Digite suas mensagens:");
            
            // Enviar mensagens para o servidor
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("/sair")) {
                    break;
                }
                out.println(userName + ": " + userInput);
            }
            
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + SERVER_IP);
        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao servidor: " + e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Cliente encerrado.");
        }
    }
}
