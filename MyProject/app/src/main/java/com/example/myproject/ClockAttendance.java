package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.jar.Attributes;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.myproject.R.color.pink;

public class ClockAttendance extends AppCompatActivity {
    DatabaseReference databaseReference;
    Calendar calendar;
    String currentdate, currenttime, Usn, str, formattedDate;
    TextView date, time;
    //Spinner decalre
    ArrayList<String> items=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    TextView courseNameList;
    String courseValue;
    //fingerprint
    Button btnAuth;
    String valname;

    TextView tvAuthStatus,textView,textView2;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_attendance);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();

//        ---------------------------------------------------DATE AND TIME STUFF---------------------------------------------------------------------------------------------
        calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date = findViewById(R.id.dates);
        date.setText(currentdate);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        currenttime = format.format(calendar.getTime());
        time = findViewById(R.id.times);
        time.setText(currenttime);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        //------------------------------------------------------------SPINNER STUFF------------------------------------------------------------------------------------
        courseNameList = findViewById(R.id.editCourse);
//        items.add("System Software And Compiler Design");
//        items.add("Computer Graphics and Visualization");
//        items.add("Web Technology and Its Applications");
//        items.add("Cloud Computing");
//        items.add("Data Mining");
//        items.add("Supply Chain Management");
//        items.add("System Software Laboratory");
//        items.add("Computer Graphics Laboratory");
//        items.add("Mobile Application Development");
//
//        //spinnerDialog = new SpinnerDialog(RegistrationActivity.this, items, "Select or Search Course", "Close Button Text");// With No Animation
//        spinnerDialog = new SpinnerDialog(ClockAttendance.this, items, "Select or Search Course", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation
//        courseNameList.setTextColor(this.getResources().getColor(pink));
//
//        spinnerDialog.setCancellable(true); // for cancellable
//        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
//
//        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
//            @Override
//            public void onClick(String item, int position) {
//               // Toast.makeText(ClockAttendance.this, item, Toast.LENGTH_SHORT).show();
//                courseNameList.setText(item);
//            }
//        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DGlobal");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lol =dataSnapshot.getValue().toString();
                System.out.println("00000000000000000000000000000"+lol);
                courseNameList.setText(lol);
//
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = getIntent();
        str = intent.getStringExtra("message_key");
        DatabaseReference refg = FirebaseDatabase.getInstance().getReference("StudentInfo").child(str);
        refg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name =dataSnapshot.child("Name").getValue().toString();
                    String usn =(String) dataSnapshot.child("Usn").getValue().toString();
                    textView.setText(name);
                    textView2.setText("" +usn);
                    valname=name;
                    Usn=usn;
                    System.out.println(valname);
                    System.out.println(Usn);
//                            }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println(valname);
        System.out.println(Usn);
        valname=textView.getText().toString();
        Usn=textView2.getText().toString();
    }
    private void setupUIViews(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnAuth = findViewById(R.id.btnAuth);
        tvAuthStatus = findViewById(R.id.tvAuthStatus);
        textView=findViewById(R.id.textview11);
        textView2=findViewById(R.id.textview12);
    }
    //---------------------------------------------------------------------------------------------------
    public void onShow(View view) {
        spinnerDialog.showSpinerDialog();
    }
    public void authenticate(){
        //------------------------------------------------------------BIOMETRICS-------------------------------------------------------------------------
        courseValue=courseNameList.getText().toString();
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(ClockAttendance.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                tvAuthStatus.setText("Error: " + errString);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String localUsn = Usn;
                DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Attendance").child(courseValue).child(formattedDate).child(Usn);
                databaseReference1.child("Usn").setValue(localUsn);
                databaseReference1.child("Name").setValue(valname);
                tvAuthStatus.setText("Successfully Authenticated");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                tvAuthStatus.setText("Authentication failed");
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Clock in your Attendance")
                .setNegativeButtonText("Cancel")
                .setSubtitle("Use your biometric credential")
                .build();
    }
    public void bucolic(View view) {
        authenticate();
        biometricPrompt.authenticate(promptInfo);
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ClockAttendance.this, MainActivity.class));

    }
}