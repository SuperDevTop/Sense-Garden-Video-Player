package com.sensegarden.sensegardenplaydev.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.databinding.ActivityLoginUserBinding;
import com.sensegarden.sensegardenplaydev.models.user.PrimaryUser;
import com.sensegarden.sensegardenplaydev.ui.VideoPlayerActivity;
import com.sensegarden.sensegardenplaydev.utils.CustomProgressDialog;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class UserLoginActivity extends AppCompatActivity {
    private ActivityLoginUserBinding binding;
    private CodeScanner mCodeScanner;
    private final Utils utils = new Utils(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }


    private void startScanner() {
        CodeScannerView scannerView = findViewById(R.id.scannerViewQRLogin);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            CustomProgressDialog dialog = new CustomProgressDialog();
            dialog.show(this, "Loading...");
            FirebaseFirestore.getInstance().collection("users").document(result.getText()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        //not manager
                        String status = document.getData().get("status").toString();
                        if (!status.equals("deactivated")) {
                            //active user
                            new SessionManager().createPrimaryUser(document.toObject(PrimaryUser.class));
                            utils.intent(VideoPlayerActivity.class, null);
                        } else {
                            //deactivated user
                            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_qrlogin), "This account is deactivated.", Snackbar.LENGTH_INDEFINITE);
                            mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                            mSnackBar.show();
                        }
                    } else {
                        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_qrlogin), "Invalid QR-code", Snackbar.LENGTH_INDEFINITE);
                        mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                        mSnackBar.show();
                        mCodeScanner.startPreview();
                    }
                } else {
                    Snackbar mSnackBar = Snackbar.make(findViewById(R.id.activity_qrlogin), "Invalid QR-code", Snackbar.LENGTH_INDEFINITE);
                    mSnackBar.setAction("OK", view -> mSnackBar.dismiss());
                    mSnackBar.show();
                    mCodeScanner.startPreview();
                }
                dialog.dismiss();
            });
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    private void init() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            startScanner();
        else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length == 0) return;
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) return;

            startScanner();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null)
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null)
            mCodeScanner.releaseResources();
        super.onPause();
    }
}