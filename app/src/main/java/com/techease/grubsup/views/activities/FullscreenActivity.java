package com.techease.grubsup.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.utils.ViewChanger;
import com.techease.grubsup.views.ui.loginSingnupFragment.LoginSignupFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    Handler handler = new Handler();
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (GeneralUtills.isLogin(this)) {
//            ViewChanger.fragmentChanger(this, new YourPreferencesFragment(), R.id.container);

            startActivity(new Intent(this, NavigationTabActivity.class));

        } else {
            ViewChanger.fragmentChanger(this, new LoginSignupFragment(), R.id.container);
        }
        getSupportActionBar().hide();
        tvTitle = findViewById(R.id.title);

//        runner();
    }
//    private void runner(){
//        handler = new Handler();
//        final Runnable r = new Runnable() {
//            public void run() {
////                if (SharedPrefUtils.isLoggedIn(SplashScreenActivity.this)){
////                    startActivity(new Intent(SplashScreenActivity.this, BottomNavigationActivity.class));
////                }else {
////                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
////                }
//                finish();
//
//            }
//        };
//        handler.postDelayed(r, 3000);
//    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
//    }
//
//    private void hideSystemUI() {
//        // Enables regular immersive mode.
//        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
//        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE
//                        // Set the content to appear under the system bars so that the
//                        // content doesn't resize when the system bars hide and show.
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }
//
//    // Shows the system bars by removing all the flags
//// except for the ones that make the content appear under the system bars.
//    private void showSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }
}
