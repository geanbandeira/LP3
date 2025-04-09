import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Servidor de chat iniciado na porta " + PORT);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
                if (clientWriters.size() >= 2) {
                    System.out.println("Dois clientes conectados. Não aceitando mais conexões.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                System.out.println("Novo cliente conectado: " + socket.getRemoteSocketAddress());
                
                if (clientWriters.size() == 2) {
                    broadcast("SERVIDOR: Ambos os clientes estão conectados. Podem começar a conversar!");
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Mensagem recebida: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Erro no handler do cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignorar
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
                System.out.println("Cliente desconectado");
            }
        }

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    if (writer != out) { // Não enviar de volta para o remetente
                        writer.println(message);
                    }
                }
            }
        }
    }
}
