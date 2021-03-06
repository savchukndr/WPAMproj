package com.working.savch.was.history;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.working.savch.was.R;
import com.working.savch.was.base.MySQLAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.MyViewHolder> {
    private List<History> historiesList;
    private MySQLAdapter dbHelper;
    private Context context;
    private String aboutTrans = "";
    private int categoriesID;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, id, time;

        MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.amountTrans);
            date = (TextView) view.findViewById(R.id.dateTrans);
            amount = (TextView) view.findViewById(R.id.idTrans);
            time = (TextView) view.findViewById(R.id.timeTrans);
        }
    }

    HistoriesAdapter(List<History> historiesList, Context cont) {
        this.historiesList = historiesList;
        this.context = cont;
        dbHelper = new MySQLAdapter(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.colmn_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        History history = historiesList.get(position);
        String tmp = history.getAmount();
        String time = tmp.substring(0, 10);
        String yeMoDe = tmp.substring(11);
        holder.id.setText(time);
        if (history.getId().equals("0")){
            holder.amount.setText(history.getId());
        }else {
            holder.date.setText(history.getDate());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat format = new DecimalFormat("#,###.00", symbols);


            String zeroEdit = String.valueOf(delSpacesInString(String.format("%14s", format.format(Double.parseDouble(history.getId())))));
            if(zeroEdit.substring(0,1).equals(",")){
                zeroEdit = "0" + zeroEdit;
                holder.amount.setText(zeroEdit);
            }else {
                holder.amount.setText(String.valueOf(delSpacesInString(String.format("%14s", format.format(Double.parseDouble(history.getId()))))));
            }



            //holder.amount.setText(String.valueOf(delSpacesInString(String.format("%14s", format.format(Double.parseDouble(history.getId()))))));
        }
        holder.time.setText(yeMoDe);
        if (Double.parseDouble(history.getId()) > 0) {
            holder.amount.setTextColor(Color.parseColor("#C6FF00"));
        }else if (Double.parseDouble(history.getId()) < 0){
            holder.amount.setTextColor(Color.parseColor("#FF5722"));
        }else{
            holder.amount.setTextColor(Color.parseColor("#FFC400"));
        }
        final String currentDate = history.getAmount();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.openToWrite();
                Cursor cursor = dbHelper.queueTransactionAbout(currentDate);

                // Checking if the table has values other than the header using the cursor
                while(cursor.moveToNext()){
                    aboutTrans = cursor.getString(cursor.getColumnIndex("about"));
                    categoriesID = cursor.getInt(cursor.getColumnIndex("track_categories"));
                }
                if(categoriesID != 6) {
                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(v.getContext());
                    inputAlert.setTitle(R.string.about_trans_alert);
                    inputAlert.setMessage(aboutTrans);
                    inputAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = inputAlert.create();
                    alertDialog.show();

                    // Closing the cursor
                    cursor.close();
                    // Closing the database
                    dbHelper.close();
                }else{
                    Toast.makeText(context, R.string.earnings_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historiesList.size();
    }

    void removeItem(int position) {
        historiesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, historiesList.size());
    }

    private String delSpacesInString(String inputStr){
        return inputStr.replaceAll("^\\s+","");
    }
}