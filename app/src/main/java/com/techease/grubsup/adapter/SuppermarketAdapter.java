package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.techease.grubsup.R;

import java.util.ArrayList;

public class SuppermarketAdapter extends RecyclerView.Adapter<SuppermarketAdapter.ViewHolder> {


    private Context context;
    private ArrayList<String> arrayListIngredients;

    public SuppermarketAdapter(Context context, ArrayList<String> arrayListIngredients) {
        this.context = context;
        this.arrayListIngredients = arrayListIngredients;

    }


    @NonNull
    @Override
    public SuppermarketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_market_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuppermarketAdapter.ViewHolder viewHolder, int i) {


        viewHolder.tvSuppermarket.setText(arrayListIngredients.get(i));


    }

    @Override
    public int getItemCount() {
        return arrayListIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSuppermarket;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSuppermarket = itemView.findViewById(R.id.tv_item);


        }
    }

}