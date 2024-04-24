package modele;

import java.sql.*;// BIBLI POUR CONNEXION SQL
import java.util.regex.*;//analyse syntaxe, chaine de caractere ectt (sert pour l'email)
import java.util.ArrayList;
import java.util.List;
public class UtilisateurDAO {
    //INFOS POUR SE CONNECTER A LA BASE DE DONNEE
    private final String jdbc_driver = "com.mysql.cj.jdbc.Driver";//JDBC est l'implentation qui nous permet de comminquer via java a notre bdd
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere";
    private final String utilisateur = "root";
    private final String motDePasse = "";

    public UtilisateurDAO() {
        try {
            Class.forName(jdbc_driver);//on charge JDBC
        } catch (ClassNotFoundException e) {//si pas trouvable
            e.printStackTrace();//nous renvoie infos manquante
        }
    }

    public boolean Champs_remplis(Utilisateur utilisateur) {
        return !utilisateur.getNom().isEmpty() && !utilisateur.getPrenom().isEmpty() && !utilisateur.getEmail().isEmpty() && !utilisateur.getMdp().isEmpty();
    }
    public boolean Champs_remplis_co(Utilisateur utilisateur) {

        return utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty() && utilisateur.getMdp() != null && !utilisateur.getMdp().isEmpty();
    }


    public boolean formatMail(String email) {
        // FORMAT EMAIL = EXPLICATION (on est en chaine de caractere)

        //^: DEBUT
        //[a-zA-Z0-9_+&*-]+: SEQUENCE AVEC AU MOINS CARACTERE ALPHABETIQUE , CHIFFRE OU CARACTERERE SPECIAL
        //@: SYMBOLE @ OBLIGATOIRE
        //(?:[a-zA-Z0-9-]+\\.)+: POUR LE GMAIL ECT SUIVIE D'UN POINT OU CONTENANT - ECT
        //[a-zA-Z]{2,7}: SEQUENCE DE 2 A 7 CARACTERE POUR LE FR OU COM ECT...
        //$: FIN

        String format = "^[a-zA-Z0-9_+&*-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern modele= Pattern.compile(format);
        Matcher match = modele.matcher(email);//on verifie si on correspond a nos attentes de format email
        return match.matches();
    }

    public boolean mailUnique(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";//on parcourt nos email de la bdd
        try (
                Connection connection = DriverManager.getConnection(url, this.utilisateur, motDePasse);
                PreparedStatement commande = connection.prepareStatement(sql);// methode securisé de preparation pour effectuer une commande sql sur element (?)
        ) {
            commande.setString(1, email);
            ResultSet resultSet = commande.executeQuery();
            if (resultSet.next()) {//comparaison
                int count = resultSet.getInt(1);
                return count > 0;// si ca correspond
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void insererUtilisateur(Utilisateur utilisateur) {
        //#1: BLINDAGE

        //VERIFICATION #1: Touts les champs du formulaire doivent etre bien remplient
        if (!Champs_remplis(utilisateur)) {
            System.out.println("REMPLISSEZ TOUTS LES CHAMPS");
            return;
        }

        //VERIFICATION #1: Touts les champs du formulaire doivent etre bien remplient
        if (!formatMail(utilisateur.getEmail())) {
            System.out.println("FORMAT EMAIL NON VALIDE");
            return;
        }

        if (mailUnique(utilisateur.getEmail())) {
            System.out.println("ADRESSE MAIL DEJA PRISE");
            return;
        }

        //#2 : on insere l'utilisateur dans la base de donnée

        String sql = "INSERT INTO utilisateur (nom, prenom, email, mdp, type) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, this.utilisateur, motDePasse);
                PreparedStatement commande = connection.prepareStatement(sql);// methode securisé de preparation pour effectuer une commande sql et inserer directement element (?)
        ) {
            commande.setString(1, utilisateur.getNom());//utilisateur.getNom()= se refere a notre constructeur
            commande.setString(2, utilisateur.getPrenom());
            commande.setString(3, utilisateur.getEmail());
            commande.setString(4, utilisateur.getMdp());
            commande.setInt(5, utilisateur.getType());

            int nb = commande.executeUpdate();//on cheque le nb de tour effectue
            //CHECKING DE LA REUSSITE D'INSERTION
            if (nb > 0) {
                System.out.println("Insertion de l'utilisateur dans la base de donnee : REUSSI");
            } else {
                System.out.println("Insertion de l'utilisateur dans la base de donnee : ECHEC");
            }
        } catch (SQLException e) {//pour exception SQL
            e.printStackTrace();
        }
    }
    public boolean verifierIdentifiants(String email, String motDePasse) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ? AND mdp = ?";
        try (
                Connection connection = DriverManager.getConnection(url, this.utilisateur, this.motDePasse);
                PreparedStatement commande = connection.prepareStatement(sql);
        ) {
            commande.setString(1, email);
            commande.setString(2, motDePasse);
            ResultSet resultSet = commande.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return true; // L'utilisateur est trouvé dans la base de données
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // L'utilisateur n'est pas trouvé dans la base de données ou une erreur s'est produite
    }
    public List<String> obtenirClients() {
        List<String> clients = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT prenom, nom FROM utilisateur WHERE type = 1"; // 1 pour client
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                clients.add(prenom + " " + nom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    }

    public List<String> obtenirVendeurs() {
        List<String> vendeurs = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT prenom, nom FROM utilisateur WHERE type = 3"; // 2 pour vendeur
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                vendeurs.add(prenom + " " + nom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return vendeurs;
    }

    public List<String> obtenirEmployes() {
        List<String> employes = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT prenom, nom FROM utilisateur WHERE type = 2"; // 3 pour employé
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                employes.add(prenom + " " + nom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employes;
    }
    public Utilisateur getUtilisateurParEmail(String email) {
        Utilisateur utilisateur = null;
        String sql = "SELECT nom, prenom, type FROM utilisateur WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, this.utilisateur, motDePasse);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                int type = resultSet.getInt("type");
                utilisateur = new Utilisateur(nom, prenom, email, null, type); // Note: Utilisateur avec email et mot de passe null
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return utilisateur;
    }

    public List<String> obtenirClientsSam() {
        List<String> clients = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT * FROM utilisateur WHERE type = 1"; // 1 pour client
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                String mail = resultSet.getString("email");
                clients.add(prenom + " " + nom + " " + mail);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    }

    public List<String> obtenirVendeursSam() {
        List<String> vendeurs = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT * FROM utilisateur WHERE type = 3";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                String mail = resultSet.getString("email");
                vendeurs.add(prenom + " " + nom + " " + mail);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return vendeurs;
    }

    public List<String> obtenirEmployesSam() {
        List<String> employes = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {
            String sql = "SELECT * FROM utilisateur WHERE type = 2";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                String mail = resultSet.getString("email");
                employes.add(prenom + " " + nom + " " + mail);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employes;
    }





}
