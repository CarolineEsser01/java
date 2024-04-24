package vue;
import com.toedter.calendar.JDateChooser;
import vue.inscription.Connexion;
import vue.MonActivite.*;
import vue.MonActivite.preAnnonces.*;
import vue.inscription.Inscription;
import vue.inscription.ListeAnnonces;

import modele.*;
import controleur.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class Menu extends JFrame{
    private ReservationDAO reservationDAO = new ReservationDAO();
    private List<Reservation> visite = reservationDAO.recupererVisite();
    private FavorisDAO favorisDAO;
    private JTextField prixMinField;
    private JTextField prixMaxField;
    private JTextField superficieMinField;
    private JTextField superficieMaxField;
    private JTextField piecesMinField;
    private JTextField piecesMaxField;
    private JTextField villeField;



    private ProprieteDAO proprieteDAO = new ProprieteDAO();
    private JPanel annoncesPanel;
    private JDateChooser dateChooser;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel middlePanel;
    private JButton annonceButton;
    private JButton monActiviteButton;
    private JButton moncompte;
    private JButton messagerie;

    public MonActiviteClient MonActiviteClientPanel;
    public MonActiviteEmploye MonActiviteEmployePanel;
    private static String mail_perso ;
    private static Etat_connexion etatConnexion = Etat_connexion.getInstance();

    public ListeAnnonces ListePanel;
    public MonActiviteVendeur ActivitePanel;
    public MonActiviteEmploye ActivitePanelE;
    public MonActiviteClient ActivitePanelC;
    public Menu() {
        // Create a new JFrame
        frame = new JFrame("Accueil");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 1050);

        ListePanel = new ListeAnnonces();
        ActivitePanel = new MonActiviteVendeur();
        ActivitePanelE = new MonActiviteEmploye();
        ActivitePanelC = new MonActiviteClient();

        // Create a new JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create the first band with the logo, tableau de bord button, and droits utilisateurs button
        JPanel firstBand = new JPanel();
        firstBand.setLayout(new BoxLayout(firstBand, BoxLayout.X_AXIS));
        firstBand.setBackground(new Color(255, 145, 175));

        ImageIcon logoIcon = new ImageIcon("logo3.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledLogo);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel phraseCaroLabel = new JLabel("<html><div style='text-align: center; font-size: 18px;'>Bienvenue à Casa Rosa,<br>votre expérience immobilière vous fera voir la vie en rose !</div></html>");
        phraseCaroLabel.setForeground(new Color(255, 255, 255));


        JPanel phraseEtBoutonsPanel = new JPanel();
        phraseEtBoutonsPanel.setLayout(new BoxLayout(phraseEtBoutonsPanel, BoxLayout.Y_AXIS));
        phraseEtBoutonsPanel.setOpaque(false);

        phraseEtBoutonsPanel.add(phraseCaroLabel);
        phraseEtBoutonsPanel.add(Box.createVerticalStrut(10));

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonsPanel.setOpaque(false);

        phraseEtBoutonsPanel.add(boutonsPanel);


        firstBand.add(Box.createHorizontalGlue());
        firstBand.add(logoLabel);
        firstBand.add(Box.createRigidArea(new Dimension(10, 0)));
        firstBand.add(phraseEtBoutonsPanel);
        firstBand.add(Box.createHorizontalGlue());

        JPanel secondBand = new JPanel();
        secondBand.setLayout(new BoxLayout(secondBand, BoxLayout.X_AXIS));
        secondBand.setBackground(new Color(255, 192, 203)); // Rose pale



        annonceButton = new JButton("ANNONCES");
        annonceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        annonceButton.addActionListener(e -> showPage("ANNONCES"));

        annonceButton.setOpaque(false);
        annonceButton.setContentAreaFilled(false);
        annonceButton.setBorderPainted(false);
        annonceButton.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutonFont = new Font(annonceButton.getFont().getName(), Font.BOLD, 20);
        annonceButton.setFont(boutonFont);


        monActiviteButton = new JButton("MON ACTIVITE");
        monActiviteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        monActiviteButton.addActionListener(e -> showPage("MON ACTIVITE"));

        monActiviteButton.setOpaque(false);
        monActiviteButton.setContentAreaFilled(false);
        monActiviteButton.setBorderPainted(false);
        monActiviteButton.setForeground(new Color(253, 79, 138)); // Rose du log
        Font boutTonFont = new Font(monActiviteButton.getFont().getName(), Font.BOLD, 20);
        monActiviteButton.setFont(boutTonFont);


        moncompte = new JButton("MON COMPTE");
        moncompte.setAlignmentX(Component.LEFT_ALIGNMENT);
        moncompte.addActionListener(e -> showPage("MON COMPTE"));

        moncompte.setOpaque(false);
        moncompte.setContentAreaFilled(false);
        moncompte.setBorderPainted(false);
        moncompte.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutTtonFont = new Font(moncompte.getFont().getName(), Font.BOLD, 20);
        moncompte.setFont(boutTtonFont);


        MonActiviteClientPanel = new MonActiviteClient();
        MonActiviteEmployePanel = new MonActiviteEmploye();

        //cardLayout = new CardLayout();
        //cardPanel = new JPanel(cardLayout);
        Menu thisMenu = this;

        // Make the inscription panel initially invisible
        middlePanel = new JPanel();

        monActiviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middlePanel.removeAll(); // Enlevez tous les composants du conteneur principal
                middlePanel.setLayout(new BorderLayout()); // Définir le gestionnaire de disposition en premier

                System.out.println("eeeee" + etatConnexion.getUtilisateurConnecteType());

                if (etatConnexion.getUtilisateurConnecteType() == 3) { // VENDEUR
                    ActivitePanel = new MonActiviteVendeur();
                    middlePanel.add(ActivitePanel, BorderLayout.CENTER);
                    System.out.println("Bouton 'acti vend' cliqué");

                } else if(etatConnexion.getUtilisateurConnecteType() == 2) { // EMPLOYE
                    MonActiviteEmploye monActiviteEmploye = new MonActiviteEmploye();
                    ActivitePanelE = new MonActiviteEmploye();
                    middlePanel.add(ActivitePanelE, BorderLayout.CENTER);

                } else if(etatConnexion.getUtilisateurConnecteType() == 1) { // CLIENT
                    MonActiviteClient monActiviteClient = new MonActiviteClient();
                    ActivitePanelC = new MonActiviteClient();
                    middlePanel.add(ActivitePanelC, BorderLayout.CENTER);
                }

                middlePanel.revalidate();  // Mettez à jour le conteneur principal
                middlePanel.repaint();
            }
        });


        annonceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middlePanel.removeAll();
                ListeAnnonces();
                middlePanel.setLayout(new BorderLayout());
                middlePanel.add(ListePanel, BorderLayout.CENTER);
                middlePanel.revalidate();  // Mettez à jour le conteneur principal
                middlePanel.repaint();
            }
        });

        messagerie = new JButton("MESSAGERIE");
        messagerie.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagerie.addActionListener(e -> {
            MessagerieVue messagerieVue = new MessagerieVue();
            middlePanel.removeAll(); // Supprimer tout contenu existant
            middlePanel.add(messagerieVue, BorderLayout.CENTER);
            //middlePanel.pack();
            middlePanel.revalidate();  // Mettez à jour le conteneur principal
            middlePanel.repaint();
            showPage("MESSAGERIE");
        });

        messagerie.setOpaque(false);
        messagerie.setContentAreaFilled(false);
        messagerie.setBorderPainted(false);
        messagerie.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutTttonFont = new Font(messagerie.getFont().getName(), Font.BOLD, 20);
        messagerie.setFont(boutTttonFont);



        moncompte.setAlignmentX(Component.LEFT_ALIGNMENT);
        moncompte.addActionListener(e -> {

            MonCompte  monnn = new MonCompte();
            middlePanel.removeAll(); // Supprimer tout contenu existant
            middlePanel.add(monnn, BorderLayout.CENTER);
            //middlePanel.pack();
            middlePanel.revalidate();  // Mettez à jour le conteneur principal
            middlePanel.repaint();


        });


        annonceButton.setOpaque(false);
        annonceButton.setContentAreaFilled(false);
        annonceButton.setBorderPainted(false);
        annonceButton.setForeground(new Color(253, 79, 138)); // Rose du logo

        monActiviteButton.setOpaque(false);
        monActiviteButton.setContentAreaFilled(false);
        monActiviteButton.setBorderPainted(false);
        monActiviteButton.setForeground(new Color(253, 79, 138)); // Rose du logo

        moncompte.setOpaque(false);
        moncompte.setContentAreaFilled(false);
        moncompte.setBorderPainted(false);
        moncompte.setForeground(new Color(253, 79, 138)); // Rose du logo

        messagerie.setOpaque(false);
        messagerie.setContentAreaFilled(false);
        messagerie.setBorderPainted(false);
        messagerie.setForeground(new Color(253, 79, 138)); // Rose

        secondBand.add(Box.createHorizontalGlue());

        secondBand.add(Box.createRigidArea(new Dimension(10, 50)));
        secondBand.add(annonceButton);
        secondBand.add(Box.createRigidArea(new Dimension(10, 50)));
        secondBand.add(monActiviteButton);
        secondBand.add(Box.createRigidArea(new Dimension(10, 50)));
        secondBand.add(moncompte);
        secondBand.add(Box.createRigidArea(new Dimension(10, 50)));
        secondBand.add(messagerie);
        secondBand.add(Box.createHorizontalGlue());

        JPanel bandsContainer = new JPanel(new BorderLayout());
        bandsContainer.add(firstBand, BorderLayout.NORTH);
        bandsContainer.add(secondBand, BorderLayout.SOUTH);

        menuBar.add(bandsContainer);

        frame.add(middlePanel, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    private void showPage(String page) {
        JOptionPane.showMessageDialog(frame, "Affichage de la page: " + page);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu();
            }
        });
    }


    public void ListeAnnonces() {

        ListePanel.removeAll(); // Nettoyer les anciens contenus
        java.util.List<Propriete> annonces = proprieteDAO.recupererAnnonces();
        favorisDAO = new FavorisDAO();
        try {
            Connection connection = favorisDAO.obtenirConnexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initComponents();

        // Définir la taille préférée du contenu
        Dimension preferredSize = new Dimension(1000, 700);

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

        // Créer un JScrollPane pour le panneau d'annonces
        JScrollPane scrollPane = new JScrollPane(annoncesPanel);
        scrollPane.setPreferredSize(preferredSize); // Définir la taille préférée du JScrollPane


        ListePanel.add(scrollPane); // Ajouter le JScrollPane au middlePanel

        // Adapter la taille de la fenêtre au contenu
        setVisible(true);
    }

    private void initComponents() {
        JPanel headerPanel = createHeaderPanel();
        ListePanel.add(headerPanel, BorderLayout.NORTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 145, 175));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Utilisation du BoxLayout verticalement

        // Création d'un panneau pour les boutons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Utilisation de FlowLayout pour les boutons

        JButton prixButton = new JButton("Trier par Prix");
        prixButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField prixMinField = new JTextField();
            JTextField prixMaxField = new JTextField();
            panel.add(new JLabel("Prix Min:"));
            panel.add(prixMinField);
            panel.add(new JLabel("Prix Max:"));
            panel.add(prixMaxField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Entrez les valeurs de prix", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    double prixMin = Double.parseDouble(prixMinField.getText());
                    double prixMax = Double.parseDouble(prixMaxField.getText());

                    List<Propriete> annoncesTriees = proprieteDAO.recupererAnnonces(); // Récupérer toutes les annonces
                    annoncesTriees.removeIf(propriete -> propriete.getPrix() < prixMin || propriete.getPrix() > prixMax); // Filtrer les annonces selon le prix
                    mettreAJourAnnonces(annoncesTriees); // Mettre à jour l'affichage avec les annonces filtrées
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Menu.this, "Veuillez entrer des valeurs numériques valides pour le prix.");
                }
            }
        });

        JButton superficieButton = new JButton("Trier par Superficie");
        superficieButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField superficieMinField = new JTextField();
            JTextField superficieMaxField = new JTextField();
            panel.add(new JLabel("Superficie Min:"));
            panel.add(superficieMinField);
            panel.add(new JLabel("Superficie Max:"));
            panel.add(superficieMaxField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Entrez les valeurs de superficie", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    double superficieMin = Double.parseDouble(superficieMinField.getText());
                    double superficieMax = Double.parseDouble(superficieMaxField.getText());

                    List<Propriete> annoncesTriees = proprieteDAO.recupererAnnonces(); // Récupérer toutes les annonces
                    annoncesTriees.removeIf(propriete -> propriete.getSuperficie() < superficieMin || propriete.getSuperficie() > superficieMax); // Filtrer les annonces selon la superficie
                    mettreAJourAnnonces(annoncesTriees); // Mettre à jour l'affichage avec les annonces filtrées
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Menu.this, "Veuillez entrer des valeurs numériques valides pour la superficie.");
                }
            }
        });

        JButton piecesButton = new JButton("Trier par Nombre de Pièces");
        piecesButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField piecesMinField = new JTextField();
            JTextField piecesMaxField = new JTextField();
            panel.add(new JLabel("Nombre de Pièces Min:"));
            panel.add(piecesMinField);
            panel.add(new JLabel("Nombre de Pièces Max:"));
            panel.add(piecesMaxField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Entrez les valeurs de nombre de pièces", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int minPieces = Integer.parseInt(piecesMinField.getText());
                    int maxPieces = Integer.parseInt(piecesMaxField.getText());

                    List<Propriete> annoncesTriees = proprieteDAO.recupererAnnonces(); // Récupérer toutes les annonces
                    annoncesTriees.removeIf(propriete -> propriete.getNbPieces() < minPieces || propriete.getNbPieces() > maxPieces); // Filtrer les annonces selon le nombre de pièces
                    mettreAJourAnnonces(annoncesTriees); // Mettre à jour l'affichage avec les annonces filtrées
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Menu.this, "Veuillez entrer des valeurs numériques valides pour le nombre de pièces.");
                }
            }
        });

        JButton villeButton = new JButton("Trier par Ville");
        villeButton.addActionListener(e -> {
            JTextField villeField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Ville:"));
            panel.add(villeField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Entrez la ville", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String ville = villeField.getText();

                List<Propriete> annoncesTriees = proprieteDAO.recupererAnnonces(); // Récupérer toutes les annonces
                annoncesTriees.removeIf(propriete -> !propriete.getVille().equalsIgnoreCase(ville)); // Filtrer les annonces selon la ville
                mettreAJourAnnonces(annoncesTriees); // Mettre à jour l'affichage avec les annonces filtrées
            }
        });

        buttonsPanel.add(prixButton);
        buttonsPanel.add(superficieButton);
        buttonsPanel.add(piecesButton);
        buttonsPanel.add(villeButton);
        headerPanel.add(buttonsPanel);

        // Reste du code pour les champs de texte et autres composants...

        return headerPanel;
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

        JPanel imagePanel = new JPanel(new BorderLayout());

        JPanel imageContainer = new JPanel(new GridLayout(1, 1));
        JLabel imageLabel = new JLabel();

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        for (int i = 0; i < imagePaths.size(); i++) {
            try {
                String imagePath = imagePaths.get(i);

                BufferedImage originalImage = ImageIO.read(new FileInputStream(new File(imagePath)));

                int scaledWidth = 600;
                int scaledHeight = (int) (((double) scaledWidth / originalImage.getWidth()) * originalImage.getHeight());
                BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());
                Graphics2D g = scaledImage.createGraphics();
                g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g.dispose();

                ImageIcon imageIcon = new ImageIcon(scaledImage);
                imageIcons[i] = imageIcon;

                imageLabel.setIcon(imageIcon);
                imageContainer.add(imageLabel);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageLabel.setIcon(imageIcons[0]);

        imagePanel.add(imageContainer, BorderLayout.CENTER);

        JButton previousButton = new JButton("<");
        previousButton.addActionListener(new ImageNavigationListener(imageIcons, imageLabel, -1));

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(new ImageNavigationListener(imageIcons, imageLabel, 1));

        navigationPanel.add(previousButton);
        navigationPanel.add(nextButton);

        imagePanel.add(navigationPanel, BorderLayout.SOUTH);

        return imagePanel;
    }

    private JPanel createInfoPanel(Propriete propriete) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        JLabel prixLabel = new JLabel("Prix: " + propriete.getPrix());
        prixLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoPanel.add(prixLabel);

        JLabel attributsLabel = new JLabel("<html><ul>" +
                "<li>Titre: " + propriete.getTitre() + "</li>" +
                "<li>Emplacement: " + propriete.getEmplacement() + "</li>" +
                "<li>Superficie: " + propriete.getSuperficie() + "</li>" +
                "<li>Nombre de pièces: " + propriete.getNbPieces() + "</li>" +
                "<li>Nombre de salles de bain: " + propriete.getNbSalleBain() + "</li>" +
                "<li>Nombre de salles d'eau: " + propriete.getNbSalleEau() + "</li>" +
                "<li>Description: " + propriete.getDescription() + "</li>" +
                "<li>Adresse: " + propriete.getAdresse() + "</li>" +
                "<li>Ville: " + propriete.getVille() + "</li>" +
                "</ul></html>");

        infoPanel.add(attributsLabel);

        if (etatConnexion.getUtilisateurConnecteType() == 1) {
            JButton favorisButton = new JButton("FAVORIS");
            favorisButton.addActionListener(e -> {
                try {
                    favorisDAO.ajouterAnnonceEnFavoris(propriete.getId(), etatConnexion.getUtilisateurConnecteEmail());
                    JOptionPane.showMessageDialog(Menu.this, "Annonce ajoutée en favoris !");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Menu.this, "Erreur lors de l'ajout de l'annonce en favoris !");
                }
            });
            infoPanel.add(favorisButton);
        }
        return infoPanel;
    }

    private void mettreAJourAnnonces(List<Propriete> annoncesFiltrees) {
        annoncesPanel.removeAll();

        for (Propriete propriete : annoncesFiltrees) {
            JPanel annoncePanel = createAnnoncePanel(propriete);
            annoncesPanel.add(annoncePanel);
        }

        annoncesPanel.revalidate();
        annoncesPanel.repaint();
    }

    private class ImageNavigationListener implements ActionListener {
        private ImageIcon[] icons;
        private JLabel label;
        private int increment;

        public ImageNavigationListener(ImageIcon[] icons, JLabel label, int increment) {
            this.icons = icons;
            this.label = label;
            this.increment = increment;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int currentIndex = -1;
            for (int i = 0; i < icons.length; i++) {
                if (icons[i] == label.getIcon()) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex != -1) {
                int newIndex = (currentIndex + increment + icons.length) % icons.length;
                label.setIcon(icons[newIndex]);
            }
        }
    }
    // debut messagerie
    class MessagerieVue extends JPanel {
        private JComboBox<String> listePersonnes;
        private JButton demarrerDiscussionButton;
        private JTextArea conversationArea;
        private Messagerie messagerie;

        public MessagerieVue() {
            setLayout(new BorderLayout());


            listePersonnes = new JComboBox<>();
            demarrerDiscussionButton = new JButton("Démarrer la discussion");
            messagerie = new Messagerie();

            // gerer la taille des boutons et liste
            listePersonnes.setPreferredSize(new Dimension(500, 50));
            demarrerDiscussionButton.setPreferredSize(new Dimension(500, 50));

            for (String personne : messagerie.obtenirPersonnesAContacter()) {
                listePersonnes.addItem(personne);
            }

            conversationArea = new JTextArea();
            conversationArea.setEditable(false);

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setBackground(new Color(250, 238, 206));
            panel.add(listePersonnes, BorderLayout.NORTH);
            panel.add(demarrerDiscussionButton, BorderLayout.CENTER);
            add(panel, BorderLayout.NORTH);
            add(new JScrollPane(conversationArea), BorderLayout.CENTER);

            // Définir la couleur de fond du bouton en marron
            demarrerDiscussionButton.setBackground(new Color(210, 180, 140));
            // Définir la couleur de fond de la barre de défilement de la JComboBox en marron
            listePersonnes.setBackground(new Color(210, 180, 140));

            Dimension preferredSize = new Dimension(1800, 1000);
            setPreferredSize(preferredSize);

            demarrerDiscussionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String personneSelectionnee = (String) listePersonnes.getSelectedItem();
                    if (personneSelectionnee != null) {
                        JOptionPane.showMessageDialog(MessagerieVue.this, "Discussion démarrée avec " + personneSelectionnee);
                        ouvrirDiscussionAvec(personneSelectionnee);
                    } else {
                        JOptionPane.showMessageDialog(MessagerieVue.this, "Veuillez sélectionner une personne !");
                    }
                }
            });

            Etat_connexion etatConnexion = Etat_connexion.getInstance();
            String utilisateurConnecte = etatConnexion.getUtilisateurConnectePrenomEtNom();
            afficherConversationsUtilisateur(utilisateurConnecte);
        }

        private void ouvrirDiscussionAvec(String personne) {
            Etat_connexion etatConnexion = Etat_connexion.getInstance();
            String utilisateurConnecte = etatConnexion.getUtilisateurConnectePrenomEtNom();
            MessagerieDAO messagerieDAO = new MessagerieDAO();
            int resultat = messagerieDAO.checkConversationExistence(utilisateurConnecte, personne);
            if (resultat == 0) {
                System.out.println("Aucune conversation avec " + personne + " n'existe. Création en cours...");
                messagerieDAO.creerDiscussion(utilisateurConnecte, personne, "null");
            } else {
                System.out.println("Une conversation avec " + personne + " existe déjà.");
            }
            //SwingUtilities.getWindowAncestor(this).dispose();
            DiscussionVue discussion = new DiscussionVue(utilisateurConnecte, personne);
            discussion.setVisible(true);
        }

        private void afficherConversationsUtilisateur(String utilisateur) {
            MessagerieDAO messagerieDAO = new MessagerieDAO();
            List<String> personnes = messagerieDAO.getPersonnesConversations(utilisateur);
            Etat_connexion etatConnexion = Etat_connexion.getInstance();
            String utilisateurConnecte = etatConnexion.getUtilisateurConnectePrenomEtNom();

            JPanel conversationPanel = new JPanel();
            conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));
            conversationPanel.setBackground(new Color(250, 238, 206));

            JLabel label = new JLabel("Vous pouvez continuer à parler avec : ");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            conversationPanel.add(label);

            for (String personne : personnes) {
                JButton button = new JButton(personne);
                button.setPreferredSize(new Dimension(400, 30));
                button.setBackground(new Color(210, 180, 140));
                conversationPanel.add(button);
                conversationPanel.add(Box.createVerticalStrut(5));

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int resultat = messagerieDAO.checkConversationExistence(utilisateurConnecte, personne);
                        if (resultat == 0) {
                            System.out.println("Aucune conversation avec " + personne + " n'existe. Création en cours...");
                            messagerieDAO.creerDiscussion(utilisateurConnecte, personne, "null");
                        } else {
                            System.out.println("Une conversation avec " + personne + " existe déjà.");
                        }
                        ouvrirDiscussionAvec(personne);
                    }
                });
            }

            //conversationPanel.add(Box.createVerticalStrut(10));// nombre de pixel entre
            add(conversationPanel, BorderLayout.CENTER);

        }

    }

    class DiscussionVue extends JFrame {
        private JTextArea conversationArea;
        private JTextField messageField;
        private JButton envoyerButton;
        private int idConversation;

        public DiscussionVue(String personne1, String personne2) {
            setTitle("Discussion avec " + personne1 + " et " + personne2);
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Messagerie methode = new Messagerie();
            MessagerieDAO methode2 =new MessagerieDAO();

            conversationArea = new JTextArea();
            conversationArea.setEditable(false);
            conversationArea.setFont(new Font("Arial", Font.PLAIN, 20));
            JScrollPane conversationScrollPane = new JScrollPane(conversationArea);

            messageField = new JTextField();
            messageField.setFont(new Font("Arial", Font.PLAIN, 20));
            envoyerButton = new JButton("Envoyer");

            setLayout(new BorderLayout());
            add(conversationScrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.add(messageField, BorderLayout.CENTER);
            messagePanel.add(envoyerButton, BorderLayout.EAST);

            bottomPanel.add(messagePanel, BorderLayout.SOUTH);
            add(bottomPanel, BorderLayout.SOUTH);

            methode.creation_nom_fichier_sauvegarde_disscussion(personne1, personne2);
            String nom_fichier = methode2.getNomFichier(personne1, personne2) + ".txt";

            File fichier = new File("historique/" + nom_fichier);

            try (BufferedReader br = new BufferedReader(new FileReader("historique/" + nom_fichier))) {
                StringBuilder contenu = new StringBuilder();
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    contenu.append(ligne).append("\n");
                }
                conversationArea.setText(contenu.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            envoyerButton.setBackground(new Color(65, 222, 9));
            envoyerButton.setPreferredSize(new Dimension(100, 50));

            envoyerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        conversationArea.append("Moi: " + message + "\n");

                        try (FileWriter fw = new FileWriter("historique/" + nom_fichier, true);
                             BufferedWriter bw = new BufferedWriter(fw);
                             PrintWriter out = new PrintWriter(bw)) {
                            out.println(personne1 + " : " + message);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        messageField.setText("");
                    }
                }
            });

        }
    }
}


