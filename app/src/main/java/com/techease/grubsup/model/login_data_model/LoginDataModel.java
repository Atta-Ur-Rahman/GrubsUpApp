
package com.techease.grubsup.model.login_data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDataModel {

    @SerializedName("user")
    @Expose
    private LoginUserModel user;

    public LoginUserModel getUser() {
        return user;
    }

    public void setUser(LoginUserModel user) {
        this.user = user;
    }

}
