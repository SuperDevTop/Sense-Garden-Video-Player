package com.sensegarden.sensegardenplaydev.adapters;

import static com.sensegarden.sensegardenplaydev.utils.Constants.Storage.GAMES_PATH;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sensegarden.sensegardenplaydev.R;
import com.sensegarden.sensegardenplaydev.models.Game;
import com.sensegarden.sensegardenplaydev.ui.movetoimprove.MoveContract;
import com.sensegarden.sensegardenplaydev.utils.StorageWR;

import java.util.ArrayList;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MoveHolder> {
    private ArrayList<Game> games;
    private Context context;
    private MoveContract moveContract;

    private int previousClicked = -1;

    public MoveAdapter(Context context, ArrayList<Game> urls, MoveContract moveContract) {
        this.context = context;
        this.games = urls;
        this.moveContract = moveContract;
    }

    @NonNull
    @Override
    public MoveAdapter.MoveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_move, parent, false);
        return new MoveHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveAdapter.MoveHolder holder, int i) {
        holder.bind(games.get(i));

        holder.itemView.setOnClickListener(view -> {
            moveContract.itemClicked(games.get(i));
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class MoveHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final ImageView imgMove;
        private final ConstraintLayout consMove;

        private final StorageWR storageWR;
        private final Context context;

        public MoveHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tGameTitle);
            imgMove = itemView.findViewById(R.id.imgMove);
            consMove = itemView.findViewById(R.id.itemMove);

            this.context = context;
            storageWR = new StorageWR(context);
        }

        public void bind(Game game) {
            if (game.isPlaying())
                consMove.setBackground(ContextCompat.getDrawable(context, R.drawable.bordered_rect));
            else consMove.setBackground(null);

            if (game.getUrlSG() == null) {
                imgMove.setVisibility(View.GONE);
                tvName.setText("empty");
            } else if (!game.getUrlSG().equals(""))
                storageWR.loadImage(GAMES_PATH + game.getUrlSG(), imgMove);

            tvName.setText(game.getName());
        }
    }
}