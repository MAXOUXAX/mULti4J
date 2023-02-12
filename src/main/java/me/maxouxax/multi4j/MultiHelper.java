package me.maxouxax.multi4j;

import me.maxouxax.multi4j.crous.RestoCrous;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import me.maxouxax.multi4j.schedule.UnivClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operationName", "timetable");
        jsonObject.put("variables", new JSONObject()
                .put("uid", username)
                .put("from", from.toInstant().toEpochMilli())
                .put("to", to.toInstant().toEpochMilli()));
        jsonObject.put("query", "query timetable($uid: String!, $from: Float, $to: Float) {\n  timetable(uid: $uid, from: $from, to: $to) {\n    id\n    messages {\n      text\n      level\n      __typename\n    }\n    plannings {\n      id\n      type\n      label\n      default\n      messages {\n        text\n        level\n        __typename\n      }\n      events {\n        id\n        startDateTime\n        endDateTime\n        day\n        duration\n        urls\n        course {\n          id\n          label\n          color\n          url\n          type\n          __typename\n        }\n        teachers {\n          name\n          email\n          __typename\n        }\n        rooms {\n          label\n          __typename\n        }\n        groups {\n          label\n          __typename\n        }\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n");

        ArrayList<UnivClass> univClasses = new ArrayList<>();

        try {
            JSONObject response = multiClient.makeGQLRequest(jsonObject);
            JSONObject userPlanning = response.getJSONObject("data").getJSONObject("timetable").getJSONArray("plannings").getJSONObject(0);
            userPlanning.getJSONArray("events").forEach(event -> {
                UnivClass univClass = new UnivClass((JSONObject) event);
                univClasses.add(univClass);
            });
        } catch (IOException | URISyntaxException | InterruptedException | JSONException e) {
            e.printStackTrace();
            throw e;
        }

        return univClasses;
    }

    public static RestoCrous getMenuRu(MultiClient mc, String nomRu) throws MultiLoginException, IOException, URISyntaxException, InterruptedException {
        mc.connect();

        JSONObject jsonObject = new JSONObject("{\"operationName\":\"crous\",\"variables\":{},\"query\":\"query crous {\\n  restos {\\n    title\\n    thumbnail_url\\n    image_url\\n    short_desc\\n    lat\\n    lon\\n    menus {\\n      date\\n      meal {\\n        name\\n        foodcategory {\\n          name\\n          dishes {\\n            name\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}");
        JSONObject response = mc.makeGQLRequest(jsonObject);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String date = s.format(today);
        int findCrous = 0;

        //Chercher Crous
        JSONArray restosArray = response.getJSONObject("data").getJSONArray("restos");
        for (int i = 0; i < restosArray.length(); i++) {
            if (restosArray.getJSONObject(i).getString("title").equals(nomRu)) {
                findCrous = i;
                break;
            }
        }

        JSONObject restosObject = response.getJSONObject("data").getJSONArray("restos").getJSONObject(findCrous);
        String titleRu = restosObject.get("title").toString();
        String imageRu = restosObject.get("thumbnail_url").toString();
        JSONArray menuArray = restosObject.getJSONArray("menus");
        int findDate = 0;
        String dateRu= "";
        
        //Chercher today
        for (int i = 0; i < menuArray.length(); i++) {
            if (menuArray.getJSONObject(i).getString("date").equals(date)) {
                dateRu = menuArray.getJSONObject(i).getString("date");
                findDate = i;
                break;
            }
        }
        
        JSONObject menuObject = restosObject.getJSONArray("menus").getJSONObject(findDate);

        //Recuperer Repas
        JSONObject mealObject = menuObject.getJSONArray("meal").getJSONObject(0);
        JSONObject foodCategoryObject = mealObject.getJSONArray("foodcategory").getJSONObject(0);
        JSONArray dishesArray = foodCategoryObject.getJSONArray("dishes");
        String str = "";
        for (int i = 0; i < dishesArray.length(); i++) {
            String temp = dishesArray.getJSONObject(i).getString("name");
            str += temp+" - ";
        }
        String menuRu = str.substring(0, str.length()-2);
        
        return (new RestoCrous(titleRu, imageRu, dateRu, menuRu));
    }

}
