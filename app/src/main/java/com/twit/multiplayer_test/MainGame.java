package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public class MainGame extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String lobbyNumber;
    String player_name;
    Lobby lobby;
    TextView gameStateTextView;
    ArrayList<Member> orderedByTurn;
    ArrayList<Member> orderedBySeat;
    SharedPreferences sp;
    HeroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        sp = getSharedPreferences("auth_data", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        player_name = sp.getString("userLogin", null) + " " + sp.getString("userId", null);
        gameStateTextView = findViewById(R.id.gameState);
        orderedByTurn = new ArrayList<>();
        orderedBySeat = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.rv_onboardheroes);
        adapter = new HeroAdapter(orderedBySeat);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(adapter);

        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lobby = snapshot.getValue(Lobby.class);
                orderedBySeat.clear();
                orderedByTurn.clear();
                for (Member q: lobby.getMembers().values()){
                    orderedByTurn.add(q);
                    orderedBySeat.add(q);
                }
                orderedByTurn.sort(new SortByTurn());
                orderedBySeat.sort(new SortBySeat());
                if (lobby.getGameState().equals("created")){
                    ImageView tooltip = findViewById(R.id.hero_tooltip);
                    switch(lobby.getMembers().get(player_name).getStats().getRole()){
                        case "Миледи":
                            tooltip.setImageResource(R.drawable.miledi_bonus);
                            break;
                        case "Шкет":
                            tooltip.setImageResource(R.drawable.shket_bonus);
                            break;
                        case "Черпак":
                            tooltip.setImageResource(R.drawable.cherpak_bonus);
                            break;
                        case "Капитан":
                            tooltip.setImageResource(R.drawable.kapitan_bonus);
                            break;
                        case "Боцман":
                            tooltip.setImageResource(R.drawable.botsman_bonus);
                            break;
                        case "Сноб":
                            tooltip.setImageResource(R.drawable.snob_bonus);
                            break;
                    }
                    ImageView friend = findViewById(R.id.hero_firend);
                    switch(lobby.getMembers().get(player_name).getStats().getFriend()){
                        case "Миледи":
                            friend.setImageResource(R.drawable.drug_miledi);
                            break;
                        case "Шкет":
                            friend.setImageResource(R.drawable.drug_shket);
                            break;
                        case "Черпак":
                            friend.setImageResource(R.drawable.drug_cherpak);
                            break;
                        case "Капитан":
                            friend.setImageResource(R.drawable.drug_kapitan);
                            break;
                        case "Боцман":
                            friend.setImageResource(R.drawable.drug_botsman);
                            break;
                        case "Сноб":
                            friend.setImageResource(R.drawable.drug_snob);
                            break;
                    }
                    ImageView enemy = findViewById(R.id.hero_enemy);
                    switch(lobby.getMembers().get(player_name).getStats().getEnemy()){
                        case "Миледи":
                            enemy.setImageResource(R.drawable.vrag_miledi);
                            break;
                        case "Шкет":
                            enemy.setImageResource(R.drawable.vrag_shket);
                            break;
                        case "Черпак":
                            enemy.setImageResource(R.drawable.vrag_cherpak);
                            break;
                        case "Капитан":
                            enemy.setImageResource(R.drawable.vrag_kapitan);
                            break;
                        case "Боцман":
                            enemy.setImageResource(R.drawable.vrag_botsman);
                            break;
                        case "Сноб":
                            enemy.setImageResource(R.drawable.vrag_snob);
                            break;
                    }
                }
                if (lobby.getGameState().equals("created") || lobby.getGameState().equals("morning")){
                    gameStateTextView.setText("Утро\n"+"Ходит "+orderedByTurn.get(lobby.getTurn()-1).getName());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

class SortByTurn implements Comparator<Member>{
    @Override
    public int compare(Member member, Member t1) {
        if (member.getTurn() > t1.getTurn()) {
            return 1;
        } else if (member.getTurn() < t1.getTurn()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class SortBySeat implements Comparator<Member>{
    @Override
    public int compare(Member member, Member t1) {
        if (member.getState().getSeat() > t1.getState().getSeat()) {
            return 1;
        } else if (member.getState().getSeat() < t1.getState().getSeat()) {
            return -1;
        } else {
            return 0;
        }
    }
}