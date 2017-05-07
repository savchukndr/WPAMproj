package com.example.savch.wpamproj;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.savch.wpamproj.base.MySQLAdapter;

public class TransactionActivity extends AppCompatActivity {
    MySQLAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new MySQLAdapter(this);
        dbHelper.openToWrite();

        Bundle extras = getIntent().getExtras();
        String email = "";
        if(extras !=null) {
            email = extras.getString("userEmail");
        }

        // Setting up the cursor which points to the desired table
        Cursor cursor = dbHelper.queueTransaction(email);

        // Checking if the table has values other than the header using the cursor
        if(cursor != null && cursor.getCount() > 0)
        {
            // Moving the cursor to the first row in the table
            cursor.moveToFirst();
            TableLayout ll = (TableLayout) findViewById(R.id.main_table);

            TableRow row0 = new TableRow(this);
            row0.setBackgroundColor(Color.rgb(224, 42, 60));
            TextView tv01 = new TextView(this);
            tv01.setText("ID");
            tv01.setTextSize(30);
            tv01.setTextColor(Color.WHITE);
            TextView tv02 = new TextView(this);
            tv02.setText("Transfer date");
            tv02.setTextSize(30);
            tv02.setTextColor(Color.WHITE);
            TextView tv03 = new TextView(this);
            tv03.setText("Amount");
            tv03.setTextSize(30);
            tv03.setTextColor(Color.WHITE);

            row0.addView(tv01);
            row0.addView(tv02);
            row0.addView(tv03);
            ll.addView(row0);

            int count = 1;
            do
            {
                //String columnID = cursor.getString(cursor.getColumnIndex("id"));
                String columnDate = cursor.getString(cursor.getColumnIndex("currentdate"));
                String columnAmount = cursor.getString(cursor.getColumnIndex("amount"));

                TableRow row = new TableRow(this);
                TextView tv1 = new TextView(this);
                tv1.setText(String.valueOf(count));
                tv1.setTextSize(25);
                TextView tv2 = new TextView(this);
                tv2.setText(columnDate);
                tv2.setTextSize(25);
                TextView tv3 = new TextView(this);
                tv3.setText(columnAmount);
                tv3.setTextSize(25);

                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                ll.addView(row);
                count++;
            }while(cursor.moveToNext()); // Moves to the next row
        }

        // Closing the cursor
        assert cursor != null;
        cursor.close();
        //dbHelper.deleteAll();
        // Closing the database
        dbHelper.close();

        //Floating button "fabTrans"
        /*FloatingActionButton fabTrans = (FloatingActionButton) findViewById(R.id.fabTrans);
        fabTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Works!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        //Start activitywithout entering onCreate()
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
