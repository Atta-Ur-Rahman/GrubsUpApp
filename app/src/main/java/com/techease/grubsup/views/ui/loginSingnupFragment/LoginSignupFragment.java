package com.techease.grubsup.views.ui.loginSingnupFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.views.activities.PreferencesActivity;
import com.techease.grubsup.views.activities.SignUpActivity;
import com.techease.grubsup.views.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginSignupFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignUp;
    @BindView(R.id.logo)
    ImageView tvAppName;
    @BindView(R.id.login)
    TextView tvWelcome;
    @BindView(R.id.lorem)
    TextView tvLorem;
    private View view;

    public LoginSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login_signup, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_signup:
                startActivity(new Intent(getActivity(), SignUpActivity.class));

        }
    }
}
