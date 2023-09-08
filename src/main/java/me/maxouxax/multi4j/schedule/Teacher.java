package me.maxouxax.multi4j.schedule;

import com.google.gson.JsonObject;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Teacher {

    private final String name;
    private final String email;

    @BsonCreator
    public Teacher(@BsonProperty("name") final String name,
                   @BsonProperty("email") final String email) {
        this.name = name;
        this.email = email;
    }

    public Teacher(JsonObject jsonObject) {
        this.name = jsonObject.get("name").getAsString();
        this.email = jsonObject.get("email").getAsString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
