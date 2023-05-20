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

public class PullAdapter extends RecyclerView.Adapter<PullAdapter.ViewHolder> {

    ArrayList<PullItem> pullItems;
    OnItemClickListener listener;

    PullAdapter(ArrayList<PullItem> pullItems, OnItemClickListener listener){
        this.pullItems = pullItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View pullItemView = inf.inflate(R.layout.pull_item, parent, false);
        pullItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        PullAdapter.ViewHolder vh = new PullAdapter.ViewHolder(pullItemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PullItem toShow = pullItems.get(position);
        holder.posit.setText(""+position);
        holder.pullNavImg.setImageResource(MainGame.getResId(toShow.getNavCard().getResource(), R.drawable.class));
        if (toShow.isChoosen()){
            holder.tick.setVisibility(View.VISIBLE);
        }
        else{
            holder.tick.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return pullItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pullNavImg;
        TextView posit;
        ImageView tick;

        ViewHolder(View view){
            super(view);
            posit = view.findViewById(R.id.pull_position);
            pullNavImg = view.findViewById(R.id.pull_nav_img);
            tick = view.findViewById(R.id.pull_tick);
        }
    }

}
