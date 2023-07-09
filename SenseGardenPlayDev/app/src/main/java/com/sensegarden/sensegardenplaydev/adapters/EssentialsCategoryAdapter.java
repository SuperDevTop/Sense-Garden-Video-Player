package com.sensegarden.sensegardenplaydev.adapters;

import static com.sensegarden.sensegardenplaydev.utils.Constants.Database.SENSE_GARDENS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.genericflow.Category;
import com.sensegarden.sensegardenplaydev.models.genericflow.SelectableModel;
import com.sensegarden.sensegardenplaydev.ui.genericflow.GenericFlowInterface;
import com.sensegarden.sensegardenplaydev.utils.SessionManager;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;

import java.util.ArrayList;

public class EssentialsCategoryAdapter extends RecyclerView.Adapter<EssentialsCategoryAdapter.CategoryHolder> {
    private Context context;
    private ArrayList<SelectableModel> categories;
    private GenericFlowInterface categoryInterface;

    private int previousSelected = -1;

    public EssentialsCategoryAdapter(Context context, ArrayList<SelectableModel> categories, GenericFlowInterface categoryInterface) {
        this.context = context;
        this.categories = categories;
        this.categoryInterface = categoryInterface;
        this.previousSelected = 0;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_essential_category, null);
        return new CategoryHolder(context, view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, @SuppressLint("RecyclerView") int i) {
        holder.bind(categories.get(i));

        holder.itemView.setOnClickListener(view -> {
            if (i != previousSelected) {
                SelectableModel selectableModel = categories.get(i);
                selectableModel.setSelected(!selectableModel.isSelected());
                categories.remove(i);
                categories.add(i, selectableModel);

                if (previousSelected != -1) {
                    SelectableModel previous = categories.get(previousSelected);
                    previous.setSelected(false);
                    categories.remove(previousSelected);
                    categories.add(previousSelected, previous);
                }

                previousSelected = i;
                notifyDataSetChanged();

                Category category = (Category) selectableModel.getValue();
                categoryInterface.onFolderClicked(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory;
        private ImageView imgCategory;
        private ConstraintLayout itemCategory;

        private Context context;
        private SessionManager sessionManager;

        public CategoryHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
            this.sessionManager = new SessionManager();
            tvCategory = itemView.findViewById(R.id.tTitleCategory);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            itemCategory = itemView.findViewById(R.id.itemEssentialCategory);
        }

        void bind(SelectableModel selectableModel) {
            if (selectableModel.isSelected())
                itemCategory.setBackgroundColor(Color.parseColor("#4c7a34"));
            else itemCategory.setBackgroundColor(Color.WHITE);

            Category category = (Category) selectableModel.getValue();
            new StorageWR(context).loadImage(SENSE_GARDENS + "/" + sessionManager.getSenseGarden() + "/" + category.getName() + "/" + category.getUrl(), imgCategory);

//            if (!category.getUrl().equals("all"))
//                imgCategory.setRotation(90f);
            tvCategory.setText(category.getName());
        }
    }
}
