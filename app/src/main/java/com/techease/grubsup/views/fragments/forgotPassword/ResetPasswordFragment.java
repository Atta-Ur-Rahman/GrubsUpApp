package com.techease.grubsup.views.fragments.forgotPassword;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.changePasswordModel.ChangePasswordResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.ui.login.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.views.fragments.profileFragments.SettingFragment.aBooleanChangePassword;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    View parentView;

    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.reset_password)
    TextView tvResetPassword;

    private String strNewPassword, strConfirmPassword;
    Dialog alertDialog;


    boolean valid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_reset_password, container, false);

        ButterKnife.bind(this, parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());

        btnReset.setOnClickListener(this);

        if (aBooleanChangePassword) {
            aBooleanChangePassword = false;
            btnReset.setText("Change");
            tvResetPassword.setText("Change Password");

        }


        return parentView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_reset:


                if (validate()) {
                    apiCallChangePassword();
                }
                break;
        }
    }


    private boolean validate() {


        strNewPassword = etNewPassword.getText().toString();
        strConfirmPassword = etConfirmPassword.getText().toString();

        valid = true;

        if (strNewPassword.isEmpty() || strNewPassword.length() < 4 || strNewPassword.length() > 12) {
            etNewPassword.setError("between 4 and 12 alphanumeric characters");
            valid = false;
        }
        if (strNewPassword.equals(strConfirmPassword)) {
            valid = true;
        } else {
            etConfirmPassword.setError("password does not match");
            valid = false;
        }

        return valid;

    }

    private void apiCallChangePassword() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<ChangePasswordResponseModel> forgotPasswordResponseModelCall = services.changePassword(strNewPassword, strConfirmPassword);
        forgotPasswordResponseModelCall.enqueue(new Callback<ChangePasswordResponseModel>() {
            @Override
            public void onResponse(Call<ChangePasswordResponseModel> call, Response<ChangePasswordResponseModel> response) {

                alertDialog.dismiss();
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus()) {

                    GeneralUtills.withOutBackStackConnectFragment(getActivity(), new LoginFragment());
                }

            }

            @Override
            public void onFailure(Call<ChangePasswordResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

    }
}
