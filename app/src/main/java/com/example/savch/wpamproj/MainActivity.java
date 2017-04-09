package com.example.savch.wpamproj;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.view.Gravity;

import com.facebook.login.LoginManager;


public class MainActivity extends AppCompatActivity {
    static private double controlSum = 0.0d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActivityBackgroundColor(getResources().getColor(R.color.primary));

        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
        textViewInfo.setText(String.format( "%.2f", controlSum));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        if (LoginActivity.isLoggedIn()){
            //if user logged in with Facebook, then after onBeckPressed - log out will be
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
