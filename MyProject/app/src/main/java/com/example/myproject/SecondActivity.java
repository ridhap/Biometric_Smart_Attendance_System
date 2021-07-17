package com.example.myproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    Button logout;
    DatabaseReference databaseReference,db;
    StorageReference storageReference;
    ListView listView;
    ArrayAdapter adapter;
    //    String[] arrayItems;
    int year,month,day;
    TextView curdate;
    public String curDate;
    Calendar calendar;
    FloatingActionButton fabs;
    String parcell,courseValue;
    TextView TT1,TT2 ;
    SwitchCompat switchCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        curdate=findViewById(R.id.datedisplay);
        logout = (Button)findViewById(R.id.btnLogout);

        MyApplication application=(MyApplication)getApplication();
        // application.globalVariable=true;

        switchCompat=findViewById(R.id.swOnOff);
        SharedPreferences state=getSharedPreferences("Save",MODE_PRIVATE);
        switchCompat.setChecked(state.getBoolean("Value",false));
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchCompat.isChecked()){
                    application.globalVariable=true;
                    String l = "true";
                    db = FirebaseDatabase.getInstance().getReference("Global");
                    db.setValue(l);
                    Toast.makeText(SecondActivity.this, "Enabled", Toast.LENGTH_SHORT).show();

                }
                else {
                    application.globalVariable= false;
                    String ll= application.globalVariable.toString();
                    db = FirebaseDatabase.getInstance().getReference("Global");
                    db.setValue(ll);
                    Toast.makeText(SecondActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
//
                }
            }
        });
        TT1=findViewById(R.id.tt1);
        TT2=findViewById(R.id.tt2);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        storageReference = FirebaseStorage.getInstance().getReference();
        listView=(ListView)findViewById(R.id.listView_data);



        fabs =findViewById(R.id.item_date);
        calendar= Calendar.getInstance();

//-------------------------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
        Intent intent = getIntent();
        parcell = intent.getStringExtra("parcel");
        System.out.println(parcell);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FacultyInfo").child(parcell);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name =dataSnapshot.child("Course").getValue(String.class);
                    TT1.setText(name);
                    DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("DGlobal");
                    dbs.setValue(name);
                    Toast.makeText(SecondActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+name);
                    courseValue=name;

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                System.out.println("00000000000000000000000000000000000000000000000000000"+curDate);
//                Toast.makeText(SecondActivity.this, curDate, Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SecondActivity.this,arrayList.get(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void datepicker(View view) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<String>();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = format.format(calendar.getTime());
//                curDate= DAY+"-"+MONTH+"-"+year;
                TT1.setText(strDate);
                String newDate = TT1.getText().toString();
                Log.d("newDATAValue",newDate);

                try {
                    DatabaseReference references = FirebaseDatabase.getInstance().getReference("Attendance").child(courseValue).child(newDate);
                    references.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String value = String.valueOf(dataSnapshot1.getValue());
                                    String newValue = value.replace("{", " ");
                                    newValue = newValue.replace("}", " ");
                                    Log.d("OURVALUE ", value);
                                    arrayList.add(newValue);

                                }
                                adapter = new ArrayAdapter<String>(SecondActivity.this,android.R.layout.simple_list_item_1,arrayList);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SecondActivity.this, "No Records", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
                catch (Exception e){
                    Toast.makeText(SecondActivity.this, "No Records", Toast.LENGTH_SHORT).show();

                }
                }

            },year,month,day);


        datePickerDialog.show();


        }
    }
