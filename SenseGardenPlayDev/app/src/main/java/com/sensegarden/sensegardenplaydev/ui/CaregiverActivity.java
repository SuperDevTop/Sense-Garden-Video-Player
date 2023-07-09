package com.sensegarden.sensegardenplaydev.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sensegarden.sensegardenplaydev.adapters.CaregiverAdapter;
import com.sensegarden.sensegardenplaydev.databinding.ActivityCaregiverBinding;
import com.sensegarden.sensegardenplaydev.models.user.PrimaryUser;
import com.sensegarden.sensegardenplaydev.ui.genericflow.GenericFlowActivity;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;

public class CaregiverActivity extends AppCompatActivity {
    private ActivityCaregiverBinding binding;

    private String tag = "TAG_CAREGIVER_ACTIVITY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaregiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void fillRecycler() {
        FirebaseFirestore.getInstance().collection("users").document(new SessionManager().getId()).collection("primary_users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        ArrayList<PrimaryUser> primaryUsers = new ArrayList<>();
                        CaregiverAdapter caregiverAdapter = new CaregiverAdapter(primaryUsers, this);
                        new Utils(this).setRecycler(binding.rvPrimaryUsersCaregiver, caregiverAdapter);

                        for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("users").document(documentSnapshots.getId()).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        primaryUsers.add(documentSnapshot.toObject(PrimaryUser.class));
                                        caregiverAdapter.notifyDataSetChanged();
                                    });
                        }
                    }
                });
    }

    private void fillData() {
        Glide.with(this)
                .load(new SessionManager().getPhoto())
                .into(binding.imgCaregiverAvatar);

        binding.tvUserInCaregiverName.setText(new SessionManager().getName());
        binding.tvCaregiverDescription.setText(new SessionManager().getType());
    }

    private void listeners() {
        binding.btnExitCaregiver.setOnClickListener(view -> finish());

        binding.btGoGeneric.setOnClickListener(view -> new Utils(this).intent(GenericFlowActivity.class, null));
    }

    private void init() {
        listeners();

        fillRecycler();

        fillData();
    }
}