package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatistiqueOffres {
    private static final String DB_URL = "jdbc:mysql://localhost:8889/gestion_immobiliere";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Map<String, Integer> genererStatistiques() {
        Map<String, Integer> statistiques = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT statut, COUNT(*) AS count FROM offre GROUP BY statut";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int statut = resultSet.getInt("statut");
                        int count = resultSet.getInt("count");
                        statistiques.put(getStatutLabel(statut), count);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistiques;
    }

    private static String getStatutLabel(int statut) {
        switch (statut) {
            case 0:
                return "Refusé";
            case 1:
                return "En attente";
            case 2:
                return "Accepté";
            default:
                return "Inconnu";
        }
    }
}