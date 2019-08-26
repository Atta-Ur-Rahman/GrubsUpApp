package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.model.getOrderModel.GetOrderDataModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<GetOrderDataModel> getOrderDataModels = new ArrayList<>();
    private Context context;


    public MyOrderAdapter(Context context, List<GetOrderDataModel> getOrderDataModels) {
        this.getOrderDataModels = getOrderDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GetOrderDataModel getOrderDataModel = getOrderDataModels.get(position);
        holder.tvMyOrderNo.setText(String.valueOf(getOrderDataModel.getId()));
        holder.tvDeliveryTime.setText(getOrderDataModel.getCreatedAt());
        holder.tvDeliveryDate.setText(getOrderDataModel.getUpdatedAt());
        holder.tvDeliveryAddress.setText(getOrderDataModel.getShippingAddress());

    }

    @Override
    public int getItemCount() {
        return getOrderDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMyOrderNo, tvDeliveryDate, tvDeliveryTime, tvDeliveryAddress;


        public ViewHolder(View itemView) {
            super(itemView);


            tvMyOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvDeliveryDate = itemView.findViewById(R.id.tv_delivery_date);
            tvDeliveryTime = itemView.findViewById(R.id.tv_delivery_time);
            tvDeliveryAddress = itemView.findViewById(R.id.tv_delivery_address);
        }
    }
}
