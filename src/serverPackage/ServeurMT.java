package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 3-1 : serveur multi-threads.
 * Le thread principal ne fait qu'accepter les connexions ;
 * chaque client est traité dans son propre thread (ClientHandler).
 */
public class ServeurMT {

    public static void main(String[] args) {
        int numeroClient = 0; // numéro d'ordre de connexion

        try (ServerSocket socketServeur = new ServerSocket(1234)) {
            System.out.println("Je suis un serveur multi-threads en attente des clients (port 1234)...");

            while (true) {
                Socket socket = socketServeur.accept();   // attend un nouveau client
                numeroClient++;

                System.out.println("Client n°" + numeroClient + " connecté depuis "
                        + socket.getInetAddress().getHostAddress());

                // Un thread dédié pour ce client
                Thread t = new Thread(new ClientHandler(socket, numeroClient));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
