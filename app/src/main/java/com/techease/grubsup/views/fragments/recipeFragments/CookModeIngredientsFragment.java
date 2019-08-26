package com.techease.grubsup.views.fragments.recipeFragments;


import android.app.Dialog;
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
import com.techease.grubsup.model.getDetailRecipeModel.RecipeDetailResponseModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryDataModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 */
public class CookModeIngredientsFragment extends Fragment {

View parentView;
    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView rvRecipeItem;

    CookModeIngredientsAdapter detailRecipeItemAdapter;


    List<GetSpecificIngredientsWithOutCategoryDataModel> ingredientArrayList = new ArrayList<>();


    Dialog alertDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView=inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this,parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());


        rvRecipeItem.hasFixedSize();
        rvRecipeItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailRecipeItemAdapter = new CookModeIngredientsAdapter(getActivity(), ingredientArrayList,R.layout.custom_large_ingredients_layout);
        rvRecipeItem.setAdapter(detailRecipeItemAdapter);


        getSpecificIngredientsWithOutCategory();


        return parentView;
    }



    private void getReciepes() {

        final APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<RecipeDetailResponseModel> getRecipiesResponseModelCall = services.recipeDetail(GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<RecipeDetailResponseModel>() {
            @Override
            public void onResponse(Call<RecipeDetailResponseModel> call, final Response<RecipeDetailResponseModel> response) {

                if (response.isSuccessful()) {



//                    ingredientArrayList.addAll(response.body().getData().getIngredients());

                    detailRecipeItemAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponseModel> call, Throwable t) {


            }
        });
    }




    private void getSpecificIngredientsWithOutCategory() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> getRecipiesResponseModelCall = services.specificRecipeIngredientsWithOutCategory(GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<GetSpecificIngredientsWithOutCategoryResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, final Response<GetSpecificIngredientsWithOutCategoryResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    alertDialog.dismiss();

                    ingredientArrayList.addAll(response.body().getData());
                    detailRecipeItemAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }


}
