package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Activité 3-1 : traitement d'un client dans un thread séparé.
 * Le client envoie une opération sous forme de texte (ex : "55 * 25"),
 * le thread calcule et renvoie le résultat.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final int numeroClient;

    public ClientHandler(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Envoyer au client son numéro
            out.println("Bienvenue, vous êtes le client n°" + numeroClient);

            String ligne;
            while ((ligne = in.readLine()) != null) {
                if (ligne.equalsIgnoreCase("quit")) {
                    break;
                }

                String reponse = calculer(ligne);
                System.out.println("[" + Thread.currentThread().getName() + "] Client n°"
                        + numeroClient + " : " + reponse);
                out.println(reponse);
            }

            System.out.println("Client n°" + numeroClient + " déconnecté.");

        } catch (IOException e) {
            System.out.println("Client n°" + numeroClient + " : connexion perdue.");
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /** Calcule une opération de la forme "a op b". */
    private String calculer(String operation) {
        String[] parts = operation.trim().split(" ");
        if (parts.length != 3) {
            return "Erreur : opération invalide";
        }
        try {
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[2]);
            String op = parts[1];
            double res;

            switch (op) {
                case "+": res = a + b; break;
                case "-": res = a - b; break;
                case "*": res = a * b; break;
                case "/":
                    if (b == 0) return "Erreur : division par zéro";
                    res = a / b;
                    break;
                default:
                    return "Erreur : opérateur inconnu";
            }
            return parts[0] + " " + op + " " + parts[2] + " = " + res;

        } catch (NumberFormatException e) {
            return "Erreur : opérandes non numériques";
        }
    }
}
