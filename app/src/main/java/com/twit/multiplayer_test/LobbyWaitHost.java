package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyWaitHost extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String lobbyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_wait_host);

        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lobby current = snapshot.getValue(Lobby.class);
                assert current != null;
                if (current.getMembers().size() == Integer.parseInt(current.getMaxCount())) {
                    createLobby();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void createLobby() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child("lobby").child(lobbyNumber).removeValue();
    }
}