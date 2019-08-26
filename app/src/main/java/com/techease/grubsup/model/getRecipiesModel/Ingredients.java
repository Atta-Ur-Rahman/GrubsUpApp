package com.techease.grubsup.model.getRecipiesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredients {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("recipe_id")
    @Expose
    private String recipeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
