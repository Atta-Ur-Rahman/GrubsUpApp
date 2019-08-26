package com.techease.grubsup.views.fragments.profileFragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.btn_add_new_card)
    Button btnAddNewCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_payment_method, container, false);

        ButterKnife.bind(this, parentView);
        ivBack.setOnClickListener(this);
        btnAddNewCard.setOnClickListener(this);


        return parentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_add_new_card:
                GeneralUtills.connectFragment(getActivity(), new AddNewCardFragment());

        }
    }
}
