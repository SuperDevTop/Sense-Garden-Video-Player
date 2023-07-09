package com.sensegarden.sensegardenplaydev.models.user;

import java.util.ArrayList;

public class PhotoVideo {
    private final String description;
    private String id;
    private final ArrayList<String> tags;
    private String uri;
    private Boolean video;
    private String caption;

    public PhotoVideo() {
        description = "";
        id = "";
        tags = new ArrayList<>();
        uri = "";
        video = false;
        caption = "";
    }

    public PhotoVideo(String description, String id, ArrayList<String> tags, String uri, Boolean video, String caption) {
        this.description = description;
        this.id = id;
        this.tags = tags;
        this.uri = uri;
        this.video = video;
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getUri() {
        return uri;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setUri(String path) {
        this.uri = path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
