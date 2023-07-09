package com.sensegarden.sensegardenplaydev.ui;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sensegarden.sensegardenplaydev.databinding.ActivityStartBinding;
import com.sensegarden.sensegardenplaydev.ui.login.LoginActivity;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        Utils utils = new Utils(this);
        binding.btnStart.setOnClickListener(view -> utils.intent(LoginActivity.class, null));

        ActivityCompat.requestPermissions(StartActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

    }
}