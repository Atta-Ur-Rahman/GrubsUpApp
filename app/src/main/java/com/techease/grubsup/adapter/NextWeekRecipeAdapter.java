package com.techease.grubsup.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeDataModel;
import com.techease.grubsup.model.siwpeRecipeModel.SwipeRecipeResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.activities.RecipeDetailActivity;
import com.techease.grubsup.views.activities.SpecificRecipeShoppinListActivity;
import com.techease.grubsup.views.fragments.yourPreferencesFragments.NextWeeKDataModel;

import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextWeekRecipeAdapter extends RecyclerView.Adapter<NextWeekRecipeAdapter.ViewHolder> {


    private Context context;
    private List<NextWeeKDataModel> getRecipiesDataModels;



    public NextWeekRecipeAdapter(Context context, List<NextWeeKDataModel> getRecipiesDataModel) {
        this.context = context;
        this.getRecipiesDataModels = getRecipiesDataModel;

    }


    @NonNull
    @Override
    public NextWeekRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_next_week_recipe_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NextWeekRecipeAdapter.ViewHolder viewHolder, final int i) {


        final NextWeeKDataModel allRecipiesModel = getRecipiesDataModels.get(i);

        Glide.with(context).load(allRecipiesModel.getImg()).placeholder(R.drawable.progress_animation).into(viewHolder.ivNextWeekRecipe);



    }

    @Override
    public int getItemCount() {
        return getRecipiesDataModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivNextWeekRecipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivNextWeekRecipe = itemView.findViewById(R.id.iv_next_week_recipe);

        }
    }


}