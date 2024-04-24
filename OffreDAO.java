package modele;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


public class OffreDAO {

    private final String jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/gestion_immobiliere";
    private final String utilisateur = "root";
    private final String motDePasse = "";

    // Méthode pour établir la connexion à la base de données
    private Connection getConnection() throws SQLException {
        try {
            Class.forName(jdbc_driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver JDBC non trouvé", e);
        }

        Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);
        if (connection != null) {
            System.out.println("Connexion à la base de données réussie  Offre!");
        } else {
            System.out.println("Échec de la connexion à la base de données Offre.");
        }
        return connection;
    }

    // Méthode pour insérer une offre dans la base de données
    public void insererOffre(Offre offre) {
        String sql = "INSERT INTO offre (id_prop, statut, proposition, mailClient, contreProposition, time) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, offre.getId_prop());
            preparedStatement.setInt(2, offre.getStatut());
            preparedStatement.setInt(3, offre.getProposition());
            preparedStatement.setString(4, offre.getMailClient());
            preparedStatement.setInt(5, offre.getContreProposition());
            preparedStatement.setTimestamp(6, offre.getTime());

            preparedStatement.executeUpdate();
            System.out.println("Offre insérée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

    }

    // Méthode pour supprimer une offre de la base de données
    public void supprimerOffre(Offre offre) {
        String sql = "DELETE FROM offre WHERE id_prop = ? AND statut = ? AND proposition = ? AND mailClient = ? AND contreProposition = ? AND time = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, offre.getId_prop());
            preparedStatement.setInt(2, offre.getStatut());
            preparedStatement.setInt(3, offre.getProposition());
            preparedStatement.setString(4, offre.getMailClient());
            preparedStatement.setInt(5, offre.getContreProposition());
            preparedStatement.setTimestamp(6, offre.getTime());


            preparedStatement.executeUpdate();
            System.out.println("Offre supprimée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }

    // Méthode pour récupérer toutes les offres de la base de données
    public List<Offre> recupererOffres() {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT * FROM offre";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                int id_prop = resultSet.getInt("id_prop");
                int statut = resultSet.getInt("statut");
                int proposition = resultSet.getInt("proposition");
                String mailClient = resultSet.getString("mailClient");
                int contreProposition = resultSet.getInt("contreProposition");
                Timestamp time = resultSet.getTimestamp("time");

                Offre offre = new Offre(id_prop, statut, proposition, mailClient, contreProposition, time);
                offres.add(offre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return offres;
    }

    public List<Offre> recupererOffresParPropriete(int idprop) {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT * FROM offre WHERE id_prop = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            // Liaison du paramètre idprop à la requête SQL
            preparedStatement.setInt(1, idprop);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id_prop = resultSet.getInt("id_prop");
                    int statut = resultSet.getInt("statut");
                    int proposition = resultSet.getInt("proposition");
                    String mailClient = resultSet.getString("mailClient");
                    int contreProposition = resultSet.getInt("contreProposition");
                    Timestamp time = resultSet.getTimestamp("time");

                    Offre offre = new Offre(id_prop, statut, proposition, mailClient, contreProposition, time);
                    offres.add(offre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return offres;
    }


    public Offre recupererOffresParProprieteUn(int idprop) {
        Offre offre = null; // Initialisez l'offre à null

        String sql = "SELECT * FROM offre WHERE id_prop = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            // Liaison du paramètre idprop à la requête SQL
            preparedStatement.setInt(1, idprop);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id_prop = resultSet.getInt("id_prop");
                    int statut = resultSet.getInt("statut");
                    int proposition = resultSet.getInt("proposition");
                    String mailClient = resultSet.getString("mailClient");
                    int contreProposition = resultSet.getInt("contreProposition");
                    Timestamp time = resultSet.getTimestamp("time");

                    // Création de l'objet Offre
                    offre = new Offre(id_prop, statut, proposition, mailClient, contreProposition, time);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return offre; // Retourne l'offre
    }


}