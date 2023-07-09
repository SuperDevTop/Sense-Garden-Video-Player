package com.sensegarden.sensegardenplaydev.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sensegarden.sensegardenplaydev.databinding.ActivityUserInCaregiverBinding;
import com.sensegarden.sensegardenplaydev.models.user.PrimaryUser;
import com.sensegarden.sensegardenplaydev.ui.movetoimprove.MoveToImproveActivity;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

public class UserInCaregiverActivity extends AppCompatActivity {
    private ActivityUserInCaregiverBinding binding;

    Utils utils = new Utils(this);
    PrimaryUser primaryUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInCaregiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().hasExtra("user"))
            primaryUser = (PrimaryUser) getIntent().getExtras().getSerializable("user");

        init();
    }

    private void setData(){
        String tempName = new SessionManager().getName();
        String tempType = new SessionManager().getType();
        String tempQR = new SessionManager().getQR();
        String tempAvatar = new SessionManager().getAvatar();

        if(primaryUser!=null){
            tempName = primaryUser.getFull_name();
            tempType = primaryUser.getType();
            tempQR = primaryUser.getQr_code();
            tempAvatar = primaryUser.getPhoto();
        }

        binding.tvUserInCaregiverName.setText(tempName);
        binding.tvUserInCaregiverType.setText(tempType);
        Glide.with(this)
                .load(tempQR)
                .into(binding.imgUserInCaregiverQRCodeExample);
        Glide.with(this)
                .load(tempAvatar)
                .into(binding.imgUserInCaregiverAvatar);
    }


    private void listeners(){
        binding.btnUserInCaregiverEndPlay.setOnClickListener(view -> finish());

        binding.btnUserInCaregiverMoveToImprove.setOnClickListener(view -> utils.intent(MoveToImproveActivity.class,null));

        binding.btnUserInCaregiverPlayAFlow.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            if(primaryUser!=null) {
                bundle.putString("userId",primaryUser.getId());
                bundle.putString("userAvatar",primaryUser.getPhoto());
                bundle.putString("userName",primaryUser.getFull_name());
            }
            utils.intent(FlowsForUserActivity.class,bundle);
        });

        binding.btnUserInCaregiverSenseGardenEssentials.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            if(primaryUser != null)
                bundle.putString("userId",primaryUser.getId());
            utils.intent(EssentialsActivity.class, bundle);
        });

    }

    private void init(){
        listeners();
        setData();
    }
}