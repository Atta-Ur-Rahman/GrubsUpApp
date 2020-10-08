package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.SpecificShoppingCartCategoryAdapter;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.SpecificShoppingCartCategoryModel;
import com.techease.grubsup.model.getSpecificRecipeIngredients.GetSpecificRecipeIngredientsResponseModel;
import com.techease.grubsup.model.getSpecificRecipeIngredients.SpecificShoppingCartIngredientsDataModel;
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
public class SpecificRecipeShoppingCartFragment extends Fragment implements View.OnClickListener {


    View parentView;

    List<SpecificShoppingCartCategoryModel> specificShoppingCartCategoryModels = new ArrayList<>();

    SpecificShoppingCartCategoryAdapter specificShoppingCartCategoryAdapter;
    List<SpecificShoppingCartIngredientsDataModel> specificShoppingCartIngredientsDataModels = new ArrayList<>();

    Grubs_Up_CURD grubs_up_curd;

    @BindView(R.id.rv_specific_category)
    RecyclerView rvSpecificCategory;
    @BindView(R.id.tv_total_ingredients_price)
    TextView tvTotalIngredients;
    @BindView(R.id.btn_go_shopping)
    Button btnGoShopping;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_add_item)
    TextView tvAddItem;

    Dialog alertDialog;

    public static TextView tvTotalIngredientsPrice;
    int recipeId;
    String strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, strTableName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_specific_recipe_shopping_list, container, false);


        ButterKnife.bind(this, parentView);
        grubs_up_curd = new Grubs_Up_CURD(getActivity());
        alertDialog = AlertUtils.createProgressDialog(getActivity());

        GeneralUtills.putBooleanValueInEditor(getActivity(), "allRecipeShopping", false);


        tvTotalIngredientsPrice = tvTotalIngredients;
        rvSpecificCategory.hasFixedSize();
        rvSpecificCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnGoShopping.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvAddItem.setOnClickListener(this);
        recipeId = GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0);


        apiCallSpecificIngredients(recipeId);


        specificShoppingCartCategoryModels = grubs_up_curd.SpecificRecipeCategory(String.valueOf(recipeId));
        specificShoppingCartCategoryAdapter = new SpecificShoppingCartCategoryAdapter(getActivity(), specificShoppingCartCategoryModels, R.layout.custom_category_layout);
        rvSpecificCategory.setAdapter(specificShoppingCartCategoryAdapter);
        specificShoppingCartCategoryAdapter.notifyDataSetChanged();



        return parentView;
    }


    private void apiCallSpecificIngredients(final int recipeId) {


        alertDialog.show();
        specificShoppingCartIngredientsDataModels.clear();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSpecificRecipeIngredientsResponseModel> getRecipiesResponseModelCall = services.specificRecipeIngredients(recipeId);
        getRecipiesResponseModelCall.enqueue(new Callback<GetSpecificRecipeIngredientsResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSpecificRecipeIngredientsResponseModel> call, final Response<GetSpecificRecipeIngredientsResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    alertDialog.dismiss();


                    specificShoppingCartIngredientsDataModels.addAll(response.body().getData());

                    String strCategoryId, strCategoryName;

                    for (int i = 0; i < specificShoppingCartIngredientsDataModels.size(); i++) {


                        strCategoryId = String.valueOf(specificShoppingCartIngredientsDataModels.get(i).getId());
                        strCategoryName = specificShoppingCartIngredientsDataModels.get(i).getName();

                        grubs_up_curd.InsertSpecificRecipeCategory(String.valueOf(recipeId), strCategoryId, strCategoryName);

                        for (int j = 0; j < response.body().getData().get(i).getCategoryIngredients().size(); j++) {

                            strCategoryId = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getCategoryId();
                            strItemId = String.valueOf(specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getId());
                            strItemName = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getItem();
                            strDiscount = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getDiscount();
                            strQuantity = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getQuantity();
                            strUnitPrice = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getUnitPrice();
                            strThumbnail = specificShoppingCartIngredientsDataModels.get(i).getCategoryIngredients().get(j).getThumbnail();

                            grubs_up_curd.insertSpecificRecipeDataInCard(String.valueOf(recipeId), strCategoryId, strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail);

                        }

                    }

                    specificShoppingCartCategoryModels = grubs_up_curd.SpecificRecipeCategory(String.valueOf(recipeId));
                    specificShoppingCartCategoryAdapter = new SpecificShoppingCartCategoryAdapter(getActivity(), specificShoppingCartCategoryModels, R.layout.custom_category_layout);
                    rvSpecificCategory.setAdapter(specificShoppingCartCategoryAdapter);
                    specificShoppingCartCategoryAdapter.notifyDataSetChanged();

                    grubs_up_curd.TotalSpecificIShoppingCartngredientsUnitPrice(String.valueOf(recipeId));


                }
            }

            @Override
            public void onFailure(Call<GetSpecificRecipeIngredientsResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_shopping:

                GeneralUtills.putStringValueInEditor(getActivity(),"total_price",tvTotalIngredients.getText().toString());
                GeneralUtills.putStringValueInEditor(getActivity(), "button_zoom", "");
                GeneralUtills.connectFragment(getActivity(), new SupperMarketFragment());
                break;

            case R.id.iv_back:

                getActivity().finish();
                break;
            case R.id.tv_add_item:

                GeneralUtills.connectFragment(getActivity(), new AddItemsFragment());

                break;
        }
    }
}
