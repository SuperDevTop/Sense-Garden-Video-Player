package com.sensegarden.sensegardenplaydev.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.database.DatabaseInterfaces;
import com.sensegarden.sensegardenplaydev.database.MediaDownloader;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;

public class StorageWR {
    private final Context context;
    private final MediaDownloader mediaDownloader;
    private final StorageReference storageReference =
            FirebaseStorage.getInstance("gs://sense-garden-home.appspot.com").getReference();

    private String tag = "TAG_STORAGE";

    public StorageWR(Context context) {
        this.context = context;
        mediaDownloader = new MediaDownloader(context);
    }

    public void uploadCategory(String path, Uri filePath, Category category, DatabaseInterfaces.VideoCategoryCallback videoCallback) {
        StorageReference videoReference = storageReference.child(path);
        UploadTask uploadTask = videoReference.putFile(filePath);
        uploadTask.addOnFailureListener(e -> Log.d(tag, "Fail video " + e))
                .addOnSuccessListener(taskSnapshot -> videoReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d(tag, "Uploading uri " + uri);

                    category.addImage(uri.toString());

                    videoCallback.onCallback(category);
                }));
    }

    public void uploadImage(byte[] data, final String path, DatabaseInterfaces.ImageCallback imageCallback) {
        final StorageReference ref = storageReference.child(path);

        ref.putBytes(data)
                .addOnFailureListener(e -> {
                    Log.d(tag, "Fail " + e);
                    imageCallback.onCallback(false);
                })
                .addOnSuccessListener(taskSnapshot -> imageCallback.onCallback(true));
    }

    public void loadVideo(VideoView videoView, String videoStorageUri) {
        if (videoView.getDuration() == -1) {
            videoView.setOnErrorListener((mp, what, extra) -> {
                videoView.setVideoURI(Uri.parse(videoStorageUri));
                return true;
            });

            if (videoStorageUri.split("token=").length > 0) {
                String fileUri = mediaDownloader.getMediaUri(videoStorageUri.split("token=")[1], true);

                if (fileUri.isEmpty()) {
                    videoView.setVideoURI(Uri.parse(videoStorageUri));
                    mediaDownloader.downloadFile(videoStorageUri.split("token=")[1], true, videoStorageUri);
                } else videoView.setVideoURI(Uri.parse(fileUri));
            }


            videoView.seekTo(1);
        }
    }

    public void loadImage(String path, final ImageView imageView) {
        String fileUri = mediaDownloader.getMediaUri(path, false);
        storageReference.child(path).getDownloadUrl().addOnSuccessListener(uri -> {
                    if (fileUri.isEmpty()) {
                        Glide.with(context)
                                .load(uri.toString())
                                .centerCrop()
                                .thumbnail(0.05f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);

                        mediaDownloader.downloadFile(path, false, uri.toString());
                    } else imageView.setImageURI(Uri.parse(fileUri));
                }
        );
    }
}
