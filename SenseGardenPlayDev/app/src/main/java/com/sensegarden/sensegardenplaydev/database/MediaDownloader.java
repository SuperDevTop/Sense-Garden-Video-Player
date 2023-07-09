package com.sensegarden.sensegardenplaydev.database;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Storage.SENSE_GARDEN_ESSENTIALS;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.utils.PrefSingleton;

import java.io.File;
import java.util.ArrayList;

public class MediaDownloader {
    String tag = "TAG_MEDIA_DOWNLOADER";
    private final String DOWNLOADED_MEDIA_KEY = "downloadedMedia";
    private final Context context;
    private final PrefSingleton prefSingleton;

    public MediaDownloader(Context context) {
        this.context = context;
        this.prefSingleton = PrefSingleton.getInstance();
    }

    public void downloadFile(String path, boolean isVideo, String downloadUrl) {
        String modPath = path.replace(":", "");
        if (isVideo) {
            saveId(downloadUrl.replace("/", ""));
            modPath = path.replace("-", "");
        } else saveId(path);

        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(downloadUrl));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String suffix = ".jpg";
        if (isVideo) suffix = ".mp4";

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, modPath + suffix);
        android.app.DownloadManager manager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void removeFile(String path, boolean isVideo) {
        Log.d(tag, "removeFile " + isVideo);
        String modPath;
        if (!isVideo) {
            modPath = path.replace(":", "");
            if (!path.contains(SENSE_GARDEN_ESSENTIALS))
                modPath = SENSE_GARDEN_ESSENTIALS + path.replace(":", "");
        } else
            modPath = path.replace("-", "").replace("/", "");

        removeId(modPath);

        String suffix = ".jpg";
        if (isVideo) suffix = ".mp4";

        String filePath = Environment.getExternalStorageDirectory() +
                "/Android/data/com.sensegarden.sensegardenplaydev/files/Download/"
                + modPath + suffix;

        File file = new File(filePath);
        Log.d(tag, "File " + file.getAbsolutePath() + " exist = " + file.exists());

        boolean isDeleted = file.delete();
        Log.d(tag, "Is file deleted: " + isDeleted);

        deleteCache(context);
    }

    public String getMediaUri(String path, boolean isVideo) {
        String suffix = ".jpg";
        if (isVideo) suffix = ".mp4";

        String modPath = path.replace(":", "");
        if (isVideo) modPath = path.replace("-", "");

        String filePath = Environment.getExternalStorageDirectory() +
                "/Android/data/com.sensegarden.sensegardenplaydev/files/Download/"
                + modPath + suffix;
        File file = new File(filePath);

        if (file.exists()) return filePath;

        return "";
    }

    public void compareLists(ArrayList<PhotoVideo> photoVideos) {
        String savedIdsString = prefSingleton.getString(DOWNLOADED_MEDIA_KEY);
        String[] savedIds = savedIdsString.split(",");

        Log.d(tag, "compareLists " + savedIds.length);

        if (savedIds.length == 0) return;

        ArrayList<String> firebaseUris = new ArrayList<>();
        for(PhotoVideo photoVideo: photoVideos) {
            Log.d(tag, "Firebase uri " + photoVideo.getUri());
            firebaseUris.add(photoVideo.getUri().replace("/", ""));
        }

        for (String savedId : savedIds) {
            String modifiedId = savedId.replace(",", "");
            Log.d(tag, "saved id " + savedId);
            if (!firebaseUris.contains(modifiedId.replace(SENSE_GARDEN_ESSENTIALS, "")))
                removeFile(modifiedId, modifiedId.contains("https"));
        }
    }

    private void saveId(String id) {
        Log.d(tag, "saveId " + id);
        String savedIds = prefSingleton.getString(DOWNLOADED_MEDIA_KEY);

        if (!savedIds.contains(id))
            prefSingleton.saveString(DOWNLOADED_MEDIA_KEY, savedIds + id + ",");
    }

    private void removeId(String id) {
        Log.d(tag, "removeId" + id);
        String savedIds = prefSingleton.getString(DOWNLOADED_MEDIA_KEY);
        savedIds = savedIds.replace("," + id, "");
        prefSingleton.saveString(DOWNLOADED_MEDIA_KEY, savedIds);
    }

    private void clearData() {
        try {
            ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
        } catch (Exception e) {
            Log.d(tag, "Clear data error " + e);
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);

                Log.d("TAG_MEDIA", "cache deleted? " + deleteDir(dir));
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}