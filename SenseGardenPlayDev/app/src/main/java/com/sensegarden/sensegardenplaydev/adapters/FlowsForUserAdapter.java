package com.sensegarden.sensegardenplaydev.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.user.Flow;
import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;
import com.sensegarden.sensegardenplaydev.ui.VideoPlayerActivity;
import com.sensegarden.sensegardenplaydev.utils.Utils;

import java.util.ArrayList;

public class FlowsForUserAdapter extends RecyclerView.Adapter<FlowsForUserAdapter.FlowsForUserAdapterHolder> {

    private final ArrayList<Flow> flows;
    private final Context context;

    private String tag = "TAG_FLOWS_FOR_USER_ADAPTER";

    public FlowsForUserAdapter(ArrayList<Flow> flows, Context context) {
        this.flows = flows;
        this.context = context;
    }

    static class FlowsForUserAdapterHolder extends RecyclerView.ViewHolder {
        private String tag = "TAG_FLOWS_FOR_USER_ADAPTER_HOLDER";

        private final ImageView imgFlowThumbnail = itemView.findViewById(R.id.imgFlowThumbnail);
        private final TextView tvFlowTitle = itemView.findViewById(R.id.tvFlowTitle);
        private final TextView tvFlowDescription = itemView.findViewById(R.id.tvFlowDescription);
        private final Button btnFlowPlay = itemView.findViewById(R.id.btnFlowPlay);

        public FlowsForUserAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Context context, Flow flow) {
            if (flow.getPhotos() != null) {
                FirebaseFirestore.getInstance().collection("users").document(flow.getDefined_for()).collection("photos").document(flow.getPhotos().get(0)).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Glide.with(context)
                                    .load(documentSnapshot.toObject(PhotoVideo.class).getUri())
                                    .into(imgFlowThumbnail);
                        });
            }
            tvFlowTitle.setText(flow.getType());
            tvFlowDescription.setText(flow.getName());
            btnFlowPlay.setOnClickListener(view -> {
                Utils utils = new Utils(context);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flow", flow);
                utils.intent(VideoPlayerActivity.class, bundle);
            });
        }
    }

    @NonNull
    @Override
    public FlowsForUserAdapter.FlowsForUserAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flows_for_user, null);
        return new FlowsForUserAdapter.FlowsForUserAdapterHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowsForUserAdapter.FlowsForUserAdapterHolder holder, int position) {
        holder.bind(context, flows.get(position));
    }

    @Override
    public int getItemCount() {
        return flows.size();
    }
}
