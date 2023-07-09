package com.sensegarden.sensegardenplaydev.ui;

import static com.sensegarden.sensegardenplaydev.adapters.EssentialsAdapter.totalMediaEssentials;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sensegarden.sensegardenplaydev.adapters.EssentialsAdapter;
import com.sensegarden.sensegardenplaydev.database.DatabaseService;
import com.sensegarden.sensegardenplaydev.database.MediaDownloader;
import com.sensegarden.sensegardenplaydev.databinding.ActivityEssentialsBinding;
import com.sensegarden.sensegardenplaydev.models.senseGardens.ThreePhoto;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;

public class EssentialsActivity extends AppCompatActivity {
    private ActivityEssentialsBinding binding;
    private final DatabaseService databaseService = new DatabaseService();

    private final String tag = "TAG_ESSENTIALS_ACTIVITY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEssentialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init();
        new MediaDownloader(this).deleteCache(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void listeners() {
        binding.btnEssentialsEndPlay.setOnClickListener(view -> finish());
    }

    public void hideUI() {
        binding.textView5.setVisibility(View.GONE);
        binding.btnEssentialsEndPlay.setVisibility(View.GONE);
        binding.rvSenseGardenEssentials.setVisibility(View.GONE);
    }

    public void showUI() {
        binding.textView5.setVisibility(View.VISIBLE);
        binding.btnEssentialsEndPlay.setVisibility(View.VISIBLE);
        binding.rvSenseGardenEssentials.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        databaseService.signInAnonymously(success -> databaseService.getEssentials(photoVideos -> {
            EssentialsAdapter essentialsAdapter = new EssentialsAdapter(EssentialsActivity.this, createThreePhotos(photoVideos));
            totalMediaEssentials = photoVideos.size();
            new Utils(EssentialsActivity.this).setRecycler(binding.rvSenseGardenEssentials, essentialsAdapter);

            new MediaDownloader(this).compareLists(photoVideos);
        }));
    }

    private void init() {
        listeners();
        loadData();
    }

    private ArrayList<ThreePhoto> createThreePhotos(ArrayList<PhotoVideo> photoVideos) {
        ArrayList<ThreePhoto> threePhotos = new ArrayList<>();

        for (int i = 0; i <= photoVideos.size() - 3; i += 3) {
            ThreePhoto threePhoto = new ThreePhoto(photoVideos.get(i), photoVideos.get(i + 1), photoVideos.get(i + 2));
            threePhotos.add(threePhoto);
        }

        int rest = photoVideos.size() % 3;
        switch (rest) {
            case 1: {
                ThreePhoto threePhoto = new ThreePhoto(photoVideos.get(photoVideos.size() - 1), null, null);
                threePhotos.add(threePhoto);
            }
            break;
            case 2: {
                ThreePhoto threePhoto = new ThreePhoto(photoVideos.get(photoVideos.size() - 2), photoVideos.get(photoVideos.size() - 1), null);
                threePhotos.add(threePhoto);
            }
            break;
        }

        return threePhotos;
    }
}