package com.techease.grubsup.views.fragments.recipeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.DetailRecipeDirectionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.techease.grubsup.views.fragments.recipeFragments.RecipeDetailFragment.directionsArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionDetailScreenFragment extends Fragment {

    View parentView;
    @BindView(R.id.rv_recipe_direction)
    RecyclerView rvDirection;
   public static DetailRecipeDirectionAdapter detailRecipeDirectionAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_direction_detail_screen, container, false);

        ButterKnife.bind(this,parentView);
        rvDirection.hasFixedSize();
        rvDirection.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailRecipeDirectionAdapter = new DetailRecipeDirectionAdapter(getActivity(), directionsArrayList);
        rvDirection.setAdapter(detailRecipeDirectionAdapter);
        detailRecipeDirectionAdapter.notifyDataSetChanged();


        return parentView;
    }

}
