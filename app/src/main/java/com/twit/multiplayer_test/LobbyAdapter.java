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

    LobbyAdapter(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    @NonNull
    @Override
    public LobbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View lobbiesView = inf.inflate(R.layout.lobby_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(lobbiesView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyAdapter.ViewHolder holder, int position) {
        Lobby lobbyToView = lobbies.get(position);
        TextView members_count = holder.members_count;
        members_count.setText(lobbyToView.members+"/"+lobbyToView.max_members);
        TextView members = holder.members;
        String toSet = "";
        for (String q: lobbyToView.members){
            toSet += q +"\n";
        }
        members.setText(toSet);
        TextView lobbyName = holder.lobbyName;
        lobbyName.setText(lobbyToView.lobby_name);
    }

    @Override
    public int getItemCount() {
        return lobbies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView members_count;
        TextView members;
        TextView lobbyName;

        public ViewHolder(View view) {
            super(view);
            members_count = view.findViewById(R.id.rv_members_count);
            members = view.findViewById(R.id.rv_members);
            lobbyName = view.findViewById(R.id.rv_lobby_name);
        }

    }
}
