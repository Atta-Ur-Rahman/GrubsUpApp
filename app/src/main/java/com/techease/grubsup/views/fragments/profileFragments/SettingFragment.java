package com.techease.grubsup.views.fragments.profileFragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.deativeAccountModel.DeactiveResponseModel;
import com.techease.grubsup.model.notificationModel.NotificationResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.FullscreenActivity;
import com.techease.grubsup.views.activities.PreferencesActivity;
import com.techease.grubsup.views.fragments.forgotPassword.ResetPasswordFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class
SettingFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.sw_location)
    SwitchCompat swLocation;

    @BindView(R.id.sw_notification)
    SwitchCompat swNotification;

    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;

    @BindView(R.id.tv_payment_method)
    TextView tvPaymentMethod;
    @BindView(R.id.tv_contact_us)
    TextView tvContactUs;
    @BindView(R.id.tv_tearm_conditions)
    TextView tvTermsAndCondition;
    @BindView(R.id.tv_deactivate_account)
    TextView tvDeactivateAccount;
    @BindView(R.id.btn_log_out)
    Button btnLogout;

    @BindView(R.id.tv_change_your_plane)
    TextView tvChangeYourPlane;
    public static boolean aBooleanChangePassword;

    String strNotificationStatus;

    Dialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_setting, container, false);

        ButterKnife.bind(this, parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());


        iniListenersClass();

        GeneralUtills.putBooleanValueInEditor(getActivity(), "setting_screen", true);

        if (GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("notification_status", "0").equals("1")) {

            swNotification.setChecked(true);
        }

        return parentView;
    }

    private void iniListenersClass() {

        ivBack.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvPaymentMethod.setOnClickListener(this);
        tvContactUs.setOnClickListener(this);
        tvTermsAndCondition.setOnClickListener(this);
        tvDeactivateAccount.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        swLocation.setOnClickListener(this);
        swNotification.setOnClickListener(this);
        tvChangeYourPlane.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).finish();
                break;

            case R.id.tv_change_password:

                aBooleanChangePassword = true;
                GeneralUtills.connectFragment(getActivity(), new ResetPasswordFragment());
                break;
            case R.id.tv_payment_method:
                GeneralUtills.connectFragment(getActivity(), new PaymentMethodFragment());


                break;
            case R.id.tv_contact_us:

                GeneralUtills.connectFragment(getActivity(), new ContactUsFragment());

                break;

            case R.id.tv_tearm_conditions:

                break;
            case R.id.tv_deactivate_account:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Deactive account")
                        .setMessage("Are you sure you want to Deactive account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation

                                apiCallDeactiveAccount();


                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                break;

            case R.id.btn_log_out:
                GeneralUtills.putBooleanValueInEditor(getContext(), "isLogin", false);
                Objects.requireNonNull(getActivity()).finishAffinity();
                startActivity(new Intent(getActivity(), FullscreenActivity.class));

                break;
            case R.id.sw_location:

                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;

            case R.id.sw_notification:

                if (GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("notification_status", "0").equals("1")) {
                    strNotificationStatus = "0";
                } else {
                    strNotificationStatus = "1";
                }
                apiCallNotificationStatus();
                break;
            case R.id.tv_change_your_plane:
                Toast.makeText(getActivity(), "testing mode", Toast.LENGTH_SHORT).show();
//                GeneralUtills.putBooleanValueInEditor(getActivity(),"isSetting",true);
//                startActivity(new Intent(getActivity(), PreferencesActivity.class));


        }
    }

    private void apiCallDeactiveAccount() {
        alertDialog.show();

        Call<DeactiveResponseModel> deactiveResponseModelCall = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity())).deactiveAccount();
        deactiveResponseModelCall.enqueue(new Callback<DeactiveResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<DeactiveResponseModel> call, Response<DeactiveResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    GeneralUtills.putBooleanValueInEditor(getContext(), "isLogin", false);
                    Toast.makeText(getActivity(), "account successfully deactivated", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    startActivity(new Intent(getActivity(), FullscreenActivity.class));
                }
            }

            @Override
            public void onFailure(Call<DeactiveResponseModel> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        swLocation.setChecked(GeneralUtills.isLocationEnabled(getActivity()));
    }


    private void apiCallNotificationStatus() {


        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);

        Call<NotificationResponseModel> call = services.notificationStatus(strNotificationStatus);

        call.enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {

                alertDialog.dismiss();
//                if (response.isSuccessful()) {
//
//                    assert response.body() != null;
//                    if (response.body().getMessage().equals("Notification Enabled Successfully"))
//                        GeneralUtills.putStringValueInEditor(getActivity(), "notification_status", "1");
//                }
//
//                assert response.body() != null;
//                if (response.body().getMessage().equals("Notification Disabled Successfully")) {
//                    GeneralUtills.putStringValueInEditor(getActivity(), "notification_status", "0");
//                }

//                Toast.makeText(getActivity(), String.valueOf(response.body().getMessage()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

//                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }

}

