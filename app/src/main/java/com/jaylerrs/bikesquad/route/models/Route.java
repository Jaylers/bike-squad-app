package com.jaylerrs.bikesquad.route.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Route {

    public String rid;
    public String mapid;
    public String title;
    public String body;
    public String dangerous;
    public String difficult;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Route() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Route(String rid, String title, String body, String dangerous, String difficult) {
        this.rid = rid;
        this.title = title;
        this.body = body;
        this.dangerous = dangerous;
        this.difficult = difficult;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("rid", rid);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("dangerous", dangerous);
        result.put("difficult", difficult);
        return result;
    }
    // [END post_to_map]

}
// [END post_class]
