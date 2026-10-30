package clientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Activité 3-1 : client qui envoie des opérations sous forme de texte
 * au serveur multi-threads.
 */
public class ClientTexte {

    // nombre, espace, opérateur, espace, nombre  (ex : 55 * 25)
    private static final String SYNTAXE = "-?\\d+(\\.\\d+)? [+\\-*/] -?\\d+(\\.\\d+)?";

    public static void main(String[] args) {
        System.out.println("Je suis un client pas encore connecté...");

        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Je suis un client connecté");
            System.out.println(in.readLine()); // message de bienvenue avec le numéro

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = scanner.nextLine().trim();

                if (saisie.equalsIgnoreCase("quit")) {
                    out.println("quit");
                    break;
                }
                if (!saisie.matches(SYNTAXE)) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                out.println(saisie);
                System.out.println("Résultat : " + in.readLine());
            }

            System.out.println("Fin de la communication");

        } catch (IOException e) {
            System.out.println("Impossible de joindre le serveur : " + e.getMessage());
        }
    }
}
