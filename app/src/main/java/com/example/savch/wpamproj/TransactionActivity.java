package com.example.savch.wpamproj;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.savch.wpamproj.base.MySQLAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.savch.wpamproj.Constants.FIRST_COLUMN;
import static com.example.savch.wpamproj.Constants.SECOND_COLUMN;
import static com.example.savch.wpamproj.Constants.THIRD_COLUMN;

public class TransactionActivity extends AppCompatActivity{
    MySQLAdapter dbHelper;
    private List<History> historyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoriesAdapter hAdapter;
    private View view;
    private Paint p = new Paint();


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

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            hAdapter = new HistoriesAdapter(historyList);
            RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(hLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            int count = 1;
            do
            {
                //String columnID = cursor.getString(cursor.getColumnIndex("id"));
                String columnDate = cursor.getString(cursor.getColumnIndex("currentdate"));
                String columnAmount = cursor.getString(cursor.getColumnIndex("amount"));

                /*
                if (Integer.parseInt(columnAmount) > 0) {

                }else if (Integer.parseInt(columnAmount) < 0){

                }else{

                }*/
                prepareMovieData(String.valueOf(count), columnDate, columnAmount);

                count++;
            }while(cursor.moveToNext()); // Moves to the next row
            hAdapter.notifyDataSetChanged();
            initSwipe();
            recyclerView.setAdapter(hAdapter);
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

    private void prepareMovieData(String id, String date,String amount) {
        History movie = new History(amount, id, date);
        historyList.add(movie);
    }


    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    hAdapter.removeItem(position);
                    //TODO: SQL query
                } else {
                    //TODO
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


    /*private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }*/
}
