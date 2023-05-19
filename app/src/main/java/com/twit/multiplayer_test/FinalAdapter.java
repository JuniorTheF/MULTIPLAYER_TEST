package com.twit.multiplayer_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FinalAdapter extends RecyclerView.Adapter<FinalAdapter.ViewHolder> {
    ArrayList<Member> members;

    FinalAdapter(ArrayList<Member> members){
        this.members = members;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member toFinal = members.get(position);
        holder.name.setText(toFinal.getName()+"("+toFinal.getStats().getRole()+")");
        String finalScore = "";
        Integer finalScoreInteger = 0;
        if (!toFinal.getState().getStatus().equals("dead")){
            Integer mult = 1;
            if (toFinal.getStats().getEnemy().equals(toFinal.getStats().getRole())){
                mult-=1;
            }
            if (toFinal.getStats().getFriend().equals(toFinal.getStats().getRole())){
                mult+=1;
            }
            finalScore+=(toFinal.getStats().getSurvival_bonus()*mult);
            finalScoreInteger += toFinal.getStats().getSurvival_bonus()*mult;
        }else{
            finalScore+="0";
        }
        finalScore+="+";
        Integer money = 0;
        if (toFinal.getTreasures().getOpen()!=null){
            for (Card q: toFinal.getTreasures().getOpen()){
                if (q.getName().equals("Пачка денег")){
                    money+=1;
                }
            }
        }
        if (toFinal.getTreasures().getClose()!=null){
            for (Card q: toFinal.getTreasures().getClose()){
                if (q.getName().equals("Пачка денег")){
                    money+=1;
                }
            }
        }
        if (toFinal.getStats().getRole().equals("Капитан")) {
            finalScoreInteger += 2*money;
            finalScore += 2*money + "+";
        }else{
            finalScoreInteger += money;
            finalScore += money + "+";
        }
        Integer jewelery = 0;
        if (toFinal.getTreasures().getOpen()!=null){
            for (Card q: toFinal.getTreasures().getOpen()){
                if (q.getName().equals("Украшения")){
                    jewelery+=1;
                }
            }
        }
        if (toFinal.getTreasures().getClose()!=null){
            for (Card q: toFinal.getTreasures().getClose()){
                if (q.getName().equals("Украшения")){
                    jewelery+=1;
                }
            }
        }
        switch (jewelery){
            case 1:
                jewelery = 1;
                break;
            case 2:
                jewelery = 4;
                break;
            case 3:
                jewelery = 8;
                break;
        }
        if (toFinal.getStats().getRole().equals("Миледи")) {
            finalScoreInteger += 2*jewelery;
            finalScore += 2*jewelery + "+";
        }else{
            finalScoreInteger += jewelery;
            finalScore += jewelery + "+";
        }
        Integer paintings = 0;
        if (toFinal.getTreasures().getOpen()!=null){
            for (Card q: toFinal.getTreasures().getOpen()){
                if (q.getName().equals("Картина")){
                    paintings+=1;
                }
            }
        }
        if (toFinal.getTreasures().getClose()!=null){
            for (Card q: toFinal.getTreasures().getClose()){
                if (q.getName().equals("Картина")){
                    paintings+=1;
                }
            }
        }
        if (toFinal.getStats().getRole().equals("Сноб")) {
            finalScoreInteger += 3*2*paintings;
            finalScore += 3*2*paintings + "+";
        }else{
            finalScoreInteger += 3*paintings;
            finalScore += 3*paintings + "+";
        }
        if (toFinal.getStats().getFriend().equals(toFinal.getStats().getRole())){
            finalScore+="0+";
        }
        else{
            for (Member q: members){
                if (q.getStats().getRole().equals(toFinal.getStats().getFriend())){
                    if (!q.getState().getStatus().equals("dead")){
                        finalScore+=q.getStats().getSurvival_bonus()+"+";
                        finalScoreInteger+=q.getStats().getSurvival_bonus();
                    }else{
                        finalScore+=0+"+";
                    }
                }
            }
        }
        if (toFinal.getStats().getEnemy().equals(toFinal.getStats().getRole())){
            finalScore+="0+";
        }
        else{
            for (Member q: members){
                if (q.getStats().getRole().equals(toFinal.getStats().getEnemy())){
                    if (q.getState().getStatus().equals("dead")){
                        finalScore+=q.getStats().getPower()+"+";
                        finalScoreInteger+=q.getStats().getPower();
                    }else{
                        finalScore+=0+"+";
                    }
                }
            }
        }
        Integer deathCounter = 0;
        if (toFinal.getStats().getRole().equals(toFinal.getStats().getEnemy())){
            for (Member q: members){
                if (!q.getName().equals(toFinal.getName()) && !toFinal.getStats().getFriend().equals(q.getStats().getRole())){
                    if (q.getState().getStatus().equals("dead")){
                        deathCounter+=1;
                    }
                }
            }
        }
        finalScoreInteger += 3*deathCounter;
        finalScore += 3*deathCounter + "=";
        finalScore+= finalScoreInteger;
        holder.score.setText(finalScore);//Бонус за выживание+деньги+украшение+картина+друг+враг+психопат = результат
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View finalView = inflater.inflate(R.layout.final_item, parent, false);
        FinalAdapter.ViewHolder vh = new FinalAdapter.ViewHolder(finalView);
        return vh;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView score;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.final_name);
            score = view.findViewById(R.id.score);
        }
    }
}
