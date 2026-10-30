package serverPackage;

/**
 * Activité 3-2 : compteur global partagé par tous les threads.
 * C'est une ressource critique : les méthodes sont synchronized
 * pour qu'un seul thread à la fois puisse la modifier.
 */
public class Compteur {

    private static int nbOperations = 0;

    /** Incrémente le compteur et renvoie la nouvelle valeur (opération atomique). */
    public static synchronized int incrementer() {
        nbOperations++;
        return nbOperations;
    }

    public static synchronized int getValeur() {
        return nbOperations;
    }
}
