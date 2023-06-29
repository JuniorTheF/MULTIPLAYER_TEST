package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPage extends AppCompatActivity {

    FirebaseDatabase database;
    private DatabaseReference mDatabase;

    EditText login;
    Button sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Context ctx = getApplicationContext();
        SharedPreferences sp = ctx.getSharedPreferences("auth_data", MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        String userLogin = sp.getString("userLogin", null);
        String userId = sp.getString("userId", null);
        Intent lobbyPage = new Intent(this, LobbyPage.class);
      if (userId == null || userLogin == null){
            mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
//            header = findViewById(R.id.)
            login = findViewById(R.id.login);
            sign_in = findViewById(R.id.sign_in);
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("players").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Long last = task.getResult().getChildrenCount();
                            int userId = generateId();
                            mDatabase.child("players").child("" + (last + 1)).child("username").setValue(login.getText().toString());
                            mDatabase.child("players").child("" + (last + 1)).child("id").setValue(userId);
                            spe.putString("userLogin", login.getText().toString());
                            spe.putString("userId", String.valueOf(userId));
                            spe.commit();
                            finish();
                            startActivity(lobbyPage);
                        }
                    });
                }
            });
        }
        else {
          finish();
            startActivity(lobbyPage);
        }


    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    public static int generateId() {
        return ((Double) (Math.random()*10000.)).intValue();
    }
};
