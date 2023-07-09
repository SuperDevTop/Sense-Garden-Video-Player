package com.sensegarden.sensegardenplaydev.ui.genericflow;

import static com.sensegarden.sensegardenplaydev.adapters.GenericAdapter.totalMedia;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.adapters.EssentialsCategoryAdapter;
import com.sensegarden.sensegardenplaydev.adapters.GenericAdapter;
import com.sensegarden.sensegardenplaydev.adapters.PickFolderAdapter;
import com.sensegarden.sensegardenplaydev.database.DatabaseService;
import com.sensegarden.sensegardenplaydev.database.MediaDownloader;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;
import com.sensegarden.sensegardenplaydev.models.genericflow.SelectableModel;
import com.sensegarden.sensegardenplaydev.models.senseGardens.ThreePhoto;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.utils.MediaWizard;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class GenericFlowActivity extends AppCompatActivity implements GenericFlowInterface {
    private DatabaseService databaseService;
    private SessionManager sessionManager;
    private Utils utils;

    private EssentialsCategoryAdapter essentialsCategoryAdapter;
    private GenericAdapter genericAdapter;
    private Dialog pickDialog;

    private Button btnEditFolder;

    private boolean shouldAddPlaceholder = false;

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<String> movableImages = new ArrayList<>();

    private Category currentCategory = null;
    private final Category categoryAll = new Category("All", "all", new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_flow);

        init();
    }

    public void hideUI() {
        btnEditFolder.setVisibility(View.INVISIBLE);
        findViewById(R.id.btBackGenFlow).setVisibility(View.INVISIBLE);
        findViewById(R.id.recGenericFlow).setVisibility(View.INVISIBLE);
    }

    public void showUI() {
        btnEditFolder.setVisibility(View.VISIBLE);
        findViewById(R.id.btBackGenFlow).setVisibility(View.VISIBLE);
        findViewById(R.id.recGenericFlow).setVisibility(View.VISIBLE);
    }

    private void loadData(boolean shouldLoadCategories) {
        if (shouldLoadCategories) {
            databaseService.getCategories(sessionManager.getSenseGarden(), categories -> {
                this.categories = categories;
                if (categories != null) {
                    ArrayList<SelectableModel> selectableModels = new ArrayList<>();
                    selectableModels.add(new SelectableModel(categoryAll, currentCategory == categoryAll));

                    fillAll(categories);

                    for (int i = 0; i < categories.size(); i++) {
                        SelectableModel selectableModel = new SelectableModel(categories.get(i), Objects.equals(currentCategory.getName(), categories.get(i).getName()));
                        selectableModels.add(selectableModel);
                    }

                    essentialsCategoryAdapter = new EssentialsCategoryAdapter
                            (GenericFlowActivity.this, selectableModels, GenericFlowActivity.this);
                    utils.setRecycler(findViewById(R.id.recGenericCategories), essentialsCategoryAdapter);

                    ArrayList<ThreePhoto> threePhotos = formUploadObjects(currentCategory.getName(), currentCategory.getImages());
                    genericAdapter = new GenericAdapter(GenericFlowActivity.this, threePhotos, this);
                    totalMedia = currentCategory.getImages().size();
                    utils.setRecycler(findViewById(R.id.recGenericFlow), genericAdapter);
                }
            });
        } else {
            ArrayList<ThreePhoto> threePhotos = formUploadObjects(currentCategory.getName(), currentCategory.getImages());
            genericAdapter = new GenericAdapter(GenericFlowActivity.this, threePhotos,this);
            totalMedia = currentCategory.getImages().size();
            utils.setRecycler(findViewById(R.id.recGenericFlow), genericAdapter);
        }
    }

    @Override
    public void onFolderClicked(Category folder) {
        if (currentCategory != folder) {
            currentCategory = folder;
            if (folder.getName().equals(categoryAll.getName())) {
                //btnUpload.setVisibility(View.INVISIBLE);
                btnEditFolder.setVisibility(View.INVISIBLE);
            } else {
                //btnUpload.setVisibility(View.VISIBLE);
                btnEditFolder.setVisibility(View.VISIBLE);

                btnEditFolder.setText("Manage photos");
            }

            loadData(false);
        }
    }

    private void removeImagesFromCurrentCategory() {
        Log.d("TAG_ANDRA", "Current category " + currentCategory.getName());
        if (currentCategory != null) {
            currentCategory.removeImages(movableImages);
            //test this one
            //new MediaDownloader(this).removeFile("WHATHERE", false);
            databaseService.createCategory(sessionManager.getSenseGarden(), currentCategory);
        }
    }

    private void dialogPickCategory() {
        pickDialog.setContentView(R.layout.popup_pick_category);
        pickDialog.show();

        PickFolderAdapter pickFolderAdapter = new PickFolderAdapter(this, categories, this);
        utils.setRecycler(pickDialog.findViewById(R.id.recPickCategories), pickFolderAdapter);
    }

    @Override
    public void onFolderToMovePicked(Category folder) {
        pickDialog.dismiss();

        removeImagesFromCurrentCategory();
        folder.addImages(movableImages);

        databaseService.createCategory(sessionManager.getSenseGarden(), folder);

        loadData(false);
    }

    @Override
    public void onSelection(boolean isSomeSelected) {
        //if (isSomeSelected)
        //else btnEditFolder.
        btnEditFolder.setText("Confirm");
    }

    private void editFolderImages() {
        if (btnEditFolder.getText().toString().equals("Manage photos")) {
            genericAdapter.setSelectable(true);
            btnEditFolder.setText("Select photos");
        } else {
            movableImages = genericAdapter.getSelectedPhotos();

            if (!movableImages.isEmpty())
                dialogPickCategory();

            genericAdapter.setSelectable(false);
            btnEditFolder.setText("Manage photos");
        }
    }

    private void init() {
        databaseService = new DatabaseService();
        sessionManager = new SessionManager();
        pickDialog = new Dialog(this);
        utils = new Utils(this);

        btnEditFolder = findViewById(R.id.btEditFolderImages);

        findViewById(R.id.btBackGenFlow).setOnClickListener(view -> finish());
        btnEditFolder.setOnClickListener(view -> editFolderImages());

        currentCategory = categoryAll;

        btnEditFolder.setVisibility(View.INVISIBLE);

        loadData(true);
    }

    private void fillAll(ArrayList<Category> categories) {
        categoryAll.setImages(null);
        for (Category category : categories) {
            if (category.getImages() != null) {
                for (String imageUrl : category.getImages()) {
                    String completeUrl = "/images/" + imageUrl;
                    if (imageUrl.contains("https")) completeUrl = imageUrl;
                    categoryAll.addImage(completeUrl);
                }
            }
        }

        if (Objects.equals(currentCategory.getName(), categoryAll.getName()))
            currentCategory = categoryAll;
    }

    private ArrayList<ThreePhoto> formUploadObjects(String catName, ArrayList<String> imageUrls) {
        ArrayList<PhotoVideo> uploadObjects = new ArrayList<>();



        if (imageUrls != null) {
            Log.d("TAG_ANDRA", "Image urls size " + imageUrls.size());

            for (int i = 0; i < imageUrls.size(); i++) {
                //Log.d("TAG_ANDRA", "url " + imageUrls.get(i));
                PhotoVideo uo = new PhotoVideo();
                String imageUrl = imageUrls.get(i);
                if (!imageUrl.contains("/")) uo.setUri("/images/" + imageUrls.get(i));
                else uo.setUri(imageUrl);
                uo.setVideo(imageUrl.contains("https"));
                uploadObjects.add(uo);
            }

            if (shouldAddPlaceholder) {
                PhotoVideo uploadObject = new PhotoVideo();
                uploadObject.setUri("uploading.png");
                uploadObject.setId(UUID.randomUUID().toString());
                uploadObjects.add(0, uploadObject);
            }
        }

        return createThreePhotos(uploadObjects);
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
            } break;
            case 2: {
                ThreePhoto threePhoto = new ThreePhoto(photoVideos.get(photoVideos.size() - 2), photoVideos.get(photoVideos.size() - 1), null);
                threePhotos.add(threePhoto);
            } break;
        }

        return threePhotos;
    }
}