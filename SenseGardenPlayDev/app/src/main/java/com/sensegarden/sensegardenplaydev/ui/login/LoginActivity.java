package com.sensegarden.sensegardenplaydev.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sensegarden.sensegardenplaydev.databinding.ActivityLoginBinding;
import com.sensegarden.sensegardenplaydev.ui.EssentialsActivity;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private final Utils utils = new Utils(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        binding.btnLoginUser.setOnClickListener(view -> utils.intent(UserLoginActivity.class, null));
        binding.btnLoginCaregiver.setOnClickListener(view -> utils.intent(CaregiverLoginActivity.class, null));
        binding.btnLoginFreeplay.setOnClickListener(view -> utils.intent(EssentialsActivity.class, null));
    }
}