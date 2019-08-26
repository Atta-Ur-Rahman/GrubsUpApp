
package com.techease.grubsup.model.getOrderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("quantity")
    @Expose
    private Object quantity;
    @SerializedName("brand")
    @Expose
    private Object brand;
    @SerializedName("size")
    @Expose
    private Object size;
    @SerializedName("frequency")
    @Expose
    private Object frequency;
    @SerializedName("customItem")
    @Expose
    private String customItem;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shoping_id")
    @Expose
    private String shopingId;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Object getQuantity() {
        return quantity;
    }

    public void setQuantity(Object quantity) {
        this.quantity = quantity;
    }

    public Object getBrand() {
        return brand;
    }

    public void setBrand(Object brand) {
        this.brand = brand;
    }

    public Object getSize() {
        return size;
    }

    public void setSize(Object size) {
        this.size = size;
    }

    public Object getFrequency() {
        return frequency;
    }

    public void setFrequency(Object frequency) {
        this.frequency = frequency;
    }

    public String getCustomItem() {
        return customItem;
    }

    public void setCustomItem(String customItem) {
        this.customItem = customItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopingId() {
        return shopingId;
    }

    public void setShopingId(String shopingId) {
        this.shopingId = shopingId;
    }

}
