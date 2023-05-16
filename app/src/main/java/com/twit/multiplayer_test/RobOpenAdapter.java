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

public class RobOpenAdapter extends RecyclerView.Adapter<RobOpenAdapter.ViewHolder> {
    OnItemClickListener listener;
    ArrayList<Card> cards;

    RobOpenAdapter(ArrayList<Card> cards, OnItemClickListener listener){
        this.cards = cards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View cardView = inf.inflate(R.layout.treasure_card_rv_item, parent, false);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        RobOpenAdapter.ViewHolder vh = new RobOpenAdapter.ViewHolder(cardView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.treasure.setImageResource(MainGame.getResId(card.getResource(), R.drawable.class));
        holder.treasure_text.setText(card.getName());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView treasure;
        TextView treasure_text;

        ViewHolder(View view){
            super(view);
            treasure = view.findViewById(R.id.treasure);
            treasure_text = view.findViewById(R.id.treasure_text);
        }

    }

}
