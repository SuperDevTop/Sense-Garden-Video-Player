package com.sensegarden.sensegardenplaydev.database;

import com.sensegarden.sensegardenplaydev.models.Game;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;

import java.util.ArrayList;

public interface DatabaseInterfaces {
    public interface EssentialsCallback {
        void onCallback(ArrayList<PhotoVideo> photoVideos);
    }

    public interface AnonymousCallback {
        void onCallback(boolean success);
    }

    interface ImageCallback {
        void onCallback(boolean success);
    }

    public interface GamesCallback {
        void onCallback(ArrayList<Game> games);
    }

    public interface EssentialsCategoryCallback {
        void onCallback(ArrayList<Category> categories);
    }

    interface VideoCallback {
        void onCallback(PhotoVideo uploadObject);
    }

    interface VideoCategoryCallback {
        void onCallback(Category category);
    }
}
