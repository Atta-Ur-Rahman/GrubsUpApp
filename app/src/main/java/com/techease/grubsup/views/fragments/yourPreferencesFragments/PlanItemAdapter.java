package com.techease.grubsup.views.fragments.yourPreferencesFragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;

import java.util.List;

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemAdapter.MyViewHolder> implements ListAdapter {

    private final LayoutInflater inflater;



    List<PlanDataModel> productList;// = new ArrayList<>();

    Context context;

    RecyclerView recyclerView;

    public PlanItemAdapter(Context context, List<PlanDataModel> products, RecyclerView recyclerView) {

        inflater = LayoutInflater.from(context);
        this.productList = products;

        this.recyclerView = recyclerView;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_dinner_plan_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        PlanDataModel data = productList.get(position);
        Glide.with(context).load(data.getImg()).into(holder.imageView);
        holder.tvPlanTitle.setText(data.getPlanTitle());
        holder.tvPlanText.setText(data.getPlanText());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        holder.imageView.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView tvPlanTitle,tvPlanText;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_plan_image);
            tvPlanTitle =itemView.findViewById(R.id.tv_plan_title);
            tvPlanText=itemView.findViewById(R.id.tv_plan_text);


        }

        @Override
        public void onClick(View v) {
            Log.e("RecyclerView", "CLICK!");
            // get position
            int pos = getAdapterPosition();

            // check if item still exists
            if (pos != RecyclerView.NO_POSITION) {


            }
        }
    }



}