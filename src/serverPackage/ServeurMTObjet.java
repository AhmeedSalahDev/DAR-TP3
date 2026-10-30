package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 3-2 : serveur multi-threads qui échange des objets Operation
 * et tient un compteur global des opérations calculées.
 */
public class ServeurMTObjet {

    public static void main(String[] args) {
        int numeroClient = 0;

        try (ServerSocket socketServeur = new ServerSocket(1234)) {
            System.out.println("Serveur (objets) en attente des clients sur le port 1234...");

            while (true) {
                Socket socket = socketServeur.accept();
                numeroClient++;

                System.out.println("Client n°" + numeroClient + " connecté depuis "
                        + socket.getInetAddress().getHostAddress());

                new Thread(new ClientHandlerObjet(socket, numeroClient)).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
