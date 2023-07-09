package com.sensegarden.sensegardenplaydev.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;
import com.sensegarden.sensegardenplaydev.ui.genericflow.GenericFlowInterface;

import java.util.ArrayList;

public class PickFolderAdapter extends RecyclerView.Adapter<PickFolderAdapter.PickFolderAdapterHolder> {

    private final Context context;
    private ArrayList<Category> folders;
    private GenericFlowInterface genericFlowInterface;

    public PickFolderAdapter(Context context, ArrayList<Category> folders, GenericFlowInterface genericFlowInterface) {
        this.context = context;
        this.folders = folders;
        this.genericFlowInterface = genericFlowInterface;
    }

    @NonNull
    @Override
    public PickFolderAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_folder, parent, false);
        return new PickFolderAdapterHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PickFolderAdapterHolder holder, int position) {
        holder.bind(folders.get(position));

        holder.itemView.setOnClickListener(view -> {
            genericFlowInterface.onFolderToMovePicked(folders.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class PickFolderAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public PickFolderAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tFolderNamePick);
        }

        public void bind(Category category) {
            tvName.setText(category.getName());
        }
    }
}
