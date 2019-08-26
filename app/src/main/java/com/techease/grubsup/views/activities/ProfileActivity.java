package com.techease.grubsup.views.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.profileFragments.EditProfileFragment;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        GeneralUtills.withOutBackStackConnectFragment(this, new EditProfileFragment());

    }
}
