package modele;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavorisDAO {
    private final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere";
    private final String utilisateur = "root";
    private final String motDePasse = "";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(jdbc_driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver JDBC non trouvé", e);
        }

        Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
        if (connection != null) {
            System.out.println("Connexion à la base de données Favoris réussie !");
        } else {
            System.out.println("Échec de la connexion à la base de données Favoris.");
        }
        return connection;
    }

    public void ajouterAnnonceEnFavoris(int idPropriete, String emailUtilisateur) throws SQLException {
        String sql = "INSERT INTO favoris (id_propriete, email_utilisateur) VALUES (?, ?)";
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, idPropriete);
            preparedStatement.setString(2, emailUtilisateur);
            preparedStatement.executeUpdate();
            System.out.println("Annonce ajoutée en favoris avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout de l'annonce en favoris.", e);
        }
    }

    public void supprimerAnnonceDesFavoris(int idPropriete, String emailUtilisateur) throws SQLException {
        String sql = "DELETE FROM favoris WHERE id_propriete = ? AND email_utilisateur = ?";
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, idPropriete);
            preparedStatement.setString(2, emailUtilisateur);
            preparedStatement.executeUpdate();
            System.out.println("Annonce supprimée des favoris avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression de l'annonce des favoris.", e);
        }
    }
    public Connection obtenirConnexion() throws SQLException {
        return getConnection();
    }

    public List<Propriete> recupererAnnoncesFavorisClient(String emailClient) {
        List<Propriete> annoncesFavoris = new ArrayList<>();

        // Établir la connexion à la base de données
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // Requête SQL pour récupérer les annonces ajoutées en favoris par le client
            String sql = "SELECT p.id, p.emplacement, p.superficie, p.nb_pieces, p.nb_salle_bain, p.nb_salle_eau, " +
                    "p.description, p.prix, p.adresse, p.photos, p.vendeur, p.ville, p.statut, p.employe, p.titre " +
                    "FROM propriete p INNER JOIN favoris f ON p.id = f.id_propriete WHERE f.email_utilisateur = ?";

            // Créer la déclaration préparée avec la requête SQL
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, emailClient);

            // Exécuter la requête SQL et récupérer les résultats
            resultSet = preparedStatement.executeQuery();

            // Parcourir les résultats et ajouter les annonces à la liste
            while (resultSet.next()) {
                // Créer un objet Propriete en utilisant les valeurs récupérées de la base de données

                // Récupérer la chaîne de photos de la base de données
                String photosString = resultSet.getString("photos");
// Diviser la chaîne en utilisant la virgule comme séparateur pour obtenir une liste de chemins d'accès aux images
                List<String> photos = Arrays.asList(photosString.split(","));

                Propriete propriete = new Propriete(
                        resultSet.getInt("id"),
                        resultSet.getString("emplacement"),
                        resultSet.getDouble("superficie"),
                        resultSet.getInt("nb_pieces"), // Utilisez "nb_pieces" à la place de "nbPieces"
                        resultSet.getInt("nb_salle_bain"), // Utilisez "nb_salle_bain" à la place de "nbSalleBain"
                        resultSet.getInt("nb_salle_eau"), // Utilisez "nb_salle_eau" à la place de "nbSalleEau"
                        resultSet.getString("description"),
                        resultSet.getDouble("prix"),
                        resultSet.getString("adresse"),
                        photos,
                        resultSet.getString("vendeur"),
                        resultSet.getString("ville"),
                        resultSet.getInt("statut"),
                        resultSet.getString("employe"),
                        resultSet.getString("titre")
                );


                // Ajouter la propriete a la liste des annonces favorites
                annoncesFavoris.add(propriete);
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

        return annoncesFavoris;
    }


}