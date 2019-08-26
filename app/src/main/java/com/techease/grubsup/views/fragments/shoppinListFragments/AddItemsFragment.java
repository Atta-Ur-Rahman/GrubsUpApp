package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.AutoCompleteIngredientsAdapter;
import com.techease.grubsup.adapter.CustomSpinnerAdapter;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getAllIngredientsWithOutCategory.GetAllIngredientsWithOutCategoryDataModel;
import com.techease.grubsup.model.getAllIngredientsWithOutCategory.GetAllIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.sp_frequency)
    Spinner spFrequency;

    @BindView(R.id.sp_quality)
    Spinner spQuality;

    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.iv_increment)
    ImageView ivIncrement;
    @BindView(R.id.iv_decrement)
    ImageView ivDecrement;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_add_to_cart)
    Button btnAddToList;

    @BindView(R.id.iv_frequency)
    ImageView ivFrequency;
    @BindView(R.id.autocomplete_country)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.tv_category_type)
    TextView tvCategoryType;
    @BindView(R.id.fl_edittext)
    FrameLayout flEditText;
    @BindView(R.id.fl_textview)
    FrameLayout flTextView;
    @BindView(R.id.et_kg)
    EditText etKg;

    int anIntItemQty = 1, specificrecipeId;
    boolean valid;
    Dialog alertDialog;
    AutoCompleteIngredientsAdapter adapter;
    List<GetAllIngredientsWithOutCategoryDataModel> autoCompleteIngredientsArrayList = new ArrayList<>();

    String strItemId, strCategoryId = null, strCategoryName, strNewItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, strItemName, strQuality = "Small", strItemFrequency = "Weekly", strQty;

    boolean aBooleanIsFrequency = false;
    Grubs_Up_CURD grubs_up_curd;

    int serchPosition;


    boolean aBooleanIsAllShoppingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_add_items, container, false);

        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        grubs_up_curd = new Grubs_Up_CURD(getActivity());

        aBooleanIsAllShoppingList = GeneralUtills.getSharedPreferences(getActivity()).getBoolean("allRecipeShopping", false);
        specificrecipeId = GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0);


        iniListeners();
        SpinnerClass();
        onBackPress(parentView);


        final int newColor = getActivity().getResources().getColor(R.color.gray);
        ivFrequency.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

        getAllIngredientsApiCall();


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int itemPosition, long id) {
                GeneralUtills.hideKeyboard(getActivity());
                GeneralUtills.hideKeyboardFrom(getActivity(), parentView);
                serchPosition = itemPosition;

                if (autoCompleteIngredientsArrayList.get(itemPosition).getCategory().equals("fruit and vegetables ")) {
                    tvCategoryType.setText("Kg");
                    flEditText.setVisibility(View.VISIBLE);
                    flTextView.setVisibility(View.GONE);
                } else {
                    tvCategoryType.setText("Qty");
                    flTextView.setVisibility(View.VISIBLE);
                    flEditText.setVisibility(View.GONE);
                }

            }
        });


        return parentView;
    }

    private void iniListeners() {
        btnAddToList.setOnClickListener(this);
        ivIncrement.setOnClickListener(this);
        ivDecrement.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivFrequency.setOnClickListener(this);


    }


    private void SpinnerClass() {
        spFrequency.setAdapter(new CustomSpinnerAdapter(getActivity(), R.layout.spinner_layout, this.getResources().getStringArray(R.array.frequency_array), "One-off"));
        spFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strItemFrequency = spFrequency.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spQuality.setAdapter(new CustomSpinnerAdapter(getActivity(), R.layout.spinner_number_layout, this.getResources().getStringArray(R.array.size_array), "01"));
        spQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strQuality = spQuality.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_to_cart:

                if (validate()) {

//                    JSONObject js = new JSONObject();
//                    try {
//                        js.put("item", strItemName);
//                        js.put("quantity", strQty);
//                        js.put("frequency", strItemFrequency);
//                        js.put("size", strQuality);
//                        js.put("brand", strItemBrand);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    jsonArray.put(js);
//                    GeneralUtills.putStringValueInEditor(getActivity(), "jsonArrayString", String.valueOf(jsonArray));


//                    apiCallForSearchIngredients(Integer.parseInt(autoCompleteIngredientsArrayList.get(serchPosition).getRecipeId()), autoCompleteIngredientsArrayList.get(serchPosition).getCategoryId(), String.valueOf(autoCompleteIngredientsArrayList.get(serchPosition).getId()));

                    if (autoCompleteIngredientsArrayList.size()==0) {
                        Toast.makeText(getActivity(), "item not found in shopping cart", Toast.LENGTH_SHORT).show();
                    } else {

                        insertSearchItemInDataBase();
                    }
                }
                break;
            case R.id.iv_increment:

                anIntItemQty = anIntItemQty + 1;
                tvQty.setText(String.valueOf(anIntItemQty));
                break;
            case R.id.iv_decrement:
                if (anIntItemQty > 1) {
                    anIntItemQty = anIntItemQty - 1;
                    tvQty.setText(String.valueOf(anIntItemQty));
                }
                break;
            case R.id.iv_back:
                GeneralUtills.hideKeyboard(Objects.requireNonNull(getActivity()));
                GeneralUtills.hideKeyboardFrom(getActivity(), parentView);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                break;
            case R.id.iv_frequency:
                if (aBooleanIsFrequency) {
                    final int newColor = Objects.requireNonNull(getActivity()).getResources().getColor(R.color.gray);
                    ivFrequency.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                    aBooleanIsFrequency = false;
                } else {
                    final int newColor = Objects.requireNonNull(getActivity()).getResources().getColor(R.color.green);
                    ivFrequency.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                    aBooleanIsFrequency = true;
                }
                break;
        }
    }

    private void insertSearchItemInDataBase() {
        strCategoryId = autoCompleteIngredientsArrayList.get(serchPosition).getCategoryId();
        strItemId = String.valueOf(autoCompleteIngredientsArrayList.get(serchPosition).getId());
        strNewItemName = autoCompleteIngredientsArrayList.get(serchPosition).getItem();
        strDiscount = autoCompleteIngredientsArrayList.get(serchPosition).getDiscount();
        strQuantity = autoCompleteIngredientsArrayList.get(serchPosition).getQuantity();
        strUnitPrice = autoCompleteIngredientsArrayList.get(serchPosition).getUnitPrice();
        strThumbnail = autoCompleteIngredientsArrayList.get(serchPosition).getThumbnail();
        strCategoryName = autoCompleteIngredientsArrayList.get(serchPosition).getCategory();


        if (aBooleanIsAllShoppingList) {
            grubs_up_curd.InsertAllRecipeCategory(strCategoryId, strCategoryName);
        } else {
            grubs_up_curd.InsertSpecificRecipeCategory(String.valueOf(specificrecipeId), strCategoryId, strCategoryName);
            Log.d("categoryNameId", strCategoryId + "  " + strCategoryName);
        }

        if (aBooleanIsAllShoppingList) {
            if (grubs_up_curd.InsertDataAllRecipeInCard(String.valueOf(specificrecipeId), strCategoryId, strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, "ALL_RECIPE_CARD_TABLE")) {
                Toast.makeText(getActivity(), "item add to cart successfully", Toast.LENGTH_SHORT).show();
                autoCompleteIngredientsArrayList.clear();
                autoCompleteTextView.setText("");
            } else {
                Toast.makeText(getActivity(), "item already in cart", Toast.LENGTH_SHORT).show();

            }
        } else {
            if (grubs_up_curd.insertSpecificRecipeDataInCard(String.valueOf(specificrecipeId), strCategoryId, strItemId, strNewItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail)) {
                autoCompleteTextView.setText("");
                Toast.makeText(getActivity(), "item add to cart successfully specific", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "item already in cart", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private boolean validate() {
        valid = true;
        strItemName = autoCompleteTextView.getText().toString();
        strQty = tvQty.getText().toString();

        if (strItemName.equals("") || strItemName.isEmpty()) {
            valid = false;
            autoCompleteTextView.setError("enter item name");
        }


        if (strQty.isEmpty() || strQty.equals("0")) {
            valid = false;
        }


        return valid;

    }

    private void getAllIngredientsApiCall() {

        alertDialog.show();
        autoCompleteIngredientsArrayList.clear();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetAllIngredientsWithOutCategoryResponseModel> getRecipiesResponseModelCall = services.getSpecificIngredientsWithOutCategory();
        getRecipiesResponseModelCall.enqueue(new Callback<GetAllIngredientsWithOutCategoryResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetAllIngredientsWithOutCategoryResponseModel> call, final Response<GetAllIngredientsWithOutCategoryResponseModel> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    autoCompleteIngredientsArrayList.addAll(response.body().getData());
                    adapter = new AutoCompleteIngredientsAdapter(getActivity(), autoCompleteIngredientsArrayList);
                    autoCompleteTextView.setAdapter(adapter);
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetAllIngredientsWithOutCategoryResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });
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

                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

                    return true;
                }
                return false;
            }
        });

    }
}
