package com.sensegarden.sensegardenplaydev.models;

public class Game {
    private String id;
    private String name;
    private String urlSG;
    private String gameUrl;
    private boolean playing;

    public Game(String id, String name, String urlSG, String gameUrl, boolean playing) {
        this.id = id;
        this.name = name;
        this.urlSG = urlSG;
        this.gameUrl = gameUrl;
        this.playing = playing;
    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlSG() {
        return urlSG;
    }

    public void setUrlSG(String urlSG) {
        this.urlSG = urlSG;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}

