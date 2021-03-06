package com.working.savch.was.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.working.savch.was.MainActivity;
import com.working.savch.was.R;
import com.working.savch.was.base.MySQLAdapter;
import com.working.savch.was.history.DividerItemDecoration;
import com.working.savch.was.history.HistoriesAdapter;
import com.working.savch.was.history.History;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity{
    MySQLAdapter dbHelper;
    private List<History> historyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoriesAdapter hAdapter;
    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTrans);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                /*intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);*/
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });

        dbHelper = new MySQLAdapter(this);
        dbHelper.openToWrite();

        // Setting up the cursor which points to the desired table
        Cursor cursor = dbHelper.queueTransaction();



        // Checking if the table has values other than the header using the cursor
        if(cursor != null && cursor.getCount() > 0)
        {
            // Moving the cursor to the first row in the table
            cursor.moveToFirst();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            hAdapter = new HistoriesAdapter(historyList, getApplicationContext());
            RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(hLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            do
            {
                String columnDate = cursor.getString(cursor.getColumnIndex("t.currentdate"));
                String columnAmount = cursor.getString(cursor.getColumnIndex("t.amount"));
                String columnCategory = cursor.getString(cursor.getColumnIndex("c.categories_name"));
                switch (columnCategory){
                    case "Income":
                        columnCategory = getString(R.string.category_6);
                        break;
                    case "Shopping, services":
                        columnCategory = getString(R.string.category_0);
                        break;
                    case "Entertainment":
                        columnCategory = getString(R.string.category_1);
                        break;
                    case "Food, dining":
                        columnCategory = getString(R.string.category_2);
                        break;
                    case "Vacation, travel":
                        columnCategory = getString(R.string.category_3);
                        break;
                    case "Bills":
                        columnCategory = getString(R.string.category_4);
                        break;
                    case "Consumer loans, fees, taxes":
                        columnCategory = getString(R.string.category_5);
                        break;
                }
                prepareTransactioneData(columnCategory, columnDate, columnAmount);

            }while(cursor.moveToNext()); // Moves to the next row
            hAdapter.notifyDataSetChanged();
            initSwipe();
            recyclerView.setAdapter(hAdapter);
        }

        // Closing the cursor
        assert cursor != null;
        cursor.close();


        // Closing the database
        dbHelper.close();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        /*intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);*/
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }

    private void prepareTransactioneData(String id, String date, String amount) {
        History hist = new History(amount, id, date);
        historyList.add(hist);
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final AlertDialog.Builder delAlert = new AlertDialog.Builder(TransactionActivity.this);
                    delAlert.setTitle(R.string.delete_rec_alert_title);
                    delAlert.setMessage(R.string.delete_rec_alert_message);
                    //inputAlert.setView(userInput);
                    delAlert.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.openToWrite();

                            History history11 = historyList.get(position);
                            String date1 = history11.getAmount();
                            dbHelper.deleteAll(date1);
                            hAdapter.removeItem(position);
                        }
                    });
                    delAlert.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });

                    AlertDialog alertDialogDel = delAlert.create();
                    alertDialogDel.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
