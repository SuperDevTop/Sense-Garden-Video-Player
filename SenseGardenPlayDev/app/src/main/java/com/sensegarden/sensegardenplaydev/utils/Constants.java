package com.sensegarden.sensegardenplaydev.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String STORAGE_REFERENCE_PATH = "gs://sportevent-26eb9.appspot.com";

    public static String getProfileImagePath(String id) {
        return "/userphotos/" + id + ".jpg";
    }

    public static class Database {
        public static String CURRENT_PHOTO_PATH = "current_photo";
        public static String SENSE_GARDEN_ESSENTIALS_PATH = "sense_garden_essentials";
        public static String SENSE_GARDENS = "sense_gardens";
        public static String USERS_PATH = "users";
        public static String GAMES = "games_sg";
        public static String FOLDERS = "folders";
    }

    public static class Storage {
        public static final String SENSE_GARDEN_ESSENTIALS = "/sense_garden_essentials/";
        public static String GAMES_PATH = "/games_sg/";
    }

    public static class RequestCodes {
        public static final int IMAGE_UPLOAD = 1;
    }

    public static class MoveGames {
        public static List<String> GAME_LINKS = Arrays.asList(
                "http://www.webdisplay.be/butterfly/teken/coloring.html",
                "http://www.webdisplay.be/butterfly/butterfly/index00.html",
                "http://www.webdisplay.be/butterfly/games/",
                "http://www.webdisplay.be/butterfly/tictactoe.html",
                "http://www.webdisplay.be/butterfly/memory4p.html",
                "http://www.webdisplay.be/butterfly/memory6p.html",
                "http://www.webdisplay.be/butterfly/vakanties.html",
                "http://www.webdisplay.be/butterfly/SGpuzzle/index4p.html",
                "http://www.webdisplay.be/butterfly/SGpuzzle/index6p.html");
    }
}
