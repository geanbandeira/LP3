import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Servidor iniciado na porta " + PORT);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);
                
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.input = new DataInputStream(socket.getInputStream());
                this.output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                // Recebe nome do cliente
                this.clientName = input.readUTF();
                broadcast("Sistema: " + clientName + " entrou no chat!");

                while (true) {
                    String message = input.readUTF();
                    String formattedMsg = formatMessage(message);
                    broadcast(formattedMsg);
                }
            } catch (EOFException e) {
                System.out.println(clientName + " desconectou");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);
                broadcast("Sistema: " + clientName + " saiu do chat");
                try { socket.close(); } catch (IOException e) {}
            }
        }

        private String formatMessage(String msg) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            return "[" + time + "] " + clientName + ": " + msg;
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                try {
                    client.output.writeUTF(message);
                    client.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
