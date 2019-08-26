package com.techease.grubsup.views.fragments.shoppinListFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.activities.RecipeDetailActivity;
import com.techease.grubsup.views.fragments.recipeFragments.AllRecipiesFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CongratulationsFragment extends Fragment implements View.OnClickListener {

    View parentView;

    @BindView(R.id.btn_back_to_home)
    Button btnBackToHome;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_congratulations, container, false);

        ButterKnife.bind(this, parentView);
        btnBackToHome.setOnClickListener(this);

        GeneralUtills.hideKeyboard(Objects.requireNonNull(getActivity()));

        onBackPress(parentView);
        return parentView;
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

                   getActivity().finish();
                    GeneralUtills.withOutBackStackConnectFragment(getActivity(),new AllRecipiesFragment());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back_to_home:
                getActivity().finish();
                startActivity(new Intent(getActivity(), NavigationTabActivity.class));
                break;
        }

    }

}
