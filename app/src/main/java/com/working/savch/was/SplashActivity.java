package com.working.savch.was;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.working.savch.was.fingerPrint.FingerprintActivity;
import com.working.savch.was.login.LoginActivity;
import com.working.savch.was.session.Session;

/**
 * Created by savch on 10.05.2017.
 */

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Session session;
    public static Activity spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(this);
        spl =this;
        if(session.loggedin() && session.fingerPrint()){
            Intent mainIntent = new Intent(SplashActivity.this, FingerprintActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            //SplashActivity.this.finish();
        }else if(session.loggedin() && !session.fingerPrint()) {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(loginIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}