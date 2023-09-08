package me.maxouxax.multi4j.crous;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CrousRestaurant {

    private final String name;
    private final String thumbnailUrl;
    private final String imageUrl;
    private final String description;
    private final double latitude, longitude;
    private final List<CrousMenu> menus;

    public CrousRestaurant(String name, String thumbnailUrl, String imageUrl, String description, double latitude, double longitude, List<CrousMenu> menus) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menus = menus;
    }

    public static CrousRestaurant fromJson(JsonObject json) throws ParseException {
        String name = json.get("title").getAsString();
        String thumbnailUrl = json.get("thumbnail_url").getAsString();
        String imageUrl = json.get("image_url").getAsString();
        String description = json.get("short_desc").getAsString();
        double latitude = json.get("lat").getAsDouble();
        double longitude = json.get("lon").getAsDouble();
        List<CrousMenu> menus = new ArrayList<>();
        JsonArray menusArray = json.getAsJsonArray("menus");
        for (Object o : menusArray) {
            CrousMenu menu = CrousMenu.fromJson((JsonObject) o);
            menus.add(menu);
        }
        return new CrousRestaurant(name, thumbnailUrl, imageUrl, description, latitude, longitude, menus);
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<CrousMenu> getMenus() {
        return menus;
    }

    @Override
    public String toString() {
        return "CrousRestaurant{" +
                "name='" + name + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", menus=" + menus +
                '}';
    }

}
