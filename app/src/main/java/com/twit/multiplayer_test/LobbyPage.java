package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class LobbyPage extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_page);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        TextView tbTextView = tb.findViewById(R.id.toolbar_textview);
        Button tbButton = tb.findViewById(R.id.toolbar_button);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("auth_data", MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        System.out.println(sp.getAll());
        tbTextView.setText(sp.getString("userLogin", null)+"#"+sp.getString("userId", null));
        tbTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp2 = getSharedPreferences("auth_data", MODE_PRIVATE);
                System.out.println(sp2.getAll());
                mDatabase.child("players").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot q: task.getResult().getChildren()){
                            if(((Map<String, String>) q.getValue()).get("username").equals(sp2.getString("userLogin", null)) && ((Map<String, Long>) q.getValue()).get("id").toString().equals(sp.getString("userId", null)))
                            {
                                System.out.println(q.getKey());
                                mDatabase.child("players").child(q.getKey()).removeValue();
                                break;
                            };
                        }
                        spe.remove("userLogin");
                        spe.remove("userId");
                        spe.commit();
                        startActivity(new Intent(LobbyPage.this, LoginPage.class));
                    }
                });
            }
        });
        tbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}