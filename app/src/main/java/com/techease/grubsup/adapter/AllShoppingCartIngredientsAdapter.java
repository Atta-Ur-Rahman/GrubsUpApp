package com.techease.grubsup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.customAllIngredientsModel.CustomIngredientsModel;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.List;

public class AllShoppingCartIngredientsAdapter extends RecyclerView.Adapter<AllShoppingCartIngredientsAdapter.ViewHolder> {


    private Context context;
    private List<CustomIngredientsModel> specificRecipeDataModels;
    int layout;
    Grubs_Up_CURD grubs_up_curd;
    boolean aBooleanFinalIngredients;

    int shoppingQty = 1;


    public AllShoppingCartIngredientsAdapter(Context context, List<CustomIngredientsModel> specificRecipeDataModels, int layout, boolean aBooleanFinalIngredients) {
        this.context = context;
        this.specificRecipeDataModels = specificRecipeDataModels;
        this.layout = layout;
        this.aBooleanFinalIngredients = aBooleanFinalIngredients;

        grubs_up_curd = new Grubs_Up_CURD(context);

    }


    @NonNull
    @Override
    public AllShoppingCartIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AllShoppingCartIngredientsAdapter.ViewHolder viewHolder, final int i) {


        final CustomIngredientsModel specificRecipeDataModel = specificRecipeDataModels.get(i);

        viewHolder.tvItemName.setText(specificRecipeDataModel.getItemName());
        viewHolder.tvItemPrice.setText(GeneralUtills.FormatterPrice(Double.parseDouble(specificRecipeDataModel.getTotalUnitPrice())));
        viewHolder.tvShoppingQty.setText(specificRecipeDataModel.getQuantity());

        Glide.with(context).load(specificRecipeDataModel.getThumbnail()).placeholder(R.drawable.progress_animation).into(viewHolder.ivThumbnail);


        viewHolder.ivIngredientIncrement.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                shoppingQty = Integer.parseInt(viewHolder.tvShoppingQty.getText().toString());
                shoppingQty = shoppingQty + 1;
                viewHolder.tvShoppingQty.setText(String.valueOf(shoppingQty));


                double unitPrice, quantityPrice, totalUnitPrice;
                unitPrice = Double.parseDouble(specificRecipeDataModel.getUnitPrice());
                quantityPrice = Double.parseDouble(viewHolder.tvShoppingQty.getText().toString());
                totalUnitPrice = unitPrice * quantityPrice;
                grubs_up_curd.UpdateAllShoppingCartQuantityAndUnitTotalPrice(specificRecipeDataModel.getUniqueDataBaseId(), String.valueOf(shoppingQty), String.valueOf(totalUnitPrice));

                grubs_up_curd.TotalAllShoppingCartUnitPrice();

                viewHolder.tvItemPrice.setText(GeneralUtills.FormatterPrice(totalUnitPrice));


            }
        });

        viewHolder.ivIngredientDecrement.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                shoppingQty = Integer.parseInt(viewHolder.tvShoppingQty.getText().toString());
                if (shoppingQty > 1) {
                    shoppingQty = shoppingQty - 1;
                    viewHolder.tvShoppingQty.setText(String.valueOf(shoppingQty));


                    double unitPrice, quantityPrice, totalUnitPrice;
                    unitPrice = Double.parseDouble(specificRecipeDataModel.getUnitPrice());
                    quantityPrice = Double.parseDouble(viewHolder.tvShoppingQty.getText().toString());
                    totalUnitPrice = unitPrice * quantityPrice;
                    grubs_up_curd.UpdateAllShoppingCartQuantityAndUnitTotalPrice(specificRecipeDataModel.getUniqueDataBaseId(), String.valueOf(shoppingQty), String.valueOf(totalUnitPrice));

                    grubs_up_curd.TotalAllShoppingCartUnitPrice();
                    viewHolder.tvItemPrice.setText(GeneralUtills.FormatterPrice(totalUnitPrice));


                }
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                grubs_up_curd.DeleteIngredient(specificRecipeDataModel.getUniqueDataBaseId());
                specificRecipeDataModels.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, specificRecipeDataModels.size());

                grubs_up_curd.TotalAllShoppingCartUnitPrice();

            }
        });

    }

    @Override
    public int getItemCount() {
        return specificRecipeDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvItemPrice, tvShoppingQty, tvCategoryName;
        ImageView ivThumbnail, ivIngredientIncrement, ivIngredientDecrement, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_shopping_ingredients_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_ingredient_item_price);
            ivThumbnail = itemView.findViewById(R.id.iv_sub_category_ingredients);


            tvShoppingQty = itemView.findViewById(R.id.tv_shopping_qty);
            ivIngredientIncrement = itemView.findViewById(R.id.iv_plus);
            ivIngredientDecrement = itemView.findViewById(R.id.iv_minus);

            ivDelete = itemView.findViewById(R.id.iv_delete);


        }
    }


}