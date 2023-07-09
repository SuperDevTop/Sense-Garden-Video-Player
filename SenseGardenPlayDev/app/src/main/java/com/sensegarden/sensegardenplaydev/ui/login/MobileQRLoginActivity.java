package com.sensegarden.sensegardenplaydev.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sensegarden.sensegardenplaydev.databinding.ActivityLoginMobileQrBinding;

public class MobileQRLoginActivity extends AppCompatActivity {
    private ActivityLoginMobileQrBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginMobileQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        //TODO qr code creation
    }
}