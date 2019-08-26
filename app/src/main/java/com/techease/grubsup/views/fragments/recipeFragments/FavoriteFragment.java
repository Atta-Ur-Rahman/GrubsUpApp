package com.techease.grubsup.views.fragments.recipeFragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.FavouriteRecipeAdapter;
import com.techease.grubsup.model.getFavouriteRecipe.GetFavouriteRecipeDataModel;
import com.techease.grubsup.model.getFavouriteRecipe.GetFavouriteResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    View parentView;
    @BindView(R.id.rv_favourite_recipe)
    RecyclerView rvFavourite;
    @BindView(R.id.tv_no_fav)
    TextView tvNoFav;
    FavouriteRecipeAdapter favouriteRecipeAdapter;
    private List<GetFavouriteRecipeDataModel> favouriteRecipeDataModels = new ArrayList<>();
    Dialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        rvFavourite.hasFixedSize();
        rvFavourite.setLayoutManager(new LinearLayoutManager(getActivity()));
        favouriteRecipeAdapter = new FavouriteRecipeAdapter(getActivity(), favouriteRecipeDataModels);
        rvFavourite.setAdapter(favouriteRecipeAdapter);

        getFavouriteReciepes();
        return parentView;
    }

    private void getFavouriteReciepes() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<GetFavouriteResponseModel> getFavouriteRecipeDataModelCall = services.favourite();
        getFavouriteRecipeDataModelCall.enqueue(new Callback<GetFavouriteResponseModel>() {
            @Override
            public void onResponse(Call<GetFavouriteResponseModel> call, Response<GetFavouriteResponseModel> response) {

                if (response.body().getData().size() == 0) {
                    tvNoFav.setVisibility(View.VISIBLE);
                }
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    favouriteRecipeDataModels.clear();
                    favouriteRecipeDataModels.addAll(response.body().getData());
                    favouriteRecipeAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetFavouriteResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
