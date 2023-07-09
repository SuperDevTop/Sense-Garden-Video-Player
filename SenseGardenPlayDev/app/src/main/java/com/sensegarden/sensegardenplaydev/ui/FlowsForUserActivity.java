package com.sensegarden.sensegardenplaydev.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sensegarden.sensegardenplaydev.adapters.FlowsForUserAdapter;
import com.sensegarden.sensegardenplaydev.databinding.ActivityFlowsForUserBinding;
import com.sensegarden.sensegardenplaydev.models.user.Flow;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;

public class FlowsForUserActivity extends AppCompatActivity {
    private ActivityFlowsForUserBinding binding;

    private final String tag = "TAG_FLOWS_FOR_USER";
    private final Context context = this;

    private String currentId = new SessionManager().getId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlowsForUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("userId")) {
            currentId = getIntent().getExtras().getString("userId");
            Glide.with(this)
                    .load(getIntent().getExtras().getString("userAvatar"))
                    .into(binding.imgFlowsForUserAvatar);
            binding.tvFlowsForUserName.setText("Flows for " + getIntent().getExtras().getString("userName"));
        } else {
            Glide.with(this)
                    .load(new SessionManager().getAvatar())
                    .into(binding.imgFlowsForUserAvatar);
            binding.tvFlowsForUserName.setText("Flows for " + new SessionManager().getName());
        }

        init();
    }

    private void fillRecycler() {
        FirebaseFirestore.getInstance().collection("users").document(currentId).collection("flows").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Flow> flows = new ArrayList<>();
                        FlowsForUserAdapter flowsForUserAdapter = new FlowsForUserAdapter(flows, context);
                        new Utils(this).setRecycler(binding.rvFlowsForUser, flowsForUserAdapter);

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            flows.add(snapshot.toObject(Flow.class));
                            flowsForUserAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void init() {
        binding.btnBackFlowsForUser.setOnClickListener(view -> finish());

        fillRecycler();
    }
}