package modele;


import java.util.Date;
import java.sql.Timestamp;

public class Reservation {
    private int id_prop;
    private int statut;
    private Timestamp dispo;
    private String mailClient; // Nouvelle variable mailClient

    public Reservation(int id_prop, int statut, Timestamp dispo, String mailClient) { // Nouveau constructeur prenant en compte mailClient
        this.id_prop = id_prop;
        this.statut = statut;
        this.dispo = dispo;
        this.mailClient = mailClient;
    }

    public int getId_prop() { return id_prop; }

    public void setId_prop(int id_prop) { this.id_prop = id_prop; }

    public int getStatut() { return statut; }

    public void setStatut(int statut) {  this.statut = statut; }

    public Timestamp getDispo() { return dispo; }

    public void setDispo(Timestamp dispo) { this.dispo = dispo; }

    // Getter et setter pour mailClient
    public String getMailClient() { return mailClient; }

    public void setMailClient(String mailClient) { this.mailClient = mailClient; }
}