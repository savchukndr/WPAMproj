package com.working.savch.was;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.working.savch.was.fingerPrint.FingerprintActivity;
import com.working.savch.was.login.LoginActivity;
import com.working.savch.was.pin.PinCodeActivityRes;
import com.working.savch.was.session.Session;

/**
 * Created by savch on 10.05.2017.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */

public class SplashActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Activity spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session session = new Session(this);
        spl = this;
        if (session.loggedin() && session.fingerPrint()) {
            Intent mainIntent = new Intent(SplashActivity.this, FingerprintActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            //SplashActivity.this.finish();
        } else if (session.loggedin() && session.isPin()){
            Intent mainIntent = new Intent(SplashActivity.this, PinCodeActivityRes.class);
            SplashActivity.this.startActivity(mainIntent);
        } else if ((session.loggedin() && !session.isPin()) || (session.loggedin() && !session.fingerPrint())) {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }else {
            int SPLASH_DISPLAY_LENGTH = 2000;
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