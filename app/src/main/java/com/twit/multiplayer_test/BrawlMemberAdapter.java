package com.twit.multiplayer_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BrawlMemberAdapter extends RecyclerView.Adapter<BrawlMemberAdapter.ViewHolder> {
    ArrayList<BrawlMember> brawlMembers;

    BrawlMemberAdapter(ArrayList<BrawlMember> brawlMembers){
        this.brawlMembers = brawlMembers;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BrawlMember brawlMember = brawlMembers.get(position);
        holder.brawlerName.setText(brawlMember.getMember().getName() + " (" + brawlMember.getMember().getStats().getRole() + ")");
        String brawlerPower = "Сила: ";
        brawlerPower += brawlMember.getMember().getStats().getPower();
        for (Card q: brawlMember.getMember().getTreasures().getOpen()) {
            if (q.getName().equals("Нож")) {
                brawlerPower += "+3";
            }
            if (q.getName().equals("Дубинка")) {
                brawlerPower += "+2";
            }
            if (q.getName().equals("Весло")) {
                brawlerPower += "+1";
            }
            if (q.getName().equals("Гарпун")) {
                brawlerPower += "+4";
            }
            if (q.getName().equals("Сигнальный пистолет")) {
                brawlerPower += "+8";
            }
        }
        holder.brawlerPower.setText(brawlerPower);
        if (brawlMember.isReady()){
            holder.brawlerReady.setVisibility(View.VISIBLE);
        }
        else{
            holder.brawlerReady.setVisibility(View.GONE);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View brawlMemberView = inf.inflate(R.layout.brawl_member_item, parent, false);
        BrawlMemberAdapter.ViewHolder vh = new BrawlMemberAdapter.ViewHolder(brawlMemberView);
        return vh;
    }

    @Override
    public int getItemCount() {
        return brawlMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView brawlerName;
        TextView brawlerPower;
        ImageView brawlerReady;

        ViewHolder(View view){
            super(view);
            brawlerName = view.findViewById(R.id.brawl_name);
            brawlerPower = view.findViewById(R.id.brawl_power);
            brawlerReady = view.findViewById(R.id.brawl_tick);
        }
    }
}
