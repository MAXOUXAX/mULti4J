package me.maxouxax.multi4j.schedule;

import com.google.gson.JsonObject;
import me.maxouxax.multi4j.LabelledType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Group extends LabelledType {

    @BsonCreator
    public Group(@BsonProperty("label") final String label) {
        super(label);
    }

    public Group(JsonObject jsonObject) {
        super(jsonObject.get("label").getAsString());
    }

}
