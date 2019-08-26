package com.techease.grubsup.views.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.recipeFragments.RecipeDetailFragment;

import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity {

    public static boolean aBooleanBack = false;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (savedInstanceState == null) {
            GeneralUtills.withOutBackStackConnectFragment(this, new RecipeDetailFragment());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (aBooleanBack) {
            finish();
            startActivity(new Intent(this, NavigationTabActivity.class));
        }
    }
}
