package com.techease.grubsup.views.fragments.recipeFragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.adapter.AllRecipeAdapter;
import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeDataModel;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeResponseModel;
import com.techease.grubsup.model.siwpeRecipeModel.SwipeRecipeResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.LoginActivity;
import com.techease.grubsup.views.activities.NavigationTabActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.views.activities.NavigationTabActivity.arrayListRecipeId;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecipiesFragment extends Fragment implements View.OnClickListener {

//    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,

    List<GetAllRecipeDataModel> allRecipeDataModelList = new ArrayList<>();

    View parentView;

    Dialog alertDialog;

    @BindView(R.id.rv_all_recipe)
    RecyclerView rvAllRecipe;
    @BindView(R.id.tv_no_recipe)
    TextView tvNoRecipe;
    @BindView(R.id.iv_add_recipe)
    ImageView ivAddRecipe;

    AllRecipeAdapter allRecipeAdapter;

    Grubs_Up_CURD grubs_up_curd;

    boolean booleanTogglePosition;

    boolean aBooleanDeleteRecipe = true;


    public static RelativeLayout coordinatorLayout;


    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_all_recipies, container, false);

        ButterKnife.bind(this, parentView);

        grubs_up_curd = new Grubs_Up_CURD(getActivity());

        alertDialog = AlertUtils.createProgressDialog(getActivity());

        ivAddRecipe.setOnClickListener(this);

        final int newColor = Objects.requireNonNull(getActivity()).getResources().getColor(R.color.green);
        ivAddRecipe.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

        coordinatorLayout = parentView.findViewById(R.id.rl_all_recipe);
        booleanTogglePosition = GeneralUtills.getSharedPreferences(getActivity()).getBoolean("togglePosition", true);


        rvAllRecipe.hasFixedSize();
        rvAllRecipe.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAllRecipe.setItemAnimator(new DefaultItemAnimator());
        rvAllRecipe.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        allRecipeAdapter = new AllRecipeAdapter(getActivity(), allRecipeDataModelList, alertDialog);
        rvAllRecipe.setAdapter(allRecipeAdapter);
        getReciepes();


        rvAllRecipe.setItemAnimator(new DefaultItemAnimator());
        rvAllRecipe.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvAllRecipe);
//
//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                // Row is swiped from recycler view
//                // remove it from adapter
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//
//        // attaching the touch helper to recycler view
//        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rvAllRecipe);


        return parentView;
    }


    private void getReciepes() {

        alertDialog.show();
        arrayListRecipeId.clear();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<GetAllRecipeResponseModel> getRecipiesResponseModelCall = services.allRecepies();
        getRecipiesResponseModelCall.enqueue(new Callback<GetAllRecipeResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<GetAllRecipeResponseModel> call, Response<GetAllRecipeResponseModel> response) {


                if (response.message().equals("Unauthorized")) {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    GeneralUtills.putBooleanValueInEditor(getContext(), "isLogin", false);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                }
                Log.d("response", response.message());
                if (response.isSuccessful()) {
                    alertDialog.dismiss();
                    assert response.body() != null;
                    allRecipeDataModelList.clear();
                    allRecipeDataModelList.addAll(response.body().getData());
                    Collections.reverse(allRecipeDataModelList);

                    if (allRecipeDataModelList.size() >= 7) {
                        ivAddRecipe.setVisibility(View.GONE);
                    }

                    if (allRecipeDataModelList.size() == 0) {
                        tvNoRecipe.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < allRecipeDataModelList.size(); i++) {


                        String strRecipeId;
                        String strCategoryId;
                        String strItemId;
                        String strCategoryName;
                        String strItemName;
                        String strUnitPrice;
                        String strQuantity;
                        String strDiscount;
                        String strThumbnail;

                        strRecipeId = String.valueOf(response.body().getData().get(i).getId());

                        for (int j = 0; j < response.body().getData().get(i).getIngredients().size(); j++) {

                            strCategoryName = response.body().getData().get(i).getIngredients().get(j).getName();
                            strCategoryId = String.valueOf(response.body().getData().get(i).getIngredients().get(j).getId());
                            grubs_up_curd.InsertAllRecipeCategory(strCategoryId, strCategoryName);

                            Log.d("categroryNameId", strCategoryName + "   " + strCategoryId);

                            Log.d("category", strCategoryName);

                            for (int k = 0; k < response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().size(); k++) {
                                strCategoryId = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getCategoryId();
                                strItemId = String.valueOf(response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getId());
                                strItemName = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getItem();
                                strUnitPrice = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getUnitPrice();
                                strQuantity = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getQuantity();
                                strDiscount = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getDiscount();
                                strThumbnail = response.body().getData().get(i).getIngredients().get(j).getCategoryIngredients().get(k).getThumbnail();

                                if (!grubs_up_curd.CheckRecipeIsShoppingCard(strRecipeId)) {

                                    Log.d("ingredientsinserted", "inserted in card" + "  " + strRecipeId);
                                    grubs_up_curd.InsertDataAllRecipeInCard(strRecipeId, strCategoryId, strItemId, strItemName, strUnitPrice, strQuantity, strDiscount, strThumbnail, "ALL_RECIPE_CARD_TABLE");

                                }
                            }
                        }

                    }


                    allRecipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetAllRecipeResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();

                Log.d("errorAllRecipe", String.valueOf(t));


            }
        });
    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//
//        if (viewHolder instanceof AllRecipeAdapter.ViewHolder) {
//            // get the removed item name to display it in snack bar
//            String name = allRecipeDataModelList.get(viewHolder.getAdapterPosition()).getTitle();
//
//            int recipeId = allRecipeDataModelList.get(viewHolder.getAdapterPosition()).getId();
//
//            // backup of removed item for undo purpose
//            final GetAllRecipeDataModel deletedItem = allRecipeDataModelList.get(viewHolder.getAdapterPosition());
//            final int deletedIndex = viewHolder.getAdapterPosition();
//
//
//            // remove the item from recycler view
//
//
//            if (direction == 4) {
//                allRecipeAdapter.removeItem(viewHolder.getAdapterPosition());
//
////               showing snack bar with Undo option
//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        // undo is selected, restore the deleted item
//                        aBooleanDeleteRecipe = false;
//                        allRecipeAdapter.restoreItem(deletedItem, deletedIndex);
//                    }
//                });
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();
//                postTaskInsideBackgroundTask(recipeId);
//            }
//
//            if (direction == 8) {
//                allRecipeAdapter.removeItem(viewHolder.getAdapterPosition());
//                SwipeApiCall(recipeId, "swipe");
//
//            }
//        }
//
//
//    }


    private void SwipeApiCall(final int recipeId, final String swipeType) {

        alertDialog.show();

        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);

        Call<SwipeRecipeResponseModel> swipeRecipeResponseModelCall = services.swipeOrDelete(recipeId, swipeType);

        swipeRecipeResponseModelCall.enqueue(new Callback<SwipeRecipeResponseModel>() {
            @Override
            public void onResponse(Call<SwipeRecipeResponseModel> call, Response<SwipeRecipeResponseModel> response) {

                grubs_up_curd.DeleteRecipeIngredients(String.valueOf(recipeId));

                if (swipeType.equals("swipe")) {
                    getActivity().finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), NavigationTabActivity.class));
                } else {
                    Toast.makeText(getActivity(), "recipe deleted successfully", Toast.LENGTH_SHORT).show();
                    ivAddRecipe.setVisibility(View.VISIBLE);
                }
                alertDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SwipeRecipeResponseModel> call, Throwable t) {
                alertDialog.dismiss();

                if (swipeType.equals("swipe")) {
                    Toast.makeText(getActivity(), "swipe recipe successfully", Toast.LENGTH_SHORT).show();
                    getActivity().finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), NavigationTabActivity.class));
                }
            }
        });
    }


    private void postTaskInsideBackgroundTask(final int recipeId) {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // pretend to do something "background-y"
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {


                        if (aBooleanDeleteRecipe) {
                            SwipeApiCall(recipeId, "delete");
                        }
                    }
                });
            }
        });

        backgroundThread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_recipe:

                Toast.makeText(getActivity(), "testing mode", Toast.LENGTH_SHORT).show();
        }
    }


}

