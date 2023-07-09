package com.sensegarden.sensegardenplaydev.ui.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.database.DatabaseService;
import com.sensegarden.sensegardenplaydev.databinding.ActivityLoginCaregiverBinding;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.Objects;

public class CaregiverLoginActivity extends AppCompatActivity {
    private ActivityLoginCaregiverBinding binding;
    private final Utils utils = new Utils(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginCaregiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void displayResetDialog() {
        Dialog dialogche = new Dialog(this);
        dialogche.setContentView(R.layout.popup_reset_password);
        dialogche.show();

        EditText etEmailReset = dialogche.findViewById(R.id.etResetEmail);

        dialogche.findViewById(R.id.btReset).setOnClickListener(view -> {
            String emailText = etEmailReset.getText().toString().trim();
            if (!emailText.equals(""))
                resetPassword(dialogche, emailText);
            else {
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.actCaregLog), "Please enter your email", Snackbar.LENGTH_INDEFINITE);
                mSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSnackBar.dismiss();
                    }
                });
                mSnackBar.show();
            }
        });
    }

    private void resetPassword(Dialog dialogche, String email) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isNewUser = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty();

                        if (!isNewUser) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful())
                                            Toast.makeText(CaregiverLoginActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();

                                        dialogche.dismiss();
                                    });
                        } else
                            Toast.makeText(CaregiverLoginActivity.this, "This user does not exist", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(CaregiverLoginActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                });
    }

    private void init() {
        binding.btnCaregiverLogin.setOnClickListener(view -> {
//            String email = "caregiver@gmail.com";
//            String password = "test123";
//            new DatabaseService().loginCaregiver(this, email, password);

            String email = utils.getETText(binding.etCaregiverLoginEmail);
            String password = utils.getETText(binding.etCaregiverLoginPass);

            if (valid()) new DatabaseService().loginCaregiver(this, email, password);
        });

        binding.tvForgot.setOnClickListener(view -> displayResetDialog());
    }

    private boolean valid() {
        boolean all_good = true;

        if (!binding.etCaregiverLoginEmail.getText().toString().equals("")) {
            if (!binding.etCaregiverLoginEmail.getText().toString().contains("@") && !binding.etCaregiverLoginEmail.getText().toString().contains(".")) {
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.actCaregLog), R.string.invalid_email, Snackbar.LENGTH_INDEFINITE);
                mSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSnackBar.dismiss();
                    }
                });
                mSnackBar.show();
                all_good = false;
            }
        }
        if (binding.etCaregiverLoginPass.getText().toString().equals("")) {
            binding.etCaregiverLoginPass.setError("");
            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.actCaregLog), R.string.mandatory_field, Snackbar.LENGTH_INDEFINITE);
            mSnackBar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSnackBar.dismiss();
                }
            });
            mSnackBar.show();
            all_good = false;
        } else if (binding.etCaregiverLoginPass.getText().toString().length() <= 6) {
            Snackbar mSnackBar = Snackbar.make(findViewById(R.id.actCaregLog), R.string.password_short, Snackbar.LENGTH_INDEFINITE);
            mSnackBar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSnackBar.dismiss();
                }
            });
            mSnackBar.show();
            all_good = false;
        }

        return all_good;
    }
}