package modele;

//ICI ON DEFINIT CE QUI COMPOSE UN UTILISATEUR
public class Utilisateur {
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private int type;

    public Utilisateur(String nom, String prenom, String email, String mdp, int type) {//CONSTRUCTEUR
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.type = type;
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {

        this.prenom = prenom;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}