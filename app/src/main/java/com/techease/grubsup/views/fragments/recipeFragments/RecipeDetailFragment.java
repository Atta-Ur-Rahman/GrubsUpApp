package com.techease.grubsup.views.fragments.recipeFragments;


import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.adapter.DetailScreenViewPagerAdapter;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getDetailRecipeModel.Direction;
import com.techease.grubsup.model.getDetailRecipeModel.Rating;
import com.techease.grubsup.model.getDetailRecipeModel.RecipeDetailResponseModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryDataModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.model.getSpecificRecipeIngredients.GetSpecificRecipeIngredientsResponseModel;
import com.techease.grubsup.model.getSpecificRecipeIngredients.SpecificShoppingCartIngredientsDataModel;
import com.techease.grubsup.model.postFavoriteModel.BaseResponse;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.utils.ZoomOutPageTransformer;
import com.techease.grubsup.views.activities.CookModeActivity;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.activities.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.views.fragments.recipeFragments.DirectionDetailScreenFragment.detailRecipeDirectionAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements View.OnClickListener {


    List<GetSpecificIngredientsWithOutCategoryDataModel> ingredientArrayList = new ArrayList<>();
    public static List<Direction> directionsArrayList = new ArrayList<>();
    List<Rating> ratinArrayList = new ArrayList<>();

    ArrayList<String> arrayListRecipeIngredientsId = new ArrayList<>();


    List<SpecificShoppingCartIngredientsDataModel> specificRecipeIngredientsDataModels = new ArrayList<>();


    View parentView;

    @BindView(R.id.iv_recipe)
    ImageView ivRecipe;

    @BindView(R.id.tv_title)
    TextView tvTitle;


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;

    @BindView(R.id.tv_cookmode)
    TextView tvCookMode;


    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.iv_dis_like)
    ImageView ivDislike;


    boolean aBooleanFav;
    Dialog alertDialog;


    @BindView(R.id.tv_rate_this)
    TextView tvRateThis;

    int recipeId;
    float aFloatRating;

    boolean aBooleanRatingUpdated = true;
    AlertDialog.Builder alertDialogRating;


    Grubs_Up_CURD grubs_up_curd;

    String strImageUrl;

    boolean aBooleanIsAllRecipe;


    String strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, strTableName;


    @BindView(R.id.tv_add_to_cart)
    TextView tvAddToCart;


    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pause)
    ImageView ivPause;

    @BindView(R.id.videoview)
    VideoView videoview;

    @BindView(R.id.rl_video_view)
    RelativeLayout rlVideoView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, parentView);

        RecipeDetailActivity.aBooleanBack = false;
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        GeneralUtills.putBooleanValueInEditor(getActivity(), "togglePosition", false);
        grubs_up_curd = new Grubs_Up_CURD(getActivity());
        arrayListRecipeIngredientsId.clear();
        recipeId = GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0);
        aBooleanIsAllRecipe = GeneralUtills.getSharedPreferences(getActivity()).getBoolean("isAllRecipeScreen", true);

        if (recipeId == 14) {
            ivRecipe.setVisibility(View.GONE);
            rlVideoView.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video);
            videoview.setVideoURI(uri);



            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });

            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });

    }


    AdapterClass();

    ListenerClass();

    AddToCartButtonClass();

        viewPager.setPageTransformer(true,new

                ZoomOutPageTransformer());
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

    setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return parentView;
}


    private void setupViewPager(final ViewPager viewPager) {
        DetailScreenViewPagerAdapter adapter = new DetailScreenViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new IngredientsDetailScreenFragment(), "Ingredients");
        adapter.addFragment(new DirectionDetailScreenFragment(), "Directions");

        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void ListenerClass() {
        ivBack.setOnClickListener(this);
        tvCookMode.setOnClickListener(this);
        ivLike.setOnClickListener(this);
        ivDislike.setOnClickListener(this);
        tvAddToCart.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPause.setOnClickListener(this);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:


                if (RecipeDetailActivity.aBooleanBack) {
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), NavigationTabActivity.class));
                } else {
                    Objects.requireNonNull(getActivity()).finish();
                }


                break;
            case R.id.tv_cookmode:

                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), CookModeActivity.class));
                break;
            case R.id.iv_like:

                ivLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.liked));
                ivDislike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dislike));

                break;
            case R.id.iv_dis_like:
                ivDislike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.disliked));
                ivLike.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.like));

                break;

            case R.id.tv_add_to_cart:


                RecipeDetailActivity.aBooleanBack = true;
                if (grubs_up_curd.CheckRecipeIsShoppingCard(String.valueOf(recipeId))) {
                    tvAddToCart.setText(getResources().getString(R.string.remove_from_cart));
                    apiCallSpecificIngredients(recipeId);
                    grubs_up_curd.DeleteRecipe(String.valueOf(recipeId));
                    Toast.makeText(getActivity(), "add to cart successfully", Toast.LENGTH_SHORT).show();
                } else {
                    tvAddToCart.setText(getResources().getString(R.string.add_to_cart));
                    grubs_up_curd.InsertRecipeId(String.valueOf(recipeId));
                    grubs_up_curd.DeleteRecipeIngredients(String.valueOf(recipeId));
                    Toast.makeText(getActivity(), "remove from cart successfully", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.iv_play:

                videoview.start();
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.VISIBLE);

                break;

            case R.id.iv_pause:

                videoview.pause();
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);

        }

    }

    private void AdapterClass() {

        getReciepes();


        getSpecificIngredientsWithOutCategory();


    }


    private void AddToCartButtonClass() {

        if (grubs_up_curd.CheckRecipeIsShoppingCard(String.valueOf(recipeId))) {
            tvAddToCart.setText(getResources().getString(R.string.add_to_cart));
        } else {
            tvAddToCart.setText(getResources().getString(R.string.remove_from_cart));
        }
    }


    private void getReciepes() {
        alertDialog.show();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<RecipeDetailResponseModel> getRecipiesResponseModelCall = services.recipeDetail(recipeId);
        getRecipiesResponseModelCall.enqueue(new Callback<RecipeDetailResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<RecipeDetailResponseModel> call, final Response<RecipeDetailResponseModel> response) {

                if (response.isSuccessful()) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();

                        assert response.body() != null;
                        strImageUrl = response.body().getData().getPicture();

                        if (strImageUrl != null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(strImageUrl).placeholder(R.drawable.progress_animation).into(ivRecipe);
                        }
                        tvTitle.setText(response.body().getData().getTitle());

//                    ingredientArrayList.addAll(response.body().getData().getIngredients());
                        directionsArrayList.addAll(response.body().getData().getDirections());
                        ratinArrayList.addAll(response.body().getData().getRatings());

                        if (response.body().getData().getIsFavourite()) {
                            ivFavorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_selected));
                        } else {
                            ivFavorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_unselected));
                        }
                        detailRecipeDirectionAdapter.notifyDataSetChanged();

                        aBooleanFav = response.body().getData().getIsFavourite();

                        ivFavorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (aBooleanFav) {
                                    favoriteApiCall(response.body().getData().getId(), "0");
                                    ivFavorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_unselected));
                                    aBooleanFav = false;

                                } else {
                                    favoriteApiCall(response.body().getData().getId(), "1");
                                    ivFavorite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_selected));
                                    aBooleanFav = true;
                                }
                            }
                        });


//                        materialRatingBarDetailRecipe.setRating(Float.parseFloat(String.valueOf(response.body().getData().getAverageRating())));
//                        materialRatingBarDetailRecipe.setIsIndicator(true);

                    }


                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponseModel> call, Throwable t) {


                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                Log.d("errorDetailRecipe", String.valueOf(t));
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void favoriteApiCall(int recipeId, String strFavStatus) {


        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);

        Call<BaseResponse> call = services.favoriteRecipe(String.valueOf(recipeId), strFavStatus);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    private void getSpecificIngredientsWithOutCategory() {

        arrayListRecipeIngredientsId.clear();
        ingredientArrayList.clear();

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> getRecipiesResponseModelCall = services.specificRecipeIngredientsWithOutCategory(GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<GetSpecificIngredientsWithOutCategoryResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, final Response<GetSpecificIngredientsWithOutCategoryResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    alertDialog.dismiss();

                    ingredientArrayList.addAll(response.body().getData());

                    for (int i = 0; i < ingredientArrayList.size(); i++) {
                        arrayListRecipeIngredientsId.add(response.body().getData().get(i).getId().toString());
                        Log.d("arraylistIngredientsId", String.valueOf(arrayListRecipeIngredientsId));

                    }

//                    detailRecipeItemAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<GetSpecificIngredientsWithOutCategoryResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }


    private void apiCallSpecificIngredients(final int recipeId) {


        alertDialog.show();
        specificRecipeIngredientsDataModels.clear();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        retrofit2.Call<GetSpecificRecipeIngredientsResponseModel> getRecipiesResponseModelCall = services.specificRecipeIngredients(recipeId);
        getRecipiesResponseModelCall.enqueue(new Callback<GetSpecificRecipeIngredientsResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<GetSpecificRecipeIngredientsResponseModel> call, final Response<GetSpecificRecipeIngredientsResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    alertDialog.dismiss();


                    specificRecipeIngredientsDataModels.addAll(response.body().getData());

                    String strCategoryId, strCategoryName;

                    for (int i = 0; i < specificRecipeIngredientsDataModels.size(); i++) {


                        strCategoryId = String.valueOf(specificRecipeIngredientsDataModels.get(i).getId());
                        strCategoryName = specificRecipeIngredientsDataModels.get(i).getName();

                        grubs_up_curd.InsertSpecificRecipeCategory(String.valueOf(recipeId), strCategoryId, strCategoryName);

                        for (int j = 0; j < response.body().getData().get(i).getCategoryIngredients().size(); j++) {


                            strCategoryId = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getCategoryId();
                            strItemId = String.valueOf(specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getId());
                            strItemName = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getItem();
                            strDiscount = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getDiscount();
                            strQuantity = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getQuantity();
                            strUnitPrice = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getUnitPrice();
                            strThumbnail = specificRecipeIngredientsDataModels.get(i).getCategoryIngredients().get(j).getThumbnail();

                            grubs_up_curd.InsertDataAllRecipeInCard(String.valueOf(recipeId), strCategoryId, strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, "ALL_RECIPE_CARD_TABLE");


                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<GetSpecificRecipeIngredientsResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }
}
