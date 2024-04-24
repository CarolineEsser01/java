package vue;

import controleur.ImageUtil;
import modele.*;
import modele.Offre;

import javax.swing.*;
import java.awt.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import java.sql.Timestamp; // NE PAS TOUCHER

import java.util.Calendar;

;
import java.awt.image.BufferedImage;

import static java.lang.Integer.parseInt;
import static modele.OffreDAO.*;
import static modele.ProprieteDAO.*;


public class AnnoncesPage extends JFrame {
    private JTextField emplacementField = new JTextField(20);
    private JTextField superficieField = new JTextField(20);
    private JTextField nbPiecesField = new JTextField(20);
    private JTextField nbSalleBainField = new JTextField(20);
    private JTextField nbSalleEauField = new JTextField(20);
    private JTextField descriptionField = new JTextField(20);
    private JTextField  titreField = new JTextField(20);
    private JTextField prixField = new JTextField(20);
    private JTextField adresseField = new JTextField(20);
    private JTextField vendeurField = new JTextField(20);;
    private JTextField villeField= new JTextField(20); ;
    private List<Image> images = new ArrayList<>();
    private JPanel photosPanel;
    private JTextField offreField; // Champ pour saisir l'offre
    private JButton enregistrerButton; // Bouton pour enregistrer l'offre
    private JPanel annoncesPanel;
    private JDateChooser dateChooser;
    private int id_prop_choisi = 0;
    private String mail_perso ;

    // Récupérer les annonces depuis la base de données
    private ProprieteDAO proprieteDAO = new ProprieteDAO();

    // Récupérer les reservation de visite depuis la base de données
    private ReservationDAO reservationDAO = new ReservationDAO();
    private List<Reservation> visite = reservationDAO.recupererVisite();

    // Récupérer les offre depuis la base de données
    private OffreDAO offreDAO = new OffreDAO();
    private List<Offre> offres = offreDAO.recupererOffres();
    private List<JLabel> imageLabels = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();
    private Etat_connexion etatConnexion = Etat_connexion.getInstance();



    public AnnoncesPage() {
        setTitle("Annonces");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        List<Propriete> annonces = proprieteDAO.recupererAnnonces(); // AAAAAAAAAAAAAAAAAAAAAAAAAA

        // Définir la taille préférée du contenu
        Dimension preferredSize = new Dimension(800, 600); // Remplacez 800 et 600 par les dimensions souhaitées
        getContentPane().setPreferredSize(preferredSize);

        // Récupérer l'état de connexion pour obtenir les informations de l'utilisateur connecté
        String utilisateurConnecte = etatConnexion.getUtilisateurConnecteEmail();

        annoncesPanel = new JPanel(new GridLayout(0, 1));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");

        mail_perso = etatConnexion.getUtilisateurConnecteEmail();
        System.out.println("mail de la personne co :" + mail_perso);
        System.out.println("son type :" + etatConnexion.getUtilisateurConnecteType());

        for (Propriete propriete : annonces) {
            JPanel annoncePanel = createAnnoncePanel(propriete);
            annoncesPanel.add(annoncePanel);
        }
        getContentPane().add(new JScrollPane(annoncesPanel), BorderLayout.CENTER);

        // Adapter la taille de la fenêtre au contenu
        pack();
        setVisible(true);
    }

    private JPanel createAnnoncePanel(Propriete propriete) {
        JPanel annoncePanel = new JPanel(new BorderLayout());

        JPanel imagePanel = createImagePanel(propriete.getPhotos());
        annoncePanel.add(imagePanel, BorderLayout.WEST);
        System.out.println("photosssss" + propriete.getPhotos());

        JPanel infoPanel = createInfoPanel(propriete);
        annoncePanel.add(infoPanel, BorderLayout.CENTER);

        return annoncePanel;
    }

    private JPanel createImagePanel(List<String> imagePaths) {
        ImageIcon[] imageIcons = new ImageIcon[imagePaths.size()];
        System.out.println(imageIcons);

        JPanel imagePanel = new JPanel(new BorderLayout()); // Utilisation d'un BorderLayout

        JPanel imageContainer = new JPanel(new GridLayout(1, 1)); // Panneau pour contenir l'image principale
        JLabel imageLabel = new JLabel(); // Étiquette pour l'image principale

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panneau pour les flèches de navigation
        System.out.println("imagepath :" + imagePaths);

        for (int i = 0; i < imagePaths.size(); i++) {
            try {
                String imagePath = imagePaths.get(i);
                System.out.println("image path :" + imagePath);

                // Utilisation de ImageIO.read() avec un InputStream au lieu de File
                BufferedImage originalImage = ImageIO.read(new FileInputStream(new File(imagePath)));
                System.out.println("originnnn" + originalImage);

                // Charger l'image à sa taille d'origine d'abord, puis la mettre à l'échelle
                int scaledWidth = 600;
                int scaledHeight = (int) (((double) scaledWidth / originalImage.getWidth()) * originalImage.getHeight());
                BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());
                Graphics2D g = scaledImage.createGraphics();
                g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g.dispose();

                ImageIcon imageIcon = new ImageIcon(scaledImage);
                imageIcons[i] = imageIcon;

                // Ajouter une étiquette de remplacement pour chaque image au panneau
                imageLabel.setIcon(imageIcon);
                imageContainer.add(imageLabel); // Ajouter l'image principale au conteneur d'image

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Définir l'image principale par défaut
        imageLabel.setIcon(imageIcons[0]);

        // Ajouter le conteneur d'image au panneau principal
        imagePanel.add(imageContainer, BorderLayout.CENTER);

        // Ajouter les flèches de navigation au panneau de navigation
        JButton previousButton = new JButton("<");
        previousButton.addActionListener(new ImageNavigationListener(imageIcons, imageLabel, -1));

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(new ImageNavigationListener(imageIcons, imageLabel, 1));

        navigationPanel.add(previousButton);
        navigationPanel.add(nextButton);

        // Ajouter le panneau de navigation sous le conteneur d'image
        imagePanel.add(navigationPanel, BorderLayout.SOUTH);

        return imagePanel;
    }

    private JPanel createInfoPanel(Propriete propriete) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        JLabel prixLabel = new JLabel("Prix: " + propriete.getPrix());
        prixLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoPanel.add(prixLabel);

        JLabel attributsLabel = new JLabel("<html><ul><li>Surface: " + propriete.getSuperficie() + "</li>"
                + "<li>Nombre de pièces: " + propriete.getNbPieces() + "</li>"
                + "<li>Emplacement: " + propriete.getEmplacement() + "</li>"
                + "<li>Description: " + propriete.getDescription() + "</li>"
                + "<li>Adresse: " + propriete.getAdresse() + "</li></ul></html>");
        infoPanel.add(attributsLabel);

        // MES BOUTONSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        if (etatConnexion.getUtilisateurConnecteType() == 1) {
            JButton resaButton = new JButton("Réserver une date de visite");
            resaButton.addActionListener(e -> SelectDateVisite(propriete.getId()));
            infoPanel.add(resaButton);


            JButton offreButton = new JButton("Faire une offre ");
            offreButton.addActionListener(e -> FaireOffre(propriete.getId()));
            infoPanel.add(offreButton);

            JButton ActionOffreClient = new JButton("ActionOffreClient ");
            ActionOffreClient.addActionListener(e -> ActionOffreClient(propriete.getId()));
            infoPanel.add(ActionOffreClient);
        }

        if (etatConnexion.getUtilisateurConnecteType() == 2) {
            JButton putDateButton = new JButton("insérer une nouvelle date dispo de visite");
            putDateButton.addActionListener(e -> AjoutDateVisite(propriete.getId()));
            infoPanel.add(putDateButton);

            JButton ModifAnnonceButton = new JButton("Modifier une annonce");
            ModifAnnonceButton.addActionListener(e -> ModifierAnnonce(propriete.getId()));
            infoPanel.add(ModifAnnonceButton);

            JButton SupAnnonceButton = new JButton("Supprimer cette annonce");
            SupAnnonceButton.addActionListener(e -> SupprimerAnnonce(propriete.getId()));
            infoPanel.add(SupAnnonceButton);

            JButton AcceptResaButton = new JButton("Accepter une resa");
            AcceptResaButton.addActionListener(e -> AccepterResaButton(propriete.getId()));
            infoPanel.add(AcceptResaButton);
        }

        if (etatConnexion.getUtilisateurConnecteType() == 3) {
            JButton putDateButton = new JButton("insérer une nouvelle date dispo de visite");
            putDateButton.addActionListener(e -> AjoutDateVisite(propriete.getId()));
            infoPanel.add(putDateButton);

            JButton AcceptResaButton = new JButton("Accepter une resa");
            AcceptResaButton.addActionListener(e -> AccepterResaButton(propriete.getId()));
            infoPanel.add(AcceptResaButton);

            JButton AcceptOffreButton = new JButton("Regarder les =! offres");
            AcceptOffreButton.addActionListener(e -> ActionOffre(propriete.getId()));
            infoPanel.add(AcceptOffreButton);
        }

        return infoPanel;
    }

    public void ModifierAnnonce(int id_prop_choisi){
        // Création d'une nouvelle fenêtre pour la modification de l'annonce
        JFrame modificationFrame = new JFrame("Modifier Annonce");
        modificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Création des panneaux pour chaque onglet
        JPanel generalPanel = new JPanel(new GridLayout(9, 3));
        JPanel montantPanel = new JPanel(new GridLayout(1, 3));
        JPanel localisationPanel = new JPanel(new GridLayout(1, 3));
        JPanel photosPanelWrapper = new JPanel(new BorderLayout());

        // Panneau pour afficher les photos existantes
        JPanel photosExistantesPanel = new JPanel(new GridLayout(1, 0));

        List<Propriete> proprietes = proprieteDAO.recupererAnnonces();

        boolean found = false;
        for (Propriete ancienneProp : proprietes) {
            if (ancienneProp.getId() == id_prop_choisi) {

                found = true;
                // Afficher les données de la propriété dans les champs de saisie
                emplacementField.setText(ancienneProp.getEmplacement());
                superficieField.setText(String.valueOf(ancienneProp.getSuperficie()));
                nbPiecesField.setText(String.valueOf(ancienneProp.getNbPieces()));
                nbSalleBainField.setText(String.valueOf(ancienneProp.getNbSalleBain()));
                nbSalleEauField.setText(String.valueOf(ancienneProp.getNbSalleEau()));
                descriptionField.setText(ancienneProp.getDescription());
                prixField.setText(String.valueOf(ancienneProp.getPrix()));
                adresseField.setText(ancienneProp.getAdresse());
                vendeurField.setText(ancienneProp.getVendeur());
                villeField.setText(ancienneProp.getVille());
                System.out.println(ancienneProp.getPhotos());

                // Ajout des champs de saisie et des boutons de validation à chaque panneau
                generalPanel.add(new JLabel("Titre:"));
                generalPanel.add(titreField = new JTextField(20));
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Type de bien (maison/appartement):"));
                generalPanel.add(emplacementField);
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Superficie (m²):"));
                generalPanel.add(superficieField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Nb de pièces:"));
                generalPanel.add(nbPiecesField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Nb de salle de bain:"));
                generalPanel.add(nbSalleBainField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Nb de salle d'eau:"));
                generalPanel.add(nbSalleEauField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Description:"));
                generalPanel.add(descriptionField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Ville :"));
                generalPanel.add(villeField );
                generalPanel.add(new JButton("Valider"));

                generalPanel.add(new JLabel("Proprio:"));
                generalPanel.add(villeField );
                generalPanel.add(new JButton("Valider"));

                montantPanel.add(new JLabel("Prix:"));
                montantPanel.add(prixField );
                montantPanel.add(new JButton("Valider"));

                localisationPanel.add(new JLabel("Adresse:"));
                localisationPanel.add(adresseField);
                localisationPanel.add(new JButton("Valider"));

                // Bouton pour ajouter des photos
                JButton btnAjouterPhotos = new JButton("Ajouter Photos");
                photosPanelWrapper.add(btnAjouterPhotos, BorderLayout.NORTH);
                photosPanel = new JPanel(new GridLayout(0, 1)); // Utilisation d'un GridLayout pour afficher les images verticalement
                photosPanelWrapper.add(new JScrollPane(photosPanel), BorderLayout.CENTER);

                // Chargement et affichage des images existantes
                for (String imagePath : ancienneProp.getPhotos()) {
                    try {
                        BufferedImage originalImage = ImageIO.read(new File(imagePath));
                        ImageIcon imageIcon = new ImageIcon(originalImage);
                        JLabel imageLabel = new JLabel(imageIcon);
                        photosPanel.add(imageLabel);
                        images.add(originalImage);
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la lecture de l'image : " + e.getMessage());
                        e.printStackTrace();
                    }
                }


                // Ajout des onglets et du bouton "Enregistrer" à la fenêtre de modification
                JTabbedPane onglets = new JTabbedPane();
                onglets.addTab("Informations Générales", generalPanel);
                onglets.addTab("Montant", montantPanel);
                onglets.addTab("Localisation", localisationPanel);
                onglets.addTab("Photos", photosPanelWrapper);
                modificationFrame.getContentPane().add(onglets, BorderLayout.CENTER);
                JButton btnEnregistrer = new JButton("Enregistrer");
                modificationFrame.getContentPane().add(btnEnregistrer, BorderLayout.SOUTH);

                // Validation pour le nombre de photos
                btnAjouterPhotos.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ajouterPhoto();
                    }
                });

                // Écouteur d'événements pour le bouton "Enregistrer"
                btnEnregistrer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String emplacement = emplacementField.getText();
                        double superficie = Double.parseDouble(superficieField.getText());
                        int nbPieces = parseInt(nbPiecesField.getText());
                        int nbSalleBain = parseInt(nbSalleBainField.getText());
                        int nbSalleEau = parseInt(nbSalleEauField.getText());
                        String description = descriptionField.getText();
                        double prix = Double.parseDouble(prixField.getText());
                        String adresse = adresseField.getText();
                        String vendeur = vendeurField.getText();
                        String ville = villeField.getText();
                        int statut = 0;
                        String employe = mail_perso;
                        String titre = titreField.getText();

                        // Convertir les images en chemins d'accès aux fichiers
                        List<String> imagesPaths = ImageUtil.convertImagesToPaths(images, id_prop_choisi);

                        // Enregistrer ces données dans la base de données
                        Propriete nouvellePropriete = new Propriete(id_prop_choisi, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, imagesPaths, vendeur, ville, statut, employe, titre);
                        proprieteDAO.insererPropriete(nouvellePropriete);

                        proprieteDAO.supprimerPropriete(ancienneProp);


                        modificationFrame.dispose();

                        // ACTUALISATION
                        annoncesPanel.removeAll();
                        List<Propriete> annonces = proprieteDAO.recupererAnnonces(); // AAAAAAAAAAAAAAAAAAAAAAAAAA

                        for (Propriete propriete : annonces) {
                            JPanel annoncePanel = createAnnoncePanel(propriete);
                            annoncesPanel.add(annoncePanel);
                        }
                        revalidate();
                        repaint();
                    }
                });
            }
        }
        if (!found) {
            System.out.println("Aucune propriété à modifier avec l'identifiant " + id_prop_choisi);
        }

        // Affichage de la fenêtre de modification
        modificationFrame.pack();
        modificationFrame.setVisible(true);
    }

    private void ajouterPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Charger l'image à partir du fichier
                Image img = ImageIO.read(selectedFile);
                // Ajouter l'image à la liste des images
                images.add(img);
                // Créer une étiquette pour afficher l'image
                JLabel photoLabel = new JLabel(new ImageIcon(img));
                // Ajouter l'étiquette à la liste des étiquettes
                imageLabels.add(photoLabel);
                // Ajouter l'étiquette à la fenêtre
                photosPanel.add(photoLabel);
                // Actualiser l'affichage
                photosPanel.revalidate();
                photosPanel.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void SupprimerAnnonce(int id_prop_choisi) {
        System.out.println(id_prop_choisi);
        // Récupérez les propriétés depuis la base de données
        List<Propriete> proprietes = proprieteDAO.recupererAnnonces();

        // Parcourez la liste des propriétés pour trouver celle à supprimer
        boolean found = false;
        for (Propriete ancienneProp : proprietes) {
            if (ancienneProp.getId() == id_prop_choisi) {
                // Nous avons trouvé la propriété à supprimer
                found = true;
                System.out.println(ancienneProp.getId());
                // Appelez la méthode pour supprimer la propriété
                proprieteDAO.supprimerPropriete(ancienneProp);
                break; // Sortez de la boucle une fois que vous avez trouvé la propriété à supprimer
            }
        }
        if (!found) {
            System.out.println("Aucune propriété à supprimer avec l'identifiant " + id_prop_choisi);
        }
        // Charger à nouveau les annonces après la suppression
        chargerAnnonces();
    }


    public void ActionOffre(int id_prop_choisi) {
        // Supprimer les composants existants dans le panneau d'annonces
        annoncesPanel.removeAll();

        // System.out.println("id" + id_prop_choisi);
        // Récupérer les offres depuis la base de données
        List<Offre> offres = offreDAO.recupererOffres();
        //System.out.println("offres" + offres);
        // Créer un panneau pour afficher les offres et les boutons
        JPanel offrePanel = new JPanel();
        offrePanel.setLayout(new GridLayout(0, 4)); // 4 colonnes// 4 colonnes : Offre, Accepter, Refuser, Contre-offre

        // Vérifier s'il reste des offres pour cette propriété avec un statut de 1
        boolean offresRestantes = false;
        for (Offre offre : offres) {
            if (offre.getId_prop() == id_prop_choisi && offre.getStatut() == 1) {
                offresRestantes = true;
                break;
            }
        }

        // Parcourir la liste des offres
        for (Offre offre : offres) {
            System.out.println(" id offre" + offre.getId_prop());
            System.out.println(" stat offre" + offre.getStatut());
            // Vérifier si l'offre correspond à la propriété choisie
            if (offre.getId_prop() == id_prop_choisi && offre.getStatut() == 1 && offre.getContreProposition() == 0 ) {
                System.out.println("ooooooo" + offre.getProposition());
                // Créer un libellé pour afficher l'offre
                JLabel offreLabel = new JLabel("Offre : " + offre.getProposition());
                offrePanel.add(offreLabel);

                // Créer les boutons pour chaque action
                JButton accepterButton = new JButton("Accepter");
                JButton refuserButton = new JButton("Refuser");
                JButton contreOffreButton = new JButton("Contre-offre");

                // Obtenir le timestamp actuel
                long timestampMillis = System.currentTimeMillis();

                // Créer un objet Timestamp à partir du timestamp actuel
                Timestamp timestamp = new Timestamp(timestampMillis);

                // Ajouter des ActionListener aux boutons
                accepterButton.addActionListener(e -> {
                    Offre nouvelleOffre = new Offre(id_prop_choisi, 2, offre.getProposition(), offre.getMailClient(), 0, timestamp  );
                    offreDAO.insererOffre(nouvelleOffre);

                    Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), 0, offre.getTime());
                    offreDAO.supprimerOffre(ancienneOffre);

                    // Mettre à jour les autres offres pour qu'elles aient un statut de 0 (non traité)
                    for (Offre autreOffre : offres) {
                        if (autreOffre.getId_prop() == id_prop_choisi && autreOffre.getStatut() == 1) {
                            Offre nouvelOffre = new Offre(id_prop_choisi, 0, autreOffre.getProposition(), autreOffre.getMailClient(), autreOffre.getContreProposition(), autreOffre.getTime());
                            offreDAO.insererOffre(nouvelOffre);

                            Offre ancienOffre = new Offre(id_prop_choisi, 1, autreOffre.getProposition(), autreOffre.getMailClient(), autreOffre.getContreProposition(), autreOffre.getTime());
                            offreDAO.supprimerOffre(ancienOffre);


                        }
                    }

                    JOptionPane.showMessageDialog(this, "Offre acceptée pour la propriété " + id_prop_choisi);

                    // Actualiser l'affichage
                    chargerAnnonces();
                });

                refuserButton.addActionListener(e -> {
                    // Ajouter le code pour refuser l'offre ici
                    Offre nouvelleOffre = new Offre(id_prop_choisi, 0, offre.getProposition(), offre.getMailClient(), 0, timestamp);
                    offreDAO.insererOffre(nouvelleOffre); // Appeler la méthode sur l'instance d'OffreDAO

                    Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), 0, offre.getTime());
                    offreDAO.supprimerOffre(ancienneOffre);

                    // Ajouter le code pour refuser l'offre ici
                    JOptionPane.showMessageDialog(this, "Offre refusée pour la propriété " + id_prop_choisi);

                    // Actualiser l'affichage
                    List<Offre> offresRestantess = offreDAO.recupererOffres();
                    for (Offre offreRestantess : offresRestantess) {
                        if (offreRestantess.getId_prop() == id_prop_choisi && offreRestantess.getStatut() == 1) {
                            // Actualiser l'affichage uniquement s'il reste des offres pour cette propriété avec un statut de 1
                            ActionOffre(id_prop_choisi);
                            return; // Sortir de la méthode après avoir actualisé l'affichage
                        }
                    }

                    // Si aucune offre restante pour cette propriété avec un statut de 1 n'est trouvée, recharger toutes les annonces
                    chargerAnnonces();
                });

                contreOffreButton.addActionListener(e -> {
                    // Créer un champ JTextField pour saisir la contre-offre
                    JTextField contreOffreField = new JTextField(10); // Vous pouvez ajuster la taille en fonction de vos besoins

                    // Afficher une boîte de dialogue pour permettre à l'utilisateur de saisir la contre-offre
                    int result = JOptionPane.showConfirmDialog(this, contreOffreField, "Entrez la contre-offre", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Récupérer la valeur saisie dans le champ JTextField
                        String contreOffreText = contreOffreField.getText();

                        // Convertir la valeur saisie en entier (vous pouvez ajouter une validation supplémentaire si nécessaire)
                        int contreOffreValue = Integer.parseInt(contreOffreText);

                        // Ajouter le code pour faire une contre-offre ici
                        // Vous pouvez utiliser la valeur de contreOffreValue pour effectuer des opérations avec la contre-offre

                        // Vérifier si la contre-offre est supérieure à l'offre initiale
                        if (contreOffreValue <= offre.getProposition()) {
                            JOptionPane.showMessageDialog(this, "La contre-offre doit être supérieure à l'offre initiale.");
                        } else {
                            // Ajouter le code pour faire une contre-offre ici
                            Offre nouvelleOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), contreOffreValue, timestamp);
                            offreDAO.insererOffre(nouvelleOffre); // Appeler la méthode sur l'instance d'OffreDAO

                            Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), 0, offre.getTime());
                            offreDAO.supprimerOffre(ancienneOffre);

                            // Actualiser l'affichage
                            List<Offre> offresRestantess = offreDAO.recupererOffres();
                            for (Offre offreRestantess : offresRestantess) {
                                if (offreRestantess.getId_prop() == id_prop_choisi && offreRestantess.getStatut() == 1 && offreRestantess.getContreProposition() != 0) {
                                    // Actualiser l'affichage uniquement s'il reste des offres pour cette propriété avec un statut de 1
                                    ActionOffre(id_prop_choisi);
                                    return; // Sortir de la méthode après avoir actualisé l'affichage
                                }
                            }

                            // Si aucune offre restante pour cette propriété avec un statut de 1 n'est trouvée, recharger toutes les annonces
                            chargerAnnonces();
                        }
                    }
                });


                // Ajouter les boutons au panneau d'offres
                offrePanel.add(accepterButton);
                offrePanel.add(refuserButton);
                offrePanel.add(contreOffreButton);
            }
        }
// Si aucune offre avec un statut de 1 n'est trouvée, recharger toutes les annonces
        if (!offresRestantes) {
            chargerAnnonces();
        }
        // Ajouter le panneau d'offres au panneau d'annonces
        annoncesPanel.add(offrePanel);

        // Rafraîchir l'interface utilisateur
        revalidate();
        repaint();
    }

    public void ActionOffreClient(int id_prop_choisi) {
        // Supprimer les composants existants dans le panneau d'annonces
        annoncesPanel.removeAll();

        // Récupérer les offres depuis la base de données
        List<Offre> offres = offreDAO.recupererOffres();

        // Créer un panneau pour afficher les offres et les boutons
        JPanel offrePanel = new JPanel();
        offrePanel.setLayout(new GridLayout(0, 4)); // 4 colonnes// 4 colonnes : Offre, Accepter, Refuser, Contre-offre

        // Vérifier s'il reste des offres pour cette propriété avec un statut de 1
        boolean offresRestantes = false;
        for (Offre offre : offres) {
            if (offre.getId_prop() == id_prop_choisi && offre.getStatut() == 1 && offre.getContreProposition() != 0 && offre.getMailClient().equals(mail_perso)) {
                offresRestantes = true;
                break;
            }
        }

        // Parcourir la liste des offres
        for (Offre offre : offres) {
            System.out.println("cccccc"+offre.getContreProposition());
            if (offre.getId_prop() == id_prop_choisi && offre.getStatut() == 1 && offre.getContreProposition() != 0 && offre.getMailClient().equals(mail_perso)) {
                // Créer un libellé pour afficher l'offre
                JLabel offreLabel = new JLabel("CONTRE Offre : " + offre.getContreProposition());
                offrePanel.add(offreLabel);
                System.out.println("cccccccccccccon"+offre.getContreProposition());

                // Créer les boutons pour chaque action
                JButton accepterButton = new JButton("Accepter");
                JButton refuserButton = new JButton("Refuser");
                JButton contreOffreButton = new JButton("Contre-offre");

                // Obtenir le timestamp actuel
                long timestampMillis = System.currentTimeMillis();

                // Créer un objet Timestamp à partir du timestamp actuel
                Timestamp timestamp = new Timestamp(timestampMillis);


                // Ajouter des ActionListener aux boutons
                accepterButton.addActionListener(e -> {
                    Offre nouvelleOffre = new Offre(id_prop_choisi, 2, offre.getContreProposition(), offre.getMailClient(), offre.getContreProposition(), timestamp);
                    offreDAO.insererOffre(nouvelleOffre);

                    Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), offre.getContreProposition(), offre.getTime());
                    offreDAO.supprimerOffre(ancienneOffre);

                    // Mettre à jour les autres offres pour qu'elles aient un statut de 0 (non traité)
                    for (Offre autreOffre : offres) {
                        if (autreOffre.getId_prop() == id_prop_choisi && autreOffre.getStatut() == 1) {
                            Offre nouvelOffre = new Offre(id_prop_choisi, 0, autreOffre.getProposition(), autreOffre.getMailClient(), autreOffre.getContreProposition(), autreOffre.getTime());
                            offreDAO.insererOffre(nouvelOffre);

                            Offre ancienOffre = new Offre(id_prop_choisi, 1, autreOffre.getProposition(), autreOffre.getMailClient(), autreOffre.getContreProposition(), autreOffre.getTime());
                            offreDAO.supprimerOffre(ancienOffre);
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Offre acceptée pour la propriété " + id_prop_choisi);

                    // Actualiser l'affichage
                    chargerAnnonces();
                });

                refuserButton.addActionListener(e -> {
                    // Ajouter le code pour refuser l'offre ici
                    Offre nouvelleOffre = new Offre(id_prop_choisi, 0, offre.getProposition(), offre.getMailClient(), offre.getContreProposition(), timestamp);
                    offreDAO.insererOffre(nouvelleOffre); // Appeler la méthode sur l'instance d'OffreDAO

                    Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), offre.getContreProposition(), offre.getTime());
                    offreDAO.supprimerOffre(ancienneOffre);

                    // Ajouter le code pour refuser l'offre ici
                    JOptionPane.showMessageDialog(this, "Offre refusée pour la propriété " + id_prop_choisi);

                    // Actualiser l'affichage
                    List<Offre> offresRestantess = offreDAO.recupererOffres();
                    for (Offre offreRestantess : offresRestantess) {
                        if (offreRestantess.getId_prop() == id_prop_choisi && offreRestantess.getStatut() == 1 && offreRestantess.getContreProposition() != 0 && offre.getMailClient().equals(mail_perso)) {
                            // Actualiser l'affichage uniquement s'il reste des offres pour cette propriété avec un statut de 1
                            ActionOffreClient(id_prop_choisi);
                            return; // Sortir de la méthode après avoir actualisé l'affichage
                        }
                    }

                    // Si aucune offre restante pour cette propriété avec un statut de 1 n'est trouvée, recharger toutes les annonces
                    chargerAnnonces();
                });

                contreOffreButton.addActionListener(e -> {
                    // Créer un champ JTextField pour saisir la contre-offre
                    JTextField contreOffreField = new JTextField(10); // Vous pouvez ajuster la taille en fonction de vos besoins

                    // Afficher une boîte de dialogue pour permettre à l'utilisateur de saisir la contre-offre
                    int result = JOptionPane.showConfirmDialog(this, contreOffreField, "Entrez la contre-offre", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Récupérer la valeur saisie dans le champ JTextField
                        String contreOffreText = contreOffreField.getText();

                        // Convertir la valeur saisie en entier (vous pouvez ajouter une validation supplémentaire si nécessaire)
                        int contreOffreValue = Integer.parseInt(contreOffreText);

                        // Ajouter le code pour faire une contre-offre ici
                        // Vous pouvez utiliser la valeur de contreOffreValue pour effectuer des opérations avec la contre-offre

                        if (contreOffreValue >= offre.getContreProposition()) {
                            JOptionPane.showMessageDialog(this, "La contre-offre de  doit être inférieur à la contre offre initiale.");
                        }else {
                            // Ajouter le code pour refuser l'offre ici
                            Offre nouvelleOffre = new Offre(id_prop_choisi, 1, contreOffreValue, offre.getMailClient(), 0, timestamp);
                            offreDAO.insererOffre(nouvelleOffre); // Appeler la méthode sur l'instance d'OffreDAO

                            Offre ancienneOffreSauvegarde = new Offre(id_prop_choisi, 0, offre.getProposition(), offre.getMailClient(), offre.getContreProposition(), offre.getTime());
                            offreDAO.insererOffre(ancienneOffreSauvegarde);

                            Offre ancienneOffre = new Offre(id_prop_choisi, 1, offre.getProposition(), offre.getMailClient(), offre.getContreProposition(), offre.getTime());
                            offreDAO.supprimerOffre(ancienneOffre);

                            // Actualiser l'affichage
                            List<Offre> offresRestantess = offreDAO.recupererOffres();
                            for (Offre offreRestantess : offresRestantess) {
                                System.out.println("aaaaaaaaaaaaaaaaa"+ offreRestantess.getContreProposition());
                                if (offreRestantess.getId_prop() == id_prop_choisi && offreRestantess.getStatut() == 1 && offreRestantess.getContreProposition() != 0) {
                                    // Actualiser l'affichage uniquement s'il reste des offres pour cette propriété avec un statut de 1
                                    ActionOffreClient(id_prop_choisi);
                                    System.out.println("bbbbbbbbbbbbbbbbb"+ offreRestantess.getContreProposition());
                                    return; // Sortir de la méthode après avoir actualisé l'affichage
                                }
                            }
                            // Si aucune offre restante pour cette propriété avec un statut de 1 n'est trouvée, recharger toutes les annonces
                            chargerAnnonces();
                        }
                    }
                });


                // Ajouter les boutons au panneau d'offres
                offrePanel.add(accepterButton);
                offrePanel.add(refuserButton);
                offrePanel.add(contreOffreButton);
            }
        }
// Si aucune offre avec un statut de 1 n'est trouvée, recharger toutes les annonces
        if (!offresRestantes) {
            chargerAnnonces();
        }
        // Ajouter le panneau d'offres au panneau d'annonces
        annoncesPanel.add(offrePanel);

        // Rafraîchir l'interface utilisateur
        revalidate();
        repaint();
    }



    // Méthode pour charger à nouveau les annonces
    private void chargerAnnonces() {
        annoncesPanel.removeAll();

        List<Propriete> annonces = proprieteDAO.recupererAnnonces(); // AAAAAAAAAAAAAAAAAAAAAAAAAA

        for (Propriete propriete : annonces) {
            JPanel annoncePanel = createAnnoncePanel(propriete);
            annoncesPanel.add(annoncePanel);
        }

        revalidate();
        repaint();
    }


    public void FaireOffre(int id_prop_choisi) {

        // Supprimer les composants existants dans le panneau d'annonces
        annoncesPanel.removeAll();

        // Créer un panneau pour le champ de texte "Faire une offre"
        JPanel offrePanel = new JPanel();
        offreField = new JTextField(10); // Champ de texte pour saisir l'offre

        // Ajout du champ pour faire une offre avec un JSpinner
        offrePanel.add(new JLabel("Faire une offre : "));
        SpinnerModel offreModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1); // Valeur initiale, valeur minimale, valeur maximale, pas
        JSpinner offreSpinner = new JSpinner(offreModel);
        offrePanel.add(offreSpinner);

        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int offreValue = (int) offreSpinner.getValue();

                // Obtenir le timestamp actuel
                long timestampMillis = System.currentTimeMillis();
                // Créer un objet Timestamp à partir du timestamp actuel
                Timestamp timestamp = new Timestamp(timestampMillis);

                Offre nouvelleOffre = new Offre(id_prop_choisi, 1, offreValue, mail_perso,0,timestamp);
                offreDAO.insererOffre(nouvelleOffre); // Appeler la méthode sur l'instance d'OffreDAO
                // Charger à nouveau les annonces
                chargerAnnonces();
            }
        });
        offrePanel.add(enregistrerButton);

        // Ajouter le panneau du champ de texte au panneau d'annonces
        annoncesPanel.add(offrePanel);

        // Rafraîchir l'interface utilisateur
        revalidate();
        repaint();
    }

    public void AccepterResaButton(int id_prop_choisi) {
        JFrame calendarFrame = new JFrame("Calendrier");
        calendarFrame.setLayout(new BorderLayout());

        List<Reservation> visiteActualise = reservationDAO.recupererVisite();

        JCalendar calendar = new JCalendar();
        calendar.getDayChooser().addPropertyChangeListener("day", e -> {
            Date selectedDate = calendar.getDate();
            dateChooser.setDate(selectedDate);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Aligner les composants horizontalement au centre

            // affiche les dispo de cette journee de la propriete avec lid : id_prop_choisi
            boolean hasPropositions = false;
            for (Reservation reservation : visiteActualise) {
                if (reservation.getId_prop() == id_prop_choisi && reservation.getStatut() == 1 && isSameDay(reservation.getDispo(), selectedDate)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String buttonText = formatter.format(reservation.getDispo());

                    JButton button = new JButton(buttonText);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Reservation nouvelleReservation = new Reservation(id_prop_choisi, 2, reservation.getDispo(), reservation.getMailClient());
                            reservationDAO.insererReservation(nouvelleReservation);

                            Reservation ancienneReservation = new Reservation(id_prop_choisi, 1, reservation.getDispo(), reservation.getMailClient());
                            reservationDAO.suppReservation(ancienneReservation);

                            // Rafraîchir l'affichage du calendrier après avoir modifié le statut
                            calendarFrame.dispose(); // Fermer la fenêtre du calendrier existante
                            AccepterResaButton(id_prop_choisi); // Rafraîchir l'affichage du calendrier avec la nouvelle réservation
                        }
                    });
                    buttonPanel.add(button);
                    hasPropositions = true;
                }
            }
            if (!hasPropositions) {
                JLabel noPropositionLabel = new JLabel("Aucune proposition pour cette date.");
                buttonPanel.add(noPropositionLabel);
            }

            JScrollPane scrollPane = new JScrollPane(buttonPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

            calendarFrame.getContentPane().removeAll(); // Supprimer tous les composants existants
            calendarFrame.add(calendar, BorderLayout.CENTER);
            calendarFrame.add(scrollPane, BorderLayout.SOUTH);
            calendarFrame.revalidate(); // Mettre à jour l'affichage du cadre
        });

        calendarFrame.add(calendar, BorderLayout.CENTER);
        calendarFrame.setSize(400, 300);
        calendarFrame.setVisible(true);
    }

    public void SelectDateVisite(int id_prop_choisi) {
        JFrame calendarFrame = new JFrame("Calendrier");
        calendarFrame.setLayout(new BorderLayout());

        List<Reservation> visiteActualise = reservationDAO.recupererVisite();

        JCalendar calendar = new JCalendar();
        calendar.getDayChooser().addPropertyChangeListener("day", e -> {
            Date selectedDate = calendar.getDate();
            dateChooser.setDate(selectedDate);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Aligner les composants horizontalement au centre

            // affiche les dispo de cette journee de la propriete avec lid : id_prop_choisi
            boolean hasPropositions = false;
            for (Reservation reservation : visiteActualise) {
                if (reservation.getId_prop() == id_prop_choisi && reservation.getStatut() == 0 && isSameDay(reservation.getDispo(), selectedDate)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String buttonText = formatter.format(reservation.getDispo());

                    JButton button = new JButton(buttonText);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Reservation nouvelleReservation = new Reservation(id_prop_choisi, 1, reservation.getDispo(), mail_perso);
                            reservationDAO.insererReservation(nouvelleReservation);

                            Reservation ancienneReservation = new Reservation(id_prop_choisi, 0, reservation.getDispo(), "null");
                            reservationDAO.suppReservation(ancienneReservation);

                            // Rafraîchir l'affichage du calendrier après avoir modifié le statut
                            calendarFrame.dispose(); // Fermer la fenêtre du calendrier existante
                            SelectDateVisite(id_prop_choisi); // Rafraîchir l'affichage du calendrier avec la nouvelle réservation
                        }
                    });
                    buttonPanel.add(button);
                    hasPropositions = true;
                }
            }
            if (!hasPropositions) {
                JLabel noPropositionLabel = new JLabel("Aucune proposition pour cette date.");
                buttonPanel.add(noPropositionLabel);
            }

            JScrollPane scrollPane = new JScrollPane(buttonPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

            calendarFrame.getContentPane().removeAll(); // Supprimer tous les composants existants
            calendarFrame.add(calendar, BorderLayout.CENTER);
            calendarFrame.add(scrollPane, BorderLayout.SOUTH);
            calendarFrame.revalidate(); // Mettre à jour l'affichage du cadre
        });

        calendarFrame.add(calendar, BorderLayout.CENTER);
        calendarFrame.setSize(400, 300);
        calendarFrame.setVisible(true);
    }

    // Vérifier si deux dates représentent le même jour
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public void AjoutDateVisite(int id_prop_choisi) {
        JFrame calendarFrame = new JFrame("Calendrier");
        calendarFrame.setLayout(new BorderLayout());

        JCalendar calendar = new JCalendar();
        calendar.getDayChooser().addPropertyChangeListener("day", e -> {
            Date selectedDate = calendar.getDate();
            java.sql.Timestamp dateStamp = new java.sql.Timestamp(selectedDate.getTime());
            dateChooser.setDate(selectedDate);

            JPanel heurePanel = new JPanel(new FlowLayout());
            heurePanel.add(new JLabel("Sélectionnez l'heure : "));

            SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 23, 1);
            JSpinner hourSpinner = new JSpinner(hourModel);
            JSpinner.NumberEditor hourEditor = new JSpinner.NumberEditor(hourSpinner);
            hourSpinner.setEditor(hourEditor);

            SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
            JSpinner minuteSpinner = new JSpinner(minuteModel);
            JSpinner.NumberEditor minuteEditor = new JSpinner.NumberEditor(minuteSpinner);
            minuteSpinner.setEditor(minuteEditor);

            heurePanel.add(hourSpinner);
            heurePanel.add(new JLabel(":"));
            heurePanel.add(minuteSpinner);

            JButton ajouterDispoButton = new JButton("Ajouter Disponibilité");
            ajouterDispoButton.addActionListener(event -> {
                int selectedHour = (int) hourSpinner.getValue();
                int selectedMinute = (int) minuteSpinner.getValue();

                Calendar calendarE = Calendar.getInstance();
                calendarE.setTime(dateStamp);
                calendarE.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendarE.set(Calendar.MINUTE, selectedMinute);
                calendarE.set(Calendar.SECOND, 0);

                System.out.println(calendarE.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = sdf.format(calendarE.getTime());
                System.out.println(formattedDateTime);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.sql.Timestamp timestamp = null;

                try {
                    Date parsedDate = dateFormat.parse(formattedDateTime);
                    long millis = parsedDate.getTime();
                    timestamp = new java.sql.Timestamp(millis);
                    System.out.println("Timestamp: " + timestamp);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                if (timestamp != null) {
                    System.out.println("saaaaa" + timestamp);
                    Reservation nouvelleReservation = new Reservation(id_prop_choisi, 0, timestamp, "null");
                    reservationDAO.insererReservation(nouvelleReservation);

                    // Réinitialiser les valeurs des spinners d'heures et de minutes à zéro
                    hourSpinner.setValue(0);
                    minuteSpinner.setValue(0);
                }
            });

            heurePanel.add(ajouterDispoButton);
            calendarFrame.add(heurePanel, BorderLayout.SOUTH);
            calendarFrame.pack();
        });

        calendarFrame.add(calendar, BorderLayout.CENTER);
        calendarFrame.setSize(400, 300);
        calendarFrame.setVisible(true);
    }

    public static class ImageNavigationListener implements ActionListener {
        private final ImageIcon[] imageIcons;
        private final JLabel imageLabel;
        private final int direction;

        public ImageNavigationListener(ImageIcon[] imageIcons, JLabel imageLabel, int direction) {
            this.imageIcons = imageIcons;
            this.imageLabel = imageLabel;
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon currentIcon = (ImageIcon) imageLabel.getIcon();
            int currentIndex = findIndex(imageIcons, currentIcon);
            int nextIndex = currentIndex + direction;
            if (nextIndex >= 0 && nextIndex < imageIcons.length) {
                imageLabel.setIcon(imageIcons[nextIndex]);
            }
        }

        private int findIndex(ImageIcon[] imageIcons, ImageIcon targetIcon) {
            for (int i = 0; i < imageIcons.length; i++) {
                if (imageIcons[i].equals(targetIcon)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AnnoncesPage::new);
    }
}