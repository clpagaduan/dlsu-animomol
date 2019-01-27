package com.example.clpagaduan.animomol.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.clpagaduan.animomol.MatchesObject;
import com.example.clpagaduan.animomol.MatchesViewHolder;
import com.example.clpagaduan.animomol.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>  {
    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

//    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv =  new ChatViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.mMessage.setText(chatList.get(i).getMessage());
        if(chatList.get(i).getCurrentUser()){
            chatViewHolder.mMessage.setGravity(Gravity.END);
            chatViewHolder.mMessage.setTextColor(Color.parseColor("#404040"));
            chatViewHolder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        } else {
            chatViewHolder.mMessage.setGravity(Gravity.START);
            chatViewHolder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            chatViewHolder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
