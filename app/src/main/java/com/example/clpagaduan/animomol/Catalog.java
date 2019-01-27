package com.example.clpagaduan.animomol;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Catalog extends AppCompatActivity implements View.OnClickListener{

    Button btn_logout;
//    Button btn_profile, btn_matches;
    ImageView btn_catalog, btn_profile, btn_matches;
    private FirebaseAuth firebaseAuth;

    private Card cards_data[];
//    private ArrayList<String> al;
    private arrayAdapter arrayAdapter;
    private int i;
    public String userID="1";
    private String checkuserID;

    private String str_user1name, str_user2name;

    private ArrayList<String> queue = new ArrayList<>();

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference usersDB;

    DatabaseReference sexDB;

    ListView listView;
    List<Card> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
//        ButterKnife.inject(this);

        usersDB = FirebaseDatabase.getInstance().getReference().child("Users");

        checkCurrentUserSex();
        rowItems = new ArrayList<Card>();

        firebaseAuth = FirebaseAuth.getInstance();

        //NAV BAR//
        btn_logout = findViewById(R.id.btn_logout);
        btn_profile = findViewById(R.id.img_profile);
        btn_matches = findViewById(R.id.img_messages);

        btn_logout.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_matches.setOnClickListener(this);
        //END NAV BAR//

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

//        arrayAdapter.notifyDataSetChanged();


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Card card = (Card) dataObject;
                String checkuserID = card.getUserID();
                usersDB.child(checkuserID).child("Connections").child("Dislike").child(userID).setValue(true);
                Toast.makeText(Catalog.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Card card = (Card) dataObject;
                checkuserID = card.getUserID();
                usersDB.child(checkuserID).child("Connections").child("Like").child(userID).setValue(true);
                isConnectionMatch(checkuserID);
                Toast.makeText(Catalog.this, "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(Catalog.this, "Clicked", Toast.LENGTH_SHORT).show();            }
        });

    }
    private String userSex, userGen, notUserSex, notUserGen;
    public void checkCurrentUserSex(){

        userID = user.getUid();

        sexDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        sexDB.addValueEventListener(new ValueEventListener(){
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot){
               User user = dataSnapshot.getValue(User.class);
               String lname = user.getLname();
               String gen = user.getGenderPref();
               String sex = user.getSex();

               userSex = sex;
               userGen = gen;

               System.out.println("Last name: " + lname);
               System.out.println("Gender pref: " + gen);
               System.out.println("Sex: " + sex);

               getPotentialMatchesUsers(userSex, userGen);


               //ORIGINAL CODE
//               if(userSex.equals("0")){
//                   notUserSex = "1";
//                   System.out.println("Match's sex: " + notUserSex);
//                   getPotentialMatchesUsers();
//               } else if (userSex.equals("1")){
//                   notUserSex = "0";
//                   System.out.println("Match's sex: " + notUserSex);
//                   getPotentialMatchesUsers();
//               }
               ///END ORIGINAL CODE
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError){

           }
        });
    }

    public void getPotentialMatches(){
        DatabaseReference potentialMatchesDB = FirebaseDatabase.getInstance().getReference().child("Users").child(notUserSex);
        potentialMatchesDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    //GENDER MATCHING ALGORITHM (DO NOT MODIFY)
    public void getPotentialMatchesUsers(final String userSex, final String userGen){
        DatabaseReference matchesDB = FirebaseDatabase.getInstance().getReference().child("Users");


        matchesDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("Dislike").hasChild(userID)
                        && !dataSnapshot.child("Connections").child("Like").hasChild(userID)){
                    User user = dataSnapshot.getValue(User.class);
                    String checkUserID = user.getUserID();

                    if (!checkUserID.equals(userID)){
                        String checkSex = user.getSex();
                        String checkGen = user.getGenderPref();
                        String getDls_id = user.getDls_id();

//                        String college = " ";

//                        String value = "11415843";
                        //              76543210

                        CharSequence dls_id = getDls_id.subSequence(0, 3);

                        String str_dlsid = "ID " + dls_id.toString();

                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }

                        String match_ID = dataSnapshot.getKey().toString();

//                        String age = user.getBday();
//                        System.out.println("Age: " + age);
//                        String str[] = age.split("-");
//                        int day = Integer.parseInt(str[0]);
//                        int month = Integer.parseInt(str[1]);
//                        int year = Integer.parseInt(str[2]);
//
//                        Calendar dob = Calendar.getInstance();
//                        Calendar today = Calendar.getInstance();
//
//                        dob.set(year, month, day);
//                        int ageInt = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//
//                        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
//                            ageInt--;
//                        }
//
//                        Integer ageIntFinal = new Integer(ageInt);
//                        String ageS = ageIntFinal.toString();
//
//                        System.out.println("Age: " + ageS);

                        if (userSex.equals("0")){                                                   //User is men
                            if (userGen.equals("0")){                                               //User is Homo men
                                if (checkSex.equals("0")){
                                    if (checkGen.equals("0") || checkGen.equals("2")){                  //Homo men for homo/bi men
                                        if (!queue.contains(match_ID)){

                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }
                            } else if (userGen.equals("1")){                                        //User is Hetero men
                                if (checkSex.equals("1")){
                                    if (checkGen.equals("0") || checkGen.equals("2")){                  //Hetero men for hetero/bi women
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            } else if (userGen.equals("2")){                                        //User is Bi men
                                if (checkSex.equals("0")){
                                    if (checkGen.equals("0") || checkGen.equals("2")){                  //Bi men for homo/bi men
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } else if (checkSex.equals("1")){                                   // User is Bi men
                                    if (checkGen.equals("0") || checkGen.equals("2")){                  //Bi men for hetero/bi women
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        } else if (userSex.equals("1")){                                            //User is women
                            if (userGen.equals("1")){                                               //User is Homo women
                                if (checkSex.equals("1")){
                                    if (checkGen.equals("1") || checkGen.equals("2")){                  //Homo women for homo/bi women
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            } else if (userGen.equals("0")){                                        //User is Hetero women
                                if (checkSex.equals("0")){
                                    if (checkGen.equals("1") || checkGen.equals("2")){                  //Hetero women for hetero/bi men
                                        if (!queue.contains(match_ID)){



//                                            LocalDate birthdate = LocalDate.of(year, month, day);
//
//                                            Period period = Period.between(birthdate, LocalDate.now());


                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            } else if (userGen.equals("2")){                                        //User is Bi women
                                if (checkSex.equals("0")){
                                    if (checkGen.equals("1") || checkGen.equals("2")){                  //Bi women for hetero/bi men
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } else if (checkSex.equals("1")){                                   // User is Bi women
                                    if (checkGen.equals("1") || checkGen.equals("2")){                  //Bi women for hetero/bi women
                                        if (!queue.contains(match_ID)){
                                            Card card = new Card(
                                                    dataSnapshot.getKey().toString(),
                                                    dataSnapshot.child("fname").getValue().toString(),
                                                    profileImageUrl,
                                                    str_dlsid,
                                                    dataSnapshot.child("desc").getValue().toString());
                                            rowItems.add(card);
                                            queue.add(match_ID);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }



                    }

                    //ORIGINAL CODE (remove parameters inside getPotentialMatchesUsers)
//                    if (!checkUserID.equals(userID)){
//                        String checkSex = user.getSex();
//                        String profileImageUrl = "default";
//                        if (userSex.equals(checkSex)){
//                            if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
//                                profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
//                            }
//                            String match_ID = dataSnapshot.getKey().toString();
//                            String match_fname = dataSnapshot.child("fname").getValue().toString();
//                            String match_dls_id = dataSnapshot.child("dls_id").getValue().toString();
//
//                            String match_desc = dataSnapshot.child("desc").getValue().toString();
////                            String match_co = dataSnapshot.child("fname").getValue().toString();
//
//                            Card card = new Card(match_ID, match_fname, profileImageUrl, match_dls_id, match_desc);
//                            rowItems.add(card);
////                            al.add(dataSnapshot.child("lname").getValue().toString());
//                            arrayAdapter.notifyDataSetChanged();
//                        }
//                    }
                    ///End original code
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

    private void getUserInfo(){

    }

    private void isConnectionMatch(String checkuserID){
        DatabaseReference currentUserConnectionsDB = usersDB.child(userID).child("Connections").child("Like").child(checkuserID);
        currentUserConnectionsDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(Catalog.this, "It's a match!", Toast.LENGTH_LONG).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
//                    usersDB.child(dataSnapshot.getKey()).child("Connections").child("Matches").child(userID).setValue(true);
                    usersDB.child(dataSnapshot.getKey()).child("Connections").child("Matches").child(userID).child("ChatID").setValue(key);

//                    usersDB.child(userID).child("Connections").child("Matches").child(dataSnapshot.getKey()).setValue(true);
                    usersDB.child(userID).child("Connections").child("Matches").child(dataSnapshot.getKey()).child("ChatID").setValue(key);


                    showMatchDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMatchDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Catalog.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_match, null);
        mBuilder.setView(mView);

        final TextView txt_user1name = mView.findViewById(R.id.txt_currentUser);
        final TextView txt_user2name = mView.findViewById(R.id.txt_matchUser);
        final ImageView profileImage1 = mView.findViewById(R.id.img1);
        final ImageView profileImage2 = mView.findViewById(R.id.img2);

        final Button btn_continue, btn_message;
        btn_message = mView.findViewById(R.id.btn_message);
        btn_continue = mView.findViewById(R.id.btn_continue);

        DatabaseReference showMatchesCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        DatabaseReference showMatchesMatchedUser = FirebaseDatabase.getInstance().getReference().child("Users").child(checkuserID);

        showMatchesCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("fname") !=null){
                        str_user1name = map.get("fname").toString();
                        txt_user1name.setText(str_user1name);
                    }
                    String profileImageURL1;
                    Glide.clear(profileImage1);
                    if(map.get("profileImageUrl") !=null){
                        profileImageURL1 = map.get("profileImageUrl").toString();
                        switch (profileImageURL1){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(profileImage1);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageURL1).into(profileImage1);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        showMatchesMatchedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("fname") !=null){
                        str_user2name = map.get("fname").toString();
                        txt_user2name.setText(str_user2name);
                    }

                    String profileImageURL2;
                    Glide.clear(profileImage2);
                    if(map.get("profileImageUrl") !=null){
                        profileImageURL2 = map.get("profileImageUrl").toString();
                        switch (profileImageURL2){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(profileImage2);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageURL2).into(profileImage2);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Catalog.this);
//        View mView = getLayoutInflater().inflate(R.layout.dialog_match, null);
//        mBuilder.setView(mView);

//        String user1name = usersDB.child(userID).child("fname").toString();
//        final TextView txt_user1name = mView.findViewById(R.id.txt_currentUser);
//        txt_user1name.setText(user1name);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

//        btn_message.setOnClickListener(this);
//        btn_continue.setOnClickListener(this);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btn_continue) {
                    dialog.dismiss();
                } else if (view == btn_message){
                    Intent intent = new Intent(getApplicationContext(), Matches.class);
                    getApplicationContext().startActivity(intent);

//                    startActivity(new Intent(getBaseContext(), Matches.class));
                }
            }
        });
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
