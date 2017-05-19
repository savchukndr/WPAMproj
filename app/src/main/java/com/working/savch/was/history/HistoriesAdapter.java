package com.working.savch.was.history;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.working.savch.was.R;

import java.util.List;

public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.MyViewHolder> {
    private List<History> historiesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, date, id;


        public MyViewHolder(View view) {
            super(view);
            amount = (TextView) view.findViewById(R.id.amountTrans);
            date = (TextView) view.findViewById(R.id.dateTrans);
            id = (TextView) view.findViewById(R.id.idTrans);
        }
    }


    public HistoriesAdapter(List<History> historiesList) {
        this.historiesList = historiesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.colmn_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        History history = historiesList.get(position);
        holder.amount.setText(history.getAmount());
        holder.date.setText(history.getDate());
        holder.id.setText(history.getId());
        if (Double.parseDouble(history.getId()) > 0) {
            holder.id.setTextColor(Color.GREEN);
        }else if (Double.parseDouble(history.getId()) < 0){
            holder.id.setTextColor(Color.RED);
        }else{
            holder.id.setTextColor(Color.YELLOW);
        }

    }

    @Override
    public int getItemCount() {
        return historiesList.size();
    }

    public void removeItem(int position) {
        historiesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, historiesList.size());
    }
}