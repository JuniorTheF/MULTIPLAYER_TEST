package com.twit.multiplayer_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FinalOfGame extends AppCompatActivity {
    Lobby lobby;
    FinalAdapter finalAdapter;
    ArrayList<Member> members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_of_game);
        Gson gson = new Gson();
        lobby = gson.fromJson(getIntent().getStringExtra("lobby"), Lobby.class);
        RecyclerView finalRv = findViewById(R.id.final_items_rv);
        members = new ArrayList<>(lobby.getMembers().values());
        finalAdapter = new FinalAdapter(members);
        finalRv.setAdapter(finalAdapter);
        finalRv.setLayoutManager(new LinearLayoutManager(FinalOfGame.this, LinearLayoutManager.VERTICAL, false));

    }
}