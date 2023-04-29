package com.twit.multiplayer_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<Message> messages;

    MessagesAdapter(List<Message> msgs){
        this.messages = msgs;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(ctx);
        View messagesView = inf.inflate(R.layout.message, parent, false);
        ViewHolder vh = new ViewHolder(messagesView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        Message messageToView = messages.get(position);
        TextView msg = holder.msg;
        msg.setText(messageToView.getValue());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView msg;

    public ViewHolder(View view){
        super(view);
        msg = view.findViewById(R.id.msg);
    }

    public TextView getMsg(){
        return msg;
    }
    }
}
