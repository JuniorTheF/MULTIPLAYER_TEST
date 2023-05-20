package com.twit.multiplayer_test;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlayerListViewArrayAdapter extends ArrayAdapter<String> {
    public PlayerListViewArrayAdapter(Context context, ArrayList<String> players){
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SharedPreferences sp = getContext().getSharedPreferences("auth_data", MODE_PRIVATE);
        String playerListViewItem = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }
        TextView listTextView = convertView.findViewById(R.id.listview_textview);
        if (playerListViewItem.equals(sp.getString("userLogin", null)+" "+sp.getString("userId", null))){
            listTextView.setText(sp.getString("userLogin", null)+"#"+sp.getString("userId", null)+" (Вы)");
        }
        else{
            listTextView.setText(playerListViewItem.substring(0, playerListViewItem.lastIndexOf(" "))+"#"+playerListViewItem.substring(playerListViewItem.lastIndexOf(" ")+1));
        }
        return convertView;
    }
}
