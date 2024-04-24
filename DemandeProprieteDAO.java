package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Date;


public class DemandeProprieteDAO {
    private final String jbdc_driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere"; // 3306 pour Windows et 8889 pour Mac
    private final String utilisateur = "root";
    private final String motDePasse = "";

    // Méthode pour établir la connexion à la base de données
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


    // Méthode pour insérer une propriété dans la base de données
    public void insererPropriete(DemandePropriete demandepropriete) {
        String sql = "INSERT INTO demandepropriete (emplacement, superficie, nb_pieces, nb_salle_bain, nb_salle_eau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, demandepropriete.getEmplacement());
            preparedStatement.setDouble(2, demandepropriete.getSuperficie());
            preparedStatement.setInt(3, demandepropriete.getNbPieces());
            preparedStatement.setInt(4, demandepropriete.getNbSalleBain());
            preparedStatement.setInt(5, demandepropriete.getNbSalleEau());
            preparedStatement.setString(6, demandepropriete.getDescription());
            preparedStatement.setDouble(7, demandepropriete.getPrix());
            preparedStatement.setString(8, demandepropriete.getAdresse());
            // Convertir la liste de photos en une chaîne et l'insérer dans la base de données
            preparedStatement.setString(9, String.join(",", demandepropriete.getPhotos()));
            preparedStatement.setString(10, demandepropriete.getVendeur());
            preparedStatement.setString(11, demandepropriete.getVille());
            preparedStatement.setInt(12, demandepropriete.getStatut());
            preparedStatement.setString(13, demandepropriete.getEmploye());
            preparedStatement.setString(14, demandepropriete.getTitre());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }


    // PERMET LAFFICHAGE DES PROPRIETES
    public List<DemandePropriete> recupererAnnonces() {
        List<DemandePropriete> annonces = new ArrayList<>();
        String sql = "SELECT * FROM demandepropriete";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (preparedStatement != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String emplacement = resultSet.getString("emplacement");
                    double superficie = resultSet.getDouble("superficie");
                    int nbPieces = resultSet.getInt("nb_pieces");
                    int nbSalleBain = resultSet.getInt("nb_salle_bain");
                    int nbSalleEau = resultSet.getInt("nb_salle_eau");
                    String description = resultSet.getString("description");
                    double prix = resultSet.getDouble("prix");
                    String adresse = resultSet.getString("adresse");
                    String vendeur = resultSet.getString("vendeur");
                    String ville = resultSet.getString("ville");
                    int statut = resultSet.getInt("statut");
                    String employe = resultSet.getString("employe");
                    String titre = resultSet.getString("titre");

                    // Récupérer les chemins d'accès des images
                    String photosString = resultSet.getString("photos");
                    List<String> photos = photosString != null ? Arrays.asList(photosString.split(",")) : new ArrayList<>();



                    DemandePropriete demandepropriete = new DemandePropriete(id, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre);
                    annonces.add(demandepropriete);
                }

            } else {
                System.out.println("Le PreparedStatement est null. La requête n'a pas été initialisée correctement.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return annonces;
    }

    public void supprimerPropriete(DemandePropriete demandepropriete) {
        String sql = "DELETE FROM demandepropriete WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, demandepropriete.getId());
            preparedStatement.executeUpdate();
            System.out.println("Propriété supprimée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }

    public DemandePropriete recupererPropriete(int idProp) {
        DemandePropriete demandepropriete = null;
        String sql = "SELECT * FROM demandepropriete WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, idProp);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id_prop = resultSet.getInt("id");
                String emplacement = resultSet.getString("emplacement");
                double superficie = resultSet.getDouble("superficie");
                int nbPieces = resultSet.getInt("nb_pieces");
                int nbSalleBain = resultSet.getInt("nb_salle_bain");
                int nbSalleEau = resultSet.getInt("nb_salle_eau");
                String description = resultSet.getString("description");
                double prix = resultSet.getDouble("prix");
                String adresse = resultSet.getString("adresse");
                // Assumez que la colonne photos est une chaîne séparée par des virgules que vous devez diviser en une liste
                String photosString = resultSet.getString("photos");
                List<String> photos = Arrays.asList(photosString.split(","));
                String vendeur = resultSet.getString("vendeur");
                String ville = resultSet.getString("ville");
                int statut = resultSet.getInt("statut");
                String employe = resultSet.getString("employe");
                String titre = resultSet.getString("titre");

                demandepropriete = new DemandePropriete(id_prop, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return demandepropriete;
    }


    public int getDernierIdAppartement() {
        int dernierId = 0;
        String sql = "SELECT MAX(id) AS max_id FROM demandepropriete ";
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) {
                dernierId = resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return dernierId;
    }
}