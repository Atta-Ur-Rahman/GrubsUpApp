package com.techease.grubsup.views.fragments.profileFragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.getProfileModel.GetProfileResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.MyOrderActivity;
import com.techease.grubsup.views.activities.ProfileActivity;
import com.techease.grubsup.views.activities.SettingsActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    View parentView;

    Dialog alertDialog;

    @BindView(R.id.iv_profile)
    ImageView ivProfileImage;
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.tv_gender)
    TextView tvGender;

    @BindView(R.id.rl_my_order)
    RelativeLayout rlMyOrder;

    @BindView(R.id.rl_profile)
    RelativeLayout rlProfile;
    @BindView(R.id.rl_settings)
    RelativeLayout rlSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
//        getProfileData();
        rlMyOrder.setOnClickListener(this);
        rlProfile.setOnClickListener(this);
        rlSettings.setOnClickListener(this);

        return parentView;
    }


    private void getProfileData() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<GetProfileResponseModel> getProfileResponseModelCall = services.profile();
        getProfileResponseModelCall.enqueue(new Callback<GetProfileResponseModel>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<GetProfileResponseModel> call, Response<GetProfileResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    alertDialog.dismiss();
//                    Glide.with(Objects.requireNonNull(getActivity())).load(response.body().getData().getProfilePictureLink()).placeholder(R.drawable.progress_animation).into(ivProfileImage);
                    tvProfileName.setText(response.body().getData().getName());
                    tvGender.setText(String.valueOf(response.body().getData().getGender()));


                }
            }

            @Override
            public void onFailure(Call<GetProfileResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_order:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.rl_profile:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.rl_settings:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SettingsActivity.class));


                break;
        }
    }
}
