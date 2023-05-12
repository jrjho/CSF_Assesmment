package ibf2022.batch2.csf.backend.models;

import java.util.ArrayList;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Archives {
    
    private String bundleId;

    private String date;

    private String name;

    private String title;

    private String comments;

    private ArrayList<String> urls;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Archives [bundleId=" + bundleId + ", date=" + date + ", name=" + name + ", title=" + title
                + ", comments=" + comments + ", urls=" + urls + "]";
    }

    
    public JsonObject toJSON(){
        JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("bundleId", getBundleId())
                    .add("name", getName())
                    .add("title", getTitle())
                    .add("comments", getComments());
                    // .add("urls", getUrls())
                    // .build();
        
        // ls

    }



}
