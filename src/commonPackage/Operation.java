package commonPackage;

import java.io.Serializable;

/**
 * Activité 3-2 : objet échangé entre le client et le serveur.
 * Le client remplit les opérandes et l'opérateur,
 * le serveur remplit la réponse puis renvoie l'objet.
 */
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double operande1;
    private final char operateur;
    private final double operande2;
    private String reponse; // rempli par le serveur

    public Operation(double operande1, char operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() { return operande1; }
    public char getOperateur()   { return operateur; }
    public double getOperande2() { return operande2; }

    public String getReponse()            { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
}
