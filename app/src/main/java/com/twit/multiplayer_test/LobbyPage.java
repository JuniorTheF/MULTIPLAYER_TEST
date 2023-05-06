package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class LobbyPage extends AppCompatActivity {

    LobbyAdapter lobbyAdapter;
    ArrayList<Lobby> lobbies;
    private DatabaseReference mDatabase;
    Integer numberOfPlayers = 4;
    String lobbyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_page);
        RecyclerView recyclerView = findViewById(R.id.rv_lobbies);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        TextView tbTextView = tb.findViewById(R.id.toolbar_textview);
        Button tbButton = tb.findViewById(R.id.toolbar_button);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("auth_data", MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        tbTextView.setText(sp.getString("userLogin", null)+"#"+sp.getString("userId", null));
        tbTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp2 = getSharedPreferences("auth_data", MODE_PRIVATE);
                mDatabase.child("players").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot q: task.getResult().getChildren()){
                            if(((Map<String, String>) q.getValue()).get("username").equals(sp2.getString("userLogin", null)) && ((Map<String, Long>) q.getValue()).get("id").toString().equals(sp.getString("userId", null)))
                            {
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LobbyPage.this);
                LayoutInflater layoutInflater = getLayoutInflater();

                alertDialogBuilder.setView(layoutInflater.inflate(R.layout.dialog, null));


                AlertDialog ad = alertDialogBuilder.show();
                Spinner spinner = ad.findViewById(R.id.dialog_spinner);
                ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(LobbyPage.this, R.array.numberOfPlayers, R.layout.spinner_view);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] choose = getResources().getStringArray(R.array.numberOfPlayers);
                        numberOfPlayers = Integer.parseInt(((TextView)view).getText().toString());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                Button dialogButton = ad.findViewById(R.id.dialog_button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        mDatabase.child("lobby").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                lobbyNumber = "" + LoginPage.generateId();
                                mDatabase.child("lobby").child(lobbyNumber).child("name").setValue(sp.getString("userLogin", null) + "#" + sp.getString("userId", null) +"’s lobby");
                                mDatabase.child("lobby").child(lobbyNumber).child("maxCount").setValue(numberOfPlayers);
                                mDatabase.child("lobby").child(lobbyNumber).child("members").child("1").setValue(sp.getString("userLogin", null) + "#" + sp.getString("userId", null));
                                mDatabase.child("lobby").child(lobbyNumber).child("hostName").setValue(sp.getString("userLogin", null));
                                mDatabase.child("lobby").child(lobbyNumber).child("hostId").setValue(sp.getString("userId", null));
                                startActivity(new Intent(LobbyPage.this, LobbyWait.class).putExtra("lobbyNumber", lobbyNumber));
                            }
                        });

                    }
                });

            }
        });
        mDatabase.child("lobby").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot q: task.getResult().getChildren()){
//                    System.out.println(q.getValue(Lobby2.class));
                    System.out.println(q.getValue());
//                    Lobby entry = new Lobby();
                }
            }
        });
//        lobbyAdapter = new LobbyAdapter(lobbies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(lobbyAdapter);

    }
}