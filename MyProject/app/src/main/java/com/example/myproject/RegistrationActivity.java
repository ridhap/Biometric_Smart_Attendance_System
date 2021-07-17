package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.myproject.R.color.black;
import static com.example.myproject.R.color.pink;

public class RegistrationActivity extends AppCompatActivity {


    EditText name, password, username,course;
    TextView userLogin;
    String nameValue,usernameValue,passwordValue,courseValue;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<String> items=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    EditText courseNameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth= FirebaseAuth.getInstance();
        //------------------------------------------------------------------------
        courseNameList = findViewById(R.id.courseforFaculty);
        items.add("System Software And Compiler Design");
        items.add("Computer Graphics and Visualization");
        items.add("Web Technology and Its Applications");
        items.add("Cloud Computing");
        items.add("Data Mining");
        items.add("Supply Chain Management");
        items.add("System Software Laboratory");
        items.add("Computer Graphics Laboratory");
        items.add("Mobile Application Development");

        //spinnerDialog = new SpinnerDialog(RegistrationActivity.this, items, "Select or Search Course", "Close Button Text");// With No Animation
        spinnerDialog = new SpinnerDialog(RegistrationActivity.this, items, "Select or Search Course", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation
        courseNameList.setTextColor(this.getResources().getColor(pink));

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Toast.makeText(RegistrationActivity.this, item, Toast.LENGTH_SHORT).show();
                course.setText(item);
            }
        });
//---------------------------------------------------------------------------------------------------
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });
    }
    private void setupUIViews(){
        name = (EditText)findViewById(R.id.etUserName);
        password = (EditText)findViewById(R.id.etUserPassword);
        username = (EditText)findViewById(R.id.etUserEmail);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        course=findViewById(R.id.courseforFaculty);
    }
    public void onShowClicked(View view) {
        spinnerDialog.showSpinerDialog();
    }

    public void OnRegisterClicked(View view) {
        nameValue = name.getText().toString();
        usernameValue = username.getText().toString();
        passwordValue=password.getText().toString();
        courseValue=course.getText().toString();


        if (usernameValue.equals("")) {
            username.setError("Enter Your Name");
            username.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
            username.setError("Enter a valid Email Id");
            username.requestFocus();
            return;
        }

        if (password.getText().toString().length() < 6) {
            password.setError("Set A Password Length greater than 6");
            password.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(usernameValue,passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    String uid =user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("FacultyInfo").child(uid);
                    databaseReference.child("Name").setValue(nameValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("Uid").setValue(uid);
                                databaseReference.child("Username").setValue(usernameValue);
                                databaseReference.child("Password").setValue(passwordValue);
                                databaseReference.child("Course").setValue(courseValue);

                                Toast.makeText(RegistrationActivity.this, "Data Added Succesfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(RegistrationActivity.this, "New USER Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Already Signed UP try logging In!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (name.getText().toString().equals("")) {
            name.setError("Enter your Name");
            name.requestFocus();
            return;
        }
        if (nameValue.contains(".") || nameValue.contains("/")) {
            name.setError("Enter your FirstName Space LastName. Do not use '.' or '/'");
            name.requestFocus();
            return;
        }

        Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
