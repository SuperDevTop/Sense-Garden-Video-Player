package com.sensegarden.sensegardenplaydev.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sensegarden.sensegardenplaydev.databinding.ActivityLoginMobileBinding;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class MobileLoginActivity extends AppCompatActivity {
    private ActivityLoginMobileBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginMobileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void nextBtnListener() {
        Utils utils = new Utils(this);
        binding.btnNextLoginMobile.setOnClickListener(view -> utils.intent(MobileQRLoginActivity.class, null));
    }

    private void listeners() {
        nextBtnListener();
    }

    private void init() {
        listeners();
    }
}