//https://www.perplexity.ai/search/um-projeto-em-java-completo-on-ui84u2VYTky66WEZ76Armg
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();

        try (Socket socket = new Socket(SERVER_IP, PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            
            output.writeUTF(nome);
            
            // Thread para receber mensagens
            new Thread(() -> {
                try {
                    while (true) {
                        String serverMessage = input.readUTF();
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com servidor perdida");
                }
            }).start();

            // Envio de mensagens
            while (true) {
                String message = scanner.nextLine();
                output.writeUTF(message);
                output.flush();
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
