package modele;

import java.sql.Timestamp;

public class Favoris {
    private int id_prop;
    private String emailUtilisateur;

    public Favoris(int id_prop, String emailUtilisateur) {
        this.id_prop = id_prop;
        this.emailUtilisateur = emailUtilisateur;
    }

    public int getId_prop() {
        return id_prop;
    }

    public void setId_prop(int id_prop) {
        this.id_prop = id_prop;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }
}