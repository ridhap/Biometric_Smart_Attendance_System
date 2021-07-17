package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.jar.Attributes;

public class RegistrationadminActivity extends AppCompatActivity {


    private EditText Email;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationadmin);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Email = (EditText)findViewById(R.id.etName1);
        Password = (EditText)findViewById(R.id.etPassword1);

    }
    public void OnAllowClick(View view) {
        String userName=Email.getText().toString();
        String userPassword=Password.getText().toString();
        if (Email.getText().toString().equals("")) {
            Email.setError("Enter a valid Email Id");
            Email.requestFocus();
            return;
        }
        if (Password.getText().toString().equals("")) {
            Password.setError("Enter Valid Admin Password");
            Password.requestFocus();
            return;
        }
        if((userName.equals("Admin")) && (userPassword.equals("ad123"))){
            Intent intent = new Intent(RegistrationadminActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(RegistrationadminActivity.this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
        }

    }
}
