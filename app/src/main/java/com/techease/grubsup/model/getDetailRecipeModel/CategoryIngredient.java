
package com.techease.grubsup.model.getDetailRecipeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryIngredient {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
