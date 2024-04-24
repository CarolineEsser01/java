package modele;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Messagerie {

    public List<String> obtenirPersonnesAContacter() {
        List<String> personnesAContacter = new ArrayList<>();
        UtilisateurDAO methode= new UtilisateurDAO();

        // Récupérer l'état de connexion pour obtenir les informations de l'utilisateur connecté
        Etat_connexion etatConnexion = Etat_connexion.getInstance();
        String utilisateurConnecte = etatConnexion.getUtilisateurConnecteEmail();

        if (etatConnexion.isUtilisateurConnecte() && utilisateurConnecte != null) {
            if (etatConnexion.getUtilisateurConnecteType() == 1) {// CLIENT
                // IL PEUT PARLER AUX EMPLOYEES
                personnesAContacter.addAll(methode.obtenirEmployes());
            } else if (etatConnexion.getUtilisateurConnecteType() == 2) {// EMPLOYEE
                // IL PEUT PARLER AUX VENDEURS ET AUX CLIENTS
                personnesAContacter.addAll(methode.obtenirVendeurs());
                personnesAContacter.addAll(methode.obtenirClients());
            } else if (etatConnexion.getUtilisateurConnecteType() == 3) { // VENDEUR
                // IL PEUT PARLER AUX EMPLOYEES
                personnesAContacter.addAll(methode.obtenirEmployes());
            }
        }

        return personnesAContacter;
    }

    public void creation_nom_fichier_sauvegarde_disscussion(String personne1, String personne2) {
        MessagerieDAO methode = new MessagerieDAO();
        int idConversation = methode.getIdConversation(personne1, personne2);
        boolean resultat = methode.verif_existance_Message(idConversation);
        if (resultat) {
            System.out.println("Un fichier txt entre " + personne1 + " et " + personne2 + " n'existe pas.");
            //il n'y a pas de message, on doit créer le fichier
            String ficher_nom = "fichier" + idConversation;
            methode.enregistrerNomFichier(idConversation, ficher_nom); //LE MET DANS BDD
            creerFichier(ficher_nom);
        } else {
            System.out.println("Un fichier txt entre " + personne1 + " et " + personne2 + " existe déjà.");
        }
    }

    public void creerFichier(String nomFichier) {
        // Chemin vers le répertoire historique de votre projet Java
        String repertoireHistorique = "historique/";

        // Créer le fichier dans le répertoire historique avec le nom spécifié
        File fichier = new File(repertoireHistorique, nomFichier + ".txt");

        try {
            // Vérifier si le fichier existe déjà
            if (!fichier.exists()) {
                if (fichier.createNewFile()) { // Créer le fichier s'il n'existe pas
                    System.out.println("Le fichier " + nomFichier + ".txt a été créé avec succès.");
                } else {
                    System.out.println("Le fichier " + nomFichier + ".txt n'a pas pu être créé.");
                }
            } else {
                System.out.println("Le fichier " + nomFichier + ".txt existe déjà.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterTexte(String nomFichier, String texteAAjouter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFichier, true))) {
            bw.newLine();
            bw.write(texteAAjouter);
            System.out.println("Texte ajouté avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lireFichier(String nomFichier) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
