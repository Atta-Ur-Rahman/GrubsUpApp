package com.techease.grubsup.views.activities;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.adapter.CookModeViewPagerAdapter;
import com.techease.grubsup.model.getDetailRecipeModel.RecipeDetailResponseModel;
import com.techease.grubsup.model.updateRatingModel.UpdateRecipeRatingResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.yourPreferencesFragments.NextWeeKDataModel;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CookModeActivity extends AppCompatActivity implements View.OnClickListener {


    View parentView;


    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_cook_mode_cross)
    ImageView ivCookModeCross;

    Dialog alertDialog;

    @BindView(R.id.sw_night_mode)
    SwitchCompat swNightMode;

    @BindView(R.id.viewpager)
    ViewPager viewPager;


    @BindView(R.id.tab_dots)
    TabLayout tabLayout;

    Dialog ratatingBarDailog;


    Button btnYes, btnNo;

    int recipeId;

    float aFloatRating = 0;

    @BindView(R.id.iv_cook_mode_recipe)
    ImageView ivCookModeRecipe;


    ArrayList<NextWeeKDataModel> arrayList = new ArrayList<>();


    ImageView ivLike;
    ImageView ivDislike;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cook_mode);
        ButterKnife.bind(this);


        recipeId = GeneralUtills.getSharedPreferences(this).getInt("recipe_id", 0);

        CookModeViewPagerAdapter cookModeDirectionViewPagerAdapter = new CookModeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cookModeDirectionViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        tabLayout.setupWithViewPager(viewPager, true);

        swNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        Objects.requireNonNull(getSupportActionBar()).hide();
        alertDialog = AlertUtils.createProgressDialog(this);
        getReciepes();
        ivCookModeCross.setOnClickListener(this);

    }


    private void getReciepes() {
        alertDialog.show();
        final APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(this)).create(APIServices.class);
        Call<RecipeDetailResponseModel> getRecipiesResponseModelCall = services.recipeDetail(GeneralUtills.getSharedPreferences(this).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<RecipeDetailResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<RecipeDetailResponseModel> call, final Response<RecipeDetailResponseModel> response) {

                if (response.isSuccessful()) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }

                    Glide.with(Objects.requireNonNull(CookModeActivity.this)).load(response.body().getData().getPicture()).placeholder(R.drawable.progress_animation).into(ivCookModeRecipe);

                    tvTitle.setText(response.body().getData().getTitle());
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponseModel> call, Throwable t) {

                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }


    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            if (position == 1) {
                tabLayout.setVisibility(View.GONE);
            } else {
                tabLayout.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {


        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_cook_mode_cross:
                ratingBarDailog();
                break;
            case R.id.btn_yes:

                ApiCallRateRecipe();
                break;
            case R.id.btn_no:
                finish();
                break;

            case R.id.iv_like:

                ivLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.liked));
                ivDislike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dislike));

                break;
            case R.id.iv_dis_like:
                ivDislike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.disliked));
                ivLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like));

                break;
        }

    }

    @Override
    public void onBackPressed() {

        ratingBarDailog();
    }

    private void ratingBarDailog() {

        ratatingBarDailog = new Dialog(this);
        ratatingBarDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratatingBarDailog.setCancelable(true);
        ratatingBarDailog.setContentView(R.layout.custom_user_like_layout);


        ivLike = ratatingBarDailog.findViewById(R.id.iv_like);
        ivDislike = ratatingBarDailog.findViewById(R.id.iv_dis_like);
        btnYes = ratatingBarDailog.findViewById(R.id.btn_yes);
        btnNo = ratatingBarDailog.findViewById(R.id.btn_no);

        ivDislike.setOnClickListener(this);
        ivLike.setOnClickListener(this);

        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        MaterialRatingBarClass();


        ratatingBarDailog.show();
        GeneralUtills.doKeepDialog(ratatingBarDailog);

    }

    private void MaterialRatingBarClass() {


    }


    private void ApiCallRateRecipe() {

        alertDialog.show();
        Call<UpdateRecipeRatingResponseModel> deactiveResponseModelCall = BaseNetworking.apiServices(GeneralUtills.getApiToken(CookModeActivity.this)).updateRecipeRating(recipeId, aFloatRating);
        deactiveResponseModelCall.enqueue(new Callback<UpdateRecipeRatingResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<UpdateRecipeRatingResponseModel> call, Response<UpdateRecipeRatingResponseModel> response) {
                alertDialog.dismiss();
                finish();

                if (response.message().equals("You have already rated this recipe")) {
                    Toast.makeText(CookModeActivity.this, "You have already rated this recipe", Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful()) {
                    Toast.makeText(CookModeActivity.this, "successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UpdateRecipeRatingResponseModel> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(CookModeActivity.this, String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });
    }

}