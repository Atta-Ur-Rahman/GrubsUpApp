package com.techease.grubsup.views.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.shoppinListFragments.SpecificRecipeShoppingCartFragment;

import java.util.Objects;

public class SpecificRecipeShoppinListActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_specific_recipe_shoppin_list);
        if (savedInstanceState == null) {
            GeneralUtills.withOutBackStackConnectFragment(this, new SpecificRecipeShoppingCartFragment());
        }
    }
}
