package com.techease.grubsup.dataBase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class Grups_Up_DataBase extends SQLiteOpenHelper {

    private static String DB_NAME = "DB_GRUBS_UP";
    private static int DB_VERSION = 1;


    public Grups_Up_DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE ALL_RECIPE_CARD_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECIPE_ID,CATEGORY_ID,ITEM_ID,ITEM_NAME,UNIT_PRICE,UNIT_TOTAL_PRICE,QUANTITY,DISCOUNT,THUMBNAIL BLOB)";
        String query1 = "CREATE TABLE ALL_RECIPE_CATEGORY_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECIPE_ID,CATEGORY_ID,CATEGORY_NAME)";


        String query2 = "CREATE TABLE SPECIFIC_RECIPE_CARD_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECIPE_ID,CATEGORY_ID,ITEM_ID,ITEM_NAME,UNIT_PRICE,UNIT_TOTAL_PRICE,QUANTITY,DISCOUNT,THUMBNAIL BLOB)";
        String query3 = "CREATE TABLE SPECIFIC_RECIPE_CATEGORY_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECIPE_ID,CATEGORY_ID,CATEGORY_NAME)";

        String query4 = "CREATE TABLE RECIPE_ID_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,RECIPE_ID)";

        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}