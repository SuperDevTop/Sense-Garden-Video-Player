package com.sensegarden.sensegardenplaydev.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.user.Flow;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.utils.LoadingDialog;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private String flow_id;
    private String primary_id;
    private String most_played_id;
    private String current_flow = "";

    private VideoView videoView, vvFullscreen, currentVideoView;
    private ImageView imageView, ivFullscreen, currentImageView;
    private FloatingActionButton bNext, bPrev;

    private List<PhotoVideo> items = new ArrayList<>();
    private ArrayList<String> ids;
    private List<File> files;
    private List<String> file_names;
    private int i = 0;
    private Flow flow = null;
    private LoadingDialog dialog;

    private ConstraintLayout constFullscreen, constNormalView;
    private TextView tfDesc;

    private ArrayList<String> downloaded_media;

    private final String tag = "TAG_VIDEO_PLAYER";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        if (getIntent().hasExtra("flow"))
            flow = (Flow) getIntent().getExtras().getSerializable("flow");

        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.ImageView);
        vvFullscreen = findViewById(R.id.vvFullscreen);
        ivFullscreen = findViewById(R.id.ivFullscreen);

        currentVideoView = videoView;
        currentImageView = imageView;

        bNext = findViewById(R.id.bNext);
        bPrev = findViewById(R.id.bPrevious);

        constFullscreen = findViewById(R.id.constFullscreen);
        constNormalView = findViewById(R.id.constNormalView);

        tfDesc = findViewById(R.id.tfDesc);
        Switch swComment = findViewById(R.id.swComment);

        downloaded_media = new ArrayList<>();
        File[] dirs = getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)[0].listFiles();
        for (File file : dirs)
            downloaded_media.add(file.getName());

        constNormalView.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(VideoPlayerActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    showFullscreen();
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        constFullscreen.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(VideoPlayerActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    exitFullscreen();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    i++;
                    if (i == items.size())
                        i = 0;
                    showItem(i);
                    return super.onSingleTapConfirmed(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volControl = (SeekBar) findViewById(R.id.sbVolume);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });

        swComment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                tfDesc.setVisibility(View.GONE);
            else
                tfDesc.setVisibility(View.VISIBLE);
        });


        files = new ArrayList<>();
        file_names = new ArrayList<>();

        File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.sensegarden.sensegardenplaydev/files/Download");
        File[] currentVideos = file.listFiles();

        assert currentVideos != null;
        files.addAll(Arrays.asList(currentVideos));
        for (File currentVideo : currentVideos) {
            file_names.add(currentVideo.getName().substring(0, currentVideo.getName().length() - 4));
            Log.d(tag, currentVideo.getName().substring(0, currentVideo.getName().length() - 4));
        }

        dialog = new LoadingDialog(this);
        SessionManager handler = new SessionManager();

        primary_id = handler.getId();
        dialog.start();
        if (flow == null) {
            FirebaseFirestore.getInstance().collection("users").document(primary_id).collection("flows")
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {

                        if (queryDocumentSnapshots.getDocuments().size() != 0) {

                            int max = 0;
                            most_played_id = queryDocumentSnapshots.getDocuments().get(0).get("id").toString();
                            flow_id = "a";


                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                if (Boolean.parseBoolean(snap.get("favourite").toString())) {
                                    flow_id = snap.get("id").toString();
                                    android.util.Log.d("favourite", flow_id);
                                }


                                if (snap.get("count") != null) {
                                    int times_played = Integer.parseInt(snap.get("count").toString());
                                    if (times_played > max) {
                                        max = times_played;
                                        most_played_id = snap.get("id").toString();
                                    }
                                }
                            }


                            FirebaseFirestore.getInstance().collection("users").document(primary_id).collection("flows").document(flow_id).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        flow = documentSnapshot.toObject(Flow.class);

                                        if (flow != null)
                                            ids = flow.getPhotos();
                                        items = new ArrayList<>();
                                        if (ids != null) {
                                            for (String id : ids) {
                                                items.add(null);
                                            }
                                            FirebaseFirestore.getInstance().collection("users").document(primary_id).collection("flows").document(most_played_id).get()
                                                    .addOnSuccessListener(documentSnapshot1 -> {
                                                        flow = documentSnapshot1.toObject(Flow.class);


                                                        ids.addAll(flow.getPhotos());

                                                        if (flow.getPhotos() != null && flow.getPhotos().size() != 0) {
                                                            for (String id : flow.getPhotos()) {
                                                                items.add(null);
                                                            }
                                                            load_photo(primary_id, 0);
                                                        } else {
                                                            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_primary_landscape), "There are no photos in this flow", Snackbar.LENGTH_INDEFINITE);
                                                            mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                                                            mSnackBar.show();
                                                        }

                                                        dialog.dismiss();

                                                    }).addOnFailureListener(e -> {
                                                        e.printStackTrace();
                                                        dialog.dismiss();
                                                    });
                                        } else {
                                            ids = new ArrayList<>();
                                            FirebaseFirestore.getInstance().collection("users").document(primary_id).collection("flows").document(most_played_id).get()
                                                    .addOnSuccessListener(documentSnapshot12 -> {
                                                        flow = documentSnapshot12.toObject(Flow.class);

                                                        ids.addAll(flow.getPhotos());

                                                        if (flow.getPhotos() != null && flow.getPhotos().size() != 0) {
                                                            for (String id : flow.getPhotos()) {
                                                                items.add(null);
                                                            }
                                                            load_photo(primary_id, 0);
                                                        } else {
                                                            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_primary_landscape), "There are no photos in this flow", Snackbar.LENGTH_INDEFINITE);
                                                            mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                                                            mSnackBar.show();
                                                        }
                                                        dialog.dismiss();

                                                    }).addOnFailureListener(e -> {
                                                        e.printStackTrace();
                                                        dialog.dismiss();
                                                    });
                                        }

                                    }).addOnFailureListener(e -> {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                    });
                        } else {
                            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_primary_landscape), "This user has no flows.", Snackbar.LENGTH_INDEFINITE);
                            mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                            mSnackBar.show();
                        }
                        dialog.dismiss();

                    }).addOnFailureListener(e -> {

                    });
        } else {
            if (flow.getPhotos() != null) {
                ids = flow.getPhotos();
                dialog.start();
                load_photo(flow.getDefined_for(), 0);
                dialog.dismiss();
            } else {
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_primary_landscape), "There are no photos in this flow", Snackbar.LENGTH_INDEFINITE);
                mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                mSnackBar.show();
            }
        }

        findViewById(R.id.bLogOut).setOnClickListener(v -> finish());

        FirebaseFirestore.getInstance().collection("users").document(primary_id).update("current_flow", "").addOnCompleteListener(task -> FirebaseFirestore.getInstance().collection("users").document(primary_id).addSnapshotListener((value, error) -> {
            if (!value.get("current_flow").toString().equals("") && !value.get("current_flow").toString().equals(current_flow)) {
                dialog.start();
                flow_id = value.get("current_flow").toString();
                current_flow = flow_id;
                FirebaseFirestore.getInstance().collection("users").document(primary_id).collection("flows").document(flow_id).get().addOnSuccessListener(documentSnapshot -> {
                    flow = documentSnapshot.toObject(Flow.class);

                    ids = flow.getPhotos();
                    items = new ArrayList<>();
                    if (ids != null && ids.size() != 0) {
                        for (String id : ids) {
                            items.add(null);
                        }
                        load_photo(primary_id, 0);
                    } else {
                        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_primary_landscape), "There are no photos in this flow", Snackbar.LENGTH_INDEFINITE);
                        mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                        mSnackBar.show();
                    }
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    dialog.dismiss();
                });
            }
        }));

        FirebaseFirestore.getInstance().collection("users").document(primary_id).update("command", "").addOnCompleteListener(task -> FirebaseFirestore.getInstance().collection("users").document(primary_id).addSnapshotListener((value, error) -> {
            String command = value.get("command").toString();
            android.util.Log.d("photo", "command " + command);
            switch (command.split(",")[0]) {
                case "next":
                    i++;
                    if (i == items.size())
                        i = 0;
                    showItem(i);
                    break;
                case "prev":
                    i--;
                    if (i == -1)
                        i = items.size() - 1;
                    showItem(i);
                    break;
                case "":
                    break;
            }
        }));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dt = sdf.format(new Date());
        FirebaseFirestore.getInstance().collection("users").document(primary_id).update("last_active", dt);
        dialog.dismiss();
    }

    private void load_photo(String userId, final int k) {
        dialog.start();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId).collection("photos")
                .document(ids.get(k)).get().addOnCompleteListener(task -> {
                    dialog.dismiss();
                    items.add(k, task.getResult().toObject(PhotoVideo.class));
                    if (k == ids.size() - 1) {
                        i = 0;
                        showItem(i);

                        if (items.size() > 1) {
                            bNext.setVisibility(View.VISIBLE);
                            bNext.setOnClickListener(v -> {
                                i++;
                                if (i == items.size())
                                    i = 0;
                                showItem(i);
                            });
                            bPrev.setVisibility(View.VISIBLE);
                            bPrev.setOnClickListener(v -> {
                                i--;
                                if (i == -1)
                                    i = items.size() - 1;
                                showItem(i);
                            });
                        }
                    } else {
                        int l = k;
                        l++;
                        load_photo(userId, l);

                    }
                    dialog.dismiss();
                });
    }

    private void showItem(int i) {
        android.util.Log.d("show ", i + "");
        Log.d(tag, String.valueOf(items.get(i).getVideo()));
        if (!items.get(i).getVideo()) {//u pitanju je slika
            Log.d(tag, "slika");
            dialog.start();
            currentImageView.setVisibility(View.VISIBLE);
            currentVideoView.setVisibility(View.GONE);

            if (downloaded_media.contains(items.get(i).getId() + ".jpg")) {//ako je skinuta dodamo je u view
                File dirs = new File(getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)[0] + "/" + items.get(i).getId() + ".jpg");
                Bitmap myBitmap = BitmapFactory.decodeFile(dirs.getAbsolutePath());
                currentImageView.setImageBitmap(myBitmap);
                android.util.Log.d("bitmap", items.get(i).getId());
            } else//skidamo je
            {
                Glide.with(VideoPlayerActivity.this)
                        .load(items.get(i).getUri())
                        .into(currentImageView);
            }

            Log.d(tag, items.get(i).getDescription());
            tfDesc.setText(items.get(i).getDescription());
            dialog.dismiss();
        } else {//video
            dialog.start();
            currentImageView.setVisibility(View.INVISIBLE);
            currentVideoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.start();
                dialog.dismiss();
            });

            Log.d(tag, items.get(i).getDescription());
            tfDesc.setText(items.get(i).getDescription());
            if (file_names.contains(items.get(i).getId()))//ako je u fajlovima dodamo ga u view
            {
                setOrientation(files.get(file_names.indexOf(items.get(i).getId())));
                currentVideoView.setVideoPath(files.get(file_names.indexOf(items.get(i).getId())).getAbsolutePath());
                currentVideoView.setVisibility(View.VISIBLE);
            } else if (downloaded_media.contains(items.get(i).getId() + ".mp3"))  //ako je sveze skinut odatle vadimo uri za video
            {
                File dirs = new File(getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)[0] + "/" + items.get(i).getId() + ".mp3");
                setOrientation(dirs);
                currentVideoView.setVideoPath(dirs.getAbsolutePath());
                currentVideoView.setVisibility(View.VISIBLE);
                android.util.Log.d("bitmap", items.get(i).getId());
            } else {// skidamo ga
                /*Log.d(tag,"skida se");
                dialog.start();

                items.set(i, new StorageWizard().checkIfDownloaded(thisContext,items.get(i)));
                Log.d(tag,"proso?");
                while(!new StorageWizard().checkStatus(thisContext, DownloadManager.STATUS_RUNNING)){
                    Log.d(tag,"download");
                }

                File file = new File(items.get(i).getUri());
                Log.d(tag,file.getAbsolutePath());

                MediaController controller = new MediaController(VideoPlayerActivity.this);
                setOrientation(file);
                currentVideoView.setVideoPath(file.getAbsolutePath());
                currentVideoView.setVisibility(View.VISIBLE);
                controller.setAnchorView(currentVideoView);
                currentVideoView.setMediaController(controller);
                dialog.dismiss();

                */
                dialog.start();
                Log.d(tag, items.get(i).getUri());
                StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(items.get(i).getUri());
                final long TWENTY_MEGABYTES = 1024 * 1024 * 2000;
                videoRef.getBytes(TWENTY_MEGABYTES).addOnSuccessListener(bytes -> {
                    try {
                        dialog.dismiss();
                        File outputFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.sensegarden.sensegardenplaydev/files/Download/" + items.get(i).getId() + ".mp4");
                        //File outputFile = File.createTempFile(items.get(i).getId(), "mp4", getCacheDir());

                        FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                        fileoutputstream.write(bytes);
                        fileoutputstream.close();

                        files.add(outputFile);
                        file_names.add(items.get(i).getId());

                        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(items.get(i).getUri());
                        Glide.with(VideoPlayerActivity.this)
                                .load(ref)
                                .into(imageView);

                        MediaController controller = new MediaController(VideoPlayerActivity.this);
                        setOrientation(outputFile);
                        currentVideoView.setVideoPath(outputFile.getAbsolutePath());
                        currentVideoView.setVisibility(View.VISIBLE);
                        controller.setAnchorView(currentVideoView);
                        currentVideoView.setMediaController(controller);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        }
    }

    private void setOrientation(File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(VideoPlayerActivity.this, Uri.fromFile(file));
        System.out.println(Uri.fromFile(file));
        int mWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int mHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        android.util.Log.d("dimensions", mWidth + " " + mHeight + " " + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

        int degree = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) currentVideoView.getLayoutParams();
        if ((mWidth < mHeight && degree == 0) || (mWidth > mHeight && degree == 90) || (mWidth < mHeight && degree == 180) || (mWidth > mHeight && degree == 270))
            layoutParams.dimensionRatio = "9:16";
        else
            layoutParams.dimensionRatio = "16:9";

        currentVideoView.setLayoutParams(layoutParams);
    }

    private void showFullscreen() {
        constNormalView.setVisibility(View.GONE);
        constFullscreen.setVisibility(View.VISIBLE);
        currentVideoView.stopPlayback();
        constFullscreen.setOnClickListener(v -> {
            i++;
            if (i == items.size())
                i = 0;
            showItem(i);
        });

        currentVideoView = vvFullscreen;
        currentImageView = ivFullscreen;
        if (items.size() != 0)
            showItem(i);
    }

    private void exitFullscreen() {
        constNormalView.setVisibility(View.VISIBLE);
        constFullscreen.setVisibility(View.GONE);
        currentVideoView.stopPlayback();

        currentVideoView = videoView;
        currentImageView = imageView;
        if (items.size() != 0)
            showItem(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
