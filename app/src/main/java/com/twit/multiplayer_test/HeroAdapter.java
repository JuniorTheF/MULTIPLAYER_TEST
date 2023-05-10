package com.twit.multiplayer_test;

import android.content.Context;
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



    HeroAdapter(List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public HeroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View heroView = inf.inflate(R.layout.hero_item, parent, false);
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
        holder.power.setText(""+member.getStats().getPower());
        holder.nick.setText(member.getName());
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
        ConstraintLayout cl;

        public ViewHolder(View view){
            super(view);
            nick = view.findViewById(R.id.hero_nick);
            power = view.findViewById(R.id.power);
            trauma = view.findViewById(R.id.trauma);
            fight = view.findViewById(R.id.fight_tiredness);
            work = view.findViewById(R.id.work_tiredness);
            cl = view.findViewById(R.id.constraint_hero);
        }


    }
}
