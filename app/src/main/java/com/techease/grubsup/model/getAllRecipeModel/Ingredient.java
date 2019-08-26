
package com.techease.grubsup.model.getAllRecipeModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categoryIngredients")
    @Expose
    private List<CategoryIngredient> categoryIngredients = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryIngredient> getCategoryIngredients() {
        return categoryIngredients;
    }

    public void setCategoryIngredients(List<CategoryIngredient> categoryIngredients) {
        this.categoryIngredients = categoryIngredients;
    }

}
