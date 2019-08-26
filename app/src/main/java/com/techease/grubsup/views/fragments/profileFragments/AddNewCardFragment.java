package com.techease.grubsup.views.fragments.profileFragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techease.grubsup.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewCardFragment extends Fragment implements View.OnClickListener {


    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_add_new_card, container, false);

        ButterKnife.bind(this, parentView);
        ivBack.setOnClickListener(this);

        return parentView;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                break;
        }
    }
}

