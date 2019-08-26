package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.AllShoppingCartCategoryAdapter;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.AllRecipeCategoryModel;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.NavigationTabActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecipeShoppingCartFragment extends Fragment implements View.OnClickListener {

    View parentView;
    Dialog alertDialog;

    List<AllRecipeCategoryModel> categoryModels = new ArrayList<>();
    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView rvIngredients;
    @BindView(R.id.btn_go_shopping)
    Button btnGoShopping;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_add_item)
    TextView tvAddItem;
    @BindView(R.id.tv_total_ingredients_price)
    TextView tvTotalIngredients;
    @BindView(R.id.tv_item_not_found)
    TextView tvItemNotFound;

    public static TextView tvTotalIngredientsPrice;
    public static JSONArray jsonArray;

    Grubs_Up_CURD grubs_up_curd;
    AllShoppingCartCategoryAdapter categoryAdapter;
    public static String strTotalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ButterKnife.bind(this, parentView);
        grubs_up_curd = new Grubs_Up_CURD(getActivity());

        btnGoShopping.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvAddItem.setOnClickListener(this);

        GeneralUtills.putBooleanValueInEditor(getActivity(), "togglePosition", false);

        GeneralUtills.putBooleanValueInEditor(getActivity(), "allRecipeShopping", true);


        categoryModels.clear();
        onBackPress(parentView);

        tvTotalIngredientsPrice = tvTotalIngredients;


        jsonArray = new JSONArray();

        alertDialog = AlertUtils.createProgressDialog(getActivity());

        rvIngredients.hasFixedSize();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvIngredients.setItemAnimator(new DefaultItemAnimator());
        rvIngredients.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        categoryModels = grubs_up_curd.GetAllRecipeCategory();
        categoryAdapter = new AllShoppingCartCategoryAdapter(getActivity(), categoryModels, R.layout.custom_category_layout);
        rvIngredients.setAdapter(categoryAdapter);

        tvTotalIngredients.setText(strTotalPrice);


        if (categoryModels.size() == 0) {
            tvItemNotFound.setVisibility(View.VISIBLE);
        }

        categoryAdapter.notifyDataSetChanged();

        grubs_up_curd.TotalAllShoppingCartUnitPrice();


        return parentView;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_go_shopping:

                GeneralUtills.putStringValueInEditor(getActivity(), "total_price", tvTotalIngredients.getText().toString());
                GeneralUtills.connectFragment(getActivity(), new SupperMarketFragment());

                break;

            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).finish();

                break;

            case R.id.tv_add_item:
                GeneralUtills.connectFragment(getActivity(), new AddItemsFragment());

                break;
        }
    }


    private void onBackPress(View parentView) {


        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //  Log.i(tag, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //   Log.i(tag, "onKey Back listener is working!!!");
                    assert getFragmentManager() != null;

                    GeneralUtills.putBooleanValueInEditor(getActivity(), "isFavouriteScreen", false);
                    getActivity().finish();
                    getActivity().startActivity(new Intent(getActivity(), NavigationTabActivity.class));

                    return true;
                }
                return false;
            }
        });

    }
}
