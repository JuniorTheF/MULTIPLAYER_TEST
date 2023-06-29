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

public class FinalAdapter extends RecyclerView.Adapter<FinalAdapter.ViewHolder> {
    ArrayList<Member> members;
    ArrayList<Result> results;

    FinalAdapter(ArrayList<Member> members, ArrayList<Result> results){
        this.members = members;
        this.results = results;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member toFinalMember = members.get(position);
        Result toFinalScore = results.get(position);
        for (Result q: results){
            if (q.getScore()> toFinalScore.getScore()){
                holder.kubok.setVisibility(View.GONE);
            }
        }
        holder.name.setText(toFinalMember.getName()+"("+toFinalMember.getStats().getRole()+")");
        holder.score.setText(toFinalScore.getCalculation());//Бонус за выживание+деньги+украшение+картина+друг+враг+психопат = результат
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
        ImageView kubok;

        ViewHolder(View view){
            super(view);
            kubok = view.findViewById(R.id.kubok);
            name = view.findViewById(R.id.final_name);
            score = view.findViewById(R.id.score);
        }
    }
}
