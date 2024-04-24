package controleur;

import vue.MenuAcceuil;
import vue.inscription.Inscription;
import modele.Utilisateur;
import modele.UtilisateurDAO;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect {
    // Autres membres de la classe
    private Inscription vue_Inscription;
    private UtilisateurDAO utilisateurDAO;
    private MenuAcceuil menuAcceuil;

    // Constructeur de Connect
    public Connect(MenuAcceuil menuAcceuil) {
        this.menuAcceuil = menuAcceuil;
        this.vue_Inscription = menuAcceuil.inscriptionPanel;

        // Ajouter un ActionListener au bouton "S'inscrire"
        this.vue_Inscription.ajouterListenerInscription(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = vue_Inscription.getNom();
                System.out.println("nom");
                System.out.println(nom);
                String prenom = vue_Inscription.getPrenom();
                String email = vue_Inscription.getEmail();
                String mdp = vue_Inscription.getMotDePasse();

                Utilisateur nouvelUtilisateur = new Utilisateur(nom, prenom, email, mdp, vue_Inscription.getTypeSelectionne()); //INITIALISATION UTILISATEUR

                UtilisateurDAO utilisateurDAO = new UtilisateurDAO(); // INITIALISATION DE utilisateurDAO

                // Utilisez cette instance pour appeler les méthodes non statiques
                if (!utilisateurDAO.Champs_remplis(nouvelUtilisateur)) {
                    vue_Inscription.messageErreur("Vous devez avoir rempli tous les champs du formulaire !");
                    return ;
                }

                if (!utilisateurDAO.formatMail(nouvelUtilisateur.getEmail())) {
                    vue_Inscription.messageErreur("Format Email INVALIDE !");
                    return ;
                }

                if (utilisateurDAO.mailUnique(nouvelUtilisateur.getEmail())) {
                    vue_Inscription.messageErreur("Cet email est déjà associé à un autre compte !");
                    return ;
                }
                else{
                    utilisateurDAO.insererUtilisateur(nouvelUtilisateur);
                    vue_Inscription.messageSucces("INSERTION DE L'UTILISATEUR : REUSSITE !");


                }
                System.out.println("Bouton S'inscrire cliqué !");
            }
        });

        this.utilisateurDAO = new UtilisateurDAO();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuAcceuil menuAcceuil = new MenuAcceuil();
                Connect connect = new Connect(menuAcceuil);
            }
        });
    }

}
