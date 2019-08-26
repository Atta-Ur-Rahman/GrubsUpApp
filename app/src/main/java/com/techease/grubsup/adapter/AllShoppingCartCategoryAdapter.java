package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.AllRecipeCategoryModel;
import com.techease.grubsup.model.customAllIngredientsModel.CustomIngredientsModel;

import java.util.List;

public class AllShoppingCartCategoryAdapter extends RecyclerView.Adapter<AllShoppingCartCategoryAdapter.ViewHolder> {


    private Context context;
    private List<AllRecipeCategoryModel> categoryIngredients;
    private Grubs_Up_CURD grubs_up_curd;
    int layout;


    public AllShoppingCartCategoryAdapter(Context context, List<AllRecipeCategoryModel> categoryIngredients, int layout) {
        this.context = context;
        this.categoryIngredients = categoryIngredients;
        this.layout = layout;
        grubs_up_curd = new Grubs_Up_CURD(context);


    }


    @NonNull
    @Override
    public AllShoppingCartCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllShoppingCartCategoryAdapter.ViewHolder viewHolder, int i) {

        AllRecipeCategoryModel categoryIngredient = categoryIngredients.get(i);
        viewHolder.tvIngredientsCategory.setText(categoryIngredient.getName());

        viewHolder.rvSubCategoryIngredients.hasFixedSize();
        viewHolder.rvSubCategoryIngredients.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvSubCategoryIngredients.setItemAnimator(new DefaultItemAnimator());
        viewHolder.rvSubCategoryIngredients.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        List<CustomIngredientsModel> customIngredientsModels = grubs_up_curd.GetAllShoppingCartSpecificIngredients(context, categoryIngredient.getCategoryId());
        AllShoppingCartIngredientsAdapter allShoppingCartIngredientsAdapter = new AllShoppingCartIngredientsAdapter(context, customIngredientsModels, R.layout.custom_shopping_list_layout, false);
        viewHolder.rvSubCategoryIngredients.setAdapter(allShoppingCartIngredientsAdapter);

        if (customIngredientsModels.size() == 0) {
            viewHolder.tvNoItemCategory.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return categoryIngredients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIngredientsCategory, tvNoItemCategory;
        RecyclerView rvSubCategoryIngredients;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientsCategory = itemView.findViewById(R.id.tv_category_name);

            rvSubCategoryIngredients = itemView.findViewById(R.id.rv_sub_category_ingredients);

            tvNoItemCategory = itemView.findViewById(R.id.tv_no_item_in_this_category);


        }
    }

}