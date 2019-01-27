package com.example.clpagaduan.animomol.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.clpagaduan.animomol.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMessage;
    public LinearLayout mContainer;

    public ChatViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view){
    }
}
