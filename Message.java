package modele;

import java.util.ArrayList;
import java.util.List;

// Classe pour représenter un message
class Message {
    private String perso1;
    private String perso2;
    private String contenu;

    public Message(String expediteur, String destinataire, String contenu) {
        this.perso1 = expediteur;
        this.perso2 = destinataire;
        this.contenu = contenu;
    }

    // Getters et Setters
    public String getperso1() {
        return perso1;
    }

    public void setperso1(String perso1) {
        this.perso1 = perso1;
    }

    public String getperso2() {
        return perso2;
    }

    public void setperso2(String perso2) {
        this.perso2 = perso2;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


}
