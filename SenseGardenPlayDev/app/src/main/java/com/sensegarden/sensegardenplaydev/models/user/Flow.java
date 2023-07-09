package com.sensegarden.sensegardenplaydev.models.user;

import java.io.Serializable;
import java.util.ArrayList;

public class Flow implements Serializable {
    private final int count;
    private final String created_by;
    private final String creation_date;
    private final String defined_for;
    private final Boolean favourite;
    private final String id;
    private final String name;
    private final ArrayList<String> photos;
    private final String type;

    public Flow() {
        count = 0;
        created_by = "";
        creation_date = "";
        defined_for = "";
        favourite = false;
        id = "";
        name = "";
        photos = new ArrayList<>();
        type = "";
    }

    public Flow(int count, String createdBy, String creationDate, String definedFor, Boolean favourite, String id, String name, ArrayList<String> photos, String type) {
        this.count = count;
        this.created_by = createdBy;
        this.creation_date = creationDate;
        this.defined_for = definedFor;
        this.favourite = favourite;
        this.id = id;
        this.name = name;
        this.photos = photos;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getDefined_for() {
        return defined_for;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public String getType() {
        return type;
    }
}
