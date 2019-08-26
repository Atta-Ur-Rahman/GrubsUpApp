
package com.techease.grubsup.model.getSpecificRecipeIngredients;

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
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("unitPrice")
    @Expose
    private String unitPrice;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}
