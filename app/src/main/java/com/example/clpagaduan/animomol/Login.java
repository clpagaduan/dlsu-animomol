package com.example.clpagaduan.animomol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btn_login;
    EditText input_email, input_password;
    TextView btn_register;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void userLogin(){
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            System.out.println("Successfully logged in!");
                            progressDialog.dismiss();
                            Intent intent = new Intent(Login.this, Catalog.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            //REMOVE COMMENT BEFORE RELEASE
//                            FirebaseUser userVerify = FirebaseAuth.getInstance().getCurrentUser();
//                            boolean emailVerify = userVerify.isEmailVerified();
//
//                            if (emailVerify == true){
//                                System.out.println("Successfully logged in!");
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(Login.this, Catalog.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                            } else {
//                                progressDialog.dismiss();
//                                Toast.makeText(Login.this, "Please verify your email address first.", Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
//                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClick (View view){
        if (view == btn_login){
            userLogin();
        } else if (view == btn_register){
            finish();
            startActivity(new Intent(this, Register.class));
        }
    }
}
