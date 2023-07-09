package com.sensegarden.sensegardenplaydev.models.senseGardens;

import java.util.ArrayList;
import java.util.Map;

public class SenseGarden {
    private final ArrayList<Request> requests;
    private final String id;
    private final String name;
    private final Map<String, String> config;

    public SenseGarden(ArrayList<Request> requests, String id, String name, Map<String, String> config) {
        this.requests = requests;
        this.id = id;
        this.name = name;
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }
}
