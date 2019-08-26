package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.techease.grubsup.R;
import com.techease.grubsup.model.shoppingModel.ShoppingResponseModel;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.utils.GetLocation;
import com.techease.grubsup.views.activities.CongratulationsActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.R.mipmap.pickup_white;
import static com.techease.grubsup.views.activities.CongratulationsActivity.arrayListIngredientsSelectedItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment implements View.OnClickListener, PlacesAutoCompleteAdapter.ClickListener {


    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_later)
    Button btnLatr;
    @BindView(R.id.btn_next)
    Button btnNext;


    @BindView(R.id.fl_pickup)
    FrameLayout flPickup;
    @BindView(R.id.iv_pickup)
    ImageView ivPickup;
    @BindView(R.id.tv_pickup)
    TextView tvPickup;


    @BindView(R.id.fl_delivery)
    FrameLayout flDelivery;
    @BindView(R.id.iv_delivery)
    ImageView ivDelivery;
    @BindView(R.id.tv_delivery)
    TextView tvDelivery;

    GetLocation getLocation;

    @BindView(R.id.et_shipping_address)
    EditText etShippingAddress;

    Dialog alertDialog;


    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;


    int colorWhite, colorOrange;

   public static String strSuperMarket, strPostalCode, strShoppingAddress, strIngredientsItems;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_adress, container, false);

//        getLocation = new GetLocation();
//        getLocation.getLocation(getActivity());
        ini();


        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));

        recyclerView = parentView.findViewById(R.id.places_recycler_view);
        etShippingAddress.addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();


        onBackPress(parentView);


        return parentView;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(getActivity(), place.getAddress() + ", " + place.getLatLng().latitude + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }

    private void ini() {
        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        ivBack.setOnClickListener(this);
        btnLatr.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        flPickup.setOnClickListener(this);
        flDelivery.setOnClickListener(this);


        colorOrange = getActivity().getResources().getColor(R.color.orange_color);
        colorWhite = getActivity().getResources().getColor(R.color.white);

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
            case R.id.btn_next:

                GetAllDataFromFragment();
                if (strShoppingAddress.isEmpty() || strShoppingAddress.length() < 3) {
                    etShippingAddress.setError("enter shipping address");
                } else {

                    GeneralUtills.connectFragment(getActivity(), new OrderTimeFragment());
                    GeneralUtills.hideKeyboard(getActivity());
                    GeneralUtills.hideKeyboardFrom(getActivity(),parentView);

                }


//                GetAllDataFromFragment();
//                if (strShoppingAddress.isEmpty() || strShoppingAddress.length() < 3) {
//                    etShippingAddress.setError("enter shipping address");
//                } else {
//                    apiCallShopping();
//                }
                break;

            case R.id.btn_later:



                GeneralUtills.hideKeyboard(getActivity());
                GeneralUtills.hideKeyboardFrom(getActivity(),parentView);

                GeneralUtills.connectFragment(getActivity(), new OrderTimeFragment());
//
//
//                String lat, log;
//
//                lat = GeneralUtills.getSharedPreferences(getActivity()).getString("latitude", "");
//                log = GeneralUtills.getSharedPreferences(getActivity()).getString("longitude", "");
//
//
//                Log.d("latlog", lat + "," + log);

                break;


            case R.id.fl_pickup:


                flPickup.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_orange));
                ivPickup.setColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP);
                tvPickup.setTextColor(colorWhite);


                flDelivery.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                ivDelivery.setColorFilter(colorOrange, PorterDuff.Mode.SRC_ATOP);
                tvDelivery.setTextColor(colorOrange);

                break;
            case R.id.fl_delivery:



                flPickup.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_white));
                ivPickup.setColorFilter(colorOrange, PorterDuff.Mode.SRC_ATOP);
                tvPickup.setTextColor(colorOrange);


                flDelivery.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.round_button_pick_or_delivey_orange));
                ivDelivery.setColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP);
                tvDelivery.setTextColor(colorWhite);



                break;

        }
    }


    private void GetAllDataFromFragment() {

        strSuperMarket = SupperMarketFragment.strSuperMarket;
        strPostalCode = GeneralUtills.getSharedPreferences(getActivity()).getString("postal_code", "");
        strShoppingAddress = etShippingAddress.getText().toString();
        strIngredientsItems = String.valueOf(arrayListIngredientsSelectedItem);
        strIngredientsItems = strIngredientsItems.replace("[", "");
        strIngredientsItems = strIngredientsItems.replace("]", "");

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
