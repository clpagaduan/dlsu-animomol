package com.example.clpagaduan.animomol.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.clpagaduan.animomol.Catalog;
import com.example.clpagaduan.animomol.EditProfile;
import com.example.clpagaduan.animomol.Matches;
import com.example.clpagaduan.animomol.MatchesAdapter;
import com.example.clpagaduan.animomol.MatchesObject;
import com.example.clpagaduan.animomol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendEditText;
    private Button mSendButton;

    private String currentUserID, matchID, chatID;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    ImageView btn_catalog, btn_profile, btn_matches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_catalog = findViewById(R.id.img_catalog);
        btn_profile = findViewById(R.id.img_profile);
        btn_matches = findViewById(R.id.img_messages);

        btn_catalog.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_matches.setOnClickListener(this);

        matchID = getIntent().getExtras().getString("matchID");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentUserID).child("Connections").child("Matches").child(matchID).child("ChatID");

        mDatabaseChat = FirebaseDatabase.getInstance().getReference()
                .child("Chat");

        getChatID();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(Chat.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatAdapter(getDataSetChat(), Chat.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();


    private List<ChatObject> getDataSetChat(){
        return resultsChat;
    }

    private void sendMessage(){
        String str_message = mSendEditText.getText().toString();
        if(!str_message.isEmpty()){
            DatabaseReference newMessageDB = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("CreatedByUserID", currentUserID);
            newMessage.put("Message", str_message);
            newMessageDB.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatID(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatID = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatID);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages(){
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String Message = null;
                    String CreatedByUser= null;

                    if(dataSnapshot.child("Message").getValue()!=null){
                        Message = dataSnapshot.child("Message").getValue().toString();
                    }

                    if(dataSnapshot.child("CreatedByUserID").getValue()!=null){
                        CreatedByUser = dataSnapshot.child("CreatedByUserID").getValue().toString();
                    }

                    if(Message != null && CreatedByUser != null){
                        Boolean currentUserBoolean = false;
                        if (CreatedByUser.equals(currentUserID)){
                            currentUserBoolean=true;
                        }
                        ChatObject newMessage = new ChatObject(Message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick (View view){
        if (view == mSendButton){
            sendMessage();
        } else if (view == btn_profile){
            startActivity(new Intent(this, EditProfile.class));
        } else if (view == btn_matches){
            startActivity(new Intent(this, Matches.class));
        } else if (view == btn_catalog){
            startActivity(new Intent(this, Catalog.class));
        }
    }
}
