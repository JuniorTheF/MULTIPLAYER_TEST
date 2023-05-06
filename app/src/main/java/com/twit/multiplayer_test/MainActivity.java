//package com.twit.multiplayer_test;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Shader;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.twit.multiplayer_test.R;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//    FirebaseDatabase database;
//    private DatabaseReference mDatabase;
//
//    RecyclerView rv;
//    MessagesAdapter ma;
//    ArrayList<Message> messages = new ArrayList<Message>();
//    ArrayList<Integer> proceeded = new ArrayList<Integer>();
//    EditText et;
//    Button btn;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Context ctx = getApplicationContext();
//        SharedPreferences sp = ctx.getSharedPreferences("auth_data", MODE_PRIVATE);
//        String userId = sp.getString("userId", null);
//        String userLogin = sp.getString("userLogin", null);
//        setTitle(userLogin + "#" + userId);
//        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
//        rv = findViewById(R.id.rv_messages);
//        ma = new MessagesAdapter(messages);
//        rv.setAdapter(ma);
//        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        et = findViewById(R.id.message);
//        btn = findViewById(R.id.submit_message);
//
//        mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot q: snapshot.getChildren()){
//                    if (!proceeded.contains(Integer.parseInt(q.getKey().toString()))){
//                        messages.add(new Message(Integer.parseInt(q.getKey()), String.valueOf(q.getValue())));
//                        proceeded.add(Integer.parseInt(q.getKey()));
//                    }
//                }
//                ma.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
//            }
//        });
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatabase.child("messages").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        Long last = task.getResult().getChildrenCount();
//                        mDatabase.child("messages").child(""+(last+1)).setValue(et.getText().toString());
//                    }
//                });
//
//            }
//        });
//    }
//}
//
