package com.example.clpagaduan.animomol;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity implements View.OnClickListener{
    EditText input_fname, input_lname, input_password, input_email, input_id;
    Spinner spn_school, spn_sex, spn_genderPreference;
    TextView textview_terms, textview_login, input_bday;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    Button btn_register;

    DatabaseReference databaseAddUser;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public String email="example@example.com";
    public String lname="Dela Cruz";
    public String fname="Juan";
    public String school="DLSU";
    public String dls_id="11412345";
    public String sex="Apache Helicopter";
    public String gender="Vegan";
    public String bday="17-01-1998";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        databaseAddUser = FirebaseDatabase.getInstance().getReference("Users");

        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        input_password = findViewById(R.id.input_password);
        input_email = findViewById(R.id.input_email);
        input_id = findViewById(R.id.input_id);
        input_bday = findViewById(R.id.input_bday);

        spn_school = findViewById(R.id.spn_school);
        spn_sex = findViewById(R.id.spn_sex);
        spn_genderPreference = findViewById(R.id.spn_genderPreference);
        textview_terms = findViewById(R.id.textview_terms);
        textview_login = findViewById(R.id.textview_login);

        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);
        textview_terms.setOnClickListener(this);
        textview_login.setOnClickListener(this);
        input_bday.setOnClickListener(this);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
//                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = dayOfMonth + "-" + month + "-" + year;
                input_bday.setText(date);
//                bday = date;
            }
        };
    }

    private void registerUser(){
        email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        lname = input_lname.getText().toString().trim();
        fname = input_fname.getText().toString().trim();
        dls_id = input_id.getText().toString().trim();

        bday = input_bday.getText().toString();


        school = spn_school.getSelectedItem().toString();
        sex = Integer.toString(spn_sex.getSelectedItemPosition());
        gender = Integer.toString(spn_genderPreference.getSelectedItemPosition());

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(lname)){
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(dls_id)){
            Toast.makeText(this, "Please enter ID number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //REMOVE COMMENT BEFORE RELEASE
//                            FirebaseUser userVerify = FirebaseAuth.getInstance().getCurrentUser();
//                            userVerify.sendEmailVerification();


                            String UID = firebaseAuth.getCurrentUser().getUid();
                            String profileImageUrl = "https://firebasestorage.googleapis.com/v0/b/dlsu-animomol.appspot.com/o/ProfileImages%2Fo2xZ1rdwSIR8xIyFFPNYLSS3PN43?alt=media&token=9672707a-c560-4a73-aeb8-994c3b557fbf";
                            String desc = " ";
                            System.out.println("Bday: " + bday);
                            User user = new User(UID, lname, fname, email, dls_id, school, sex, gender, bday, desc, profileImageUrl);
                            databaseAddUser.child(UID).setValue(user);

                            Toast.makeText(Register.this, "Check your email to verify!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            Register.this.startActivity(intent);
                        }
                    }
                });
    }

    public void openDialog(){
        TermsAndConditions dialog = new TermsAndConditions();
        dialog.show(getSupportFragmentManager(), "Terms and conditions");
    }

    public void onClick (View view){
        if (view == btn_register){
            registerUser();
        } else if (view == textview_login){
            startActivity(new Intent(this, Login.class));
        } else if (view == textview_terms){
            openDialog();
        } else if (view == input_bday){
            Calendar cal =  Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    Register.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}
