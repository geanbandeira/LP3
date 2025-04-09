import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorEcho {
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        // Configuração para preferir IPv4
        System.setProperty("java.net.preferIPv4Stack", "true");
        
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor de Eco iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões...");

            while (true) {
                // Aguarda uma conexão de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Cria threads para lidar com cada cliente
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    // Classe interna para lidar com cada cliente em uma thread separada
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Recebido do cliente: " + inputLine);
                    
                    // Envia a mensagem de volta para o cliente
                    out.println("Eco: " + inputLine);
                    
                    // Se o cliente enviar "sair", encerra a conexão
                    if (inputLine.equalsIgnoreCase("sair")) {
                        break;
                    }
                }

                System.out.println("Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress());
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao lidar com cliente: " + e.getMessage());
            }
        }
    }
}
