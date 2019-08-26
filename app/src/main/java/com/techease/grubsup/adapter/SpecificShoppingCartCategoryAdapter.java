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
import com.techease.grubsup.model.SpecificShoppingCartIngredientsModel;
import com.techease.grubsup.model.SpecificShoppingCartCategoryModel;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.List;

public class SpecificShoppingCartCategoryAdapter extends RecyclerView.Adapter<SpecificShoppingCartCategoryAdapter.ViewHolder> {


    private Context context;
    private Grubs_Up_CURD grubs_up_curd;
    private List<SpecificShoppingCartCategoryModel> specificCategoryModels;
    int layout;


    public SpecificShoppingCartCategoryAdapter(Context context, List<SpecificShoppingCartCategoryModel> specificCategoryModels, int layout) {
        this.context = context;
        this.specificCategoryModels = specificCategoryModels;
        this.layout = layout;
        grubs_up_curd = new Grubs_Up_CURD(context);


    }


    @NonNull
    @Override
    public SpecificShoppingCartCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificShoppingCartCategoryAdapter.ViewHolder viewHolder, int i) {

        SpecificShoppingCartCategoryModel categoryIngredient = specificCategoryModels.get(i);
        viewHolder.tvIngredientsCategory.setText(categoryIngredient.getName());

        viewHolder.rvSubCategoryIngredients.hasFixedSize();
        viewHolder.rvSubCategoryIngredients.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvSubCategoryIngredients.setItemAnimator(new DefaultItemAnimator());
        viewHolder.rvSubCategoryIngredients.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        List<SpecificShoppingCartIngredientsModel> specificShoppingCartIngredientsModels = grubs_up_curd.GetSpecificShoppingCartCategoryIngredients(context, String.valueOf(GeneralUtills.getSharedPreferences(context).getInt("recipe_id", 0)), categoryIngredient.getCategoryId());

        SpecificShoppingCartIngredientsAdapter specificIngredientsAdapter = new SpecificShoppingCartIngredientsAdapter(context, specificShoppingCartIngredientsModels, R.layout.custom_shopping_list_layout);
        viewHolder.rvSubCategoryIngredients.setAdapter(specificIngredientsAdapter);
        specificIngredientsAdapter.notifyDataSetChanged();

        if (specificShoppingCartIngredientsModels.size() == 0) {
            viewHolder.tvNoItemCategory.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return specificCategoryModels.size();
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