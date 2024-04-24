package modele;

import java.sql.Timestamp;

public class Offre {
    private int id_prop;
    private int statut;
    private int proposition;
    private String mailClient;
    private int contreProposition;
    private Timestamp time;

    public Offre(int id_prop, int statut, int proposition, String mailClient, int contreProposition, Timestamp time) {
        this.id_prop = id_prop;
        this.statut = statut;
        this.proposition = proposition;
        this.mailClient = mailClient;
        this.contreProposition = contreProposition;
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
    public int getId_prop() {
        return id_prop;
    }

    public void setId_Prop(int id_prop) {
        this.id_prop = id_prop;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public int getProposition() {
        return proposition;
    }

    public void setProposition(int proposition) {
        this.proposition = proposition;
    }

    public String getMailClient() {
        return mailClient;
    }

    public void setMailClient(String mailClient) {
        this.mailClient = mailClient;
    }

    public int getContreProposition() {
        return contreProposition;
    }

    public void setContreProposition(int contreProposition) {
        this.contreProposition = contreProposition;
    }
}