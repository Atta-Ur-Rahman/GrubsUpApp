package com.techease.grubsup.views.fragments.forgotPassword;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.ForgotPasswordResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    View parentView;

    @BindView(R.id.et_forgot_email)
    EditText etForgotEmail;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    boolean valid;

    Dialog alertDialog;

    String strForgotPasswordEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());
        btnSendCode.setOnClickListener(this);


        return parentView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_send_code:

                if (validate()) {
                    apiCallForgotPasswordEmail();
                }

                break;
        }
    }


    private void apiCallForgotPasswordEmail() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient().create(APIServices.class);
        Call<ForgotPasswordResponseModel> forgotPasswordResponseModelCall = services.forgotPassword(strForgotPasswordEmail);
        forgotPasswordResponseModelCall.enqueue(new Callback<ForgotPasswordResponseModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponseModel> call, Response<ForgotPasswordResponseModel> response) {

                alertDialog.dismiss();
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    GeneralUtills.putStringValueInEditor(getActivity(),"forgot_email",strForgotPasswordEmail);
                    GeneralUtills.connectFragment(getActivity(), new ForgotPasswordSendCodeFragment());
                } else {
                    Toast.makeText(getActivity(), "something went wrong please try again with valid email", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

    }


    private boolean validate() {
        valid = true;

        strForgotPasswordEmail = etForgotEmail.getText().toString();

        if (strForgotPasswordEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(strForgotPasswordEmail).matches()) {
            etForgotEmail.setError("enter a valid email address");
            valid = false;
        }

        return valid;

    }

}

