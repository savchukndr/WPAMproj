package com.working.savch.was.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 5/5/2016.
 */
public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    String email;
    String name;
    String personPhoto;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setPhoto(String personPhoto){
        this.personPhoto = personPhoto;
        editor.putString("photo", this.personPhoto);
        editor.commit();
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

    public String getPhoto(){
        return prefs.getString("photo", null);
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

    public void setHasPhoto(boolean hasPhoto){
        editor.putBoolean("hasPhoto",hasPhoto);
        editor.commit();
    }

    public void setFingerPrint(boolean fingerprint){
        editor.putBoolean("fingerPrintMode",fingerprint);
        editor.commit();
    }

    public boolean fingerPrint(){
        return prefs.getBoolean("fingerPrintMode", false);
    }

    public boolean hasPhoto(){
        return prefs.getBoolean("hasPhoto", false);
    }
}