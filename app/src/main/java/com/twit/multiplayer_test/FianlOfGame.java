package com.twit.multiplayer_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;

public class FianlOfGame extends AppCompatActivity {
    Lobby lobby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fianl_of_game);
        Gson gson = new Gson();
        lobby = gson.fromJson(getIntent().getStringExtra("lobby"), Lobby.class);


    }
}