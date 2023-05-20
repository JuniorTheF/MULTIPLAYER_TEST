package com.twit.multiplayer_test;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private List<Member> members;
    private OnItemClickListener listener;
    private OnItemClickListener nickListener;



    HeroAdapter(List<Member> members, OnItemClickListener listener, OnItemClickListener nickListener) {
        this.members = members;
        this.listener = listener;
        this.nickListener = nickListener;
    }

    @NonNull
    @Override
    public HeroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View heroView = inf.inflate(R.layout.hero_item, parent, false);
        heroView.findViewById(R.id.constraint_hero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        heroView.findViewById(R.id.hero_nick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickListener.onItemClick(view);
            }
        });

        HeroAdapter.ViewHolder vh = new HeroAdapter.ViewHolder(heroView);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull HeroAdapter.ViewHolder holder, int position) {
        Member member = members.get(position);
        if (member.getState().getBrawled().equals(1)){
            holder.fight.setVisibility(View.VISIBLE);
        }
        else{
            holder.fight.setVisibility(View.GONE);
        }
        if (member.getState().getPulled().equals(1)){
            holder.work.setVisibility(View.VISIBLE);
        }
        else{
            holder.work.setVisibility(View.GONE);
        }
        if (member.getState().getInjuries().equals(0)){
            holder.trauma.setVisibility(View.GONE);
        }
        else{
            holder.trauma.setVisibility(View.VISIBLE);
            holder.trauma.setText(""+member.getState().getInjuries());
        }
        if (member.getState().getThirst().equals(0)){
            holder.thirst.setVisibility(View.GONE);
        }
        else{
            holder.thirst.setVisibility(View.VISIBLE);
            holder.thirst.setText(""+member.getState().getThirst());
        }
        boolean haveZhilet = false;
        boolean haveZont = false;
        for (Card q: member.getTreasures().getOpen()){
            if (q.getName().equals("Спасательный жилет")){
                haveZhilet = true;
            }
            if (q.getName().equals("Зонтик")){
                haveZont = true;
            }
        }
        if (haveZhilet){
            holder.zhilet.setVisibility(View.VISIBLE);
        }
        else{
            holder.zhilet.setVisibility(View.GONE);
        }
        if (haveZont){
            holder.zont.setVisibility(View.VISIBLE);
        }
        else{
            holder.zont.setVisibility(View.GONE);
        }
        holder.power.setText(""+member.getStats().getPower());
        String status = "";
        switch (member.getState().getStatus()){
            case "alive":
                status = "жив";
                break;
            case "dead":
                status = "мертв";
                break;
            case "unconscious":
                status = "без сознания";
                break;

        }
        if (member.getState().getOverboard().equals(1)){
            status+=" за бортом";
        }
        holder.nick.setText(member.getName()+"\n"+status);
        holder.nick2.setText(member.getName());
        switch (member.getStats().getRole()){
            case "Миледи":
                holder.cl.setBackgroundResource(R.drawable.miledi_pic);
                break;
            case "Шкет":
                holder.cl.setBackgroundResource(R.drawable.shket_pic);
                break;
            case "Черпак":
                holder.cl.setBackgroundResource(R.drawable.cherpak_pic);
                break;
            case "Капитан":
                holder.cl.setBackgroundResource(R.drawable.kapitan_pic);
                break;
            case "Боцман":
                holder.cl.setBackgroundResource(R.drawable.botsman_pic);
                break;
            case "Сноб":
                holder.cl.setBackgroundResource(R.drawable.snob_pic);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView power;
        TextView trauma;
        ImageView fight;
        ImageView work;
        TextView nick;
        TextView nick2;
        ImageView zhilet;
        ImageView zont;
        TextView thirst;
        ConstraintLayout cl;

        public ViewHolder(View view){
            super(view);
            nick = view.findViewById(R.id.hero_nick);
            nick2 = view.findViewById(R.id.hero_nick2);
            power = view.findViewById(R.id.power);
            trauma = view.findViewById(R.id.trauma);
            fight = view.findViewById(R.id.fight_tiredness);
            work = view.findViewById(R.id.work_tiredness);
            cl = view.findViewById(R.id.constraint_hero);
            thirst = view.findViewById(R.id.thirst);
            zhilet = view.findViewById(R.id.zhilet);
            zont = view.findViewById(R.id.zont);
        }


    }
}
