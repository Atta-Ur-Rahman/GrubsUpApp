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
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.activities.RecipeDetailActivity;
import com.techease.grubsup.views.activities.SpecificRecipeShoppinListActivity;

import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRecipeAdapter extends RecyclerView.Adapter<AllRecipeAdapter.ViewHolder> {


    private Context context;
    private List<GetAllRecipeDataModel> getRecipiesDataModels;
    private Grubs_Up_CURD grubs_up_curd;
    private Dialog alertDialog;


    public AllRecipeAdapter(Context context, List<GetAllRecipeDataModel> getRecipiesDataModel,Dialog alertDialog) {
        this.context = context;
        this.getRecipiesDataModels = getRecipiesDataModel;

        grubs_up_curd = new Grubs_Up_CURD(context);
        this.alertDialog=alertDialog;


    }


    @NonNull
    @Override
    public AllRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_all_recipe_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllRecipeAdapter.ViewHolder viewHolder, final int i) {


        final GetAllRecipeDataModel allRecipiesModel = getRecipiesDataModels.get(i);

        Glide.with(context).load(allRecipiesModel.getPicture()).placeholder(R.drawable.progress_animation).into(viewHolder.ivAllRecipeImage);
        viewHolder.tvAllRecipeTitle.setText(allRecipiesModel.getTitle());



        if (grubs_up_curd.CheckRecipeIsShoppingCard(String.valueOf(allRecipiesModel.getId()))) {
            final int newColor = context.getResources().getColor(R.color.red);
            viewHolder.ivShoppingList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.ivShoppingList.setEnabled(false);
        } else {

            final int newColor = context.getResources().getColor(R.color.green);
            viewHolder.ivShoppingList.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            viewHolder.ivShoppingList.setEnabled(true);

        }


        viewHolder.ivShoppingList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                GeneralUtills.putIntegerValueInEditor(context, "recipe_id", allRecipiesModel.getId());
                Objects.requireNonNull(context).startActivity(new Intent(context, SpecificRecipeShoppinListActivity.class));

                Log.d("all_item_recipe_id", String.valueOf(allRecipiesModel.getId()));

            }
        });

        viewHolder.frameLayout
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GeneralUtills.putIntegerValueInEditor(context, "recipe_id", allRecipiesModel.getId());
                        context.startActivity(new Intent(context, RecipeDetailActivity.class));
                    }
                });


        viewHolder.ivDeletRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getRecipiesDataModels.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getRecipiesDataModels.size());
                SwipeApiCall(context,allRecipiesModel.getId(),"delete");

            }
        });


        viewHolder.ivSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecipiesDataModels.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getRecipiesDataModels.size());
                SwipeApiCall(context,allRecipiesModel.getId(),"swipe");
            }
        });

    }

    @Override
    public int getItemCount() {
        return getRecipiesDataModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAllRecipeTitle;
        ImageView ivAllRecipeImage, ivShoppingList, ivDeletRecipe, ivSwap;
        RelativeLayout rlAllRecipe;
        FrameLayout frameLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAllRecipeTitle = itemView.findViewById(R.id.tv_recipes_title);
            ivAllRecipeImage = itemView.findViewById(R.id.iv_all_recipies);


            rlAllRecipe = itemView.findViewById(R.id.rl_all_recipe);

            ivShoppingList = itemView.findViewById(R.id.iv_add_to_card);


            ivDeletRecipe = itemView.findViewById(R.id.iv_delete_recipe);
            ivSwap = itemView.findViewById(R.id.iv_swipe_recipe);

            frameLayout = itemView.findViewById(R.id.fl_recipe);


        }
    }

    public void removeItem(int position) {

        getRecipiesDataModels.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getRecipiesDataModels.size());


    }

    public void restoreItem(GetAllRecipeDataModel item, int position) {
        getRecipiesDataModels.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    private void SwipeApiCall(final Context context, final int recipeId, final String swipeType) {

        alertDialog.show();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(context)).create(APIServices.class);

        Call<SwipeRecipeResponseModel> swipeRecipeResponseModelCall = services.swipeOrDelete(recipeId, swipeType);

        swipeRecipeResponseModelCall.enqueue(new Callback<SwipeRecipeResponseModel>() {
            @Override
            public void onResponse(Call<SwipeRecipeResponseModel> call, Response<SwipeRecipeResponseModel> response) {

                grubs_up_curd.DeleteRecipeIngredients(String.valueOf(recipeId));

                if (swipeType.equals("swipe")) {
                    ((NavigationTabActivity) context).finishAffinity();
                    context.startActivity(new Intent(context, NavigationTabActivity.class));
                } else {
                    Toast.makeText(context, "recipe deleted successfully", Toast.LENGTH_SHORT).show();

                }
                alertDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SwipeRecipeResponseModel> call, Throwable t) {
                alertDialog.dismiss();

                if (swipeType.equals("swipe")) {
                    Toast.makeText(context, "swipe recipe successfully", Toast.LENGTH_SHORT).show();
                    ((NavigationTabActivity) context).finishAffinity();
                    context.startActivity(new Intent(context, NavigationTabActivity.class));
                }
            }
        });
    }

}