package com.example.clpagaduan.animomol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder>  {
    private List<MatchesObject> matchesList;
    private Context context;

    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

//    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolder rcv =  new MatchesViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder matchesViewHolder, int i) {
        matchesViewHolder.mMatchID.setText(matchesList.get(i).getUserID());
        matchesViewHolder.mMatchName.setText(matchesList.get(i).getFname());
        Glide.with(context).load(matchesList.get(i).getProfileImageUrl()).into(matchesViewHolder.mMatchImage);
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
