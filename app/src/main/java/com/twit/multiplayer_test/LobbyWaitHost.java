package com.twit.multiplayer_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LobbyWaitHost extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String lobbyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_wait_host);

        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child("lobby").child(lobbyNumber).removeValue();
    }
}