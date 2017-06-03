package com.working.savch.was.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 5/5/2016.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */
public class Session {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Session(Context ctx) {
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public String getName() {
        return prefs.getString("name", null);
    }

    public void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String getEmail() {
        return prefs.getString("email", null);
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getPhoto() {
        return prefs.getString("photo", null);
    }

    public void setPhoto(String personPhoto) {
        editor.putString("photo", personPhoto);
        editor.commit();
    }

    public void setLoggedin(boolean logggedin) {
        editor.putBoolean("loggedInmode", logggedin);
        editor.commit();
    }

    public boolean loggedin() {
        return prefs.getBoolean("loggedInmode", false);
    }

    public void setHasPhoto(boolean hasPhoto) {
        editor.putBoolean("hasPhoto", hasPhoto);
        editor.commit();
    }

    public void setFingerPrint(boolean fingerprint) {
        editor.putBoolean("fingerPrintMode", fingerprint);
        editor.commit();
    }

    public boolean fingerPrint() {
        return prefs.getBoolean("fingerPrintMode", false);
    }

    public boolean hasPhoto() {
        return prefs.getBoolean("hasPhoto", false);
    }
}