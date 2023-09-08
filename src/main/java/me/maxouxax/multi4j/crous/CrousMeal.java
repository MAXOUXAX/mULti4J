package me.maxouxax.multi4j.crous;

import com.google.gson.JsonObject;

import java.util.Arrays;

public class CrousMeal {

    private final String name;
    private final CrousFoodCategory[] foodCategories;

    public CrousMeal(String name, CrousFoodCategory[] foodCategories) {
        this.name = name;
        this.foodCategories = foodCategories;
    }

    public static CrousMeal fromJson(JsonObject meal) {
        String name = meal.get("name").getAsString();
        CrousFoodCategory[] foodCategories = new CrousFoodCategory[meal.getAsJsonArray("foodcategory").size()];
        for (int i = 0; i < meal.getAsJsonArray("foodcategory").size(); i++) {
            JsonObject foodCategory = meal.getAsJsonArray("foodcategory").get(i).getAsJsonObject();
            foodCategories[i] = CrousFoodCategory.fromJson(foodCategory);
        }
        return new CrousMeal(name, foodCategories);
    }

    public String getName() {
        return name;
    }

    public CrousFoodCategory[] getFoodCategories() {
        return foodCategories;
    }

    @Override
    public String toString() {
        return "CrousMeal{" +
                "name='" + name + '\'' +
                ", foodCategories=" + Arrays.toString(foodCategories) +
                '}';
    }

}
