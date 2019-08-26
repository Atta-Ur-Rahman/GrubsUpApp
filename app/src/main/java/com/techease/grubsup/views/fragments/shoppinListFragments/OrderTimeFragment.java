package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.shoppingModel.ShoppingResponseModel;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.CongratulationsActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.views.fragments.shoppinListFragments.AddressFragment.strPostalCode;
import static com.techease.grubsup.views.fragments.shoppinListFragments.AddressFragment.strShoppingAddress;
import static com.techease.grubsup.views.fragments.shoppinListFragments.AddressFragment.strSuperMarket;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderTimeFragment extends Fragment implements View.OnClickListener {


    View parentView;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_tomorrow)
    TextView tvTomorrow;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;


    @BindView(R.id.rl_morning)
    RelativeLayout rlMorning;
    @BindView(R.id.tv_morning_time)
    TextView tvMorningTime;
    @BindView(R.id.tv_morning_available)
    TextView tvMorningAvailable;


    @BindView(R.id.rl_afternoon)
    RelativeLayout rlAfternoon;
    @BindView(R.id.tv_afternoon_time)
    TextView tvAfternoonTime;
    @BindView(R.id.tv_afternoon_available)
    TextView tvAfternoonAvailable;


    @BindView(R.id.rl_evening)
    RelativeLayout rlEveining;
    @BindView(R.id.tv_evening_time)
    TextView tvEveningTime;
    @BindView(R.id.tv_evening_avilable)
    TextView tvEveningAvailable;
    int colorBlack, colorWhite;


    @BindView(R.id.btn_checkout)
    Button btnCheckOut;

    Dialog alertDialog;

    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order_time, container, false);
        ButterKnife.bind(this, parentView);

        ivBack.setOnClickListener(this);
        colorBlack = getActivity().getResources().getColor(R.color.black);
        colorWhite = getActivity().getResources().getColor(R.color.white);


        alertDialog = AlertUtils.createProgressDialog(getActivity());

        tvToday.setOnClickListener(this);
        tvTomorrow.setOnClickListener(this);
        tvOrderDate.setOnClickListener(this);


        rlMorning.setOnClickListener(this);
        rlAfternoon.setOnClickListener(this);
        rlEveining.setOnClickListener(this);

        btnCheckOut.setOnClickListener(this);


        tvTotalPrice.setText(GeneralUtills.getSharedPreferences(getActivity()).getString("total_price",""));



        return parentView;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:

                GeneralUtills.hideKeyboardFrom(getActivity(), parentView);
                GeneralUtills.hideKeyboard(getActivity());
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

                break;
            case R.id.tv_today:

                tvToday.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_orange));
                tvToday.setTextColor(colorWhite);


                tvTomorrow.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvTomorrow.setTextColor(colorBlack);

                tvOrderDate.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvOrderDate.setTextColor(colorBlack);


                break;
            case R.id.tv_tomorrow:

                tvToday.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvToday.setTextColor(colorBlack);


                tvTomorrow.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_orange));
                tvTomorrow.setTextColor(colorWhite);

                tvOrderDate.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvOrderDate.setTextColor(colorBlack);

                break;
            case R.id.tv_order_date:


                tvToday.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvToday.setTextColor(colorBlack);


                tvTomorrow.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_line_background));
                tvTomorrow.setTextColor(colorBlack);

                tvOrderDate.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_orange));
                tvOrderDate.setTextColor(colorWhite);
                break;
            case R.id.rl_morning:


                rlMorning.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_orange));
                tvMorningTime.setTextColor(colorWhite);
                tvMorningAvailable.setTextColor(colorWhite);


                rlAfternoon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvAfternoonTime.setTextColor(colorBlack);
                tvAfternoonAvailable.setTextColor(colorBlack);

                rlEveining.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvEveningTime.setTextColor(colorBlack);
                tvEveningAvailable.setTextColor(colorBlack);
                break;
            case R.id.rl_afternoon:

                rlMorning.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvMorningTime.setTextColor(colorBlack);
                tvMorningAvailable.setTextColor(colorBlack);


                rlAfternoon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_orange));
                tvAfternoonTime.setTextColor(colorWhite);
                tvAfternoonAvailable.setTextColor(colorWhite);

                rlEveining.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvEveningTime.setTextColor(colorBlack);
                tvEveningAvailable.setTextColor(colorBlack);
                break;
            case R.id.rl_evening:


                rlMorning.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvMorningTime.setTextColor(colorBlack);
                tvMorningAvailable.setTextColor(colorBlack);


                rlAfternoon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                tvAfternoonTime.setTextColor(colorBlack);
                tvAfternoonAvailable.setTextColor(colorBlack);

                rlEveining.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_orange));
                tvEveningTime.setTextColor(colorWhite);
                tvEveningAvailable.setTextColor(colorWhite);

                break;

            case R.id.btn_checkout:
                apiCallShopping();
                break;

        }
    }


    private void apiCallShopping() {

//        GeneralUtills.getSharedPreferences(getActivity()).getString("jsonArrayString", "")

        Call<ShoppingResponseModel> addedResponseModelCall = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity())).shopping(strSuperMarket, strPostalCode, strShoppingAddress, "item1,item2");
        addedResponseModelCall.enqueue(new Callback<ShoppingResponseModel>() {
            @Override
            public void onResponse(Call<ShoppingResponseModel> call, Response<ShoppingResponseModel> response) {

                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "successfully", Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                    getActivity().startActivity(new Intent(getActivity(), CongratulationsActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ShoppingResponseModel> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();


            }
        });
    }

}
