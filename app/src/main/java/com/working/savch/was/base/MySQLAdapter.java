package com.working.savch.was.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by savch on 28.03.2017.
 */

public class MySQLAdapter {
    private static final String DBNAME  = "DB_r_11"; //DB_r_11
    private static final String TABLE   = "user";
    private static final String TABLE_TRANSACTION   = "trans";
    private static final String TABLE_CATEGORIES   = "categories";
    public static final int    VERSION = 2;

    SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    private static final String CREATE_TABLE =
            "CREATE TABLE user ("
                    + "id_user INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "name TEXT,"
                    + "email TEXT,"
                    + "password TEXT);";

    private static final String CREATE_TABLE_TRANS =
            "CREATE TABLE trans ("
                    + "id_trans INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "amount REAL,"
                    + "currentdate DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "about TEXT,"
                    + "track_user INTEGER,"
                    + "track_categories INTEGER,"
                    + "FOREIGN KEY(track_user) REFERENCES user(id_user),"
                    + "FOREIGN KEY(track_categories) REFERENCES categories(id_categories));";

    private static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE categories ("
                    + "id_categories INTEGER PRIMARY KEY NOT NULL,"
                    + "categories_name TEXT);";

    public MySQLAdapter(Context context){
        mContext = context;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public int deleteAll(String date) {
        return sqLiteDatabase.delete(TABLE_TRANSACTION, "currentdate='" + date + "'", null);
    }

    public long insert(String nameVal, String emailVal, String passwordVal) {
        ContentValues cv = new ContentValues();
        cv.put("name", nameVal);
        cv.put("email", emailVal);
        cv.put("password", passwordVal);
        return sqLiteDatabase.insert(TABLE, null, cv);
    }

    public long insertTransactionTable(double amountVal, String aboutVal, int userVal, int categoriesVal){
        ContentValues cv = new ContentValues();
        cv.put("amount", amountVal);
        cv.put("currentdate", getDateTime());
        cv.put("about", aboutVal);
        cv.put("track_user", userVal);
        cv.put("track_categories", categoriesVal);
        return sqLiteDatabase.insert(TABLE_TRANSACTION, null, cv);
    }

    public MySQLAdapter openToRead() throws SQLException {
        try {
            sqLiteHelper = new SQLiteHelper(mContext, DBNAME, null, VERSION);
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        } catch (Exception e){}
        return this;
    }

    public MySQLAdapter openToWrite() throws SQLException {
        try {
            sqLiteHelper = new SQLiteHelper(mContext, DBNAME, null, VERSION);
            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        } catch (Exception e){}
        return this;
    }

    public Cursor queueAll() {
        return sqLiteDatabase.rawQuery("SELECT * FROM user", null);
    }

    public Cursor queueUserId(String email){
        return sqLiteDatabase.rawQuery("SELECT id_user FROM user WHERE email='" + email + "';", null);
    }

    public Cursor queueTransaction(){
        return sqLiteDatabase.rawQuery("SELECT t.currentdate, t.amount, c.categories_name " +
                "FROM trans t, user u, categories c " +
                "WHERE t.track_user=u.id_user " +
                "AND t.track_categories=c.id_categories;", null);
    }

    public Cursor queueTransactionAbout(String currentDate){
        return sqLiteDatabase.rawQuery("SELECT t.about, t.track_categories FROM trans t WHERE t.currentdate='" + currentDate + "';", null);
    }

    public Cursor querySum(){
        return  sqLiteDatabase.rawQuery("SELECT sum(amount) AS totalSum FROM trans, user WHERE trans.track_user=user.id_user;", null);
    }

    private class SQLiteHelper extends SQLiteOpenHelper {
        SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_CATEGORIES);

            ContentValues cv = new ContentValues();
            cv = new ContentValues();
            cv.put("id_categories", 0);
            cv.put("categories_name", "Shopping, services");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv = new ContentValues();
            cv.put("id_categories", 1);
            cv.put("categories_name", "Entertainment");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv = new ContentValues();
            cv.put("id_categories", 2);
            cv.put("categories_name", "Food, dining");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv = new ContentValues();
            cv.put("id_categories", 3);
            cv.put("categories_name", "Vacation, travel");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv = new ContentValues();
            cv.put("id_categories", 4);
            cv.put("categories_name", "Bills");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv = new ContentValues();
            cv.put("id_categories", 5);
            cv.put("categories_name", "Consumer loans, fees, taxes");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.put("id_categories", 6);
            cv.put("categories_name", "Income");
            db.insert(TABLE_CATEGORIES, null, cv);
            db.execSQL(CREATE_TABLE_TRANS);

        }

        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
            if(oldversion < 2){
                db.execSQL("DROP TABLE IF EXISTS 'user'");
                db.execSQL("DROP TABLE IF EXISTS 'trans'");
            }
            onCreate(db);
        }



    }

    //Date format
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH : mm : ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}