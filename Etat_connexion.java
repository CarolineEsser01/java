package modele;
//ON IMPORTE NOS PACKAGES NECESSAIRES

public class Etat_connexion {
    private static Etat_connexion instance;
    private boolean utilisateurConnecte;
    private Utilisateur utilisateurConnecteInfo; // Informations sur l'utilisateur connecté

    private void GestionConnexion() {
        utilisateurConnecte = false;
        utilisateurConnecteInfo=null;

    }

    public static synchronized Etat_connexion getInstance() {
        if (instance == null) {
            instance = new Etat_connexion();
        }
        return instance;
    }

    public void connecterUtilisateur(Utilisateur utilisateur) {
        utilisateurConnecte = true;
        utilisateurConnecteInfo = utilisateur;
    }

    public void deconnecterUtilisateur() {
        utilisateurConnecte = false;
        utilisateurConnecteInfo = null;
    }

    public boolean isUtilisateurConnecte() {

        return utilisateurConnecte;
    }

    public String getUtilisateurConnecteEmail() {
        if (utilisateurConnecteInfo != null) {
            return utilisateurConnecteInfo.getEmail();
        } else {
            return null; // ou une chaîne vide selon vos besoins
        }
    }

    public String getUtilisateurConnectePrenomEtNom() {
        if (utilisateurConnecteInfo != null) {
            return utilisateurConnecteInfo.getPrenom() +" "+ utilisateurConnecteInfo.getNom();
        } else {
            return null; // ou une chaîne vide selon vos besoins
        }
    }

    public int getUtilisateurConnecteType() {
        if (utilisateurConnecteInfo != null) {
            return utilisateurConnecteInfo.getType();
        } else {
            return 0; // ou une chaîne vide selon vos besoins
        }
    }


}
