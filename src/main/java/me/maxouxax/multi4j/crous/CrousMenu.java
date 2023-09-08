package me.maxouxax.multi4j.crous;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CrousMenu {

    private final Date date;
    private final CrousMeal[] meals;

    public CrousMenu(Date date, CrousMeal[] meals) {
        this.date = date;
        this.meals = meals;
    }

    public static CrousMenu fromJson(JsonObject json) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(json.get("date").getAsString());

        JsonArray mealsArray = json.get("meal").getAsJsonArray();
        CrousMeal[] meals = new CrousMeal[mealsArray.size()];
        for (int i = 0; i < mealsArray.size(); i++) {
            JsonObject meal = mealsArray.get(i).getAsJsonObject();
            meals[i] = CrousMeal.fromJson(meal);
        }
        return new CrousMenu(date, meals);
    }

    public Date getDate() {
        return date;
    }

    public CrousMeal[] getMeals() {
        return meals;
    }

    @Override
    public String toString() {
        return "CrousMenu{" +
                "date=" + date +
                ", meals=" + Arrays.toString(meals) +
                '}';
    }

}
