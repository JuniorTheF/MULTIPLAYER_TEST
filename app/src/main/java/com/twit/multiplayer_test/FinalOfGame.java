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
//        mDatabase.child("lobby").child("9997").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                lobby = task.getResult().getValue(Lobby.class);
//                members.clear();
//                for (Member q: lobby.getMembers().values()){
//                    members.add(q);
//                }
//                finalAdapter.notifyDataSetChanged();
//            }
//        });
        members = new ArrayList<>(lobby.getMembers().values());
        RecyclerView finalRv = findViewById(R.id.final_items_rv);
        finalAdapter = new FinalAdapter(members);
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
}