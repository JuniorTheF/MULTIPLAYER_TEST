package com.twit.multiplayer_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.ViewHolder> {

    private List<Lobby> lobbies;
    OnItemClickListener listener;

    LobbyAdapter(List<Lobby> lobbies, OnItemClickListener listener) {
        this.lobbies = lobbies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LobbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View lobbiesView = inf.inflate(R.layout.lobby_recycler_view, parent, false);
        lobbiesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view);
            }
        });
        ViewHolder vh = new ViewHolder(lobbiesView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyAdapter.ViewHolder holder, int position) {
        Lobby lobbyToView = lobbies.get(position);

        TextView members_count = holder.members_count;
        members_count.setText(lobbyToView.getMembers().size()+"/"+lobbyToView.getMaxCount());
        TextView members = holder.members;
        String toSet = "";
        for (String q: lobbyToView.getMembers().keySet()){
            toSet += q.substring(0, q.lastIndexOf(" "))+"#"+q.substring(q.lastIndexOf(" ")+1) +"\n";
        }
        members.setText(toSet);
        TextView lobbyName = holder.lobbyName;
        lobbyName.setText(lobbyToView.getName());
        TextView number = holder.number;
        number.setText(lobbyToView.getNumber());

    }

    @Override
    public int getItemCount() {
        return lobbies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView members_count;
        TextView members;
        TextView lobbyName;
        TextView number;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.rv_number);
            members_count = view.findViewById(R.id.rv_members_count);
            members = view.findViewById(R.id.rv_members);
            lobbyName = view.findViewById(R.id.rv_lobby_name);
        }

    }
}

