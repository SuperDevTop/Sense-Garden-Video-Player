package com.sensegarden.sensegardenplaydev.adapters;

import static com.sensegarden.sensegardenplaydev.utils.Constants.Storage.SENSE_GARDEN_ESSENTIALS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
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
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.senseGardens.ThreePhoto;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.ui.EssentialsActivity;
import com.sensegarden.sensegardenplaydev.utils.LoadingDialog;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;

import java.io.File;
import java.util.ArrayList;

public class EssentialsAdapter extends RecyclerView.Adapter<EssentialsAdapter.EssentialsAdapterHolder> {

    private final Context context;
    private final ArrayList<ThreePhoto> photos;
    public static VideoView videoViewMrtvi;
    private boolean isTotalCounted = false;
    public static int totalMediaEssentials = 0;
    public static int currentIndex = 0;
    public static int currentPosition = 0;

    private String tag = "TAG_ESSENTIALS_ADAPTER";

    public EssentialsAdapter(Context context, ArrayList<ThreePhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public EssentialsAdapter.EssentialsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_essentials_video, null);
        return new EssentialsAdapter.EssentialsAdapterHolder(layout, context, photos);
    }

    @Override
    public void onBindViewHolder(@NonNull EssentialsAdapter.EssentialsAdapterHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class EssentialsAdapterHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout constFullscreenEssentials;
        private final ImageView ivFullscreenEssentials;
        private final VideoView vvFullscreenEssentials;

        private final EssentialsActivity essentialsActivity;
        private final StorageWR storageWR;

        private final ArrayList<VideoView> videoViews = new ArrayList<>();
        private final ArrayList<ImageView> imageViews = new ArrayList<>();
        private final ArrayList<TextView> textViews = new ArrayList<>();
        private final ArrayList<ConstraintLayout> constraintLayouts = new ArrayList<>();

        //private final RecyclerView recEssentials;

        private final ArrayList<ThreePhoto> threePhotos;

        private String tag = "TAG_ESSENTIALS_VIDEO_ADAPTER_HOLDER";

        public EssentialsAdapterHolder(@NonNull View itemView, Context context, ArrayList<ThreePhoto> tps) {
            super(itemView);

            this.essentialsActivity = (EssentialsActivity) context;
            this.threePhotos = tps;
            storageWR = new StorageWR(context);

            constFullscreenEssentials = ((Activity) context).findViewById(R.id.constFullscreenEssentials);
            //recEssentials = essentialsActivity.findViewById(R.id.rvSenseGardenEssentials);
            ivFullscreenEssentials = constFullscreenEssentials.findViewById(R.id.ivFullscreenEssentials);
            vvFullscreenEssentials = constFullscreenEssentials.findViewById(R.id.vvFullscreenEssentials);

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
            ConstraintLayout cons1 = itemView.findViewById(R.id.consItem1);
            constraintLayouts.add(cons1);
            ConstraintLayout cons2 = itemView.findViewById(R.id.consItem2);
            constraintLayouts.add(cons2);
            ConstraintLayout cons3 = itemView.findViewById(R.id.consItem3);
            constraintLayouts.add(cons3);
            TextView textView1 = itemView.findViewById(R.id.tEssentialsText);
            textViews.add(textView1);
            TextView textView2 = itemView.findViewById(R.id.tEssentialsText2);
            textViews.add(textView2);
            TextView textView3 = itemView.findViewById(R.id.tEssentialsText3);
            textViews.add(textView3);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(int position) {
            for (int i = 0; i < 3; i++) {
                PhotoVideo photo = threePhotos.get(position).getPhotos().get(i);
                if (photo != null) {
                    setMedia(photo, i);

                    fullScreenListeners();
                    itemListener(position, i);
                } else hide(i);
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private void fullScreenListeners() {
            constFullscreenEssentials.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);

                    return true;
                }

                private final GestureDetector gestureDetector = new GestureDetector(essentialsActivity, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        constFullscreenEssentials.setVisibility(View.GONE);
                        essentialsActivity.showUI();
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
                private final GestureDetector gestureDetector = new GestureDetector(essentialsActivity, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        doubleTapped(position, i);

                        currentIndex = i;
                        currentPosition = position;

                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Log.d(tag, "Single tap " + position + " - " + i);
                        PhotoVideo photoVideo = threePhotos.get(position).getPhotos().get(i);

                        if (videoViewMrtvi != null) {
                            videoViewMrtvi.stopPlayback();
                            videoViewMrtvi = null;
                        }

                        if (photoVideo.getVideo()) {
                            VideoView videoView = videoViews.get(i);
                            if (videoView.isPlaying()) {
                                Log.d(tag, "Pausing play");
                                videoView.pause();
                            } else {
                                videoViewMrtvi = videoView;
                                videoView.start();
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

        private void nextMedia() {
            Log.d(tag, "next media!");

            currentIndex++;
            if (currentIndex > 2) {
                currentPosition++;
                currentIndex = 0;
            }

            int mediaIndex = currentPosition * 3 + currentIndex;

            Log.d(tag, "Media index " + mediaIndex + " total media " + totalMediaEssentials);
            if (mediaIndex >= totalMediaEssentials) {
                currentPosition = 0;
                currentIndex = 0;
            }

            doubleTapped(currentPosition, currentIndex);
        }

        private void setMedia(PhotoVideo photo, int i) {
            if (photo == null) return;

            textViews.get(i).setText(photo.getCaption());

            if (photo.getVideo()) {
                VideoView videoView = videoViews.get(i);
                videoView.setVisibility(View.VISIBLE);
                imageViews.get(i).setVisibility(View.GONE);

                storageWR.loadVideo(videoView, photo.getUri());
            } else {
                String path = SENSE_GARDEN_ESSENTIALS + photo.getUri();
                ImageView imageView = imageViews.get(i);
                imageView.setVisibility(View.VISIBLE);
                videoViews.get(i).setVisibility(View.GONE);

                if (imageView.getDrawable() == null)
                    storageWR.loadImage(path, imageView);
            }
        }

        private void doubleTapped(int position, int i) {
            Log.d(tag, "Double tapped! Position: " + position + " i: " + i);

            ArrayList<PhotoVideo> photos = threePhotos.get(position).getPhotos();
            PhotoVideo photo = photos.get(i);

            if (photo == null) {
                constFullscreenEssentials.setVisibility(View.GONE);
                vvFullscreenEssentials.setVisibility(View.GONE);
                ivFullscreenEssentials.setVisibility(View.GONE);

                return;
            }

            constFullscreenEssentials.setVisibility(View.VISIBLE);
            essentialsActivity.hideUI();

            if (videoViewMrtvi != null) {
                videoViewMrtvi.stopPlayback();
                videoViewMrtvi = null;
            }

            if (photo.getVideo()) {
                vvFullscreenEssentials.setVisibility(View.VISIBLE);
                ivFullscreenEssentials.setVisibility(View.GONE);

                vvFullscreenEssentials.stopPlayback();
                vvFullscreenEssentials.suspend();

                storageWR.loadVideo(vvFullscreenEssentials, photo.getUri());

                vvFullscreenEssentials.start();
            } else {
                String path = SENSE_GARDEN_ESSENTIALS + photo.getUri();

                Log.d(tag, "Loading image: " + path);

                vvFullscreenEssentials.setVisibility(View.GONE);
                ivFullscreenEssentials.setVisibility(View.VISIBLE);

                storageWR.loadImage(path, ivFullscreenEssentials);
            }
        }

        private void hide(int i) {
            videoViews.get(i).setVisibility(View.GONE);
            imageViews.get(i).setVisibility(View.GONE);
        }
    }
}
