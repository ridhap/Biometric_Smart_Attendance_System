package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class StudentLogin extends AppCompatActivity {
    private EditText Email, Password;
    private Button Lins;
    private TextView Info, studentLogin;
    private int counter = 3;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Boolean accessToLogin;
    //    String lol;
    private TextView userRegistrationStu,iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPass);
        Lins = (Button) findViewById(R.id.btnLins);
        Info = (TextView) findViewById(R.id.tvInf);
        userRegistration = (TextView) findViewById(R.id.tvUserLogin);
        studentLogin = findViewById(R.id.tvStudent);
        MyApplication application=(MyApplication)getApplication();
        accessToLogin=application.globalVariable;
        iv=findViewById(R.id.invisibless);
        // application.globalVariable=true;
        Info.setText("No of attempt remaining : 3");
        // Toast.makeText(StudentLogin.this, application.globalVariable.toString(), Toast.LENGTH_SHORT).show();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Global");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lol =dataSnapshot.getValue().toString();
                System.out.println("00000000000000000000000000000"+lol);
//                if(lol=="false"){
//                    Lins.setEnabled(false);
//                }
//                else {
//                    Lins.setEnabled(true);
//                }
                boolean b = Boolean.parseBoolean(lol);
                Lins.setEnabled(b);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FirebaseUser user = firebaseAuth.getCurrentUser();

//        if(user != null) {
//            finish();
//            startActivity(new Intent(StudentLogin.this, ClockAttendance.class));
//            finish();
//
//        }

        Lins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });
        userRegistrationStu = (TextView)findViewById(R.id.tvstuRegister);
        userRegistrationStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLogin.this, RegistrationStudent.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    String Uid=currentFirebaseUser.getUid().toString();
                    Toast.makeText(StudentLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(StudentLogin.this, ClockAttendance.class);
                    intent.putExtra("message_key", Uid);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(StudentLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No of attempt remaining : " + counter);
                    progressDialog.dismiss();
                    if(counter == 0) {
                        Lins.setEnabled(false);
                    }

                }
            }
        });
    }
}