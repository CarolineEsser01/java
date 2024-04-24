package vue;
import javax.swing.*;
import java.awt.*;
import modele.Etat_connexion;

public class Profil extends JFrame {
    // INITIALISATION
    private JLabel labelNomUtilisateur;

    public Profil() {
        setTitle("Profil Utilisateur");
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme seulement la fenêtre de profil

        JPanel panelProfil = new JPanel(new BorderLayout());
        add(panelProfil);

        // Récupérer l'état de connexion pour obtenir les informations de l'utilisateur connecté
        Etat_connexion etatConnexion = Etat_connexion.getInstance();
        String utilisateurConnecte = etatConnexion.getUtilisateurConnecteEmail();

        if (etatConnexion.isUtilisateurConnecte() && utilisateurConnecte != null) {
            if (etatConnexion.getUtilisateurConnecteType() == 1) {
                labelNomUtilisateur = new JLabel("Vous êtes un client " + utilisateurConnecte);
            } else if (etatConnexion.getUtilisateurConnecteType() == 2) {
                labelNomUtilisateur = new JLabel("Vous êtes un employé " + utilisateurConnecte);
            } else if (etatConnexion.getUtilisateurConnecteType() == 3) {
                labelNomUtilisateur = new JLabel("Vous êtes un vendeur " + utilisateurConnecte);
            }
        } else {
            labelNomUtilisateur = new JLabel("Profil Utilisateur");
        }

        // Ajouter le JLabel au panneau
        panelProfil.add(labelNomUtilisateur, BorderLayout.CENTER);

        setVisible(true);
    }
}
