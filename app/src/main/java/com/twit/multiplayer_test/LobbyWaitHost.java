package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
                finish();
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

                    Lobby lobby = snapshot.getValue(Lobby.class);
                if (lobby != null) {
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
        lobby.setMaxCount(""+lobby.getMembers().size());
        if (6 -Integer.parseInt(lobby.getMaxCount()) >= 0) {
            roles = new ArrayList<>(roles.subList(0, Integer.parseInt(lobby.getMaxCount())));
        }
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<String> enemies = new ArrayList<>();
        for (String s: roles){
            switch (s){
                case "Капитан":
                    friends.add("Капитан");
                    enemies.add("Капитан");
                    break;
                case "Черпак":
                    friends.add("Черпак");
                    enemies.add("Черпак");
                    break;
                case "Миледи":
                    friends.add("Миледи");
                    enemies.add("Миледи");
                    break;
                case "Сноб":
                    friends.add("Сноб");
                    enemies.add("Сноб");
                    break;
                case "Шкет":
                    friends.add("Шкет");
                    enemies.add("Шкет");
                    break;
                case "Боцман":
                    friends.add("Боцман");
                    enemies.add("Боцман");
                    break;
            }
        }
        Collections.shuffle(friends);
        Collections.shuffle(enemies);
        Collections.addAll(treasures,
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Вода", "voda"),
                new Card("treasure", "Аптечка",  "aptechka"),
                new Card("treasure", "Аптечка",  "aptechka"),
                new Card("treasure", "Аптечка",  "aptechka"),
                new Card("treasure", "Зонтик",  "zontik"),
                new Card("treasure", "Приманка для акул",  "akuli"),
                new Card("treasure", "Приманка для акул",  "akuli"),
                new Card("treasure", "Компас",  "kompas"),
                new Card("treasure", "Весло",  "veslo"),
                new Card("treasure", "Весло",  "veslo"),
                new Card("treasure", "Дубинка",  "dubinka"),
                new Card("treasure", "Нож",  "nozh"),
                new Card("treasure", "Пистолет",  "pistolet"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Пачка денег",  "dengi"),
                new Card("treasure", "Картина", "kartina1"),
                new Card("treasure", "Картина", "kartina2"),
                new Card("treasure", "Картина", "kartina3"),
                new Card("treasure", "Украшения", "ukrashenia1"),
                new Card("treasure", "Украшения", "ukrashenia2"),
                new Card("treasure", "Украшения", "ukrashenia3"),
                new Card("treasure", "Спасательный жилет", "zhilet")
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
            lobby.getMembers().get(n.getKey()).setName(n.getKey().substring(0, n.getKey().lastIndexOf(" "))+"#"+n.getKey().substring(n.getKey().lastIndexOf(" ")+1));
        };
        Integer seat = 1;
        String[] ordered = {"Миледи", "Сноб", "Капитан", "Боцман", "Черпак", "Шкет"};
        for (String ro: ordered){
            for (String le: lobby.getMembers().keySet()){
                if (ro.equals(lobby.getMembers().get(le).getStats().getRole())){
                    lobby.getMembers().get(le).getState().setSeat(seat);
                    lobby.getMembers().get(le).setTurn(seat);
                    seat+=1;
                }
            }

        }
        lobby.setNavCardsDeck(createNavCards());
        lobby.setTreasuresDeck(treasures);
        lobby.setGameState("created");
        lobby.setTurn(1);
        System.out.println(lobby);
        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
        finish();
        startActivity(new Intent(this, MainGame.class).putExtra("lobbyNumber", lobbyNumber));
    }

    private ArrayList<NavCard> createNavCards() {
        ArrayList<NavCard> navCards = new ArrayList<>();
        //1+
        ArrayList<String> bort1 = new ArrayList<>();
        ArrayList<String> thirst1 = new ArrayList<>();
        bort1.add("Боцман");
        thirst1.add("Капитан");
        thirst1.add("Черпак");
        navCards.add(new NavCard(bort1, thirst1, 0, 1, 1, "n1"));
        //2+
        ArrayList<String> bort2 = new ArrayList<>();
        ArrayList<String> thirst2 = new ArrayList<>();
        bort2.add("Сноб");
        thirst2.add("Капитан");
        thirst2.add("Шкет");
        navCards.add(new NavCard(bort2, thirst2, 1, 0, 1, "n2"));
        //3+
        ArrayList<String> bort3 = new ArrayList<>();
        ArrayList<String> thirst3 = new ArrayList<>();
        bort3.add("Черпак");
        thirst3.add("Капитан");
        thirst3.add("Боцман");
        thirst3.add("Миледи");
        navCards.add(new NavCard(bort3, thirst3, 0, 1, 0, "n3"));
        //4+
        ArrayList<String> bort4 = new ArrayList<>();
        ArrayList<String> thirst4 = new ArrayList<>();
        bort4.add("Шкет");
        thirst4.add("Капитан");
        thirst4.add("Боцман");
        thirst4.add("Черпак");
        thirst4.add("Шкет");
        navCards.add(new NavCard(bort4, thirst4, 0, 1, 0, "n4"));
        //5+
        ArrayList<String> bort5 = new ArrayList<>();
        ArrayList<String> thirst5 = new ArrayList<>();
        bort5.add("Миледи");
        thirst5.add("Капитан");
        thirst5.add("Боцман");
        thirst5.add("Миледи");
        thirst5.add("Черпак");
        thirst5.add("Сноб");
        navCards.add(new NavCard(bort5, thirst5, -1, 0, 1, "n5"));
        //6+
        ArrayList<String> bort6 = new ArrayList<>();
        ArrayList<String> thirst6 = new ArrayList<>();
        bort6.add("Сноб");
        thirst6.add("Капитан");
        thirst6.add("Черпак");
        thirst6.add("Боцман");
        navCards.add(new NavCard(bort6, thirst6, 0, 1, 1, "n6"));
        //7+
        ArrayList<String> bort7 = new ArrayList<>();
        ArrayList<String> thirst7 = new ArrayList<>();
        bort7.add("Сноб");
        bort7.add("Капитан");
        bort7.add("Шкет");
        bort7.add("Черпак");
        bort7.add("Миледи");
        bort7.add("Боцман");
        thirst7.add("Сноб");
        thirst7.add("Капитан");
        thirst7.add("Шкет");
        thirst7.add("Черпак");
        thirst7.add("Миледи");
        thirst7.add("Боцман");
        navCards.add(new NavCard(bort7, thirst7, 1, 1, 1, "n7"));
        //8+
        ArrayList<String> bort8 = new ArrayList<>();
        ArrayList<String> thirst8 = new ArrayList<>();
        bort8.add("Черпак");
        thirst8.add("Капитан");
        thirst8.add("Шкет");
        thirst8.add("Боцман");
        navCards.add(new NavCard(bort8, thirst8, 0, 0, 1, "n8"));
        //9+
        ArrayList<String> bort9 = new ArrayList<>();
        ArrayList<String> thirst9 = new ArrayList<>();
        bort9.add("Черпак");
        thirst9.add("Капитан");
        thirst9.add("Черпак");
        thirst9.add("Боцман");
        thirst9.add("Сноб");
        navCards.add(new NavCard(bort9, thirst9, 0, 1, 1, "n9"));
        //10+
        ArrayList<String> bort10 = new ArrayList<>();
        ArrayList<String> thirst10 = new ArrayList<>();
        bort10.add("Боцман");
        thirst10.add("Миледи");
        navCards.add(new NavCard(bort10, thirst10, 0, 0, 0, "n10"));
        //11+
        ArrayList<String> bort11 = new ArrayList<>();
        ArrayList<String> thirst11 = new ArrayList<>();
        bort11.add("Капитан");
        thirst11.add("Капитан");
        navCards.add(new NavCard(bort11, thirst11, 0, 0, 0, "n11"));
        //12+
        ArrayList<String> bort12 = new ArrayList<>();
        ArrayList<String> thirst12 = new ArrayList<>();
        bort12.add("Шкет");
        thirst12.add("Капитан");
        thirst12.add("Боцман");
        thirst12.add("Миледи");
        thirst12.add("Сноб");
        thirst12.add("Черпак");
        navCards.add(new NavCard(bort12, thirst12, 0, 0, 1, "n12"));
        //13+
        ArrayList<String> bort13 = new ArrayList<>();
        ArrayList<String> thirst13 = new ArrayList<>();
        bort13.add("Черпак");
        thirst13.add("Капитан");
        thirst13.add("Боцман");
        thirst13.add("Сноб");
        navCards.add(new NavCard(bort13, thirst13, 1, 0, 0, "n13"));
        //14+
        ArrayList<String> bort14 = new ArrayList<>();
        ArrayList<String> thirst14 = new ArrayList<>();
        bort14.add("Сноб");
        thirst14.add("Капитан");
        thirst14.add("Сноб");
        navCards.add(new NavCard(bort14, thirst14, 0, 0, 0, "n14"));
        //15+
        ArrayList<String> bort15 = new ArrayList<>();
        ArrayList<String> thirst15 = new ArrayList<>();
        bort15.add("Капитан");
        thirst15.add("Сноб");
        navCards.add(new NavCard(bort15, thirst15, 0, 1, 1, "n15"));
        //16+
        ArrayList<String> bort16 = new ArrayList<>();
        ArrayList<String> thirst16 = new ArrayList<>();
        bort16.add("Капитан");
        thirst16.add("Черпак");
        navCards.add(new NavCard(bort16, thirst16, 0, 0, 0, "n16"));
        //17+
        ArrayList<String> bort17 = new ArrayList<>();
        ArrayList<String> thirst17 = new ArrayList<>();
        bort17.add("Сноб");
        thirst17.add("Капитан");
        thirst17.add("Миледи");
        navCards.add(new NavCard(bort17, thirst17, 0, 1, 0, "n17"));
        //18+
        ArrayList<String> bort18 = new ArrayList<>();
        ArrayList<String> thirst18 = new ArrayList<>();
        bort18.add("Шкет");
        thirst18.add("Капитан");
        thirst18.add("Шкет");
        thirst18.add("Черпак");
        thirst18.add("Боцман");
        thirst18.add("Сноб");
        navCards.add(new NavCard(bort18, thirst18, 1, 1, 1, "n18"));
        //19+
        ArrayList<String> bort19 = new ArrayList<>();
        ArrayList<String> thirst19 = new ArrayList<>();
        bort19.add("Шкет");
        thirst19.add("Капитан");
        thirst19.add("Черпак");
        thirst19.add("Боцман");
        thirst19.add("Миледи");
        navCards.add(new NavCard(bort19, thirst19, 0, 0, 0, "n19"));
        //20+
        ArrayList<String> bort20 = new ArrayList<>();
        ArrayList<String> thirst20 = new ArrayList<>();
        bort20.add("Боцман");
        thirst20.add("Капитан");
        thirst20.add("Боцман");
        navCards.add(new NavCard(bort20, thirst20, 0, 0, 1, "n20"));
        //21+
        ArrayList<String> bort21 = new ArrayList<>();
        ArrayList<String> thirst21 = new ArrayList<>();
        navCards.add(new NavCard(bort21, thirst21, 1, 0, 0, "n21"));
        //22+
        ArrayList<String> bort22 = new ArrayList<>();
        ArrayList<String> thirst22 = new ArrayList<>();
        bort22.add("Боцман");
        bort22.add("Капитан");
        bort22.add("Миледи");
        bort22.add("Сноб");
        bort22.add("Шкет");
        bort22.add("Черпак");
        thirst22.add("Боцман");
        thirst22.add("Капитан");
        thirst22.add("Миледи");
        thirst22.add("Сноб");
        thirst22.add("Шкет");
        thirst22.add("Черпак");
        navCards.add(new NavCard(bort22, thirst22, 0, 1, 0, "n22"));
        //23+
        ArrayList<String> bort23 = new ArrayList<>();
        ArrayList<String> thirst23 = new ArrayList<>();
        bort23.add("Капитан");
        thirst23.add("Боцман");
        navCards.add(new NavCard(bort23, thirst23, 1, 1, 0, "n23"));
        //24+
        ArrayList<String> bort24 = new ArrayList<>();
        ArrayList<String> thirst24 = new ArrayList<>();
        bort24.add("Боцман");
        thirst24.add("Боцман");
        navCards.add(new NavCard(bort24, thirst24, 1, 1, 0, "n24"));


        return navCards;
    };
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LobbyWaitHost", "onResume");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LobbyWaitHost", "onDestroy");
    }
}