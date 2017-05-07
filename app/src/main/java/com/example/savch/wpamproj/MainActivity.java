package com.example.savch.wpamproj;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savch.wpamproj.base.MySQLAdapter;
import com.example.savch.wpamproj.login.LoginActivity;
import com.facebook.login.LoginManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static private double controlSum = 0.0d;
    private final String LOG_TAG = "myLogs_MAIN";
    Context context;
    static final String STATE_NAME = "masterName";
    private String mCurrentName;
    private String mCurrentEmail;
    private boolean inputMoneyFlag = true;
    private boolean plusMinusChoise = true;
    MySQLAdapter dbHelper;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_NAME, mCurrentName);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mCurrentEmail = extras.getString("userEmail");

        setActivityBackgroundColor(getResources().getColor(R.color.primary));

        dbHelper = new MySQLAdapter(this);

        TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
        textViewInfo.setText(String.format( "%.2f", controlSum));

        dbHelper.openToWrite();
        Cursor cursor = dbHelper.querySum(mCurrentEmail);
        while(cursor.moveToNext())
        {
            textViewInfo.setText(String.valueOf(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("totalSum")))));
        }


        //Add transaction record to db
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
                EditText addEditText = (EditText) findViewById(R.id.addEditText);
                EditText delEditText = (EditText) findViewById(R.id.delEditText);
                String tmpAdd = addEditText.getText().toString();;
                String tmpDel = delEditText.getText().toString();;

                //check if not entered both field in one time
                strCheck(tmpAdd, tmpDel);
                if(inputMoneyFlag) {
                    //TODO: name, email and amount
                    double amount;
                    if(plusMinusChoise){
                        amount = Double.parseDouble(tmpAdd);
                    }else{
                        amount = Double.parseDouble(tmpDel) * (-1);
                    }
                    String ss  = mCurrentName;
                    String kk = mCurrentEmail;
                    dbHelper.openToWrite();
                    long rowID = dbHelper.insertTransactionTable("name", "email", "amount", "currentdate",
                            mCurrentName, mCurrentEmail, amount);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                    Snackbar.make(view, "Record added!",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Cursor cursor = dbHelper.querySum(mCurrentEmail);
                    while(cursor.moveToNext())
                    {
                        textViewInfo.setText(String.valueOf(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("totalSum")))));
                    }
                    addEditText.setText("");
                    delEditText.setText("");
                }else{
                    Snackbar.make(view, "You can input just in one field at the same time!",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    addEditText.setText("");
                    delEditText.setText("");
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if (savedInstanceState != null) {
            mCurrentName = savedInstanceState.getString(STATE_NAME);
            Log.d("STATE", "------ not null --------");
        } else {
            Log.d("STATE", "------ null --------");
            mCurrentName = extras.getString("userName");
        }
        // Probably initialize members with default values for a new instance

        //if (extras != null) {

            String firstLetter = imageSelecter(mCurrentName);
            View hView =  navigationView.getHeaderView(0);
            String helloNameUser = "Hello, " + firstWord(mCurrentName) + "!";

            TextView txtHelloName = (TextView) hView.findViewById(R.id.userNameHelloTextView);
            txtHelloName.setText(helloNameUser);

            TextView txtHelloEmail = (TextView) hView.findViewById(R.id.userEmailHelloTextView);
            txtHelloEmail.setText(mCurrentEmail);

            ImageView imgName = (ImageView) hView.findViewById(R.id.imageAcc);
            switch (firstLetter) {
                case "A":
                    imgName.setImageResource(R.drawable.a);
                    break;
                case "B":
                    imgName.setImageResource(R.drawable.b);
                    break;
                case "C":
                    imgName.setImageResource(R.drawable.c);
                    break;
                case "D":
                    imgName.setImageResource(R.drawable.d);
                    break;
                case "E":
                    imgName.setImageResource(R.drawable.e);
                    break;
                case "F":
                    imgName.setImageResource(R.drawable.f);
                    break;
                case "G":
                    imgName.setImageResource(R.drawable.g);
                    break;
                case "H":
                    imgName.setImageResource(R.drawable.h);
                    break;
                case "I":
                    imgName.setImageResource(R.drawable.i);
                    break;
                case "J":
                    imgName.setImageResource(R.drawable.j);
                    break;
                case "K":
                    imgName.setImageResource(R.drawable.k);
                    break;
                case "L":
                    imgName.setImageResource(R.drawable.l);
                    break;
                case "M":
                    imgName.setImageResource(R.drawable.m);
                    break;
                case "N":
                    imgName.setImageResource(R.drawable.n);
                    break;
                case "O":
                    imgName.setImageResource(R.drawable.o);
                    break;
                case "P":
                    imgName.setImageResource(R.drawable.p);
                    break;
                case "Q":
                    imgName.setImageResource(R.drawable.q);
                    break;
                case "R":
                    imgName.setImageResource(R.drawable.r);
                    break;
                case "S":
                    imgName.setImageResource(R.drawable.s);
                    break;
                case "T":
                    imgName.setImageResource(R.drawable.t);
                    break;
                case "U":
                    imgName.setImageResource(R.drawable.u);
                    break;
                case "V":
                    imgName.setImageResource(R.drawable.v);
                    break;
                case "W":
                    imgName.setImageResource(R.drawable.w);
                    break;
                case "X":
                    imgName.setImageResource(R.drawable.x);
                    break;
                case "Y":
                    imgName.setImageResource(R.drawable.y);
                    break;
                case "Z":
                    imgName.setImageResource(R.drawable.z);
                    break;
                default:
                    imgName.setImageResource(R.drawable.failed_print);
                    break;
            //}
            }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (LoginActivity.isLoggedIn()){
                //if user logged in with Facebook, then after onBeckPressed - log out will be
                LoginManager.getInstance().logOut();
            }
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_main) {
            //intent = new Intent(getApplicationContext(),MainActivity.class);

        } else if (id == R.id.nav_transaction) {
            intent = new Intent(getApplicationContext(),TransactionActivity.class);
            intent.putExtra("userEmail", mCurrentEmail);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void strCheck(String x, String y) {
        //inputMoneyFlag = !x.isEmpty() && y.isEmpty() || !(!x.isEmpty() && !y.isEmpty());
        if (!x.isEmpty() && y.isEmpty()){
            inputMoneyFlag = true;
            plusMinusChoise = true;
        }else if (x.isEmpty() && !y.isEmpty()){
            inputMoneyFlag = true;
            plusMinusChoise = false;
        }else if(x.isEmpty() && y.isEmpty()){
            inputMoneyFlag = false;
        }else if(!x.isEmpty() && !y.isEmpty()){
            inputMoneyFlag = false;
        }
    }

    private String imageSelecter(String name){
        return name.substring(0,1).toUpperCase();
    }

    private String firstWord(String sentense){
        Pattern p = Pattern.compile("^([\\w\\-]+)");
        Matcher m = p.matcher(sentense);
        if (m.find())
        {
            return m.group(1);
        }else{
            return "NO";
        }
    }
}
