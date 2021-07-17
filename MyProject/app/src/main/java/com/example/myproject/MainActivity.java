package com.example.myproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.SoundEffectConstants;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private EditText Name, Password;
    private Button Login;
    private TextView Info,studentLogin;
    private int counter = 3;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        Info = (TextView)findViewById(R.id.tvInfo);
        userRegistration = (TextView)findViewById(R.id.tvRegister);
        studentLogin=findViewById(R.id.tvStudent);

        Info.setText("No of attempt remaining : 3");


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

//
//        if(user != null) {
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//            finish();
//
//        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationadminActivity.class));
                finish();
            }
        });
        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudentLogin.class));
                finish();
            }
        });
    }

    private void validate(String userName, String userPassword) {
        String usernameValue = Name.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
            Name.setError("Enter a valid Email Id");
            Name.requestFocus();
            return;
        }

        if (Password.getText().toString().length() < 6) {
            Password.setError("Set A Password Length greater than 6");
            Password.requestFocus();
            return;
        }



        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

//
//                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX      " + Uid);
//                DatabaseReference corona = FirebaseDatabase.getInstance().getReference("FacultyInfo");
//
//                corona.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild(Uid)) {
//                            String name =dataSnapshot.child("Uid").getValue(String.class);
//                            if (name==Uid){

                                if (task.isSuccessful()) {
                                    Log.d("XXXXXXXXXXXXXXXXXXXXXXXXXXX------>", "ALLOW THEM AT ONCE");
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                    Uid = currentFirebaseUser.getUid();
                                    progressDialog.dismiss();

                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                    intent.putExtra("parcel", Uid);
                                    startActivity(intent);
                                    finish();
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    counter--;
                                    Info.setText("No of attempt remaining : " + counter);
                                    progressDialog.dismiss();
                                    if (counter == 0) {
                                        Login.setEnabled(false);
                                    }
                                }
                            }
//                        }
//                        else {
//                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//            }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }


        });
//
//                else {
//                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//            }


    }
}