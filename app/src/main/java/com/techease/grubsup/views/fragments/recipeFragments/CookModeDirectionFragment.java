package com.techease.grubsup.views.fragments.recipeFragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.model.getDetailRecipeModel.Direction;
import com.techease.grubsup.model.getDetailRecipeModel.RecipeDetailResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookModeDirectionFragment extends Fragment {

    View parentView;

    MyViewPagerAdapter myViewPagerAdapter;

    @BindView(R.id.viewpager_direction)
    ViewPager viewPager;

    ArrayList<Direction> stringArrayList = new ArrayList<>();

    @BindView(R.id.tab_dots)
    TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_direction, container, false);

        ButterKnife.bind(this, parentView);


        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);

        getReciepes();
        return parentView;
    }


    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_horizontal_view_layout, container, false);

            Direction direction = stringArrayList.get(position);
            TextView textView = view.findViewById(R.id.tv_direction);
            TextView tvDirectionSteps = view.findViewById(R.id.tv_direction_steps);

            int i =1;
            i=position+1;
            tvDirectionSteps.setText("STEP " +  i);
            textView.setText(direction.getName());
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return stringArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private void getReciepes() {

        final APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<RecipeDetailResponseModel> getRecipiesResponseModelCall = services.recipeDetail(GeneralUtills.getSharedPreferences(getActivity()).getInt("recipe_id", 0));
        getRecipiesResponseModelCall.enqueue(new Callback<RecipeDetailResponseModel>() {
            @Override
            public void onResponse(Call<RecipeDetailResponseModel> call, final Response<RecipeDetailResponseModel> response) {

                if (response.isSuccessful()) {
                    stringArrayList.addAll(response.body().getData().getDirections());
                    myViewPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponseModel> call, Throwable t) {

            }
        });
    }


}
