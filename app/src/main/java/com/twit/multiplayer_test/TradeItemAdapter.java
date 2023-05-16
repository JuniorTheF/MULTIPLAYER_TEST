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

public class TradeItemAdapter extends RecyclerView.Adapter<TradeItemAdapter.ViewHolder> {

    ArrayList<TradeItem> items;
    OnItemClickListener listener;

    TradeItemAdapter(ArrayList<TradeItem> items, OnItemClickListener listener){
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View tradeItemView = inf.inflate(R.layout.trade_item, parent, false);
        tradeItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        TradeItemAdapter.ViewHolder vh = new TradeItemAdapter.ViewHolder(tradeItemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TradeItem tradeItem = items.get(position);
        holder.name.setText(tradeItem.getCard().getName());
        holder.tradePosition.setText(""+position);
        if (tradeItem.isChoosen()){
            holder.tick.setVisibility(View.VISIBLE);
        }
        else{
            holder.tick.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView tradePosition;
        ImageView tick;
        public ViewHolder(View view){
            super(view);
            tradePosition = view.findViewById(R.id.trade_position);
            name = view.findViewById(R.id.trade_item_name);
            tick = view.findViewById(R.id.trade_tick);
        }


    }
}
