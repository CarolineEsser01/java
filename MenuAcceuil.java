package vue;

import com.toedter.calendar.JDateChooser;
import controleur.Connect;
import controleur.ConnectSession;
import modele.*;
import vue.inscription.*;
import vue.inscription.ListeAnnonces;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class MenuAcceuil extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel middlePanel;


    private JButton accueilButton;
    private JButton AnnonceButton;
    private JButton ConnexionButton;
    private JButton InscriptionButton;
    public Inscription inscriptionPanel;
    public Connexion ConnexionPanel;
    public Acceuil_invite Acceuil_invitePanel;
    public ListeAnnonces ListeAnnoncesPanel;

    public ConnectSession connectSession; // Ajoutez cette ligne
    public ListeAnnonces listeannonces; // jjjjjjjjjjjjjjjjjjjjjj
    private JPanel annoncesPanel;
    private JDateChooser dateChooser;
    private int id_prop_choisi = 0;
    private String mail_perso ;

    // Récupérer les annonces depuis la base de données
    private ProprieteDAO proprieteDAO = new ProprieteDAO();

    // Récupérer les reservation de visite depuis la base de données
    private ReservationDAO reservationDAO = new ReservationDAO();
    private List<Reservation> visite = reservationDAO.recupererVisite();

    private Etat_connexion etatConnexion = Etat_connexion.getInstance();
    private JPanel secondBand;
    private MenuAcceuil thisMenuAcceuil;
    public MenuAcceuil() {
        setTitle("Accueil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 1050);
        thisMenuAcceuil = this;

        initUI();
        chargerPageAccueil();
        setVisible(true);
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();

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


        JButton connexionButton = new JButton("Connexion");
        connexionButton.setForeground(new Color(75, 0, 56));
        boutonsPanel.add(connexionButton);

        firstBand.add(Box.createHorizontalGlue());
        firstBand.add(logoLabel);
        firstBand.add(Box.createRigidArea(new Dimension(10, 0)));
        firstBand.add(phraseEtBoutonsPanel);
        firstBand.add(Box.createHorizontalGlue());

        secondBand = new JPanel();
        middlePanel = new JPanel();
        secondBand.setLayout(new BoxLayout(secondBand, BoxLayout.X_AXIS));
        secondBand.setBackground(new Color(255, 192, 203));

        accueilButton = new JButton("ACCUEIL");
        AnnonceButton = new JButton("ANNONCES");
        ConnexionButton = new JButton("CONNEXION");
        InscriptionButton = new JButton("INSCRIPTION");
        InscriptionButton.setAlignmentX(Component.LEFT_ALIGNMENT);


        accueilButton.setOpaque(false);
        accueilButton.setContentAreaFilled(false);
        accueilButton.setBorderPainted(false);
        accueilButton.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutonFont = new Font(accueilButton.getFont().getName(), Font.BOLD, 20);
        accueilButton.setFont(boutonFont);

        AnnonceButton.setOpaque(false);
        AnnonceButton.setContentAreaFilled(false);
        AnnonceButton.setBorderPainted(false);
        AnnonceButton.setForeground(new Color(253, 79, 138)); // Rose du log
        Font boutTonFont = new Font(AnnonceButton.getFont().getName(), Font.BOLD, 20);
        AnnonceButton.setFont(boutTonFont);

        ConnexionButton.setOpaque(false);
        ConnexionButton.setContentAreaFilled(false);
        ConnexionButton.setBorderPainted(false);
        ConnexionButton.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutTtonFont = new Font(ConnexionButton.getFont().getName(), Font.BOLD, 20);
        ConnexionButton.setFont(boutTtonFont);

        InscriptionButton.setOpaque(false);
        InscriptionButton.setContentAreaFilled(false);
        InscriptionButton.setBorderPainted(false);
        InscriptionButton.setForeground(new Color(253, 79, 138)); // Rose du logo
        Font boutTttonFont = new Font(InscriptionButton.getFont().getName(), Font.BOLD, 20);
        InscriptionButton.setFont(boutTttonFont);

        secondBand.add(Box.createHorizontalGlue());
        secondBand.add(accueilButton);
        secondBand.add(Box.createRigidArea(new Dimension(10, 100)));
        secondBand.add(AnnonceButton);
        secondBand.add(Box.createRigidArea(new Dimension(10, 100)));
        secondBand.add(ConnexionButton);
        secondBand.add(Box.createRigidArea(new Dimension(10, 100)));
        secondBand.add(InscriptionButton);
        secondBand.add(Box.createHorizontalGlue());

        JPanel bandsContainer = new JPanel(new BorderLayout());
        bandsContainer.add(firstBand, BorderLayout.NORTH);
        bandsContainer.add(secondBand, BorderLayout.CENTER);
        bandsContainer.add(middlePanel, BorderLayout.SOUTH);

        menuBar.add(bandsContainer);

        setJMenuBar(menuBar);

        MenuAcceuil thisMenuAcceuil = this;


        accueilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerPageAccueil();
            }
        });

        AnnonceButton.addActionListener(e -> {
            ListeAnnonces();
        });


        ConnexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll(); // Enlevez tous les composants du conteneur principal
                middlePanel.removeAll();
                ConnexionPanel = new Connexion();
                System.out.println("Bouton 'Connexion' cliqué");
                setLayout(new BorderLayout());
                middlePanel.add(ConnexionPanel, BorderLayout.CENTER);
                middlePanel.revalidate();  // Mettez à jour le conteneur principal
                middlePanel.repaint();     // Rafraîchissez l'affichage
                ConnectSession connectSession = new ConnectSession(thisMenuAcceuil);
            }
        });





        InscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middlePanel.removeAll();
                inscriptionPanel = new Inscription();
                System.out.println("Bouton 'S'inscrire' cliqué");
                if (connectSession != null) {
                    connectSession.fermerSession();
                }
                setLayout(new BorderLayout());
                getContentPane().add(inscriptionPanel, BorderLayout.CENTER);
                middlePanel.add(inscriptionPanel);
                middlePanel.revalidate();  // Ajoutez cette ligne
                middlePanel.repaint();     // Ajoutez cette ligne
                Connect co = new Connect(thisMenuAcceuil);
            }
        });

        setVisible(true);
    }

    private void chargerPageAccueil() {
        middlePanel.removeAll();
        Acceuil_invitePanel = new Acceuil_invite(thisMenuAcceuil);

        JPanel beigePanel = new JPanel();
        beigePanel.setLayout(new GridBagLayout());
        beigePanel.setBackground(new Color(245, 245, 220));

        JLabel helloBienvenueLabel = new JLabel("Acheter le bien qui me ressemble");
        helloBienvenueLabel.setFont(new Font("Serif", Font.BOLD, 36));
        helloBienvenueLabel.setForeground(new Color(75, 0, 56));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(-300, 200, 500, 100);
        beigePanel.add(helloBienvenueLabel, constraints);

        JLabel hellABienvenueLabel = new JLabel("Acheter le bien qui me ressemble");
        hellABienvenueLabel.setFont(new Font("Serif", Font.BOLD, 36));
        hellABienvenueLabel.setForeground(new Color(75, 0, 56));

        constraints.gridy = 1; // Change to the next row
        constraints.insets = new Insets(-450, 200, 50, 50);
        beigePanel.add(hellABienvenueLabel, constraints);

        JLabel holaComoEstasLabel = new JLabel("Vous avez un projet d’achat ou de vente d'immobilier dans le neuf ou dans l’ancien ?");
        holaComoEstasLabel.setFont(new Font("Serif", Font.BOLD, 24));
        holaComoEstasLabel.setForeground(new Color(75, 0, 56));

        constraints.gridy = 1; // Change to the next row
        constraints.insets = new Insets(-200, 50, 50, 50);
        beigePanel.add(holaComoEstasLabel, constraints);

        JLabel repLabel = new JLabel("CasaRosa c’est plus de 100 000 annonces d’appartements ou de maisons à votre disposition ");
        repLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        repLabel.setForeground(new Color(75, 0, 56));

        constraints.gridy = 1; // Change to the next row
        constraints.insets = new Insets(-70, 50, 50, 50);
        beigePanel.add(repLabel, constraints);

        JLabel suiteLabel = new JLabel("pour répondre à votre projet d’achat immobilier et voir enfin la vie en rose !!");
        suiteLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        suiteLabel.setForeground(new Color(75, 0, 56));

        constraints.gridy = 1; // Change to the next row
        constraints.insets = new Insets(0, 50, 50, 50);
        beigePanel.add(suiteLabel, constraints);

        // Ajoutez beigePanel à Acceuil_invitePanel
        Acceuil_invitePanel.add(beigePanel);

        middlePanel.add(Acceuil_invitePanel);
        System.out.println("Bouton 'Acceuil' cliqué");
        middlePanel.revalidate();  // Ajoutez cette ligne
        middlePanel.repaint();     // Ajoutez cette ligne
    }
    public void ListeAnnonces() {
        java.util.List<Propriete> annonces = proprieteDAO.recupererAnnonces();

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

        middlePanel.removeAll(); // Nettoyer les anciens contenus
        middlePanel.add(scrollPane); // Ajouter le JScrollPane au middlePanel

        // Adapter la taille de la fenêtre au contenu
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

        JLabel attributsLabel = new JLabel("<html><ul><li>Surface: " + propriete.getSuperficie() + "</li>"
                + "<li>Nombre de pièces: " + propriete.getNbPieces() + "</li>"
                + "<li>Emplacement: " + propriete.getEmplacement() + "</li>"
                + "<li>Description: " + propriete.getDescription() + "</li>"
                + "<li>Adresse: " + propriete.getAdresse() + "</li></ul></html>");
        infoPanel.add(attributsLabel);


        if (etatConnexion.getUtilisateurConnecteType() == 1) {
            JButton resaButton = new JButton("FAVORIS");
            resaButton.addActionListener(e -> {
                // SelectDateVisite(propriete.getId());
            });
            infoPanel.add(resaButton);
        }



        return infoPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuAcceuil::new);
    }
    private JButton messageriebutton;
}
