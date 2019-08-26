package com.techease.grubsup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class ViewChanger {


    public static Fragment fragmentChanger(Context context, Fragment fragment, int id) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
        return fragment;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Configuation.MY_PREF, 0);
    }

    public static boolean isLogin(Context context){
        return getSharedPreferences(context).getBoolean("isLogin",false);
    }

}