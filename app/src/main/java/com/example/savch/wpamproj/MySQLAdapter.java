package com.example.savch.wpamproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by savch on 28.03.2017.
 */

public class MySQLAdapter {
    final String LOG_TAG = "myLogs";
    public static final String DBNAME  = "walletDB";
    public static final String TABLE   = "user";
    public static final int    VERSION = 1;

    public static final String KEY_ID = "_id";
    // Add other fields here

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    //this is how I write the create db script, you may do it you own way;)

    private static final String CREATE_TABLE =
            "create table user ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text,"
                    + "password text" + ");";


    public MySQLAdapter(Context context){
        mContext = context;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public int deleteAll() {
        return sqLiteDatabase.delete(TABLE, null, null);
    }

    public long insert(String name, String email, String password,
                       String nameVal, String emailVal, String passwordVal) {
        ContentValues cv = new ContentValues();
        cv.put(name, nameVal);
        cv.put(email, emailVal);
        cv.put(password, passwordVal);
        return sqLiteDatabase.insert(TABLE, null, cv);
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

    public Cursor queueDay(String query) {
        String[] KEYS = { KEY_ID /* and all other KEYS*/ };
        return sqLiteDatabase.query(TABLE, KEYS, query, null, null, null, null);
    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            //db.execSQL(CREATE_TABLE);
            db.execSQL("DROP TABLE user");
        }

        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {}
    }
}
