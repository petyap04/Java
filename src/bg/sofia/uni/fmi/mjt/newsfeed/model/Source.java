package bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}