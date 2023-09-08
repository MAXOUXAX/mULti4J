package me.maxouxax.multi4j.crous;

import com.google.gson.JsonObject;

import java.util.Arrays;

public class CrousFoodCategory {

    private final String name;
    private final String[] dishes;

    public CrousFoodCategory(String name, String[] dishes) {
        this.name = name;
        this.dishes = dishes;
    }

    public static CrousFoodCategory fromJson(JsonObject foodCategory) {
        String name = foodCategory.get("name").getAsString();
        String[] dishes = new String[foodCategory.getAsJsonArray("dishes").size()];
        for (int i = 0; i < foodCategory.getAsJsonArray("dishes").size(); i++) {
            JsonObject dish = foodCategory.getAsJsonArray("dishes").get(i).getAsJsonObject();
            dishes[i] = dish.get("name").getAsString();
        }
        return new CrousFoodCategory(name, dishes);
    }

    public String getName() {
        return name;
    }

    public String[] getDishes() {
        return dishes;
    }

    @Override
    public String toString() {
        return "CrousFoodCategory{" +
                "name='" + name + '\'' +
                ", dishes=" + Arrays.toString(dishes) +
                '}';
    }

}
