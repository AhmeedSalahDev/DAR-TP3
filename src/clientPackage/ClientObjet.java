package clientPackage;

import commonPackage.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Activité 3-2 : client qui envoie des objets Operation au serveur multi-threads.
 */
public class ClientObjet {

    private static final String SYNTAXE = "-?\\d+(\\.\\d+)? [+\\-*/] -?\\d+(\\.\\d+)?";

    public static void main(String[] args) {
        System.out.println("Je suis un client pas encore connecté...");

        try (Socket socket = new Socket("localhost", 1234);
             Scanner scanner = new Scanner(System.in)) {

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Je suis un client connecté");
            System.out.println((String) ois.readObject()); // numéro du client

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = scanner.nextLine().trim();

                if (saisie.equalsIgnoreCase("quit")) {
                    oos.writeObject("quit");
                    oos.flush();
                    break;
                }
                if (!saisie.matches(SYNTAXE)) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                String[] parts = saisie.split(" ");
                Operation op = new Operation(Double.parseDouble(parts[0]),
                        parts[1].charAt(0),
                        Double.parseDouble(parts[2]));

                oos.writeObject(op);
                oos.flush();

                Operation reponse = (Operation) ois.readObject();
                System.out.println("Résultat : " + reponse.getReponse());
            }

            System.out.println("Fin de la communication");
            ois.close();
            oos.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
