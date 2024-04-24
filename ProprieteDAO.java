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


public class ProprieteDAO {
    private final String jbdc_driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere"; // 3306 pour Windows et 8889 pour Mac
    private final String utilisateur = "root";
    private final String motDePasse = "";
    private FavorisDAO favorisDAO;
    public ProprieteDAO() {
        this.favorisDAO = new FavorisDAO();
    }


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
    public void insererPropriete(Propriete propriete) {
        String sql = "INSERT INTO propriete (emplacement, superficie, nb_pieces, nb_salle_bain, nb_salle_eau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, propriete.getEmplacement());
            preparedStatement.setDouble(2, propriete.getSuperficie());
            preparedStatement.setInt(3, propriete.getNbPieces());
            preparedStatement.setInt(4, propriete.getNbSalleBain());
            preparedStatement.setInt(5, propriete.getNbSalleEau());
            preparedStatement.setString(6, propriete.getDescription());
            preparedStatement.setDouble(7, propriete.getPrix());
            preparedStatement.setString(8, propriete.getAdresse());
            // Convertir la liste de photos en une chaîne et l'insérer dans la base de données
            preparedStatement.setString(9, String.join(",", propriete.getPhotos()));
            preparedStatement.setString(10, propriete.getVendeur());
            preparedStatement.setString(11, propriete.getVille());
            preparedStatement.setInt(12, propriete.getStatut());
            preparedStatement.setString(13, propriete.getEmploye());
            preparedStatement.setString(14, propriete.getTitre());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }


    // PERMET LAFFICHAGE DES PROPRIETES
    public List<Propriete> recupererAnnonces() {
        List<Propriete> annonces = new ArrayList<>();
        String sql = "SELECT * FROM propriete";

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



                    Propriete propriete = new Propriete(id, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre);
                    annonces.add(propriete);
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

    public void supprimerPropriete(Propriete propriete) {
        String sql = "DELETE FROM propriete WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, propriete.getId());
            preparedStatement.executeUpdate();
            System.out.println("Propriété supprimée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }

    public Propriete recupererPropriete(int idProp) {
        Propriete propriete = null;
        String sql = "SELECT * FROM propriete WHERE id = ?";

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

                propriete = new Propriete(id_prop, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return propriete;
    }


    public int getDernierIdAppartement() {
        int dernierId = 0;
        String sql = "SELECT MAX(id) AS max_id FROM propriete ";
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

    public List<Propriete> recupererProprietesAcheteParClient(String emailClient) {
        List<Propriete> proprietesAcheteParClient = new ArrayList<>();
        String sql = "SELECT * FROM propriete INNER JOIN offre ON propriete.id = offre.id_propriete WHERE offre.mailClient = ? AND propriete.statut = 1 AND offre.statut = 2";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, emailClient);
            ResultSet resultSet = preparedStatement.executeQuery();
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
                List<String> photos = Arrays.asList(photosString.split(","));
                Propriete propriete = new Propriete(id, emplacement, superficie, nbPieces, nbSalleBain, nbSalleEau, description, prix, adresse, photos, vendeur, ville, statut, employe, titre);
                proprietesAcheteParClient.add(propriete);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return proprietesAcheteParClient;
    }



}