package com.example.clpagaduan.animomol;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import androi

public class Matches extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    ImageView btn_catalog, btn_profile, btn_matches;


    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        btn_catalog = findViewById(R.id.img_catalog);
        btn_profile = findViewById(R.id.img_profile);
        btn_matches = findViewById(R.id.img_messages);

        btn_catalog.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_matches.setOnClickListener(this);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager = new LinearLayoutManager(Matches.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), Matches.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        getUserMatchID();
    }

    private void getUserMatchID(){
        DatabaseReference matchDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("Connections").child("Matches");
        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        FetchMatchInfo(match.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInfo(String key){
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(key);

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userID = dataSnapshot.getKey();
                    String fname = "Name";
                    String profileImageUrl = "URL";
                    if (dataSnapshot.child("fname").getValue()!=null){
                        fname = dataSnapshot.child("fname").getValue().toString();
                    }

                    if (dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject(userID, fname, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDataSetMatches(){
        return resultsMatches;
    }

    public void onClick (View view){
//        if(view == btn_logout){
////            checkCurrentUserSex();
//            firebaseAuth.signOut();
//            finish();
//            startActivity(new Intent(this, Login.class));
//            return;
//        }

        if (view == btn_profile){
            startActivity(new Intent(this, EditProfile.class));
        } else if (view == btn_matches){
            startActivity(new Intent(this, Matches.class));
        } else if (view == btn_catalog){
            startActivity(new Intent(this, Catalog.class));
        }
    }
}
