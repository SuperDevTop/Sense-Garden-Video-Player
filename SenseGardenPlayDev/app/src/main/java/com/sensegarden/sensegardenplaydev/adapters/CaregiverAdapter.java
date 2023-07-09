package com.sensegarden.sensegardenplaydev.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.user.PrimaryUser;
import com.sensegarden.sensegardenplaydev.ui.UserInCaregiverActivity;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class CaregiverAdapter extends RecyclerView.Adapter<CaregiverAdapter.CaregiverAdapterHolder> {

    private final ArrayList<PrimaryUser> primaryUsers;
    private final Context context;

    public CaregiverAdapter(ArrayList<PrimaryUser> primaryUsers, Context context) {
        this.primaryUsers = primaryUsers;
        this.context = context;
    }

    static class CaregiverAdapterHolder extends RecyclerView.ViewHolder {
        private final ImageView imgItemCaregiverStar = itemView.findViewById(R.id.imgItemCaregiverStar);
        private final ImageView imgItemCaregiverAvatar = itemView.findViewById(R.id.imgItemCaregiverAvatar);
        private final TextView tvItemCaregiverName = itemView.findViewById(R.id.tvItemCaregiverName);

        public CaregiverAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Context context, PrimaryUser primaryUser) {
            Glide.with(itemView)
                    .load(primaryUser.getPhoto())
                    .into(imgItemCaregiverAvatar);
            tvItemCaregiverName.setText(primaryUser.getFull_name());
            if (!Objects.equals(primaryUser.getMain_contact(), "null")) {
                imgItemCaregiverStar.setImageResource(R.drawable.star);
            } else {
                imgItemCaregiverStar.setVisibility(View.GONE);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", primaryUser);
            itemView.setOnClickListener(view -> new Utils(context).intent(UserInCaregiverActivity.class, bundle));
        }

    }

    @NonNull
    @Override
    public CaregiverAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_caregiver, null);
        return new CaregiverAdapterHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CaregiverAdapterHolder holder, int position) {
        holder.bind(context, primaryUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return primaryUsers.size();
    }
}
