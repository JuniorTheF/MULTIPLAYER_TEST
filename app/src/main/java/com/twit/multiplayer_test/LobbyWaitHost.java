package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LobbyWaitHost extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String lobbyNumber;
    SharedPreferences sp;
    ArrayList<String> arrayList = new ArrayList<>();
    PlayerListViewArrayAdapter adapter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_wait_host);

        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();

        sp = getSharedPreferences("auth_data", MODE_PRIVATE);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar_lobby_wait_host);
        Button deleteLobby = findViewById(R.id.lobby_player_button);

        adapter = new PlayerListViewArrayAdapter(LobbyWaitHost.this, arrayList);
        ListView lv = findViewById(R.id.listview_lobby_wait_player);
        lv.setAdapter(adapter);
        deleteLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("lobby").child(lobbyNumber).removeValue();
                startActivity(new Intent(LobbyWaitHost.this, LobbyPage.class));
            }
        });
        Button forceStart = findViewById(R.id.host_force_start);
        tv = findViewById(R.id.lobby_player_waiting);
        forceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("lobby").child(lobbyNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Lobby lobby = task.getResult().getValue(Lobby.class);
                        createLobby(lobby);
                    }
                });
            }
        });
        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    Lobby lobby = snapshot.getValue(Lobby.class);
                    assert lobby != null;
                    if ((lobby.getMembers().size() == Integer.parseInt(lobby.getMaxCount())) && lobby.getGameState().equals("waitingForPlayers")) {
                        createLobby(lobby);
                    }
                    Set<String> q = lobby.getMembers().keySet();
                    arrayList.clear();
                    for (String i : q) {
                        arrayList.add(i);
                    }
                    adapter.notifyDataSetChanged();
                    tv.setText(String.format("Ожидание игроков (%d/%s)...", lobby.getMembers().size(), lobby.getMaxCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void createLobby(Lobby lobby) {
        ArrayList<String> roles = new ArrayList<>();

        ArrayList<Card> treasures = new ArrayList<>();
        Collections.addAll(roles, "Капитан", "Миледи", "Шкет", "Боцман", "Сноб", "Черпак");
        Collections.shuffle(roles);
        if (6 - Integer.parseInt(lobby.getMaxCount()) >= 0) {
            roles = new ArrayList<>(roles.subList(0, Integer.parseInt(lobby.getMaxCount())));
            System.out.println(roles);
        }
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<String> enemies = new ArrayList<>();
        for (String s: roles){
            enemies.add(String.valueOf(s));
            friends.add(String.valueOf(s));
        }
        Collections.shuffle(friends);
        Collections.shuffle(enemies);
        Collections.addAll(treasures,
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Вода"),
                new Card("treasure", "Аптечка"),
                new Card("treasure", "Аптечка"),
                new Card("treasure", "Аптечка"),
                new Card("treasure", "Зонтик"),
                new Card("treasure", "Приманка для акул"),
                new Card("treasure", "Приманка для акул"),
                new Card("treasure", "Компас"),
                new Card("treasure", "Весло"),
                new Card("treasure", "Весло"),
                new Card("treasure", "Дубинка"),
                new Card("treasure", "Нож"),
                new Card("treasure", "Пистолет"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Пачка денег"),
                new Card("treasure", "Картина"),
                new Card("treasure", "Картина"),
                new Card("treasure", "Картина"),
                new Card("treasure", "Украшения"),
                new Card("treasure", "Украшения"),
                new Card("treasure", "Украшения"),
                new Card("treasure", "Спасательный жилет")
                );
        Collections.shuffle(treasures);
        for (Map.Entry<String, Member> n : lobby.getMembers().entrySet()) {
            Stats stats = new Stats();
            String character = roles.get(0);
            switch (character) {
                case "Капитан":
                    stats.setPower(7);
                    stats.setSurvival_bonus(5);
                    stats.setRole("Капитан");
                    break;
                case "Миледи":
                    stats.setPower(4);
                    stats.setSurvival_bonus(8);
                    stats.setRole("Миледи");
                    break;
                case "Шкет":
                    stats.setPower(3);
                    stats.setSurvival_bonus(9);
                    stats.setRole("Шкет");
                    break;
                case "Боцман":
                    stats.setPower(8);
                    stats.setSurvival_bonus(4);
                    stats.setRole("Боцман");
                    break;
                case "Сноб":
                    stats.setPower(5);
                    stats.setSurvival_bonus(7);
                    stats.setRole("Сноб");
                    break;
                case "Черпак":
                    stats.setPower(6);
                    stats.setSurvival_bonus(6);
                    stats.setRole("Черпак");
                    break;
            }
            stats.setEnemy(enemies.get(0));
            stats.setFriend(friends.get(0));
            roles.remove(0);
            enemies.remove(0);
            friends.remove(0);
            lobby.getMembers().get(n.getKey()).setStats(stats);
        };
        lobby.setGameState("created");
        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
        startActivity(new Intent(this, MainGame.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child("lobby").child(lobbyNumber).removeValue();
    }
}