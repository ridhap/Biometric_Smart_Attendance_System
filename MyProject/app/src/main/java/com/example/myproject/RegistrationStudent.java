package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.myproject.R.color.pink;

public class RegistrationStudent extends AppCompatActivity {

    EditText name, password, email,Usn;
    TextView userLogin;
    String nameValue,emailValue,passwordValue,usnValue;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_student);
        firebaseAuth= FirebaseAuth.getInstance();

        setupUIViews();
    }
    private void setupUIViews(){
        name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPassword);
        email = (EditText)findViewById(R.id.etEmail);
        Usn=findViewById(R.id.etusn);
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public void OnRegisterClicked(View view) {
        nameValue = name.getText().toString();
        usnValue=Usn.getText().toString();
        emailValue = email.getText().toString();
        passwordValue=password.getText().toString();
        if (nameValue.equals("")) {
            name.setError("Enter Your Name");
            name.requestFocus();
            return;
        }
        if (usnValue.equals("")) {
            Usn.setError("Enter Your USN");
            Usn.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            email.setError("Enter a valid Email Id");
            email.requestFocus();
            return;
        }

        if (password.getText().toString().length() < 6) {
            password.setError("Set A Password Length greater than 6");
            password.requestFocus();
            return;
        }
        if (nameValue.contains(".") || nameValue.contains("/")) {
            name.setError("Enter your FirstName Space LastName. Do not use '.' or '/'");
            name.requestFocus();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationStudent.this, "New USER Created", Toast.LENGTH_SHORT).show();
                     FirebaseUser user = task.getResult().getUser();
                     String uid =user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("StudentInfo").child(uid);
                    databaseReference.child("Username").setValue(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("Uid").setValue(uid);
                                databaseReference.child("Usn").setValue(usnValue);
                                databaseReference.child("Password").setValue(passwordValue);
                                databaseReference.child("Name").setValue(nameValue);

                                Toast.makeText(RegistrationStudent.this, "Data Added Succesfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationStudent.this, "Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistrationStudent.this, "Already Signed UP try logging In!", Toast.LENGTH_SHORT).show();
                }

            }

        });

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USER_SERVICE);

               // Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();



        Intent intent=new Intent(RegistrationStudent.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
