package com.sensegarden.sensegardenplaydev.database;

import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.FOLDERS;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.GAMES;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.SENSE_GARDENS;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.SENSE_GARDEN_ESSENTIALS_PATH;
import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.USERS_PATH;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.Game;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;
import com.sensegarden.sensegardenplaydev.models.user.Caregiver;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.ui.CaregiverActivity;
import com.sensegarden.sensegardenplaydev.utils.Constants;
import com.sensegarden.sensegardenplaydev.utils.CustomProgressDialog;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DatabaseService {

    private String tag = "DATABASE_SERVICE";

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private boolean shouldReadGames = false;

    public DatabaseService() {
    }

    public void createCategory(String sgId, Category category) {
        firebaseFirestore.collection(SENSE_GARDENS).document(sgId).collection(FOLDERS).document(category.getName()).set(category);
    }

    public void getCategories(String sgId, DatabaseInterfaces.EssentialsCategoryCallback essentialsCategoryCallback) {
        firebaseFirestore.collection(SENSE_GARDENS).document(sgId).collection(FOLDERS).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Category> categories = new ArrayList<>();
                        QuerySnapshot result = task.getResult();

                        if (result != null) {
                            List<DocumentSnapshot> documentSnapshots = result.getDocuments();
                            Log.d(tag, "Results " + documentSnapshots.size());
                            if (documentSnapshots.size() == 0) {
                                essentialsCategoryCallback.onCallback(null);
                                return;
                            } else {
                                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                    Category category = documentSnapshot.toObject(Category.class);
                                    categories.add(category);
                                }
                            }
                        }
                        essentialsCategoryCallback.onCallback(categories);
                    } else essentialsCategoryCallback.onCallback(null);
                });
    }

    public void loginCaregiver(Context context, String email, String password) {
        CustomProgressDialog customProgressDialog = new CustomProgressDialog();
        customProgressDialog.show(context, "Loading...");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    customProgressDialog.dismiss();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (task.isSuccessful() && currentUser != null) {
                        firebaseFirestore.collection(USERS_PATH).document(currentUser.getUid()).get()
                                .addOnCompleteListener(taskCaregiver -> {
                                    if (taskCaregiver.isSuccessful()) {
                                        DocumentSnapshot result = taskCaregiver.getResult();
                                        if (result != null) {
                                            Caregiver caregiver = result.toObject(Caregiver.class);
                                            if (caregiver != null) {
                                                new SessionManager().createCaregiver(caregiver);
                                                Utils utils = new Utils(context);
                                                utils.intent(CaregiverActivity.class, null);
                                            }
                                        }
                                    }
                                });
                    } else {
                        try {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            parseError(context, errorCode);
                        } catch (Exception e) {
                            new Utils(context).displayToast("No network connection.");
                        }
                    }
                });
    }

    private void parseError(Context context, String errorCode) {
        String error = "Unknown Error";
        switch (errorCode) {
            case "ERROR_INVALID_CREDENTIAL":
                error = context.getString(R.string.ERROR_INVALID_CREDENTIAL);
                break;

            case "ERROR_INVALID_EMAIL":
                error = context.getString(R.string.ERROR_INVALID_EMAIL);
                break;

            case "ERROR_WRONG_PASSWORD":
                error = context.getString(R.string.ERROR_WRONG_PASSWORD);
                break;

            case "ERROR_USER_MISMATCH":
                error = context.getString(R.string.ERROR_USER_MISMATCH);
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                error = context.getString(R.string.ERROR_REQUIRES_RECENT_LOGIN);
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                error = context.getString(R.string.ERROR_EMAIL_ALREADY_IN_USE);
                break;

            case "ERROR_USER_DISABLED":
                error = context.getString(R.string.ERROR_USER_DISABLED);
                break;

            case "ERROR_USER_NOT_FOUND":
                error = context.getString(R.string.ERROR_USER_NOT_FOUND);
                break;

            case "ERROR_WEAK_PASSWORD":
                error = context.getString(R.string.ERROR_WEAK_PASSWORD);
                break;
        }
        new Utils(context).displayToast(error);
    }

    public void getEssentials(DatabaseInterfaces.EssentialsCallback essentialsCallback) {
        firebaseFirestore.collection(SENSE_GARDEN_ESSENTIALS_PATH).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<PhotoVideo> photoVideos = new ArrayList<>();
                        QuerySnapshot result = task.getResult();
                        if (result != null) {
                            List<DocumentSnapshot> documentSnapshots = result.getDocuments();
                            Log.d(tag, "Results " + documentSnapshots.size());
                            for (DocumentSnapshot documentSnapshot : documentSnapshots)
                                photoVideos.add(documentSnapshot.toObject(PhotoVideo.class));

                            essentialsCallback.onCallback(photoVideos);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        essentialsCallback.onCallback(null);
                    }
                });
    }

    public void signInAnonymously(DatabaseInterfaces.AnonymousCallback anonymousCallback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                anonymousCallback.onCallback(true);
            }
        });
    }

    public void updatePair(String sgId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sense_garden_id", sgId);
        hashMap.put("time", new Date());
        firebaseFirestore.collection(GAMES).document("pairing").set(hashMap);
    }

    public void getGames(String userId, DatabaseInterfaces.GamesCallback gamesCallback) {
        shouldReadGames = true;
        firebaseFirestore.collection(GAMES).document(userId).collection(userId).get()
                .addOnCompleteListener(task -> {
                    if (shouldReadGames) {
                        shouldReadGames = false;
                        if (task.isSuccessful()) {
                            ArrayList<Game> games = new ArrayList<>();
                            QuerySnapshot result = task.getResult();

                            if (result != null) {
                                List<DocumentSnapshot> documentSnapshots = result.getDocuments();
                                Log.d(tag, "Results " + documentSnapshots.size());
                                if (documentSnapshots.size() == 0) {
                                    gamesCallback.onCallback(new ArrayList<>());
                                    return;
                                } else {
                                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                        Game game = documentSnapshot.toObject(Game.class);

                                        Log.d(tag, "Adding game!");

                                        if (game != null) games.add(game);
                                        else
                                            games.add(new Game("id", "name ko", "urlko", "", false));
                                    }
                                }
                            }

                            gamesCallback.onCallback(games);
                        }
                    }
                });
    }

    public void setPlaying(Game game, String userId, String sgId) {
        if (game.getId().equals("")) {
            firebaseFirestore.collection("sense_gardens").document(sgId).update("current_game", null);
            updatePair(sgId);
        }

        for (int i = 0; i < 9; i++) {
            if (game.getId().equals(String.valueOf(i))) {
                firebaseFirestore.collection(GAMES).document(userId).collection(userId).document(String.valueOf(i)).update("playing", true);
                firebaseFirestore.collection("sense_gardens").document(sgId).update("current_game", game);
            } else
                firebaseFirestore.collection(GAMES).document(userId).collection(userId).document(String.valueOf(i)).update("playing", false);
        }
    }

    public void createGames(String userId) {
        for (int i = 0; i < 9; i++)
            uploadGame(new Game(String.valueOf(i), "game " + (i + 1), "", Constants.MoveGames.GAME_LINKS.get(i), false), userId);
    }

    public void uploadGame(Game game, String userId) {
        firebaseFirestore.collection(GAMES).document(userId).collection(userId).document(game.getId()).set(game);
    }
}
