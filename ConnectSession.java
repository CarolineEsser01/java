package controleur;

import vue.Menu;
import vue.MenuAcceuil;
import vue.inscription.*;
import modele.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectSession extends JFrame {
    private Connexion vue_Connexion;
    private UtilisateurDAO utilisateurDAO;
    private Etat_connexion etatConnexion; // Instance de Etat_connexion
    private MenuAcceuil menuAcceuil;

    public ConnectSession(MenuAcceuil menuAcceuil) {
        this.menuAcceuil = menuAcceuil;
        this.vue_Connexion = menuAcceuil.ConnexionPanel;

        this.utilisateurDAO = new UtilisateurDAO();
        this.etatConnexion = Etat_connexion.getInstance(); // Initialisation de l'instance Etat_connexion

        // Ajout de l'ActionListener au bouton de connexion
        this.vue_Connexion.ajouterListenerConnexion(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = vue_Connexion.getEmail();
                String mdp = vue_Connexion.getMotDePasse();
                Utilisateur coUtilisateur = new Utilisateur(null, null, email, mdp, 0);

                if (!utilisateurDAO.Champs_remplis_co(coUtilisateur)) {
                    vue_Connexion.messageErreur("Vous devez avoir rempli tous les champs du formulaire !");
                    return;
                }

                if (utilisateurDAO.verifierIdentifiants(coUtilisateur.getEmail(), coUtilisateur.getMdp())) {
                    vue_Connexion.messageSucces("Connexion réussie !");
                    Utilisateur utilisateurComplet = utilisateurDAO.getUtilisateurParEmail(coUtilisateur.getEmail());
                    coUtilisateur.setNom(utilisateurComplet.getNom());
                    coUtilisateur.setPrenom(utilisateurComplet.getPrenom());
                    coUtilisateur.setType(utilisateurComplet.getType());
                    etatConnexion.connecterUtilisateur(coUtilisateur);

                    // Insérez ici le code pour la suite du processus de connexion
                    // Par exemple, ouvrir une nouvelle fenêtre ou effectuer une autre action
                    if (menuAcceuil != null && menuAcceuil.isVisible()) {
                        menuAcceuil.dispose(); // Dispose la fenêtre principale (MenuAcceuil)
                    }
                    Menu menu = new Menu();


                } else {
                    vue_Connexion.messageErreur("Identifiants incorrects !");
                }
            }
        });
    }

    // Supprimez la méthode main, car cette classe ne devrait pas être exécutée directement
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuAcceuil menuAcceuil = new MenuAcceuil();
                Connect connectS = new Connect(menuAcceuil);
            }
        });
    }
    public void fermerSession() {
        if (isVisible()) {
            setVisible(false); // Masquer la fenêtre de connexion si elle est visible
        }

        // Réinitialiser les champs d'entrée de l'utilisateur
        if (menuAcceuil != null && menuAcceuil.ConnexionPanel != null) {
            menuAcceuil.ConnexionPanel.resetFields(); // Méthode hypothétique pour réinitialiser les champs
        }
    }



}
