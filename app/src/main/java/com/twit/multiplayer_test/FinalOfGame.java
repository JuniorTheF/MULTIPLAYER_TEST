package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FinalOfGame extends AppCompatActivity {
    Lobby lobby;
    FinalAdapter finalAdapter;
    ArrayList<Member> members;
    ArrayList<Result> results;
    private DatabaseReference mDatabase;
    SharedPreferences sp;
    String player_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        members = new ArrayList<>();
        setContentView(R.layout.activity_final_of_game);
        sp = getSharedPreferences("auth_data", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        player_name = sp.getString("userLogin", null) + " " + sp.getString("userId", null);
        Gson gson = new Gson();
        lobby = gson.fromJson(getIntent().getStringExtra("lobby"), Lobby.class);
        members = new ArrayList<>(lobby.getMembers().values());
        results = new ArrayList<>();
        setResults();
        RecyclerView finalRv = findViewById(R.id.final_items_rv);
        finalAdapter = new FinalAdapter(members, results);
        finalRv.setAdapter(finalAdapter);
        finalRv.setLayoutManager(new LinearLayoutManager(FinalOfGame.this, LinearLayoutManager.VERTICAL, false));
        Button exit = findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player_name.equals(lobby.getHostName()+" "+lobby.getHostId())){
                    mDatabase.child("lobby").child(lobby.getNumber()).removeValue();
                }
                Intent q = new Intent(FinalOfGame.this, LobbyPage.class);
                startActivity(q);
                finish();
            }
        });
    }

    public void setResults() {
        for (Member toFinal : members) {
            String calculation = "";
            Integer score = 0;
            if (!toFinal.getState().getStatus().equals("dead")) {
                Integer mult = 1;
                if (toFinal.getStats().getEnemy().equals(toFinal.getStats().getRole())) {
                    mult -= 1;
                }
                if (toFinal.getStats().getFriend().equals(toFinal.getStats().getRole())) {
                    mult += 1;
                }
                calculation += (toFinal.getStats().getSurvival_bonus() * mult);
                score += toFinal.getStats().getSurvival_bonus() * mult;
            } else {
                calculation += "0";
            }
            calculation += "+";
            Integer money = 0;
            if (toFinal.getTreasures().getOpen() != null) {
                for (Card q : toFinal.getTreasures().getOpen()) {
                    if (q.getName().equals("Пачка денег")) {
                        money += 1;
                    }
                }
            }
            if (toFinal.getTreasures().getClose() != null) {
                for (Card q : toFinal.getTreasures().getClose()) {
                    if (q.getName().equals("Пачка денег")) {
                        money += 1;
                    }
                }
            }
            if (toFinal.getStats().getRole().equals("Капитан")) {
                score += 2 * money;
                calculation += 2 * money + "+";
            } else {
                score += money;
                calculation += money + "+";
            }
            Integer jewelery = 0;
            if (toFinal.getTreasures().getOpen() != null) {
                for (Card q : toFinal.getTreasures().getOpen()) {
                    if (q.getName().equals("Украшения")) {
                        jewelery += 1;
                    }
                }
            }
            if (toFinal.getTreasures().getClose() != null) {
                for (Card q : toFinal.getTreasures().getClose()) {
                    if (q.getName().equals("Украшения")) {
                        jewelery += 1;
                    }
                }
            }
            switch (jewelery) {
                case 1:
                    jewelery = 1;
                    break;
                case 2:
                    jewelery = 4;
                    break;
                case 3:
                    jewelery = 8;
                    break;
            }
            if (toFinal.getStats().getRole().equals("Миледи")) {
                score += 2 * jewelery;
                calculation += 2 * jewelery + "+";
            } else {
                score += jewelery;
                calculation += jewelery + "+";
            }
            Integer paintings = 0;
            if (toFinal.getTreasures().getOpen() != null) {
                for (Card q : toFinal.getTreasures().getOpen()) {
                    if (q.getName().equals("Картина")) {
                        paintings += 1;
                    }
                }
            }
            if (toFinal.getTreasures().getClose() != null) {
                for (Card q : toFinal.getTreasures().getClose()) {
                    if (q.getName().equals("Картина")) {
                        paintings += 1;
                    }
                }
            }
            if (toFinal.getStats().getRole().equals("Сноб")) {
                score += 3 * 2 * paintings;
                calculation += 3 * 2 * paintings + "+";
            } else {
                score += 3 * paintings;
                calculation += 3 * paintings + "+";
            }
            if (toFinal.getStats().getFriend().equals(toFinal.getStats().getRole())) {
                calculation += "0+";
            } else {
                for (Member q : members) {
                    if (q.getStats().getRole().equals(toFinal.getStats().getFriend())) {
                        if (!q.getState().getStatus().equals("dead")) {
                            calculation += q.getStats().getSurvival_bonus() + "+";
                            score += q.getStats().getSurvival_bonus();
                        } else {
                            calculation += 0 + "+";
                        }
                    }
                }
            }
            if (toFinal.getStats().getEnemy().equals(toFinal.getStats().getRole())) {
                calculation += "0+";
            } else {
                for (Member q : members) {
                    if (q.getStats().getRole().equals(toFinal.getStats().getEnemy())) {
                        if (q.getState().getStatus().equals("dead")) {
                            calculation += q.getStats().getPower() + "+";
                            score += q.getStats().getPower();
                        } else {
                            calculation += 0 + "+";
                        }
                    }
                }
            }
            Integer deathCounter = 0;
            if (toFinal.getStats().getRole().equals(toFinal.getStats().getEnemy())) {
                for (Member q : members) {
                    if (!q.getName().equals(toFinal.getName()) && !toFinal.getStats().getFriend().equals(q.getStats().getRole())) {
                        if (q.getState().getStatus().equals("dead")) {
                            deathCounter += 1;
                        }
                    }
                }
            }
            score += 3 * deathCounter;
            calculation += 3 * deathCounter + "=";
            calculation += score;
            results.add(new Result(calculation, score));
        }
    }


}

class Result{
    public Result(String calculation, Integer score) {
        this.calculation = calculation;
        this.score = score;
    }

    String calculation;
    Integer score;

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}