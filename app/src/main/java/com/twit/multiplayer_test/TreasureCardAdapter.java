package com.twit.multiplayer_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TreasureCardAdapter extends RecyclerView.Adapter<TreasureCardAdapter.ViewHolder>{
    ArrayList<Card> treasures;
    OnItemClickListener listener;

    TreasureCardAdapter(ArrayList<Card> treasures, OnItemClickListener listener){
        this.treasures = treasures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View treasureView = inf.inflate(R.layout.treasure_card_rv_item, parent, false);
        treasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        TreasureCardAdapter.ViewHolder vh = new TreasureCardAdapter.ViewHolder(treasureView);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card treas = treasures.get(position);
        holder.treasure_text.setText(treas.getName());
        holder.treasure.setImageResource(MainGame.getResId(treas.getResource(), R.drawable.class));
    }

    @Override
    public int getItemCount() {
        return treasures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView treasure;
        TextView treasure_text;
        public ViewHolder(View view){
            super(view);
            treasure = view.findViewById(R.id.treasure);
            treasure_text = view.findViewById(R.id.treasure_text);
        }
    }
}
