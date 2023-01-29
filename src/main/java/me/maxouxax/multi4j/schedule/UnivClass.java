package me.maxouxax.multi4j.schedule;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UnivClass {

    private final String id;

    @BsonProperty("start_date_time")
    private final String startDateTime;

    @BsonProperty("end_date_time")
    private final String endDateTime;

    private final long day;

    private final int duration;
    private final List<String> urls;
    private final Course course;
    private final List<Teacher> teachers;
    private final List<Room> rooms;
    private final List<Group> groups;

    @BsonCreator
    public UnivClass(@BsonProperty("id") final String id,
                     @BsonProperty("start_date_time") final String startDateTime,
                     @BsonProperty("end_date_time") final String endDateTime,
                     @BsonProperty("day") final long day,
                     @BsonProperty("duration") final int duration,
                     @BsonProperty("urls") final List<String> urls,
                     @BsonProperty("course") final Course course,
                     @BsonProperty("teachers") final List<Teacher> teachers,
                     @BsonProperty("rooms") final List<Room> rooms,
                     @BsonProperty("groups") final List<Group> groups) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.day = day;
        this.duration = duration;
        this.urls = urls;
        this.course = course;
        this.teachers = teachers;
        this.rooms = rooms;
        this.groups = groups;
    }

    public UnivClass(JSONObject event) {
        this.id = event.getString("id");
        this.startDateTime = event.getString("startDateTime");
        this.endDateTime = event.getString("endDateTime");
        this.day = event.getLong("day");
        this.duration = event.getInt("duration");
        this.urls = event.getJSONArray("urls").toList().stream().map(o -> (String) o).toList();
        this.course = new Course(event.getJSONObject("course"));
        this.teachers = event.getJSONArray("teachers").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Teacher::new).toList();
        this.rooms = event.getJSONArray("rooms").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Room::new).toList();
        this.groups = event.getJSONArray("groups").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Group::new).toList();
    }

    public String getId() {
        return id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public long getDay() {
        return day;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getUrls() {
        return urls;
    }

    public Course getCourse() {
        return course;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
