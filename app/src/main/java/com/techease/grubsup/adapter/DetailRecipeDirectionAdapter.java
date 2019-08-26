package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.model.getDetailRecipeModel.Direction;

import java.util.List;

public class DetailRecipeDirectionAdapter extends RecyclerView.Adapter<DetailRecipeDirectionAdapter.ViewHolder> {


    private Context context;
    List<Direction> directions;


    public DetailRecipeDirectionAdapter(FragmentActivity activity, List<Direction> directionsArrayList) {
        this.context = context;
        this.directions = directionsArrayList;
    }


    @NonNull
    @Override
    public DetailRecipeDirectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_ingredients_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRecipeDirectionAdapter.ViewHolder viewHolder, int i) {

        Direction direction = directions.get(i);
        viewHolder.tvPreviousPirce.setText(direction.getName());


    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPreviousPirce;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPreviousPirce = itemView.findViewById(R.id.tv_item);


        }
    }

}