package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_of_game);
//        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        Gson gson = new Gson();
        lobby = gson.fromJson(getIntent().getStringExtra("lobby"), Lobby.class);
//        mDatabase.child("lobby").child("9998").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
    }
}