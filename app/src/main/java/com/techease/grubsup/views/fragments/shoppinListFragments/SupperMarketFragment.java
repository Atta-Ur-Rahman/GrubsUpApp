package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.SuppermarketAdapter;
import com.techease.grubsup.model.getSuperMarket.GetSuperMarketResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupperMarketFragment extends Fragment implements View.OnClickListener {


    View parentView;

    ArrayList<String> arrayListIngredientsItems = new ArrayList<>();

    SuppermarketAdapter ingredientsAdapter;

    @BindView(R.id.rv_supermarket)
    RecyclerView rvIngredients;
    @BindView(R.id.tv_shopping_postal_code)
    TextView tvShoppingPostalCode;

    @BindView(R.id.tv_super_market)
    TextView tvSuperMarket;

    @BindView(R.id.btn_start_shopping)
    Button btnStartShopping;

    @BindView(R.id.iv_freshchoice)
    ImageView ivFreshChoice;
    @BindView(R.id.iv_new_world)
    ImageView ivNewWorld;
    @BindView(R.id.iv_countdown)
    ImageView ivCountDown;
    @BindView(R.id.iv_paknsave)
    ImageView ivPaknsave;
    Dialog alertDialog;


    String strPostalCode;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    public static String strSuperMarket;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_supper_market, container, false);
        ButterKnife.bind(this, parentView);

        strSuperMarket = "";
        tvSuperMarket.setText(strSuperMarket);

        alertDialog = AlertUtils.createProgressDialog(getActivity());

        strPostalCode = GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("postal_code", "");
        tvShoppingPostalCode.setText(strPostalCode);

        rvIngredients.hasFixedSize();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        ingredientsAdapter = new SuppermarketAdapter(getActivity(), arrayListIngredientsItems);
        rvIngredients.setAdapter(ingredientsAdapter);


        upDateButtonZoom();


        iniListners();

        onBackPress(parentView);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        return parentView;
    }

    private void iniListners() {

        ivFreshChoice.setOnClickListener(this);
        ivNewWorld.setOnClickListener(this);
        ivCountDown.setOnClickListener(this);
        ivPaknsave.setOnClickListener(this);
        btnStartShopping.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void getSuperMarketApiCall() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSuperMarketResponseModel> getRecipiesResponseModelCall = services.getSuperMarket();
        getRecipiesResponseModelCall.enqueue(new Callback<GetSuperMarketResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSuperMarketResponseModel> call, final Response<GetSuperMarketResponseModel> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        arrayListIngredientsItems.add(response.body().getData().get(i));
                    }
                    ingredientsAdapter.notifyDataSetChanged();

                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetSuperMarketResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void apiCallShopping() {
//        alertDialog.show();
//
//        Call<ShoppingResponseModel> shoppingResponseModelCall = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity()))
//                .shopping("Monthly", strSuperMarket, tvShoppingPostalCode.getText().toString(), "","");
//        shoppingResponseModelCall.enqueue(new Callback<ShoppingResponseModel>() {
//            @Override
//            public void onResponse(Call<ShoppingResponseModel> call, Response<ShoppingResponseModel> response) {
//
//                Log.d("response", String.valueOf(response));
//                if (response.isSuccessful()) {
//                    GeneralUtills.connectFragment(getActivity(), new AddressFragment());
//                    alertDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ShoppingResponseModel> call, Throwable t) {
//                alertDialog.dismiss();
//            }
//        });
//    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_freshchoice:
                GeneralUtills.putStringValueInEditor(getActivity(), "button_zoom", "freshchoice");
                RefreshFragment();
                break;
            case R.id.iv_new_world:
                GeneralUtills.putStringValueInEditor(getActivity(), "button_zoom", "New World");
                RefreshFragment();
                break;
            case R.id.iv_countdown:
                GeneralUtills.putStringValueInEditor(getActivity(), "button_zoom", "countdown");
                RefreshFragment();
                break;
            case R.id.iv_paknsave:
                GeneralUtills.putStringValueInEditor(getActivity(), "button_zoom", "PaknSave");
                RefreshFragment();
                break;
            case R.id.btn_start_shopping:

                if (strSuperMarket.isEmpty()) {
                    Toast.makeText(getActivity(), "please select super market", Toast.LENGTH_SHORT).show();
                } else {
                    GeneralUtills.connectFragment(getActivity(), new AddressFragment());
                }

                break;
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        }
        tvSuperMarket.setText(strSuperMarket);
    }

    private void RefreshFragment() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void upDateButtonZoom() {
        strSuperMarket = GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("button_zoom", "");

        if (strSuperMarket.equals("freshchoice")) {
            selectedMarket(ivFreshChoice);
        } else if (strSuperMarket.equals("New World")) {
            selectedMarket(ivNewWorld);

        } else if (strSuperMarket.equals("countdown")) {
            selectedMarket(ivCountDown);
        } else if (strSuperMarket.equals("PaknSave")) {
            selectedMarket(ivPaknsave);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void selectedMarket(ImageView imageView) {
        imageView.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_super_market_seleted));

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
