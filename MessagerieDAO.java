package modele;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

public class MessagerieDAO {
    private final String jbdc_driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere";
    private final String utilisateur = "root";
    private final String motDePasse = "";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(jbdc_driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver JDBC non trouvé", e);
        }

        Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
        if (connection != null) {
            System.out.println("Connexion à la base de données réussie.");
        } else {
            System.out.println("Échec de la connexion à la base de données.");
        }
        return connection;
    }

    public void creerDiscussion(String personne1, String personne2, String message) {
        String sql =  "INSERT INTO messagerie (perso1,perso2,message)VALUES(?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, personne1);
            preparedStatement.setString(2, personne2);
            preparedStatement.setString(3, message);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nouvelle discussion créée avec succès.");
            } else {
                System.out.println("Échec de la création de la discussion.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getIdConversation(String personne1, String personne2) {
        int idConversation = -1; // Initialisation avec une valeur par défaut

        String sql = "SELECT conversation FROM messagerie WHERE (perso1 = ? AND perso2 = ?) OR (perso1 = ? AND perso2 = ?)";
        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, personne1);
            preparedStatement.setString(2, personne2);
            preparedStatement.setString(3, personne2);
            preparedStatement.setString(4, personne1);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idConversation = resultSet.getInt("conversation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(idConversation);

        return idConversation;
    }


    public List<String> getPersonnesConversations(String utilisateur) {
        List<String> personnes = new ArrayList<>();
        String sql = "SELECT DISTINCT perso1, perso2 FROM messagerie WHERE perso1 = ? OR perso2 = ?";
        try (Connection connection = DriverManager.getConnection(url, this.utilisateur, motDePasse);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utilisateur);
            statement.setString(2, utilisateur);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String perso1 = resultSet.getString("perso1");
                String perso2 = resultSet.getString("perso2");
                // Ajouter les personnes à la liste si elles ne sont pas l'utilisateur lui-même
                if (!perso1.equals(utilisateur)) {
                    personnes.add(perso1);
                }
                if (!perso2.equals(utilisateur)) {
                    personnes.add(perso2);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return personnes;
    }


    //ENREGISTRE LE NOM STRING PUIS A PARTIR DE DOSSIER HISTORIQUE OUVRIR LE FICHIER CORRESPONDANT AU NO ENREGSITRE
    public void enregistrerNomFichier(int idConversation, String nomFichier) {
        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE messagerie SET message = ? WHERE Conversation = ?")) {

            // Définition des valeurs des paramètres dans la requête SQL
            preparedStatement.setString(1, nomFichier);
            preparedStatement.setInt(2, idConversation);

            // Exécution de la requête
            preparedStatement.executeUpdate();

            System.out.println("Nom du fichier enregistré avec succès dans la base de données pour la conversation : " + idConversation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean verif_existance_Message(int idConversation) {
        String sql = "SELECT message FROM messagerie WHERE conversation = ?";
        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idConversation);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String message = resultSet.getString("message");
                return message != null; // Retourne false si le message est null, true sinon
            } else {
                return false; // Retourne false si aucune conversation n'est trouvée
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, retourne false
        }
    }


    public String getNomFichier(String personne1, String personne2) {
        String nomFichier = null;
        int idConversation= getIdConversation(personne1,personne2);
        String sql = "SELECT message FROM messagerie WHERE conversation = ?";

        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idConversation);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nomFichier = resultSet.getString("message");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nomFichier;
    }

    public int checkConversationExistence(String personne1, String personne2) {
        int existence = 0;
        String sql = "SELECT COUNT(*) AS count FROM messagerie WHERE (perso1 = ? AND perso2 = ?) OR (perso1 = ? AND perso2 = ?)";
        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, personne1);
            preparedStatement.setString(2, personne2);
            preparedStatement.setString(3, personne2);
            preparedStatement.setString(4, personne1);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    existence = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existence;
    }


}

