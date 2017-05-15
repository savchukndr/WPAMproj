package com.working.savch.walletas.base;

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
    //final String LOG_TAG = "myLogs";
    private static final String DBNAME  = "DB_r_8";
    private static final String TABLE   = "user";
    private static final String TABLE_TRANSACTION   = "trans";
    public static final int    VERSION = 1;

    private static final String KEY_ID = "_id";
    // Add other fields here

    SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    //this is how I write the create db script, you may do it you own way;)

    private static final String CREATE_TABLE =
            "CREATE TABLE user ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "name TEXT,"
                    + "email TEXT,"
                    + "password TEXT);";

    private static final String CREATE_TABLE_TRANS =
            "CREATE TABLE trans ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "name TEXT,"
                    + "email TEXT,"
                    + "amount REAL,"
                    + "currentdate DATETIME DEFAULT CURRENT_TIMESTAMP);";


    public MySQLAdapter(Context context){
        mContext = context;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public int deleteAll(String date) {
        return sqLiteDatabase.delete(TABLE_TRANSACTION, "currentdate='" + date + "'", null);
    }

    public long insert(String name, String email, String password,
                       String nameVal, String emailVal, String passwordVal) {
        ContentValues cv = new ContentValues();
        cv.put(name, nameVal);
        cv.put(email, emailVal);
        cv.put(password, passwordVal);
        return sqLiteDatabase.insert(TABLE, null, cv);
    }

    public long insertTransactionTable(String name, String email, String amount, String currentdate,
                                       String nameVal, String emailVal, double amountVal){
        ContentValues cv = new ContentValues();
        cv.put(name, nameVal);
        cv.put(email, emailVal);
        cv.put(amount, amountVal);
        cv.put(currentdate, getDateTime());
        return sqLiteDatabase.insert(TABLE_TRANSACTION, null, cv);
    }

    public MySQLAdapter openToRead() throws SQLException {
        try {
            sqLiteHelper = new SQLiteHelper(mContext, DBNAME, null, 1);
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        } catch (Exception e){}
        return this;
    }

    public MySQLAdapter openToWrite() throws SQLException {
        try {
            sqLiteHelper = new SQLiteHelper(mContext, DBNAME, null, 1);
            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        } catch (Exception e){}
        return this;
    }

    public Cursor queueAll() {
        return sqLiteDatabase.rawQuery("SELECT * FROM user", null);
    }

    public Cursor queueTransaction(String email){
        return sqLiteDatabase.rawQuery("SELECT id, currentdate, amount FROM trans WHERE email = '" + email + "';", null);
    }

    public Cursor deleteTransaction(String date){
        return sqLiteDatabase.rawQuery("DELETE FROM trans WHERE currentdate = '" + date + "';", null);
    }

    public Cursor querySum(String email){
        return  sqLiteDatabase.rawQuery("SELECT sum(amount) AS totalSum FROM trans WHERE email = '" + email + "';", null);
    }


    public Cursor queueDay(String query) {
        String[] KEYS = { KEY_ID /* and all other KEYS*/ };
        return sqLiteDatabase.query(TABLE, KEYS, query, null, null, null, null);
    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            //Log.d(LOG_TAG, "--- onDrop database ---");
            //db.execSQL("DROP TABLE user");
            //db.execSQL("DROP TABLE trans");
            //Log.d(LOG_TAG, "--- onCreate database ---");
            //Log.d(LOG_TAG, "--- Create user ---");
            db.execSQL(CREATE_TABLE);
            //db.execSQL("delete from sqlite_sequence where name='trans';");
            //Log.d(LOG_TAG, "--- Create transaction ---");
            db.execSQL(CREATE_TABLE_TRANS);

        }

        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {}



    }

    //Date format
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}