package com.sensegarden.sensegardenplaydev.adapters;

import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.SENSE_GARDENS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.senseGardens.ThreePhoto;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.ui.genericflow.GenericFlowActivity;
import com.sensegarden.sensegardenplaydev.ui.genericflow.GenericFlowInterface;
import com.sensegarden.sensegardenplaydev.utils.LoadingDialog;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;

import java.util.ArrayList;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.GenericAdapterHolder> {

    private final Context context;
    private final ArrayList<ThreePhoto> photos;
    public static ArrayList<Boolean> selected;

    public static boolean isSelectable = false;

    public static VideoView videoViewMrtvi;

    public static int totalMedia = 0;
    public static int currentIndex = 0;
    public static int currentPosition = 0;

    public static GenericFlowInterface genericFlowInterface;

    private String tag = "TAG_ESSENTIALS_ADAPTER";

    public GenericAdapter(Context context, ArrayList<ThreePhoto> photos, GenericFlowInterface genericFlowInterface) {
        this.context = context;
        this.photos = photos;
        selected = new ArrayList<>(photos.size() * 3);
        for (int i = 0; i < photos.size() * 3; i++)
            selected.add(false);

        this.genericFlowInterface = genericFlowInterface;
    }

    public void setSelectable(boolean selectable) {
        this.isSelectable = selectable;
    }

    public ArrayList<String> getSelectedPhotos() {
        ArrayList<String> selectedPhotos = new ArrayList<>();

        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i)) {
                int ind = i / 3;
                ThreePhoto threePhoto = photos.get(ind);

                PhotoVideo uploadObject;
                switch (i % 3) {
                    case 0: {
                        uploadObject = threePhoto.getPhoto1();
                    }
                    break;
                    case 1: {
                        uploadObject = threePhoto.getPhoto2();
                    }
                    break;
                    default: {
                        uploadObject = threePhoto.getPhoto3();
                    }
                    break;
                }

                if (uploadObject != null) {
                    if (uploadObject.getUri().contains("https"))
                        selectedPhotos.add(uploadObject.getUri());
                    else {
                        Log.d("TAG_ANDRA", "Nije video " + uploadObject.getUri());
                        String[] split = uploadObject.getUri().split("/");
                        if (split.length == 3)
                            selectedPhotos.add(split[2]);
                    }
                }
            }
        }

        return selectedPhotos;
    }

    @NonNull
    @Override
    public GenericAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_essentials_video, null);
        return new GenericAdapterHolder(layout, context, photos);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericAdapterHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class GenericAdapterHolder extends RecyclerView.ViewHolder {
        private final StorageWR storageWR;

        private final ConstraintLayout constFullscreenEssentials;
        private final ImageView ivFullscreenEssentials;
        private final VideoView vvFullscreenEssentials;

        private final ArrayList<VideoView> videoViews = new ArrayList<>();
        private final ArrayList<ImageView> imageViews = new ArrayList<>();
        private final ArrayList<ConstraintLayout> constraintLayouts = new ArrayList<>();

        public ConstraintLayout cons1, cons2, cons3;

        private final ArrayList<ThreePhoto> threePhotos;
        private SessionManager sessionManager;

        private GenericFlowActivity genericFlowActivity;

        private String tag = "TAG_GENERIC_ADAPTER_VIEW_HOLDER";

        public GenericAdapterHolder(@NonNull View itemView, Context context, ArrayList<ThreePhoto> tps) {
            super(itemView);

            genericFlowActivity = (GenericFlowActivity) context;

            this.threePhotos = tps;
            storageWR = new StorageWR(context);
            sessionManager = new SessionManager();

            constFullscreenEssentials = ((Activity) context).findViewById(R.id.constFullscreenGeneric);
            ivFullscreenEssentials = constFullscreenEssentials.findViewById(R.id.ivFullscreenGeneric);
            vvFullscreenEssentials = constFullscreenEssentials.findViewById(R.id.vvFullscreenGeneric);

            ImageView imageView1 = itemView.findViewById(R.id.essentialsImage);
            imageViews.add(imageView1);
            ImageView imageView2 = itemView.findViewById(R.id.essentialsImage2);
            imageViews.add(imageView2);
            ImageView imageView3 = itemView.findViewById(R.id.essentialsImage3);
            imageViews.add(imageView3);
            VideoView videoView1 = itemView.findViewById(R.id.essentialsVideo);
            videoViews.add(videoView1);
            VideoView videoView2 = itemView.findViewById(R.id.essentialsVideo2);
            videoViews.add(videoView2);
            VideoView videoView3 = itemView.findViewById(R.id.essentialsVideo3);
            videoViews.add(videoView3);
            cons1 = itemView.findViewById(R.id.consItem1);
            constraintLayouts.add(cons1);
            cons2 = itemView.findViewById(R.id.consItem2);
            constraintLayouts.add(cons2);
            cons3 = itemView.findViewById(R.id.consItem3);
            constraintLayouts.add(cons3);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(int position) {
            for (int i = 0; i < 3; i++) {
                PhotoVideo photo = threePhotos.get(position).getPhotos().get(i);
                if (photo != null) {
                    //totalMedia++;

                    setMedia(photo, i);
                    fullScreenListeners();
                    itemListener(position, i);
                } else hide(i);
            }
        }

        private void setMedia(PhotoVideo photo, int i) {
            videoViews.get(i).stopPlayback();

            if (photo.getVideo()) {
                VideoView videoView = videoViews.get(i);
                videoView.setVisibility(View.VISIBLE);
                imageViews.get(i).setVisibility(View.GONE);

                storageWR.loadVideo(videoView, photo.getUri());
            } else {
                String path = SENSE_GARDENS + "/" + sessionManager.getSenseGarden() + "/" + photo.getUri();
                ImageView imageView = imageViews.get(i);
                imageView.setVisibility(View.VISIBLE);
                videoViews.get(i).setVisibility(View.INVISIBLE);

                if (imageView.getDrawable() == null)
                    storageWR.loadImage(path, imageView);
            }
        }

        private void hide(int i) {
            videoViews.get(i).setVisibility(View.GONE);
            imageViews.get(i).setVisibility(View.GONE);
        }

        private void nextMedia() {
            currentIndex++;

            if (currentIndex > 2) {
                currentPosition++;
                currentIndex = 0;
            }

            int mediaIndex = currentPosition * 3 + currentIndex;
            Log.d(tag, "Media index " + mediaIndex + " total media " + totalMedia);

            if (mediaIndex >= totalMedia) {
                currentPosition = 0;
                currentIndex = 0;
            }

            Log.d(tag, "index" + currentIndex + " : position" + currentPosition);

            doubleTapped(currentPosition, currentIndex);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void fullScreenListeners() {
            constFullscreenEssentials.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);

                    return true;
                }

                private final GestureDetector gestureDetector = new GestureDetector(genericFlowActivity, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        constFullscreenEssentials.setVisibility(View.GONE);
                        genericFlowActivity.showUI();
                        vvFullscreenEssentials.stopPlayback();

                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        nextMedia();

                        return true;
                    }
                });
            });
        }

        @SuppressLint("ClickableViewAccessibility")
        private void itemListener(int position, int i) {
            constraintLayouts.get(i).setOnTouchListener(new View.OnTouchListener() {
                private final GestureDetector gestureDetector = new GestureDetector(genericFlowActivity, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        doubleTapped(position, i);

                        currentIndex = i;
                        currentPosition = position;

                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (isSelectable) {
                            int index = position * 3 + i;

                            if (selected.get(index)) constraintLayouts.get(i).setBackground(null);
                            else constraintLayouts.get(i).setBackground(ContextCompat.getDrawable(genericFlowActivity, R.drawable.bordered_rect));

                            boolean value = !selected.get(index);
                            selected.remove(index);
                            selected.add(index, value);

                            genericFlowInterface.onSelection(selected.contains(true));
                        } else {
                            if (videoViewMrtvi != null) {
                                videoViewMrtvi.stopPlayback();
                                videoViewMrtvi = null;
                            }

                            PhotoVideo photoVideo = threePhotos.get(position).getPhotos().get(i);
                            if (photoVideo.getVideo()) {
                                VideoView videoView = videoViews.get(i);
                                if (videoView.isPlaying()) videoView.pause();
                                else {
                                    videoView.start();
                                    videoViewMrtvi = videoView;
                                }
                            }
                        }

                        return true;
                    }
                });

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);

                    return true;
                }
            });
        }

        private void doubleTapped(int position, int i) {
            ArrayList<PhotoVideo> photos = threePhotos.get(position).getPhotos();
            PhotoVideo photo = photos.get(i);

            if (photo == null) return;

            constFullscreenEssentials.setVisibility(View.VISIBLE);
            genericFlowActivity.hideUI();
            
            if (videoViewMrtvi != null) {
                videoViewMrtvi.stopPlayback();
                videoViewMrtvi = null;
            }

            if (photo.getVideo()) {
//                LoadingDialog loadingDialog = new LoadingDialog(genericFlowActivity);
//                loadingDialog.start();
                vvFullscreenEssentials.setVisibility(View.VISIBLE);
                ivFullscreenEssentials.setVisibility(View.GONE);

                vvFullscreenEssentials.stopPlayback();
                vvFullscreenEssentials.suspend();

                storageWR.loadVideo(vvFullscreenEssentials, photo.getUri());
                vvFullscreenEssentials.start();
//                vvFullscreenEssentials.setOnPreparedListener(mediaPlayer -> loadingDialog.dismiss());
            } else {
                String path = SENSE_GARDENS + "/" + sessionManager.getSenseGarden() + "/" + photo.getUri();
                vvFullscreenEssentials.setVisibility(View.GONE);
                ivFullscreenEssentials.setVisibility(View.VISIBLE);

                storageWR.loadImage(path, ivFullscreenEssentials);
            }
        }
    }
}
