package com.techease.grubsup.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techease.grubsup.R;

import java.text.DecimalFormat;
import java.util.List;

public class GeneralUtills {


    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static ImageView imageView;
    public static LinearLayout linearLayout;
    public static TextView textView;


    public static Fragment connectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("true").commit();
        return fragment;
    }

    public static Fragment withOutBackStackConnectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        return fragment;
    }

    public static SharedPreferences.Editor putStringValueInEditor(Context context, String key, String value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putIntegerValueInEditor(Context context, String key, int value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putInt(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putBooleanValueInEditor(Context context, String key, boolean value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
        return editor;
    }


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Config.MY_PREF, 0);
    }

    public static int getItemID(Context context) {
        return getSharedPreferences(context).getInt("itemID", 0);
    }

    public static String getApiToken(Context context) {
        return getSharedPreferences(context).getString("api_token", "");
    }

    public static String getForgotEmail(Context context) {
        return getSharedPreferences(context).getString("forgot_email", "");
    }

    public static String getLat(Context context) {
        return getSharedPreferences(context).getString("latitude", "");
    }

    public static String getLng(Context context) {
        return getSharedPreferences(context).getString("longitude", "");
    }

    public static String latitude(Context context) {
        return getSharedPreferences(context).getString("resturant_latitude", "");
    }

    public static String longitude(Context context) {
        return getSharedPreferences(context).getString("resturant_longitude", "");
    }

    public static String getName(Context context) {
        return getSharedPreferences(context).getString("name", "");
    }

    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString("email", "");
    }

    public static boolean isLogin(Context context) {
        return getSharedPreferences(context).getBoolean("isLogin", false);
    }

    public static String getLocation(Context context) {
        return getSharedPreferences(context).getString("location", "");
    }

    public static String getItemName(Context context) {
        return getSharedPreferences(context).getString("item_name", "");
    }

    public static String getItemCalories(Context context) {
        return getSharedPreferences(context).getString("calories", "");
    }

    public static String getPublished(Context context) {
        return getSharedPreferences(context).getString("published", "");
    }

    public static String getImageLink(Context context) {
        return getSharedPreferences(context).getString("item_image", "");
    }

    public static String getFacebookImage(Context context) {
        return getSharedPreferences(context).getString("facebook_profile", "");
    }

    public static void grantPermission(Activity context) {
        Dexter.withActivity(context)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public static String FormatterPrice(double aDoubleTotal) {
        DecimalFormat formatter = new DecimalFormat("$ #,###.00");
        String yourFormattedString = formatter.format(aDoubleTotal);

        return yourFormattedString;
    }

    public static String FormatterPeopleSize(int i) {

        String s = String.format ("%02d",i);



        return s;
    }


    public static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

//    public static void setArrayPrefs(String arrayName, ArrayList<String> array, Context mContext) {
//        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(arrayName +"_size", array.size());
//        for(int i=0;i<array.size();i++)
//            editor.putString(arrayName + "_" + i, array.get(i));
//        editor.apply();
//    }
//    public static ArrayList<String> getArrayPrefs(String arrayName, Context mContext) {
//        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
//        int size = prefs.getInt(arrayName + "_size", 0);
//        ArrayList<String> array = new ArrayList<>(size);
//        for(int i=0;i<size;i++)
//            array.add(prefs.getString(arrayName + "_" + i, null));
//        return array;
//    }
}
