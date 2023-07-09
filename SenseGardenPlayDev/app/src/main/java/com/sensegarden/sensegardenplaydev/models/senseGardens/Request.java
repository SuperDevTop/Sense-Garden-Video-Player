package com.sensegarden.sensegardenplaydev.models.senseGardens;

public class Request {
    private final String action;
    private final String answeredOn;
    private final String code;
    private final String id;
    private final String name;
    private final String requestedBy;
    private final String requestedFor;
    private final String requestedOn;
    private final String status;

    public Request(String action, String answeredOn, String code, String id, String name, String requestedBy, String requestedFor, String requestedOn, String status) {
        this.action = action;
        this.answeredOn = answeredOn;
        this.code = code;
        this.id = id;
        this.name = name;
        this.requestedBy = requestedBy;
        this.requestedFor = requestedFor;
        this.requestedOn = requestedOn;
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public String getAnsweredOn() {
        return answeredOn;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public String getRequsetedFor() {
        return requestedFor;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public String getStatus() {
        return status;
    }
}
