package vue;


import modele.*;
import modele.DemandeProprieteDAO;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


import static modele.ReservationDAO.*;
import static modele.UtilisateurDAO.*;

public class MonCompte extends JPanel {

    private String mail_perso ;
    private ProprieteDAO proprieteDAO = new ProprieteDAO();
    List<Propriete> annonces = proprieteDAO.recupererAnnonces();
    private OffreDAO offreDAO = new OffreDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private  UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    private Etat_connexion etatConnexion = Etat_connexion.getInstance();
    private List<String> clients;
    private List<String> vendeurs;
    private List<String> employes;
    private JPanel contentPanel;
    private DemandeProprieteDAO demandeProprieteDAO;




    public MonCompte() {


        Dimension preferredSize = new Dimension(1700, 500);

        this.demandeProprieteDAO = new DemandeProprieteDAO();


        mail_perso = etatConnexion.getUtilisateurConnecteEmail();
        String emailUtilisateurConnecte = etatConnexion.getUtilisateurConnecteEmail();
        int typeUtilisateurConnecte = etatConnexion.getUtilisateurConnecteType();

        // Couleurs personnalisées
        Color beigeClair = new Color(250, 238, 206);
        Color marron = new Color(210, 180, 140);
        Color rosePale = new Color(255, 192, 203);
        Color rosePetant = new Color(255, 145, 175);

// Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(rosePale); // Définir la couleur de fond du panneau principal

// En-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(rosePetant); // Définir la couleur de fond du panneau d'en-tête
        headerPanel.setPreferredSize(new Dimension(1600, 100));

// Titre "Mon Compte"
        JLabel headerLabel = new JLabel("Mon Compte");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLACK); // Définir la couleur du texte en blanc
        headerPanel.add(headerLabel, BorderLayout.NORTH);

// Création d'un JLabel pour afficher le logo
        JLabel logoLabel = new JLabel();
// Chargez votre image de logo
        ImageIcon logoIcon = new ImageIcon("logo/logo3.png");
// Appliquer l'icône au JLabel
        logoLabel.setIcon(logoIcon);
// Redimensionner l'icône du logo
        Image image = logoIcon.getImage(); // Transformez l'ImageIcon en Image
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Redimensionnez l'image avec les dimensions souhaitées
        logoIcon = new ImageIcon(newImage); // Transformez l'Image redimensionnée en ImageIcon
        logoLabel.setIcon(logoIcon); // Appliquez le logo redimensionné au JLabel

// Ajoutez le JLabel contenant le logo au panneau d'en-tête
        headerPanel.add(logoLabel, BorderLayout.EAST);

// Ajouter les détails de l'utilisateur dans l'en-tête
        JPanel userDetailsPanel = AfficherUtilisateur();
        headerPanel.add(userDetailsPanel, BorderLayout.CENTER);
        userDetailsPanel.setBackground(rosePetant); // Définir la couleur de fond du panneau des détails de l'utilisateur
        userDetailsPanel.setForeground(Color.WHITE); // Définir la couleur du texte en blanc

        mainPanel.add(headerPanel, BorderLayout.NORTH);

// Panneau gauche avec les catégories
        JPanel categoriesPanel = new JPanel();
        categoriesPanel.setPreferredSize(new Dimension(200, 700));
        categoriesPanel.setBackground(rosePale); // Définir la couleur de fond du panneau des catégories
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        JLabel categoriesLabel = new JLabel("Catégories");
        categoriesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        categoriesLabel.setForeground(Color.BLACK); // Définir la couleur du texte en blanc
        categoriesPanel.add(categoriesLabel);

// Déclaration et initialisation du panneau central
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(rosePale); // Définir la couleur de fond du panneau central
        JTextArea contentArea = new JTextArea("Contenu principal");
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

// Bouton pour "Mes Propriétés"
        JButton propertiesButton = new JButton("Mes Propriétés");
        propertiesButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
        propertiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                JPanel propertiesPanel = AfficherHistoriqueProprietes();
                propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS)); // Utilisation du BoxLayout en mode Y_AXIS
                JScrollPane scrollPane = new JScrollPane(propertiesPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                contentPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.setBackground(beigeClair);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        categoriesPanel.add(propertiesButton);

        // Ajout du bouton "Mes propriétés" pour le client
        if (typeUtilisateurConnecte == 2) { // Assurez-vous que l'utilisateur connecté est un client
            JButton mesProprietesButton = new JButton("Mes Propriétés (Client)");
            mesProprietesButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
            mesProprietesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Afficher les propriétés achetées par le client connecté
                    JPanel mesProprietesPanel = afficherProprietesClient(emailUtilisateurConnecte);

                    // Remplacer le contenu du panneau central par les propriétés achetées
                    contentPanel.removeAll(); // Nettoyer le panneau central
                    contentPanel.add(new JScrollPane(mesProprietesPanel), BorderLayout.CENTER); // Ajouter les propriétés achetées dans un JScrollPane pour permettre le défilement si nécessaire
                    contentPanel.setBackground(beigeClair); // Définir la couleur de fond du panneau central
                    contentPanel.revalidate(); // Mettre à jour l'affichage du panneau central
                    contentPanel.repaint();
                }
            });
            categoriesPanel.add(mesProprietesButton);
        }


// Bouton pour "Mes Offres"
        JButton offersButton = new JButton("Mes Offres");
        offersButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
        offersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                JPanel offersPanel = AfficherHistoriqueOffres();
                offersPanel.setLayout(new BoxLayout(offersPanel, BoxLayout.Y_AXIS)); // Utilisation du BoxLayout en mode Y_AXIS
                JScrollPane scrollPane = new JScrollPane(offersPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                contentPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.setBackground(beigeClair);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        categoriesPanel.add(offersButton);

// Bouton pour "Mes Réservations"
        JButton reservationsButton = new JButton("Mes Réservations");
        reservationsButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
        reservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                JPanel reservationsPanel = AfficherHistoriqueReservations();
                reservationsPanel.setLayout(new BoxLayout(reservationsPanel, BoxLayout.Y_AXIS)); // Utilisation du BoxLayout en mode Y_AXIS
                JScrollPane scrollPane = new JScrollPane(reservationsPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                contentPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.setBackground(beigeClair);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        categoriesPanel.add(reservationsButton);

        // Ajout du bouton "Mes demandes" pour le vendeur
        if (typeUtilisateurConnecte == 3) { // Assurez-vous que l'utilisateur connecté est un vendeur
            JButton mesDemandesButton = new JButton("Mes demandes");
            mesDemandesButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
            mesDemandesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Récupérer les demandes de propriété du vendeur connecté
                    List<DemandePropriete> demandes = demandeProprieteDAO.recupererAnnonces();

                    // Déclaration du panneau qui contiendra les demandes de propriété
                    JPanel demandesPanel = new JPanel();
                    demandesPanel.setLayout(new BoxLayout(demandesPanel, BoxLayout.Y_AXIS));

                    // Ajouter chaque demande de propriété au panneau des demandes
                    for (DemandePropriete demande : demandes) {
                        JPanel demandePanel = createDemandePanel(demande); // Créer un panneau pour afficher la demande
                        demandesPanel.add(demandePanel); // Ajouter le panneau de demande au panneau des demandes
                    }

                    // Remplacer le contenu du panneau central par les demandes de propriété
                    contentPanel.removeAll(); // Nettoyer le panneau central
                    contentPanel.add(new JScrollPane(demandesPanel), BorderLayout.CENTER); // Ajouter les demandes de propriété dans un JScrollPane pour permettre le défilement si nécessaire
                    contentPanel.setBackground(beigeClair); // Définir la couleur de fond du panneau central
                    contentPanel.revalidate(); // Mettre à jour l'affichage du panneau central
                    contentPanel.repaint();
                }
            });
            categoriesPanel.add(mesDemandesButton);
        }





// Bouton pour "Mes Statistiques"
        if (typeUtilisateurConnecte == 2) {
            // Si l'utilisateur connecté est un employé (type == 1), affichez le bouton "Mes Statistiques"
            JButton statisticsButton = new JButton("Mes Statistiques");
            statisticsButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
            statisticsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Générer et afficher les statistiques
                    JPanel statisticsPanel = genererStatistiques();
                    contentPanel.removeAll();
                    contentPanel.add(statisticsPanel, BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            });
            categoriesPanel.add(statisticsButton);
        }



        mainPanel.add(categoriesPanel, BorderLayout.WEST);

// Panneau de droite avec les publicités
        JPanel adsPanel = new JPanel();
        adsPanel.setPreferredSize(new Dimension(200, 700));
        adsPanel.setBackground(beigeClair); // Définir la couleur de fond du panneau des publicités
        adsPanel.setLayout(new BoxLayout(adsPanel, BoxLayout.Y_AXIS));
        JLabel adsLabel = new JLabel("Publicités");
        adsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        adsLabel.setForeground(Color.BLACK); // Définir la couleur du texte en blanc
        adsPanel.add(adsLabel);

// Ajouter des images publicitaires
// Utilisation de la méthode pour redimensionner les images et les afficher dans les JLabels
        JLabel imageLabel1 = new JLabel(resizeImage("logo/pub1.jpg", 200, 100));
        JLabel imageLabel2 = new JLabel(resizeImage("logo/pub2.jpg", 200, 200));
        JLabel imageLabel3 = new JLabel(resizeImage("logo/pub3.jpg", 200, 200));

        adsPanel.add(imageLabel1);
        adsPanel.add(imageLabel2);
        adsPanel.add(imageLabel3);

        mainPanel.add(adsPanel, BorderLayout.EAST);



        // Bouton pour "Déconnexion"
        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // effacer la fenetre
                etatConnexion.deconnecterUtilisateur();
                // Fermeture de la fenêtre actuelle
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
                frame.dispose();

                MenuAcceuil menu = new MenuAcceuil();
                //System.exit(0);

            }
        });
        logoutButton.setBackground(rosePetant); // Définir la couleur de fond du bouton
        categoriesPanel.add(logoutButton);


// Afficher la fenêtre
        add(mainPanel);
        //pack();
        setVisible(true);


    }


    // HISTORIQUE PROPRIETE
    public JPanel AfficherHistoriqueProprietes() {
        // Créer un JPanel pour afficher l'historique des propriétés
        JPanel historiqueProprietesPanel = new JPanel();

        // Récupérer les propriétés depuis la base de données
        List<Propriete> proprietes = proprieteDAO.recupererAnnonces();

        // Créer une JTextArea pour afficher l'historique des propriétés
        JTextArea historiqueProprietesTextArea = new JTextArea(20, 50);
        historiqueProprietesTextArea.setEditable(false); // Empêcher l'édition du texte

        // Construire l'historique des propriétés
        StringBuilder historiqueProprietesBuilder = new StringBuilder();
        for (Propriete propriete : proprietes) {
            if (propriete.getVendeur().equals(mail_perso) || propriete.getEmploye().equals(mail_perso)) {
                JPanel annoncePanel = createAnnoncePanel(propriete);
                historiqueProprietesPanel.add(annoncePanel);

            } else{
                // Vérifier s'il y a une offre pour cette propriété impliquant l'utilisateur connecté
                List<Offre> offres = offreDAO.recupererOffresParPropriete(propriete.getId());
                for (Offre offre : offres) {
                    if (offre.getMailClient().equals(mail_perso) && offre.getStatut() == 2) {
                        JPanel annoncePanel = createAnnoncePanel(propriete);
                        historiqueProprietesPanel.add(annoncePanel);
                        break; // Sortir de la boucle une fois qu'une offre acceptée est trouvée
                    }
                }
            }
        }
        // Retourner le JPanel contenant l'historique des propriétés
        return historiqueProprietesPanel;
    }

    private JPanel afficherProprietesClient(String emailClient) {
        JPanel panelProprietes = new JPanel();
        panelProprietes.setLayout(new BoxLayout(panelProprietes, BoxLayout.Y_AXIS));

        // Récupérer les propriétés achetées par le client depuis la base de données
        List<Propriete> proprietesAcheteClient = proprieteDAO.recupererProprietesAcheteParClient(emailClient);

        // Pour chaque propriété achetée, créer un panneau et l'ajouter au panel des propriétés
        for (Propriete propriete : proprietesAcheteClient) {
            JPanel proprietePanel = createAnnoncePanel(propriete); // Utiliser la méthode existante pour créer un panneau de propriété
            panelProprietes.add(proprietePanel);
        }

        return panelProprietes;
    }


    private JPanel createAnnoncePanel(Propriete propriete) {
        JPanel annoncePanel = new JPanel(new BorderLayout());

        JPanel imagePanel = createImagePanel(propriete.getPhotos());
        annoncePanel.add(imagePanel, BorderLayout.WEST);

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
        previousButton.addActionListener(new AnnoncesPage.ImageNavigationListener(imageIcons, imageLabel, -1));

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(new AnnoncesPage.ImageNavigationListener(imageIcons, imageLabel, 1));

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

        JLabel attributsLabel = new JLabel("<html><ul>" +
                "<li>Emplacement: " + propriete.getEmplacement() + "</li>" +
                "<li>Superficie: " + propriete.getSuperficie() + "</li>" +
                "<li>Nombre de pièces: " + propriete.getNbPieces() + "</li>" +
                "<li>Nombre de salles de bain: " + propriete.getNbSalleBain() + "</li>" +
                "<li>Nombre de salles d'eau: " + propriete.getNbSalleEau() + "</li>" +
                "<li>Description: " + propriete.getDescription() + "</li>" +
                "<li>Prix: " + propriete.getPrix() + "</li>" +
                "<li>Adresse: " + propriete.getAdresse() + "</li>" +
                "<li>Vendeur: " + propriete.getVendeur() + "</li>" +
                "<li>Ville: " + propriete.getVille() + "</li>" +
                "</ul></html>");

        infoPanel.add(attributsLabel);
        return infoPanel;
    }


    private String getStatusLabelProp(int statut) {
        switch (statut) {
            case 0:
                return "Disponible";
            case 1:
                return "Vendu";
            default:
                return "Statut inconnu";
        }
    }


    // PROFIL
    public JPanel AfficherUtilisateur() {
        // Créer un JPanel pour afficher les utilisateurs
        JPanel utilisateurPanel = new JPanel();

        // Créer une instance de UtilisateurDAO
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        // Récupérer les clients depuis la base de données
        List<String> clients = utilisateurDAO.obtenirClientsSam();
        List<String> vendeurs = utilisateurDAO.obtenirVendeursSam();
        List<String> employes = utilisateurDAO.obtenirEmployesSam();

        // Construire le texte à afficher
        StringBuilder utilisateurBuilder = new StringBuilder();

        for (String client : clients) {
            // Séparer l'e-mail et le nom
            String[] parts = client.split(" ");
            String prenom = parts[0];
            String nom = parts[1];
            String mail = parts[2];

            if (mail.equals(mail_perso)) {
                JLabel utilisateurLabel = new JLabel("<html><ul>" + "<li>Prénom: " + prenom + "</li>" + "<li>Nom: " + nom + "</li>" + "<li>Mail: " + mail + "</li>" + "</ul></html>");
                utilisateurPanel.add(utilisateurLabel);
            }
        }

        for (String vendeur : vendeurs) {
            // Séparer l'e-mail et le nom
            String[] parts = vendeur.split(" ");
            String prenom = parts[0];
            String nom = parts[1];
            String mail = parts[2];

            if (mail.equals(mail_perso)) {
                JLabel utilisateurLabel = new JLabel("<html><ul>" + "<li>Prénom: " + prenom + "</li>" + "<li>Nom: " + nom + "</li>" + "<li>Mail: " + mail + "</li>" + "</ul></html>");
                utilisateurPanel.add(utilisateurLabel);
            }
        }

        for (String employe : employes) {
            // Séparer l'e-mail et le nom
            String[] parts = employe.split(" ");
            String prenom = parts[0];
            String nom = parts[1];
            String mail = parts[2];

            if (mail.equals(mail_perso)) {
                JLabel utilisateurLabel = new JLabel("<html><ul>" + "<li>Prénom: " + prenom + "</li>" + "<li>Nom: " + nom + "</li>" + "<li>Mail: " + mail + "</li>" + "</ul></html>");
                utilisateurPanel.add(utilisateurLabel);
            }
        }
        return utilisateurPanel;
    }



    // HISTORIQUE OFFRES
    public JPanel AfficherHistoriqueOffres() {
        // Créer un JPanel pour afficher l'historique des offres
        JPanel historiquePanel = new JPanel();

        // Récupérer les offres depuis la base de données
        List<Offre> offres = offreDAO.recupererOffres();

        // Construire l'historique des offres
        StringBuilder historiqueBuilder = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'à' HH'h'mm");
        for (Offre offre : offres) {
            Propriete propriete = proprieteDAO.recupererPropriete(offre.getId_prop());
            if (propriete.getVendeur().equals(mail_perso) || propriete.getEmploye().equals(mail_perso) || offre.getMailClient().equals(mail_perso)) {
                JLabel offreLabel = new JLabel("<html><ul>" +
                        "<li>ID de la propriété: " + offre.getId_prop() + "</li>" +
                        "<li>Statut de l'offre: " + getStatusLabelOffre(offre.getStatut()) + "</li>" +
                        "<li>Proposition: " + offre.getProposition() + "</li>");

                if (offre.getContreProposition() == 0) {
                    offreLabel.setText(offreLabel.getText() + "<li>Contre-proposition: aucune proposée</li>");
                } else {
                    offreLabel.setText(offreLabel.getText() + "<li>Contre-proposition: " + offre.getContreProposition() + "</li>");

                    for (Offre autreOffre : offres) {
                        Propriete autrepropriete = proprieteDAO.recupererPropriete(offre.getId_prop());
                        // Vérifier si c'est une autre offre et si elle est en attente ou validée
                        if (autreOffre.getId_prop() == offre.getId_prop() && autreOffre.getMailClient().equals(offre.getMailClient())
                                && (autreOffre.getStatut() == 0 || autreOffre.getStatut() == 2)
                                && autreOffre.getProposition() == offre.getProposition()  && propriete.getVendeur().equals(autrepropriete.getVendeur())) {


                            offreLabel.setText(offreLabel.getText() + "<li>Statut Contre-proposition: contre offre refusée</li>");
                            break; // Sortir de la boucle dès qu'une contre-offre refusée est trouvée
                        }
                    }

                }

                offreLabel.setText(offreLabel.getText() +
                        "<li>Mail du client: " + offre.getMailClient() + "</li>" +
                        "<li>Temps: " + dateFormat.format(offre.getTime()) + "</li>" +
                        "</ul></html>");

                // Ajouter le JLabel à historiqueProprietesPanel
                historiquePanel.add(offreLabel);
            }
        }

        return historiquePanel;
    }

    private String getStatusLabelOffre(int statut) {
        switch (statut) {
            case 0:
                return "Refusé";
            case 1:
                return "En attente";
            case 2:
                return "Accepté";
            default:
                return "Statut inconnu";
        }
    }


    // HISTORIQUE RESA
    public JPanel AfficherHistoriqueReservations() {
        // Créer un JPanel pour afficher l'historique des réservations
        JPanel historiqueReservationsPanel = new JPanel();

        // Récupérer les réservations de visite depuis la base de données
        List<Reservation> reservations = reservationDAO.recupererVisite();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Format pour inclure à la fois la date et l'heure
        StringBuilder historiqueReservationsBuilder = new StringBuilder();
        for (Reservation reservation : reservations) {
            Propriete propriete = proprieteDAO.recupererPropriete(reservation.getId_prop());
            if (propriete.getVendeur().equals(mail_perso) || propriete.getEmploye().equals(mail_perso) || reservation.getMailClient().equals(mail_perso)) {
                JLabel reservationLabel = new JLabel("<html><ul>" +
                        "<li>ID de la propriété: " + reservation.getId_prop() + "</li>" +
                        "<li>Statut de la réservation: " + getStatusLabelResa(reservation.getStatut()) + "</li>" +
                        "<li>Date et heure de réservation: " + dateFormat.format(reservation.getDispo()) + "</li>");
                if (!reservation.getMailClient().equals(mail_perso)) {
                    reservationLabel.setText(reservationLabel.getText() + "<li>Mail du client: " + reservation.getMailClient() + "</li>");
                }
                reservationLabel.setText(reservationLabel.getText() + "</ul></html>");

                // Ajouter le JLabel à historiqueReservationsPanel
                historiqueReservationsPanel.add(reservationLabel);
            }
        }

        return historiqueReservationsPanel;
    }

    private String getStatusLabelResa(int statut) {
        switch (statut) {
            case 0:
                return "Disponible";
            case 1:
                return "Demande de réservation";
            case 2:
                return "Réservé";
            default:
                return "Statut inconnu";
        }

    }
    public JPanel createDemandePanel(DemandePropriete demande) {
        // Créer un JPanel pour afficher les détails de la demande
        JPanel demandePanel = new JPanel(new BorderLayout());

        // Créer un JPanel pour afficher les images de la demande
        JPanel imagePanel = createImagePanel(demande.getPhotos());
        demandePanel.add(imagePanel, BorderLayout.WEST);

        // Créer un JPanel pour afficher les informations textuelles de la demande
        JPanel infoPanel = createInfoPanel(demande);
        demandePanel.add(infoPanel, BorderLayout.CENTER);

        return demandePanel;
    }


    private JPanel createAnnoncePanel(DemandePropriete demande) {
        JPanel annoncePanel = new JPanel(new BorderLayout());

        JPanel imagePanel = createImagePanel(demande.getPhotos());
        annoncePanel.add(imagePanel, BorderLayout.WEST);

        JPanel infoPanel = createInfoPanel(demande);
        annoncePanel.add(infoPanel, BorderLayout.CENTER);

        return annoncePanel;
    }
    private JPanel createInfoPanel(DemandePropriete demande) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        JLabel titreLabel = new JLabel("Titre: " + demande.getTitre());
        titreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoPanel.add(titreLabel);

        JLabel attributsLabel = new JLabel("<html><ul>" +
                "<li>Emplacement: " + demande.getEmplacement() + "</li>" +
                "<li>Superficie: " + demande.getSuperficie() + "</li>" +
                "<li>Nombre de pièces: " + demande.getNbPieces() + "</li>" +
                "<li>Nombre de salles de bain: " + demande.getNbSalleBain() + "</li>" +
                "<li>Nombre de salles d'eau: " + demande.getNbSalleEau() + "</li>" +
                "<li>Description: " + demande.getDescription() + "</li>" +
                "<li>Prix: " + demande.getPrix() + "</li>" +
                "<li>Adresse: " + demande.getAdresse() + "</li>" +
                "<li>Vendeur: " + demande.getVendeur() + "</li>" +
                "<li>Ville: " + demande.getVille() + "</li>" +
                "</ul></html>");

        infoPanel.add(attributsLabel);
        return infoPanel;
    }




    public JPanel AfficherHistoriqueDemandes() {
        // Créer un JPanel pour afficher l'historique des demandes de propriétés
        JPanel historiqueDemandesPanel = new JPanel();
        historiqueDemandesPanel.setLayout(new BoxLayout(historiqueDemandesPanel, BoxLayout.Y_AXIS));

        // Récupérer les demandes de propriétés depuis la base de données
        List<DemandePropriete> demandes = demandeProprieteDAO.recupererAnnonces();

        // Parcourir toutes les demandes de propriétés
        for (DemandePropriete demande : demandes) {
            // Créer un panneau pour afficher la demande
            JPanel demandePanel = createDemandePanel(demande);

            // Ajouter le panneau de demande au panneau historiqueDemandesPanel
            historiqueDemandesPanel.add(demandePanel);
        }

        // Retourner le JPanel contenant l'historique des demandes de propriétés
        return historiqueDemandesPanel;
    }




    private JPanel genererStatistiques() {
        JPanel statisticsPanel = new JPanel(new BorderLayout());

        // Récupérer les statistiques depuis la base de données
        Map<String, Integer> statistiques = recupererStatistiques();

        // Créer un ensemble de données pour le graphique
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
            dataset.addValue(entry.getValue(), "Statut", entry.getKey());
        }

        // Créer le graphique à barres
        JFreeChart barChart = ChartFactory.createBarChart(
                "Statistiques des offres",
                "Statut",
                "Nombre",
                dataset
        );

        // Créer le panneau de visualisation pour afficher le graphique
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 600));

        statisticsPanel.add(chartPanel, BorderLayout.CENTER);

        return statisticsPanel;
    }

    private Map<String, Integer> recupererStatistiques() {
        Map<String, Integer> statistiques = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Établir la connexion à la base de données
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_immobiliere", "root", "");

            // Préparer la requête SQL
            String sql = "SELECT o.statut, COUNT(*) AS count " +
                    "FROM offre o " +
                    "JOIN propriete p ON o.id_prop = p.id " +
                    "WHERE p.employe = ? " +
                    "GROUP BY o.statut";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mail_perso);

            // Exécuter la requête SQL
            resultSet = preparedStatement.executeQuery();

            // Parcourir les résultats de la requête et stocker les statistiques dans la map
            while (resultSet.next()) {
                int statut = resultSet.getInt("statut");
                String label = getLabelForStatut(statut);
                int count = resultSet.getInt("count");
                statistiques.put(label, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return statistiques;
    }


    private String getLabelForStatut(int statut) {
        switch (statut) {
            case 0:
                return "Refusé";
            case 1:
                return "En attente";
            case 2:
                return "Vendu";
            default:
                return "Inconnu";
        }
    }

    // Méthode pour redimensionner une image
    private ImageIcon resizeImage(String imagePath, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imagePath); // Charger l'image depuis le chemin spécifié
        Image image = imageIcon.getImage(); // Obtenir l'image à partir de l'image icon
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Redimensionner l'image avec les dimensions spécifiées
        return new ImageIcon(resizedImage); // Retourner l'image redimensionnée sous forme d'ImageIcon
    }


}