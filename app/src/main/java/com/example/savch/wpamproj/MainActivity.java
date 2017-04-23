package com.example.savch.wpamproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.savch.wpamproj.login.LoginActivity;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static private double controlSum = 0.0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActivityBackgroundColor(getResources().getColor(R.color.primary));

        TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
        textViewInfo.setText(String.format( "%.2f", controlSum));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
        //TextView textViewInfo1 = (TextView) findViewById(R.id.textViewBalance);
        switch (view.getId()){
            case R.id.countButton:
                String tmpAdd;
                String tmpDel;

                EditText addEditText = (EditText) findViewById(R.id.addEditText);
                EditText delEditText = (EditText) findViewById(R.id.delEditText);

                tmpAdd = addEditText.getText().toString();
                tmpDel = delEditText.getText().toString();

                strCheck(textViewInfo, tmpAdd, tmpDel);
                addEditText.setText("");
                delEditText.setText("");
                break;
            case R.id.nullButton:
                controlSum = 0.0d;
                textViewInfo.setText(String.format( "%.2f", controlSum));
                break;
        }
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
}
