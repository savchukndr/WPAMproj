package com.example.savch.wpamproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.savch.wpamproj.login.LoginActivity;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static private double controlSum = 0.0d;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActivityBackgroundColor(getResources().getColor(R.color.primary));

        TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
        textViewInfo.setText(String.format( "%.2f", controlSum));


        //Add transaction record to db
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Record added!",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
                String tmpAdd;
                String tmpDel;

                EditText addEditText = (EditText) findViewById(R.id.addEditText);
                EditText delEditText = (EditText) findViewById(R.id.delEditText);

                tmpAdd = addEditText.getText().toString();
                tmpDel = delEditText.getText().toString();

                strCheck(textViewInfo, tmpAdd, tmpDel);
                addEditText.setText("");
                delEditText.setText("");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String firstLetter = imageSelecter(extras.getString("userName"));
            /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.nav_header_main, null);
            ImageView imgName = (ImageView) ll.findViewById(R.id.imageAcc);*/
            View hView =  navigationView.getHeaderView(0);
            ImageView imgName = (ImageView) hView.findViewById(R.id.imageAcc);
            switch (firstLetter){
                case"A":
                    imgName.setImageResource(R.drawable.a);
                    break;
                case"B":
                    imgName.setImageResource(R.drawable.b);
                    break;
                case"C":
                    imgName.setImageResource(R.drawable.c);
                    break;
                case"D":
                    imgName.setImageResource(R.drawable.d);
                    break;
                case"E":
                    imgName.setImageResource(R.drawable.e);
                    break;
                case"F":
                    imgName.setImageResource(R.drawable.f);
                    break;
                case"G":
                    imgName.setImageResource(R.drawable.g);
                    break;
                case"H":
                    imgName.setImageResource(R.drawable.h);
                    break;
                case"I":
                    imgName.setImageResource(R.drawable.i);
                    break;
                case"J":
                    imgName.setImageResource(R.drawable.j);
                    break;
                case"K":
                    imgName.setImageResource(R.drawable.k);
                    break;
                case"L":
                    imgName.setImageResource(R.drawable.l);
                    break;
                case"M":
                    imgName.setImageResource(R.drawable.m);
                    break;
                case"N":
                    imgName.setImageResource(R.drawable.n);
                    break;
                case"O":
                    imgName.setImageResource(R.drawable.o);
                    break;
                case"P":
                    imgName.setImageResource(R.drawable.p);
                    break;
                case"Q":
                    imgName.setImageResource(R.drawable.q);
                    break;
                case"R":
                    imgName.setImageResource(R.drawable.r);
                    break;
                case"S":
                    imgName.setImageResource(R.drawable.s);
                    break;
                case"T":
                    imgName.setImageResource(R.drawable.t);
                    break;
                case"U":
                    imgName.setImageResource(R.drawable.u);
                    break;
                case"V":
                    imgName.setImageResource(R.drawable.v);
                    break;
                case"W":
                    imgName.setImageResource(R.drawable.w);
                    break;
                case"X":
                    imgName.setImageResource(R.drawable.x);
                    break;
                case"Y":
                    imgName.setImageResource(R.drawable.y);
                    break;
                case"Z":
                    imgName.setImageResource(R.drawable.z);
                    break;
                default:
                    imgName.setImageResource(R.drawable.failed_print);
                    break;
            }

            //The key argument here must match that used in the other activity

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

    public void onClick(View view) {
        //TODO: implementaion
    }

    public void strCheck(TextView txt, String x, String y) {
        double delAmount;
        double addAmount;
        Toast toast;
        if (!x.isEmpty() && y.isEmpty()) {
            addAmount = Double.parseDouble(x);
            controlSum += addAmount;
            txt.setText(String.format( "%.2f", controlSum));
        }else if(x.isEmpty() && !y.isEmpty()){
            delAmount = Double.parseDouble(y);
            if (controlSum < delAmount){
                toast = Toast.makeText(getApplicationContext(), "Персик ты попытался удалить слишком много",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else{
                controlSum -= delAmount;
                txt.setText(String.format( "%.2f", controlSum));
            }
        }else if(!x.isEmpty() && !y.isEmpty()){
            toast = Toast.makeText(getApplicationContext(), "Йоу персик, можно только одно поле в один момент",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            txt.setText(String.format( "%.2f", controlSum));
        }
    }

    private String imageSelecter(String name){
        return name.substring(0,1).toUpperCase();
    }
}
