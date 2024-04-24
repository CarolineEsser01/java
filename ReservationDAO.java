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

import java.sql.Timestamp;

public class ReservationDAO {

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
            System.out.println("Connexion à la base de données réussie Resa.");
        } else {
            System.out.println("Échec de la connexion à la base de données Resa.");
        }
        return connection;
    }


    // Méthode pour insérer une propriété dans la base de données
    // Méthode pour insérer une propriété dans la base de données
    public void insererReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_prop, statut, dispo,mailClient) " + "VALUES (?, ?, ?,?)";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, reservation.getId_prop());
            preparedStatement.setInt(2, reservation.getStatut());
            preparedStatement.setTimestamp(3, new Timestamp(reservation.getDispo().getTime()));
            preparedStatement.setString(4,reservation.getMailClient());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }

    public void suppReservation(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id_prop = ? AND statut = ? AND dispo = ? AND mailClient = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, reservation.getId_prop());
            preparedStatement.setInt(2, reservation.getStatut());
            preparedStatement.setTimestamp(3, new Timestamp(reservation.getDispo().getTime()));
            preparedStatement.setString(4, reservation.getMailClient());

            preparedStatement.executeUpdate();
            System.out.println("La réservation a été supprimée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
    }


    public List<Reservation> recupererVisite() {
        List<Reservation> visite = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (preparedStatement != null) {
                while (resultSet.next()) {
                    int id_prop = resultSet.getInt("id_prop");
                    Timestamp  dispo = resultSet.getTimestamp("dispo");
                    int statut = resultSet.getInt("statut");
                    String mailClient = resultSet.getString("mailClient");

                    System.out.println("resa récupérée :");
                    System.out.println("id_prop : " + id_prop);
                    System.out.println("dispo : " + dispo);
                    System.out.println("statut : " + statut);
                    System.out.println("mailClient : " + mailClient);


                    Reservation reservation = new Reservation(id_prop,statut, dispo, mailClient);
                    visite.add(reservation);
                }

            } else {
                System.out.println("Le PreparedStatement est null. La requête n'a pas été initialisée correctement.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }
        return visite;
    }


    public Reservation recupererVisiteParId(int id) {
        Reservation reservation = null;
        String sql = "SELECT * FROM reservation WHERE id_prop = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id_prop = resultSet.getInt("id_prop");
                Timestamp dispo = resultSet.getTimestamp("dispo");
                int statut = resultSet.getInt("statut");
                String mailClient = resultSet.getString("mailClient");

                reservation = new Reservation(id_prop, statut, dispo, mailClient);
            } else {
                System.out.println("Aucune réservation trouvée avec l'id : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de la base de données
        }

        return reservation;
    }


}