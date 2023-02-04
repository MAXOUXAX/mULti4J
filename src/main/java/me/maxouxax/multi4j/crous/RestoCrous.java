package me.maxouxax.multi4j.crous;

public class RestoCrous {

    String nomRu;
    String imageRu;
    String dateRu;
    String menuRu;

    public RestoCrous(String nomRu, String imageRu, String dateRu, String menuRu) {
        NomRu = nomRu;
        ImageRu = imageRu;
        DateRu = dateRu;
        MenuRu = menuRu;
    }

    public String getNomRu() {
        return NomRu;
    }

    public String getImageRu() {
        return ImageRu;
    }

    public String getDateRu() {
        return DateRu;
    }

    public String getMenuRu() {
        return MenuRu;
    }
}
