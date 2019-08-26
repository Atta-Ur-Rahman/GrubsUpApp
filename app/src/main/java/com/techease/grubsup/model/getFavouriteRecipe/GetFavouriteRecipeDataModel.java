
package com.techease.grubsup.model.getFavouriteRecipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFavouriteRecipeDataModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("isFavourite")
    @Expose
    private Boolean isFavourite;
    @SerializedName("averageRating")
    @Expose
    private Integer averageRating;
    @SerializedName("userRatingStatus")
    @Expose
    private Boolean userRatingStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    public Boolean getUserRatingStatus() {
        return userRatingStatus;
    }

    public void setUserRatingStatus(Boolean userRatingStatus) {
        this.userRatingStatus = userRatingStatus;
    }

}
