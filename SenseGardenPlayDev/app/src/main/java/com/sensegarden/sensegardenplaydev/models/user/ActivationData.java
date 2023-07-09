package com.sensegarden.sensegardenplaydev.models.user;

public class ActivationData {

    private final String date;
    private final String id;
    private final String status;


    public ActivationData(String date, String id, String status) {
        this.date = date;
        this.id = id;
        this.status = status;
    }

    public ActivationData() {
        date = "";
        status = "";
        id = "";
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
