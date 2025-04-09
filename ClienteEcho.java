import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteEcho {
    private static final String SERVIDOR = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado ao servidor de eco. Digite 'sair' para terminar.");

            String userInput;
            while (true) {
                System.out.print("> ");
                userInput = scanner.nextLine();
                
                out.println(userInput);
                
                String resposta = in.readLine();
                if (resposta != null) {
                    System.out.println("Servidor: " + resposta);
                } else {
                    System.out.println("Servidor desconectado.");
                    break;
                }
                
                if (userInput.equalsIgnoreCase("sair")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + SERVIDOR);
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        }
    }
}
