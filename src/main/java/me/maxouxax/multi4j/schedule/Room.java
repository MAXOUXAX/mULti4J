package me.maxouxax.multi4j.schedule;

import me.maxouxax.multi4j.LabelledType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

public class Room extends LabelledType {

    @BsonCreator
    public Room(@BsonProperty("label") final String label) {
        super(label);
    }

    public Room(JSONObject jsonObject) {
        super(jsonObject.getString("label"));
    }

}
