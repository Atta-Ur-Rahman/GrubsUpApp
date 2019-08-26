package com.techease.grubsup.views.fragments.forgotPassword;


import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.ForgotPasswordResponseModel;
import com.techease.grubsup.model.verifyCodeModel.VerifyCodeResponseModel;
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
public class ForgotPasswordSendCodeFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.tv_count_down_timer)
    TextView tvCountDownTimer;

    @BindView(R.id.btn_resend_code)
    Button btnResendCode;
    @BindView(R.id.ll_resend_conformation_code)
    LinearLayout llResendCodeConformation;

    @BindView(R.id.btn_verify_code)
    Button btnVerifyCode;

    @BindView(R.id.et_code_num1)
    EditText et_num1;
    @BindView(R.id.et_code_num2)
    EditText et_num2;
    @BindView(R.id.et_code_num3)
    EditText et_num3;
    @BindView(R.id.et_code_num4)
    EditText et_num4;

    String strVerifycode, strForgotPasswordEmail;


    Dialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_forgot_password_send_code, container, false);

        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        strForgotPasswordEmail = GeneralUtills.getSharedPreferences(getActivity()).getString("forgot_email", "");

        initiateListener();
        CountDownTimerClass();
        return parentView;
    }

    private void initiateListener() {
        btnResendCode.setOnClickListener(this);
        btnVerifyCode.setOnClickListener(this);

        et_num1.addTextChangedListener(genraltextWatcher);
        et_num2.addTextChangedListener(genraltextWatcher);
        et_num3.addTextChangedListener(genraltextWatcher);
        et_num4.addTextChangedListener(genraltextWatcher);

    }

    private void CountDownTimerClass() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountDownTimer.setText("(" + millisUntilFinished / 1000 + ")");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                et_num1.setText("");
                et_num2.setText("");
                et_num3.setText("");
                et_num4.setText("");
                llResendCodeConformation.setVisibility(View.GONE);
                btnResendCode.setVisibility(View.VISIBLE);
                btnVerifyCode.setVisibility(View.GONE);


            }

        }.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_resend_code:
                apiCallReSendCode();
                break;

            case R.id.btn_verify_code:

                onDataInput();
                if (strVerifycode.length() == 4) {
                    apiCallForgotVerifyCode();
                } else {
                    Toast.makeText(getActivity(), "Please enter a valid code", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void onDataInput() {
        String num1 = et_num1.getText().toString();
        String num2 = et_num2.getText().toString();
        String num3 = et_num3.getText().toString();
        String num4 = et_num4.getText().toString();

        strVerifycode = num1 + num2 + num3 + num4;


    }


    private TextWatcher genraltextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            onDataInput();
            if (strVerifycode.length()==4){
                apiCallForgotVerifyCode();


            }

            if (et_num1.length() == 1) {

                et_num2.requestFocus();

            }
            if (et_num2.length() == 1) {

                et_num3.requestFocus();

            }
            if (et_num3.length() == 1) {

                et_num4.requestFocus();

            }
        }

    };


    private void apiCallForgotVerifyCode() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient().create(APIServices.class);
        Call<VerifyCodeResponseModel> forgotPasswordResponseModelCall = services.verifyCode(strForgotPasswordEmail, strVerifycode);
        forgotPasswordResponseModelCall.enqueue(new Callback<VerifyCodeResponseModel>() {
            @Override
            public void onResponse(Call<VerifyCodeResponseModel> call, Response<VerifyCodeResponseModel> response) {

                alertDialog.dismiss();
                if (response.message().contains("Bad Request")) {
                    Toast.makeText(getActivity(), "your code is incorrect", Toast.LENGTH_SHORT).show();
                }

                if (response.message().contains("OK")) {
                    GeneralUtills.withOutBackStackConnectFragment(getActivity(), new ResetPasswordFragment());
                    GeneralUtills.putStringValueInEditor(getActivity(), "api_token", response.body().getData().getUser().getToken());
                }

            }

            @Override
            public void onFailure(Call<VerifyCodeResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

    }


    private void apiCallReSendCode() {

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
}
