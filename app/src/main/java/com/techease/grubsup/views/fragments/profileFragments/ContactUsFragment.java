package com.techease.grubsup.views.fragments.profileFragments;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.model.contactUsModel.ContactUsResponseModel;
import com.techease.grubsup.model.notificationModel.NotificationResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_contact_us_email)
    TextView tvEmail;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.btn_contact_us_send)
    Button btnSend;
    Dialog alertDialog;

    String strDescription;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, parentView);

        alertDialog = AlertUtils.createProgressDialog(getActivity());

        ivBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        tvEmail.setText(GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("email",""));


        return parentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

                break;
            case R.id.btn_contact_us_send:

                strDescription = etDescription.getText().toString();

                if (strDescription.isEmpty() || strDescription.length() < 5) {
                    etDescription.setError("write your description");
                } else {
                    apiCallContactUs();
                }

                break;
        }
    }

    private void apiCallContactUs() {


        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<ContactUsResponseModel> call = services.contactUs(strDescription);
        call.enqueue(new Callback<ContactUsResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ContactUsResponseModel> call, Response<ContactUsResponseModel> response) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(response.body().getMessage()), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    etDescription.setText("");
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }
}

