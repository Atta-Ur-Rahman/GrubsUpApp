package com.techease.grubsup.views.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.shoppinListFragments.AllRecipeShoppingCartFragment;
import com.techease.grubsup.views.fragments.recipeFragments.AllRecipiesFragment;
import com.techease.grubsup.views.fragments.recipeFragments.FavoriteFragment;
import com.techease.grubsup.views.fragments.profileFragments.ProfileFragment;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationTabActivity extends AppCompatActivity {


    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    Context context;

    public static ArrayList<String> arrayListRecipeId = new ArrayList<>();

    int anIntNavigationPosition;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_tab);


        Objects.requireNonNull(getSupportActionBar()).hide();
        ButterKnife.bind(this);
        context = NavigationTabActivity.this;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (savedInstanceState == null) {

            if (GeneralUtills.getSharedPreferences(this).getBoolean("isFavouriteScreen", false)) {
                GeneralUtills.withOutBackStackConnectFragment(this, new FavoriteFragment());
                navigation.getMenu().findItem(R.id.navigation_favorite).setChecked(true);

            } else {
                GeneralUtills.withOutBackStackConnectFragment(this, new AllRecipiesFragment());
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_all_recipes:
                    GeneralUtills.putBooleanValueInEditor(NavigationTabActivity.this, "isFavouriteScreen", false);
                    GeneralUtills.withOutBackStackConnectFragment(context, new AllRecipiesFragment());
                    return true;
                case R.id.navigation_shopping_list:

                    GeneralUtills.putBooleanValueInEditor(NavigationTabActivity.this, "isFavouriteScreen", false);
                    GeneralUtills.withOutBackStackConnectFragment(context, new AllRecipeShoppingCartFragment());

                    return true;
                case R.id.navigation_favorite:
                    GeneralUtills.putBooleanValueInEditor(NavigationTabActivity.this, "isFavouriteScreen", true);
                    GeneralUtills.withOutBackStackConnectFragment(NavigationTabActivity.this, new FavoriteFragment());
                    return true;
                case R.id.navigation_profile:
                    GeneralUtills.putBooleanValueInEditor(NavigationTabActivity.this, "isFavouriteScreen", false);
                    GeneralUtills.withOutBackStackConnectFragment(NavigationTabActivity.this, new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
                Toast.makeText(context, "land", Toast.LENGTH_SHORT).show();
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // port
                Toast.makeText(context, "port", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception ex) {
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GeneralUtills.putBooleanValueInEditor(NavigationTabActivity.this, "isFavouriteScreen", false);
        finishAffinity();
    }


}
