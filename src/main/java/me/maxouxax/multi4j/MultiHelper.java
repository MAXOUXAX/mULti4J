package me.maxouxax.multi4j;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.multi4j.schedule.UnivClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class MultiHelper {

    /**
     * Get the schedule for the logged-in user between two dates
     *
     * @param multiClient The MultiClient instance
     * @param from        The start date
     * @param to          The end date
     * @return The schedule of the user between the two dates
     * @throws IOException          If an error occurs while sending the request
     * @throws URISyntaxException   If the URI is not valid
     * @throws InterruptedException If the thread is interrupted
     */
    public static ArrayList<UnivClass> getUnivSchedule(MultiClient multiClient, ZonedDateTime from, ZonedDateTime to) throws IOException, URISyntaxException, InterruptedException {
        return getUnivScheduleForUser(multiClient, multiClient.getMultiConfig().getUsername(), from, to);
    }

    /**
     * Get the schedule for a specific user between two dates
     *
     * @param multiClient The MultiClient instance
     * @param username    The username of the user you want to get the schedule from
     * @param from        The start date
     * @param to          The end date
     * @return The schedule of the user between the two dates
     * @throws IOException          If an error occurs while sending the request
     * @throws URISyntaxException   If the URI is not valid
     * @throws InterruptedException If the thread is interrupted
     */
    public static ArrayList<UnivClass> getUnivScheduleForUser(MultiClient multiClient, String username, ZonedDateTime from, ZonedDateTime to) throws IOException, URISyntaxException, InterruptedException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operationName", "timetable");
        JsonObject variables = new JsonObject();
        variables.addProperty("uid", username);
        variables.addProperty("from", from.toInstant().toEpochMilli());
        variables.addProperty("to", to.toInstant().toEpochMilli());
        jsonObject.add("variables", variables);
        jsonObject.addProperty("query", "query timetable($uid: String!, $from: Float, $to: Float) {\n  timetable(uid: $uid, from: $from, to: $to) {\n    id\n    messages {\n      text\n      level\n      __typename\n    }\n    plannings {\n      id\n      type\n      label\n      default\n      messages {\n        text\n        level\n        __typename\n      }\n      events {\n        id\n        startDateTime\n        endDateTime\n        day\n        duration\n        urls\n        course {\n          id\n          label\n          color\n          url\n          type\n          __typename\n        }\n        teachers {\n          name\n          email\n          __typename\n        }\n        rooms {\n          label\n          __typename\n        }\n        groups {\n          label\n          __typename\n        }\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n");

        ArrayList<UnivClass> univClasses = new ArrayList<>();

        try {
            JsonObject response = multiClient.makeGQLRequest(jsonObject);
            JsonObject userPlanning = response.get("data").getAsJsonObject().get("timetable").getAsJsonObject().get("plannings").getAsJsonArray().get(0).getAsJsonObject();
            userPlanning.get("events").getAsJsonArray().forEach(event -> {
                UnivClass univClass = new UnivClass((JsonObject) event);
                univClasses.add(univClass);
            });
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            throw e;
        }

        return univClasses;
    }

    /**
     * Gets details about a CROUS restaurant
     *
     * @param multiClient    The MultiClient instance
     * @param restaurantName The name of the restaurant
     * @return The restaurant details
     * @throws IOException          If an error occurs while sending the request
     * @throws URISyntaxException   If the URI is not valid
     * @throws InterruptedException If the thread is interrupted
     * @throws ParseException       If the parsed date is not valid
     */
    public static CrousRestaurant getRestaurantDetails(MultiClient multiClient, String restaurantName) throws IOException, URISyntaxException, InterruptedException, ParseException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson("{\"operationName\":\"crous\",\"variables\":{},\"query\":\"query crous {\\n  restos {\\n    title\\n    thumbnail_url\\n    image_url\\n    short_desc\\n    lat\\n    lon\\n    menus {\\n      date\\n      meal {\\n        name\\n        foodcategory {\\n          name\\n          dishes {\\n            name\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}", JsonObject.class);
        JsonObject response = multiClient.makeGQLRequest(jsonObject);

        JsonArray restosArray = response.get("data").getAsJsonObject().get("restos").getAsJsonArray();
        JsonObject o = restosArray.asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .filter(resto -> resto.get("title").getAsString().equalsIgnoreCase(restaurantName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        return CrousRestaurant.fromJson(o);
    }

}
