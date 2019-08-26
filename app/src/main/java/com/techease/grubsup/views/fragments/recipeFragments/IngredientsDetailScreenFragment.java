package com.techease.grubsup.views.fragments.recipeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.CookModeIngredientsAdapter;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryDataModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsDetailScreenFragment extends Fragment {


    View parentView;
    CookModeIngredientsAdapter detailRecipeItemAdapter;
    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView rvRecipeItem;
    List<GetSpecificIngredientsWithOutCategoryDataModel> ingredientArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_ingredients_detail_screen, container, false);

        ButterKnife.bind(this, parentView);

        rvRecipeItem.hasFixedSize();
        rvRecipeItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailRecipeItemAdapter = new CookModeIngredientsAdapter(getActivity(), ingredientArrayList, R.layout.custom_ingredients_layout);
        rvRecipeItem.setAdapter(detailRecipeItemAdapter);
        detailRecipeItemAdapter.notifyDataSetChanged();

        getSpecificIngredientsWithOutCategory();


        return parentView;
    }


    private void getSpecificIngredientsWithOutCategory() {


        ingredientArrayList.clear();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> getRecipiesResponseModelCall = services.specificRecipeIngredientsWithOutCategory(GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<GetSpecificIngredientsWithOutCategoryResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, final Response<GetSpecificIngredientsWithOutCategoryResponseModel> response) {

                if (response.isSuccessful()) {


                    ingredientArrayList.addAll(response.body().getData());

                    detailRecipeItemAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
