package modele;
import java.util.List;
import java.util.Date;

public class Propriete {
    private String emplacement;
    private double superficie;
    private int nbPieces;
    private int nbSalleBain;
    private int nbSalleEau;
    private String description;
    private double prix;
    private String adresse;
    private List<String> photos; // Liste des chemins d'accès aux fichiers images
    private int id;
    private String vendeur;
    private String ville;
    private int statut;
    private String employe;
    private String titre;


    public Propriete(int id, String emplacement, double superficie, int nbPieces, int nbSalleBain, int nbSalleEau, String description, double prix, String adresse, List<String> photos, String vendeur, String ville, int statut, String employe, String titre) {
        this.id = id;
        this.emplacement = emplacement;
        this.superficie = superficie;
        this.nbPieces = nbPieces;
        this.nbSalleBain = nbSalleBain;
        this.nbSalleEau = nbSalleEau;
        this.description = description;
        this.prix = prix;
        this.adresse = adresse;
        this.photos = photos;
        this.vendeur = vendeur;
        this.ville = ville;
        this.statut = statut;
        this.employe = employe;
        this.titre = titre;

    }

    // Getters et setters
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public int getStatut() { return statut; }

    public void setStatut(int statut) { this.statut = statut; }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public int getNbPieces() {
        return nbPieces;
    }

    public void setNbPieces(int nbPieces) {
        this.nbPieces = nbPieces;
    }

    public int getNbSalleBain() {
        return nbSalleBain;
    }

    public void setNbSalleBain(int nbSalleBain) {
        this.nbSalleBain = nbSalleBain;
    }

    public int getNbSalleEau() {
        return nbSalleEau;
    }

    public void setNbSalleEau(int nbSalleEau) {
        this.nbSalleEau = nbSalleEau;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}