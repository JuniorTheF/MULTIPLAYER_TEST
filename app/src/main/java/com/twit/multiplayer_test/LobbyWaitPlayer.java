package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class LobbyWaitPlayer extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String lobbyNumber;
    TextView tv;
    ArrayList<String> arrayList = new ArrayList<>();
    PlayerListViewArrayAdapter adapter;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_wait_player);

        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();

        sp = getSharedPreferences("auth_data", MODE_PRIVATE);
        Toolbar tb = findViewById(R.id.toolbar);
        Button leaveButton = findViewById(R.id.lobby_player_button);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar_lobby_wait_player);
        tv = findViewById(R.id.lobby_player_waiting);
        PlayerListViewArrayAdapter adapter = new PlayerListViewArrayAdapter(LobbyWaitPlayer.this, arrayList);
        ListView lv = findViewById(R.id.listview_lobby_wait_player);
        lv.setAdapter(adapter);

        mDatabase.child("lobby").child(lobbyNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Lobby lobby = task.getResult().getValue(Lobby.class);
                ((TextView)tb.findViewById(R.id.tb_lobby_wait_text)).setText(lobby.getName());
            }
        });
        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot task) {
                Lobby lobby = task.getValue(Lobby.class);
                if (lobby == null){
                    Toast.makeText(getApplicationContext(), "Создатель удалил лобби", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LobbyWaitPlayer.this, LobbyPage.class));
                }
                else {
                    Set<String> q = lobby.getMembers().keySet();
                    arrayList.clear();
                    for (String i : q) {
                        arrayList.add(i);
                    }
                    adapter.notifyDataSetChanged();
                    tv.setText(String.format("Ожидание игроков (%d/%s)...", lobby.getMembers().size(), lobby.getMaxCount()));
                    if (!lobby.getGameState().equals("waitingForPlayers")) {
                        startActivity(new Intent(LobbyWaitPlayer.this, MainGame.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("lobby").child(lobbyNumber).child("members").child(sp.getString("userLogin", null) + " " + sp.getString("userId", null)).removeValue();
                startActivity(new Intent(LobbyWaitPlayer.this, LobbyPage.class));
            }
        });
    }
}