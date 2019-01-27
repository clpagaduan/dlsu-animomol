package com.example.clpagaduan.animomol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clpagaduan.animomol.Chat.Chat;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchID, mMatchName;
    public ImageView mMatchImage;
    public MatchesViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchID = itemView.findViewById(R.id.txt_matchID);
        mMatchName = itemView.findViewById(R.id.txt_matchName);
        mMatchImage = itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(view.getContext(), Chat.class);
        Bundle b = new Bundle();
        b.putString("matchID", mMatchID.getText().toString());
        intent.putExtras(b);

        view.getContext().startActivity(intent);
    }
}
