package com.sensegarden.sensegardenplaydev.ui.movetoimprove;


import static com.sensegarden.sensegardenplaydev.utils.Constants.RequestCodes.IMAGE_UPLOAD;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Storage.GAMES_PATH;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.adapters.MoveAdapter;
import com.sensegarden.sensegardenplaydev.database.DatabaseService;
import com.sensegarden.sensegardenplaydev.models.Game;
import com.sensegarden.sensegardenplaydev.utils.LoadingDialog;
import com.sensegarden.sensegardenplaydev.utils.MediaWizard;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class MoveToImproveActivity extends AppCompatActivity implements MoveContract {
    private RecyclerView recGames;
    private Button btnDone;
    private ConstraintLayout consBottom;

    private MoveAdapter moveAdapter;
    private StorageWR storageWR;
    private MediaWizard mediaWizard;
    private Dialog dialogImage;
    private LoadingDialog loadingDialog;
    private DatabaseService databaseService;
    private SessionManager sessionManager;

    private boolean isEditing = false;
    private Game clickedGame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_improve);

        recGames = findViewById(R.id.recGames);
        consBottom = findViewById(R.id.bottomMove);
        btnDone = findViewById(R.id.btDoneMove);

        databaseService = new DatabaseService();
        loadingDialog = new LoadingDialog(this);
        storageWR = new StorageWR(this);
        mediaWizard = new MediaWizard();
        sessionManager = new SessionManager();

        Log.d("TAG_ANDRA", "Current user " + sessionManager.getId());
        Log.d("TAG_ANDRA", "Current sense garden " + sessionManager.getSenseGarden());

        listeners();
        loadGames();
    }

    @Override
    public void itemClicked(Game game) {
        clickedGame = game;
        if (isEditing) displayPickerDialog();
        else {
            if (clickedGame.isPlaying()) {
                databaseService.setPlaying(new Game("", "", "", "", false), sessionManager.getId(), sessionManager.getSenseGarden());
            } else {
                clickedGame.setPlaying(true);
                databaseService.setPlaying(clickedGame, sessionManager.getId(), sessionManager.getSenseGarden());
            }

            new Handler().postDelayed(this::loadGames, 250);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IMAGE_UPLOAD) {
                Uri filePath = data.getData();

                byte[] imageData = mediaWizard.baosFromUri(filePath);
                storageWR.uploadImage(imageData, GAMES_PATH + filePath.getLastPathSegment(), success -> {
                    clickedGame.setUrlSG(filePath.getLastPathSegment());

                    databaseService.uploadGame(clickedGame, sessionManager.getId());

                    new Handler().postDelayed(this::loadGames, 450);
                });
            }
        }
    }

    private void loadGames() {
        //CredentialsHandler sessionManager = new CredentialsHandler(this);
        databaseService.getGames(sessionManager.getId(), games -> {
            Log.d("TAG_MOVE_TO_IMPROVE", "Loaded images " + games.size());
            if (games.isEmpty()) {
                databaseService.createGames(sessionManager.getId());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        loadGames();
                    }
                }, 450);
            } else {
                loadingDialog.dismiss();
                moveAdapter = new MoveAdapter(MoveToImproveActivity.this, games, MoveToImproveActivity.this);
                new Utils(MoveToImproveActivity.this).setRecyclerThreeSpan(recGames, moveAdapter);
            }
        });
    }

    private void displayPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_image_text, viewGroup, false);
        builder.setView(dialogView);

        dialogView.findViewById(R.id.layoutPhotoText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaWizard.loadFromGallery(MoveToImproveActivity.this);
                dialogImage.dismiss();
            }
        });

        dialogView.findViewById(R.id.layoutText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTextDialog();
                dialogImage.dismiss();
            }
        });

        dialogImage = builder.create();
        dialogImage.show();
    }

    private void displayTextDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_name);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.etGameName);
        dialog.findViewById(R.id.btConfirmName).setOnClickListener(view -> {
            if (!editText.getText().toString().isEmpty()) {
                clickedGame.setName(editText.getText().toString());

                databaseService.uploadGame(clickedGame, sessionManager.getId());

                new Handler().postDelayed(this::loadGames, 450);
            }

            dialog.dismiss();
        });
    }

    private void listeners() {
        findViewById(R.id.tvEditMove).setOnClickListener(view -> {
            isEditing = true;
            consBottom.setVisibility(View.INVISIBLE);
            btnDone.setVisibility(View.VISIBLE);

            databaseService.setPlaying(new Game("", "", "", "", false), sessionManager.getId(), sessionManager.getSenseGarden());

            new Handler().postDelayed(this::loadGames, 80);
        });

        btnDone.setOnClickListener(view -> {
            isEditing = false;
            consBottom.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.bBackMove).setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseService.setPlaying(new Game("", "", "", "", false), sessionManager.getId(), sessionManager.getSenseGarden());
    }
}