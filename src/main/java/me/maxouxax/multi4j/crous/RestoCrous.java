package me.maxouxax.multi4j.crous;

public class RestoCrous {

    String nomRu;
    String imageRu;
    String dateRu;
    String menuRu;

    public RestoCrous(String nomRu, String imageRu, String dateRu, String menuRu) {
        this.nomRu = nomRu;
        this.imageRu = imageRu;
        this.dateRu = dateRu;
        this.menuRu = menuRu;
    }

    public String getNomRu() {
        return this.nomRu;
    }

    public String getImageRu() {
        return this.imageRu;
    }

    public String getDateRu() {
        return this.dateRu;
    }

    public String getMenuRu() {
        return this.menuRu;
    }
}
