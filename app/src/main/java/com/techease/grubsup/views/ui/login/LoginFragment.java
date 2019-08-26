package com.techease.grubsup.views.ui.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.login_data_model.LoginResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.fragments.forgotPassword.ForgotPasswordFragment;
import com.techease.grubsup.views.ui.signup.SignUpFragment;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    View parentView;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forgot)
    TextView tvForgot;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_signup)
    TextView tvSignUp;

    Dialog alertDialog;

//    @BindView(R.id.iv_back)
//    ImageView ivBack;

    @BindView(R.id.radioRemember)
    RadioButton radioRemember;


    boolean isRemember = false;

    String strLoginEmail, strLoginPassword, strDeviceToken;

    String strEmail;

    Grubs_Up_CURD grubs_up_curd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());
        grubs_up_curd = new Grubs_Up_CURD(getActivity());
        GeneralUtills.putStringValueInEditor(getActivity(), "planId", "");


        initListener();
        return parentView;
    }

    private void initListener() {
        tvForgot.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
//        ivBack.setOnClickListener(this);


        radioRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRemember == true) {
                    radioRemember.setChecked(false);
                    isRemember = false;

                } else {
                    radioRemember.setChecked(true);
                    isRemember = true;

                }
            }
        });


    }


    private boolean validate() {
        boolean valid = true;

        strLoginEmail = etEmail.getText().toString();
        strLoginPassword = etPassword.getText().toString();

        if (strLoginEmail.isEmpty()) {

            etEmail.setError("enter a valid username");
            valid = false;
        }
        if (strLoginPassword.isEmpty() || strLoginPassword.length() < 4 || strLoginPassword.length() > 12) {
            etPassword.setError("between 4 and 12 alphanumeric characters");
            valid = false;
        }

        return valid;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:
                if (validate()) {

                    loginApiCall();
                }
                break;

            case R.id.tv_signup:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SignUpFragment.newInstance())
                        .commitNow();


                break;

            case R.id.tv_forgot:

                GeneralUtills.connectFragment(getActivity(), new ForgotPasswordFragment());
                break;

//            case R.id.iv_back:
//                GeneralUtills.withOutBackStackConnectFragment(getActivity(), new LoginSignupFragment());
//                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("HardwareIds")
    private void loginApiCall() {


        strDeviceToken = Settings.Secure.getString(Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        alertDialog.show();

        APIServices services = APIClient.getApiClient().create(APIServices.class);
        Call<LoginResponseModel> userLogin = services.userLogin(strLoginEmail, strLoginPassword, strDeviceToken);
        userLogin.enqueue(new Callback<LoginResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {


                alertDialog.dismiss();

                if (response.body() == null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (response.body().getStatus()) {


                    strEmail = GeneralUtills.getEmail(getActivity());

                    if (!strEmail.equals(response.body().getData().getUser().getEmail())) {
                        grubs_up_curd.ClearAllTables();
                    }


                    alertDialog.dismiss();
                    String userID = response.body().getData().getUser().getId().toString();

                    if (isRemember) {
                        GeneralUtills.putBooleanValueInEditor(getContext(), "isLogin", true);
                    }
                    GeneralUtills.putStringValueInEditor(getActivity(), "userID", userID);
                    GeneralUtills.putStringValueInEditor(getActivity(), "api_token", response.body().getData().getUser().getToken());
                    GeneralUtills.putStringValueInEditor(getActivity(), "postal_code", String.valueOf(response.body().getData().getUser().getPostalCode()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "notification_status", response.body().getData().getUser().getNotificationStatus());
                    GeneralUtills.putStringValueInEditor(getActivity(), "email", response.body().getData().getUser().getEmail());
                    GeneralUtills.putStringValueInEditor(getActivity(), "name", response.body().getData().getUser().getName());
                    GeneralUtills.putStringValueInEditor(getActivity(), "location", String.valueOf(response.body().getData().getUser().getLocation()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "gender", String.valueOf(response.body().getData().getUser().getGender()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "adultsSize", String.valueOf(response.body().getData().getUser().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "kidsUnder14Size", String.valueOf(response.body().getData().getUser().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "planId", String.valueOf(response.body().getData().getUser().getPlanId()));

                    Objects.requireNonNull(getActivity()).finishAffinity();
                    startActivity(new Intent(getActivity(), NavigationTabActivity.class));

                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
}
