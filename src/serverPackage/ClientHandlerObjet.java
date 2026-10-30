package serverPackage;

import commonPackage.Operation;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Activité 3-2 : thread qui traite un client.
 * Il reçoit des objets Operation, calcule, met à jour le compteur global
 * (synchronisé) et renvoie l'objet complété au client.
 */
public class ClientHandlerObjet implements Runnable {

    private final Socket socket;
    private final int numeroClient;

    public ClientHandlerObjet(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }

    @Override
    public void run() {
        try {
            // Créer l'ObjectOutputStream AVANT l'ObjectInputStream (sinon blocage)
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject("Bienvenue, vous êtes le client n°" + numeroClient);
            oos.flush();

            while (true) {
                Object recu = ois.readObject();

                if (recu instanceof String && ((String) recu).equalsIgnoreCase("quit")) {
                    break;
                }

                Operation op = (Operation) recu;
                System.out.println("Client n°" + numeroClient + " -> objet Operation reçu : " + op);

                calculer(op);

                oos.writeObject(op);   // renvoyer l'objet avec la réponse
                oos.flush();
                oos.reset();
            }

            System.out.println("Client n°" + numeroClient + " déconnecté. Total des opérations : "
                    + Compteur.getValeur());

            ois.close();
            oos.close();

        } catch (EOFException e) {
            System.out.println("Client n°" + numeroClient + " : connexion fermée.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client n°" + numeroClient + " : erreur " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        double res;

        switch (op.getOperateur()) {
            case '+': res = a + b; break;
            case '-': res = a - b; break;
            case '*': res = a * b; break;
            case '/':
                if (b == 0) {
                    op.setReponse("Erreur : division par zéro");
                    return;
                }
                res = a / b;
                break;
            default:
                op.setReponse("Erreur : opérateur inconnu");
                return;
        }

        // Section critique : mise à jour du compteur partagé
        int total = Compteur.incrementer();

        op.setReponse(op + " = " + res + "  (opération n°" + total + " traitée par le serveur)");
        System.out.println("Client n°" + numeroClient + " : " + op + " = " + res
                + " | Compteur global = " + total);
    }
}
