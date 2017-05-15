package com.working.savch.walletas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

/**
 * Created by Administrator on 5/5/2016.
 */
public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    String email;
    String name;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setEmail(String email){
        this.email = email;
        editor.putString("email", this.email);
        editor.commit();
    }

    public void setName(String name){
        this.name = name;
        editor.putString("name", this.name);
        editor.commit();
    }

    public String getName(){
        return prefs.getString("name", null);
    }

    public String getEmail(){
        return prefs.getString("email", null);
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}