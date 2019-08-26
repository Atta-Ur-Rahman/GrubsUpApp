package com.techease.grubsup.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getFavouriteRecipe.GetFavouriteRecipeDataModel;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.RecipeDetailActivity;
import com.techease.grubsup.views.activities.SpecificRecipeShoppinListActivity;

import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FavouriteRecipeAdapter extends RecyclerView.Adapter<FavouriteRecipeAdapter.ViewHolder> {


    private Context context;
    private List<GetFavouriteRecipeDataModel> favouriteRecipeDataModels;
    Grubs_Up_CURD grubs_up_curd;

    public FavouriteRecipeAdapter(Context context, List<GetFavouriteRecipeDataModel> favouriteRecipeDataModel) {
        this.context = context;
        this.favouriteRecipeDataModels = favouriteRecipeDataModel;
        grubs_up_curd=new Grubs_Up_CURD(context);

    }


    @NonNull
    @Override
    public FavouriteRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_favourite_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecipeAdapter.ViewHolder viewHolder, int i) {


        final GetFavouriteRecipeDataModel favouriteRecipeDataModel = favouriteRecipeDataModels.get(i);
        viewHolder.tvTitleFavouriteRecipe.setText(favouriteRecipeDataModel.getTitle());
        Glide.with(context).load(favouriteRecipeDataModel.getPicture()).placeholder(R.drawable.progress_animation).into(viewHolder.ivFavourite);
        viewHolder.materialRatingBarFavouriteRecipe.setRating(favouriteRecipeDataModel.getAverageRating());



        if (grubs_up_curd.CheckRecipeIsShoppingCard(String.valueOf(favouriteRecipeDataModel.getId()))) {
            final int newColor = context.getResources().getColor(R.color.red);
            viewHolder.ivShoppingList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.ivShoppingList.setEnabled(false);
        } else {

            final int newColor = context.getResources().getColor(R.color.green_text_color);
            viewHolder.ivShoppingList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.ivShoppingList.setEnabled(true);

        }


        viewHolder.rlFavouriteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtills.putIntegerValueInEditor(context, "recipe_id", favouriteRecipeDataModel.getId());
                context.startActivity(new Intent(context, RecipeDetailActivity.class));
            }
        });


        viewHolder.ivShoppingList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                GeneralUtills.putIntegerValueInEditor(context, "recipe_id", favouriteRecipeDataModel.getId());
                Objects.requireNonNull(context).startActivity(new Intent(context, SpecificRecipeShoppinListActivity.class));


            }
        });




    }

    @Override
    public int getItemCount() {
        return favouriteRecipeDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleFavouriteRecipe;
        ImageView ivFavourite, ivShoppingList;
        MaterialRatingBar materialRatingBarFavouriteRecipe;
        RelativeLayout rlFavouriteRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitleFavouriteRecipe = itemView.findViewById(R.id.tv_favourite_name);
            ivFavourite = itemView.findViewById(R.id.iv_custom_favorite);
            materialRatingBarFavouriteRecipe = itemView.findViewById(R.id.ratingBarFavourite);
            rlFavouriteRecipe = itemView.findViewById(R.id.rl_favourite_recipe);
            ivShoppingList = itemView.findViewById(R.id.iv_add_to_card);


        }
    }

}