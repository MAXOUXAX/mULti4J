package me.maxouxax.multi4j.schedule;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.maxouxax.multi4j.LabelledType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Course extends LabelledType {

    private final String id;
    private final String color;
    private final String url;
    private final String type;

    @BsonCreator
    public Course(@BsonProperty("id") final String id,
                  @BsonProperty("label") final String label,
                  @BsonProperty("color") final String color,
                  @BsonProperty("url") final String url,
                  @BsonProperty("type") final String type) {
        super(label);
        this.id = id;
        this.color = color;
        this.url = url;
        this.type = type;
    }

    public Course(JsonObject course) {
        super(course.get("label").getAsString());

        String id = course.get("id").getAsString();
        String color = course.get("color").getAsString();
        String type = course.get("type").getAsString();

        JsonElement courseUrl = course.get("url");
        String url = null;
        if(courseUrl != null && !courseUrl.isJsonNull()) url = courseUrl.getAsString();

        this.id = id;
        this.color = color;
        this.url = url;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

}
