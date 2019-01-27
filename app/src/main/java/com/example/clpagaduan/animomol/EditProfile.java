package com.example.clpagaduan.animomol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{

    Button btn_save, btn_back, btn_logout;
    EditText edit_lname, edit_fname, edit_course, edit_desc;

    ImageView profileImage;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    Spinner spn_college;

    String userID, str_lname, str_fname, str_course, str_college, profileImageURL, str_desc;

    private Uri resultUri;

    ImageView btn_catalog, btn_profile, btn_matches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btn_catalog = findViewById(R.id.img_catalog);
        btn_profile = findViewById(R.id.img_profile);
        btn_matches = findViewById(R.id.img_messages);

        btn_catalog.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_matches.setOnClickListener(this);

        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);
        btn_logout = findViewById(R.id.btn_logout);

        edit_lname = findViewById(R.id.edit_lname);
        edit_fname = findViewById(R.id.edit_fname);
        edit_course = findViewById(R.id.edit_course);
        edit_desc = findViewById(R.id.edit_desc);

        profileImage = findViewById(R.id.profileImage);

        spn_college = findViewById(R.id.spn_college);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        getUserInfo();
        profileImage.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


    }

    public void onClick (View view){
        if(view == btn_save){
            updateData();
        } else if (view == btn_back){
            startActivity(new Intent(this, Catalog.class));
        } else if (view == btn_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
            return;
        } else if (view == btn_profile){
            startActivity(new Intent(this, EditProfile.class));
        } else if (view == btn_matches){
            startActivity(new Intent(this, Matches.class));
        } else if (view == btn_catalog){
            startActivity(new Intent(this, Catalog.class));
        }
//        else if (view == profileImage){
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, 1);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImage.setImageURI(resultUri);
        }
    }

    private void getUserInfo(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("lname") !=null){
                        str_lname = map.get("lname").toString();
                        edit_lname.setText(str_lname);
                    }

                    if(map.get("fname") !=null){
                        str_fname = map.get("fname").toString();
                        edit_fname.setText(str_fname);
                    }

                    if(map.get("course") !=null){
                        str_course = map.get("course").toString();
                        edit_course.setText(str_course);
                    }

                    if(map.get("college") !=null){
                        str_college = map.get("college").toString();
                        for(int i=0; i< spn_college.getAdapter().getCount(); i++){
                            if(spn_college.getAdapter().getItem(i).toString().contains(str_college)){
                                spn_college.setSelection(i);
                            }
                        }
                    }

                    if(map.get("desc") !=null){
                        str_desc = map.get("desc").toString();
                        edit_desc.setText(str_desc);
                    }
                    Glide.clear(profileImage);
                    if(map.get("profileImageUrl") !=null){
                        profileImageURL = map.get("profileImageUrl").toString();
                        switch (profileImageURL){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(profileImage);                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageURL).into(profileImage);
                                break;
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateData(){
        str_lname = edit_lname.getText().toString();
        str_fname = edit_fname.getText().toString();
        str_course = edit_course.getText().toString();
        str_college = spn_college.getSelectedItem().toString();
        str_desc = edit_desc.getText().toString();

        Map userInfo = new HashMap();

        userInfo.put("lname", str_lname);
        userInfo.put("fname", str_fname);
        userInfo.put("course", str_course);
        userInfo.put("college", str_college);
        userInfo.put("desc", str_desc);

        databaseReference.updateChildren(userInfo);

        if(resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            databaseReference.updateChildren(newImage);
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                            return;
                        }
                    });
//                    Map userInfo = new HashMap();
//                    userInfo.put("profileImageUrl", downloadUrl);
//                    databaseReference.updateChildren(userInfo);
//
//                    finish();
//                    return;
                }
            });
        } else {
            finish();
        }

    }
}
