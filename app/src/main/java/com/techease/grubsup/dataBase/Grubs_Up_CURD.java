package com.techease.grubsup.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.techease.grubsup.model.AllRecipeCategoryModel;
import com.techease.grubsup.model.SpecificShoppingCartIngredientsModel;
import com.techease.grubsup.model.SpecificShoppingCartCategoryModel;
import com.techease.grubsup.model.customAllIngredientsModel.CustomIngredientsModel;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.fragments.shoppinListFragments.SpecificRecipeShoppingCartFragment;

import java.util.ArrayList;
import java.util.List;

import static com.techease.grubsup.views.fragments.shoppinListFragments.AllRecipeShoppingCartFragment.tvTotalIngredientsPrice;

public class Grubs_Up_CURD {


    SQLiteDatabase sqLiteDatabase;
    private Context context;
    private int i = 1;
    boolean returnBoolean = true;





    public Grubs_Up_CURD(Context context) {
        Grups_Up_DataBase contact_dataBase = new Grups_Up_DataBase(context);
        sqLiteDatabase = contact_dataBase.getWritableDatabase();
        this.context = context;

    }


    public void InsertAllRecipeCategory(String categoryID, String categoryName) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ALL_RECIPE_CATEGORY_TABLE WHERE CATEGORY_ID = '" + categoryID + "' ", null);

        if (cursor.moveToFirst()) {
            Log.d("insert_category_already", "Already Exist");
            returnBoolean = false;

        } else {
            ContentValues values = new ContentValues();
            values.put("CATEGORY_ID", categoryID);
            values.put("CATEGORY_NAME", categoryName);
            sqLiteDatabase.insert("ALL_RECIPE_CATEGORY_TABLE", null, values);

            Log.d("insert_category", "inserted");

            returnBoolean = true;
        }
    }


    public boolean InsertDataAllRecipeInCard(String recipeId, String categoryID, String itemID, String itemName, String unitPrice, String quantity, String discount, String thumbnail, String tableName) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM '" + tableName + "' WHERE ITEM_NAME = '" + itemName + "' ", null);


        if (cursor.moveToFirst()) {

//            Toast.makeText(context, "Already Exist ", Toast.LENGTH_SHORT).show();
            Log.d("dataInsertedAlready", "Already Exist");

            returnBoolean = false;


        } else {
            ///here insert new student in database
            ContentValues values = new ContentValues();
            values.put("RECIPE_ID", recipeId);
            values.put("CATEGORY_ID", categoryID);
            values.put("ITEM_ID", itemID);
            values.put("ITEM_NAME", itemName);
            values.put("UNIT_PRICE", unitPrice);
            values.put("UNIT_TOTAL_PRICE", unitPrice);
            values.put("QUANTITY", "1");
            values.put("DISCOUNT", discount);
            values.put("THUMBNAIL", thumbnail);

            sqLiteDatabase.insert(tableName, null, values);

            Log.d("dataInserted", "inserted");

            returnBoolean = true;


        }
        return returnBoolean;
    }


    ///shopping list

    public List<AllRecipeCategoryModel> GetAllRecipeCategory() {


        List<AllRecipeCategoryModel> list = new ArrayList<>();
        String query = "SELECT  * FROM ALL_RECIPE_CATEGORY_TABLE";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                AllRecipeCategoryModel categoryModel = new AllRecipeCategoryModel();
                categoryModel.setCategoryId(cursor.getString(cursor.getColumnIndex("CATEGORY_ID")));
                categoryModel.setName(cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));

                list.add(categoryModel);

            }
            while (cursor.moveToNext());
        }

        return list;


    }


    @SuppressLint("ResourceAsColor")
    public List<CustomIngredientsModel> GetAllShoppingCartSpecificIngredients(Context context, String categoryId) {


        List<CustomIngredientsModel> list = new ArrayList<>();
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ALL_RECIPE_CARD_TABLE WHERE CATEGORY_ID = '" + categoryId + "'", null);

        if (cursor.moveToFirst()) {

            if (cursor.moveToFirst()) {
                do {

                    CustomIngredientsModel customIngredientsModel = new CustomIngredientsModel();
                    customIngredientsModel.setUniqueDataBaseId(cursor.getString(cursor.getColumnIndex("ID")));
                    customIngredientsModel.setRecipeId(cursor.getString(cursor.getColumnIndex("RECIPE_ID")));
                    customIngredientsModel.setItemId(cursor.getString(cursor.getColumnIndex("ITEM_ID")));
                    customIngredientsModel.setItemName(cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                    customIngredientsModel.setUnitPrice(cursor.getString(cursor.getColumnIndex("UNIT_PRICE")));
                    customIngredientsModel.setTotalUnitPrice(cursor.getString(cursor.getColumnIndex("UNIT_TOTAL_PRICE")));
                    customIngredientsModel.setQuantity(cursor.getString(cursor.getColumnIndex("QUANTITY")));
                    customIngredientsModel.setDiscount(cursor.getString(cursor.getColumnIndex("DISCOUNT")));
                    customIngredientsModel.setThumbnail(cursor.getString(cursor.getColumnIndex("THUMBNAIL")));

              list.add(customIngredientsModel);

                } while (cursor.moveToNext());
            }
        } else {
//            Toast.makeText(context, "no exist", Toast.LENGTH_SHORT).show();
        }

        return list;
    }


    ///this insert recipe id is user for if item remove from shopping card hear store recipe id in database
    public void InsertRecipeId(String recipeId) {
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM RECIPE_ID_TABLE WHERE RECIPE_ID = '" + recipeId + "' ", null);

        if (cursor.moveToFirst()) {
            returnBoolean = false;

        } else {
            ContentValues values = new ContentValues();
            values.put("RECIPE_ID", recipeId);
            sqLiteDatabase.insert("RECIPE_ID_TABLE", null, values);
            Log.d("insert_recipeId", "inserted" + "  " + recipeId);


        }
    }


    ///this check is user for shopping card icon red or green
    public boolean CheckRecipeIsShoppingCard(String recipeId) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM RECIPE_ID_TABLE WHERE RECIPE_ID = '" + recipeId + "'", null);

        returnBoolean = false;

        if (cursor.moveToFirst()) {
            returnBoolean = true;

        } else {
            returnBoolean = false;

        }
        return returnBoolean;
    }


    public void InsertSpecificRecipeCategory(String recipeId, String categoryId, String categoryName) {


        @SuppressLint("Recycle") Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM SPECIFIC_RECIPE_CATEGORY_TABLE WHERE RECIPE_ID = '" + recipeId + "' AND CATEGORY_ID = '" + categoryId + "'", null);

        if (cursor.moveToFirst()) {
            Log.d("dataInserted", "Already Exist Specific category");
        } else {
            ///here insert new student in database
            ContentValues values = new ContentValues();
            values.put("RECIPE_ID", recipeId);
            values.put("CATEGORY_ID", categoryId);
            values.put("CATEGORY_NAME", categoryName);
            sqLiteDatabase.insert("SPECIFIC_RECIPE_CATEGORY_TABLE", null, values);

            Log.d("dataInserted", "inserted Specific category");


        }
    }


    public boolean insertSpecificRecipeDataInCard(String recipeId, String categoryID, String itemID, String itemName, String unitPrice, String quantity, String discount, String thumbnail) {


        String query = "SELECT * FROM SPECIFIC_RECIPE_CARD_TABLE WHERE RECIPE_ID = '" + recipeId + "' AND CATEGORY_ID = '" + categoryID + "' AND ITEM_ID = '" + itemID + "'";
        @SuppressLint("Recycle") Cursor cursor = this.sqLiteDatabase.rawQuery(query, null);

        Log.d("itemId", itemID);
        if (cursor.moveToFirst()) {

            Log.d("dataInserted", "Already Exist Specific Item in card");
            returnBoolean = false;

        } else {
            ContentValues values = new ContentValues();
            values.put("RECIPE_ID", recipeId);
            values.put("CATEGORY_ID", categoryID);
            values.put("ITEM_ID", itemID);
            values.put("ITEM_NAME", itemName);
            values.put("UNIT_PRICE", unitPrice);
            values.put("UNIT_TOTAL_PRICE", unitPrice);
            values.put("QUANTITY", "1");
            values.put("DISCOUNT", discount);
            values.put("THUMBNAIL", thumbnail);

            sqLiteDatabase.insert("SPECIFIC_RECIPE_CARD_TABLE", null, values);
            Log.d("dataInserted", "specific item inserted");


            returnBoolean = true;


        }

        return returnBoolean;
    }


    @SuppressLint("ResourceAsColor")
    public List<SpecificShoppingCartCategoryModel> SpecificRecipeCategory(String recipeId) {


        List<SpecificShoppingCartCategoryModel> list = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM SPECIFIC_RECIPE_CATEGORY_TABLE WHERE RECIPE_ID = '" + recipeId + "'", null);

        if (cursor.moveToFirst()) {
            do {

                SpecificShoppingCartCategoryModel specificCategoryModel = new SpecificShoppingCartCategoryModel();
                specificCategoryModel.setCategoryId(cursor.getString(cursor.getColumnIndex("CATEGORY_ID")));
                specificCategoryModel.setName(cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));

                list.add(specificCategoryModel);

            }
            while (cursor.moveToNext());
        }

        return list;
    }

    @SuppressLint("ResourceAsColor")
    public List<SpecificShoppingCartIngredientsModel> GetSpecificShoppingCartCategoryIngredients(Context context, String recipeId, String categoryId) {

        List<SpecificShoppingCartIngredientsModel> list = new ArrayList<>();
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM SPECIFIC_RECIPE_CARD_TABLE WHERE RECIPE_ID = '" + recipeId + "' AND CATEGORY_ID = '" + categoryId + "'", null);

        if (cursor.moveToFirst()) {

            do {
                SpecificShoppingCartIngredientsModel customSpecificIngredientsModel = new SpecificShoppingCartIngredientsModel();
                customSpecificIngredientsModel.setUniqueDataBaseId(cursor.getString(cursor.getColumnIndex("ID")));
                customSpecificIngredientsModel.setRecipeId(cursor.getString(cursor.getColumnIndex("RECIPE_ID")));
                customSpecificIngredientsModel.setCategoryId(cursor.getString(cursor.getColumnIndex("CATEGORY_ID")));
                customSpecificIngredientsModel.setItemId(cursor.getString(cursor.getColumnIndex("ITEM_ID")));
                customSpecificIngredientsModel.setItemName(cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                customSpecificIngredientsModel.setUnitPrice(cursor.getString(cursor.getColumnIndex("UNIT_PRICE")));
                customSpecificIngredientsModel.setTotalUnitPrice(cursor.getString(cursor.getColumnIndex("UNIT_TOTAL_PRICE")));
                customSpecificIngredientsModel.setQuantity(cursor.getString(cursor.getColumnIndex("QUANTITY")));
                customSpecificIngredientsModel.setDiscount(cursor.getString(cursor.getColumnIndex("DISCOUNT")));
                customSpecificIngredientsModel.setThumbnail(cursor.getString(cursor.getColumnIndex("THUMBNAIL")));


                list.add(customSpecificIngredientsModel);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public void DeleteIngredient(String strId) {
        this.sqLiteDatabase.delete("ALL_RECIPE_CARD_TABLE", "ID = '" + strId + "'", null);
    }


    public void DeleteSpecificIngredient(String strId) {
        this.sqLiteDatabase.delete("SPECIFIC_RECIPE_CARD_TABLE", "ID = '" + strId + "'", null);
    }

    public void DeleteRecipeIngredients(String recipeId) {
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ALL_RECIPE_CARD_TABLE WHERE RECIPE_ID= '" + recipeId + "'", null);
        if (cursor.moveToFirst()) {
            this.sqLiteDatabase.delete("ALL_RECIPE_CARD_TABLE", "RECIPE_ID = '" + recipeId + "'", null);
            Log.d("deleted", "recipedeted");
        } else {

//            Toast.makeText(context, "No Exist", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteRecipe(String recipeId) {
        this.sqLiteDatabase.delete("RECIPE_ID_TABLE", "RECIPE_ID = '" + recipeId + "'", null);
    }


    public void ClearAllTables() {

        sqLiteDatabase.execSQL("delete from ALL_RECIPE_CARD_TABLE");
        sqLiteDatabase.execSQL("delete from ALL_RECIPE_CATEGORY_TABLE");
        sqLiteDatabase.execSQL("delete from SPECIFIC_RECIPE_CARD_TABLE");
        sqLiteDatabase.execSQL("delete from SPECIFIC_RECIPE_CATEGORY_TABLE");
        sqLiteDatabase.execSQL("delete from RECIPE_ID_TABLE");

    }


    public void TotalAllShoppingCartUnitPrice() {


        Cursor cursors = sqLiteDatabase.rawQuery(
                "SELECT SUM(UNIT_TOTAL_PRICE) FROM ALL_RECIPE_CARD_TABLE", null);

        if (cursors.moveToFirst()) {
            tvTotalIngredientsPrice.setText(GeneralUtills.FormatterPrice(cursors.getFloat(0)));
            if (GeneralUtills.FormatterPrice(cursors.getDouble(0)).equals("$ .00")) {
                tvTotalIngredientsPrice.setText("$ 00.00");
            }
        }

    }


    public void TotalSpecificIShoppingCartngredientsUnitPrice(String recipeId) {

        /////here sum of total advance
        Cursor cursors = sqLiteDatabase.rawQuery(
                "SELECT SUM(UNIT_TOTAL_PRICE) FROM SPECIFIC_RECIPE_CARD_TABLE WHERE RECIPE_ID = '" + recipeId + "' ", null);
        if (cursors.moveToFirst()) {
            SpecificRecipeShoppingCartFragment.tvTotalIngredientsPrice.setText(GeneralUtills.FormatterPrice(cursors.getFloat(0)));

            if (GeneralUtills.FormatterPrice(cursors.getFloat(0)).equals("$ .00")) {
                SpecificRecipeShoppingCartFragment.tvTotalIngredientsPrice.setText("$ 00.00");
            }

        }
    }


    public void UpdateAllShoppingCartQuantityAndUnitTotalPrice(String strId, String strQuantity, String strUnitTotalPrice) {

        String whereClause = "ID = '" + strId + "'";
        ContentValues values = new ContentValues();
        values.put("QUANTITY", strQuantity);
        values.put("UNIT_TOTAL_PRICE", strUnitTotalPrice);
        this.sqLiteDatabase.update("ALL_RECIPE_CARD_TABLE", values, whereClause, null);
    }

    public void UpdateSpecificShoppingCartQuantityAndUnitTotalPrice(String strId, String strQuantity, String strUnitTotalPrice) {

        String whereClause = "ID = '" + strId + "'";
        ContentValues values = new ContentValues();
        values.put("QUANTITY", strQuantity);
        values.put("UNIT_TOTAL_PRICE", strUnitTotalPrice);
        this.sqLiteDatabase.update("SPECIFIC_RECIPE_CARD_TABLE", values, whereClause, null);
    }


}






