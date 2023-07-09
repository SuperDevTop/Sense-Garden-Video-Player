package com.sensegarden.sensegardenplaydev.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.sensegarden.sensegardenplaydev.models.user.Caregiver;
import com.sensegarden.sensegardenplaydev.models.user.PrimaryUser;

public class SessionManager {
    private final static String ID_KEY = "ID";
    private final static String PHOTO_KEY = "PHOTO";
    private final static String CURRENT_FLOW_KEY = "FLOW";
    private final static String TOKEN_KEY = "ACCESS_TOKEN";
    private final static String TIME_KEY = "TOKEN_TIME";
    private final static String NAME_KEY = "NAME";
    private final static String TYPE_KEY = "TYPE";
    private final static String QR_KEY = "QR";
    private final static String AVATAR_KEY = "AVATAR";
    private final static String CURRENT_SENSE_GARDEN_KEY = "CURRENT_SENSE_GARDEN";

    @SuppressLint("StaticFieldLeak")
    private PrefSingleton prefSingleton;
    private final DateTime dateTime;

    private final String tag = "TAG_SESSION_MANAGER";

    public SessionManager() {
        prefSingleton = PrefSingleton.getInstance();
        dateTime = new DateTime();
    }

    public void createPrimaryUser(PrimaryUser primaryUser) {
        prefSingleton.saveString(ID_KEY, primaryUser.getId());
        prefSingleton.saveString(PHOTO_KEY, primaryUser.getPhoto());
        prefSingleton.saveString(CURRENT_FLOW_KEY, primaryUser.getCurrent_flow());
        prefSingleton.saveString(TYPE_KEY, primaryUser.getType());
        prefSingleton.saveString(NAME_KEY, primaryUser.getFull_name());
        prefSingleton.saveString(QR_KEY, primaryUser.getQr_code());
        prefSingleton.saveString(AVATAR_KEY, primaryUser.getPhoto());
    }

    public void createCaregiver(Caregiver caregiver) {
        Log.d(tag, caregiver.getId());
        prefSingleton.saveString(ID_KEY, caregiver.getId());
        prefSingleton.saveString(PHOTO_KEY, caregiver.getPhoto());
        prefSingleton.saveString(TYPE_KEY, caregiver.getType());
        prefSingleton.saveString(CURRENT_SENSE_GARDEN_KEY, caregiver.getSense_garden_id());
        //TODO dodaj flows
    }

    public String getSenseGarden() {
        return prefSingleton.getString(CURRENT_SENSE_GARDEN_KEY);
    }

    public String getId() {
        return prefSingleton.getString(ID_KEY);
    }

    public String getName() {
        return prefSingleton.getString(NAME_KEY);
    }

    public String getType() {
        return prefSingleton.getString(TYPE_KEY);
    }

    public String getQR() {
        return prefSingleton.getString(QR_KEY);
    }

    public String getAvatar() {
        return prefSingleton.getString(AVATAR_KEY);
    }

    public String getPhoto() {
        return prefSingleton.getString(PHOTO_KEY);
    }
}
