package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryDataModel;

import java.util.List;

public class CookModeIngredientsAdapter extends RecyclerView.Adapter<CookModeIngredientsAdapter.ViewHolder> {


    private Context context;
    int ingrediemtsLayout;
    protected List<GetSpecificIngredientsWithOutCategoryDataModel> ingredients;

    public CookModeIngredientsAdapter(Context context, List<GetSpecificIngredientsWithOutCategoryDataModel> ingredients, int ingrediemtsLayout) {
        this.context = context;
        this.ingredients = ingredients;
        this.ingrediemtsLayout = ingrediemtsLayout;

    }


    @NonNull
    @Override
    public CookModeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(ingrediemtsLayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookModeIngredientsAdapter.ViewHolder viewHolder, int i) {

        GetSpecificIngredientsWithOutCategoryDataModel ingredient = ingredients.get(i);
        viewHolder.tvPreviousPirce.setText(ingredient.getItem());


        viewHolder.tvIngredientsNumber.setText(String.valueOf(i + 1));


        viewHolder.rvRecipeCategoryIngredients.hasFixedSize();
        viewHolder.rvRecipeCategoryIngredients.setLayoutManager(new LinearLayoutManager(context));
//        categoryIngredientAdapter = new AllShoppingCartCategoryAdapter(context, ingredient.getCategoryIngredients(), R.layout.custom_intgredients_sub_category_layout);
//        viewHolder.rvRecipeCategoryIngredients.setAdapter(categoryIngredientAdapter);


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPreviousPirce, tvIngredientsNumber;

        RecyclerView rvRecipeCategoryIngredients;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPreviousPirce = itemView.findViewById(R.id.tv_item);
            rvRecipeCategoryIngredients = itemView.findViewById(R.id.rv_recipe_category_ingredients);
            tvIngredientsNumber = itemView.findViewById(R.id.tv_ingredient_item_number);


        }
    }

}