package com.techease.grubsup.networking;

import com.techease.grubsup.model.changePasswordModel.ChangePasswordResponseModel;
import com.techease.grubsup.model.ForgotPasswordResponseModel;
import com.techease.grubsup.model.getAllIngredientsWithOutCategory.GetAllIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeResponseModel;
import com.techease.grubsup.model.getDetailRecipeModel.RecipeDetailResponseModel;
import com.techease.grubsup.model.getFavouriteRecipe.GetFavouriteResponseModel;
import com.techease.grubsup.model.getOrderModel.GetOrderResponseModel;
import com.techease.grubsup.model.getProfileModel.GetProfileResponseModel;
import com.techease.grubsup.model.getSpecificIngredientsWithOutCategory.GetSpecificIngredientsWithOutCategoryResponseModel;
import com.techease.grubsup.model.getSpecificRecipeIngredients.GetSpecificRecipeIngredientsResponseModel;
import com.techease.grubsup.model.login_data_model.LoginResponseModel;
import com.techease.grubsup.model.profileUpdateModel.ProfileUpdateResponseModel;
import com.techease.grubsup.model.shoppingModel.ShoppingResponseModel;
import com.techease.grubsup.model.signUpDataModel.SignUpResponseModel;
import com.techease.grubsup.model.siwpeRecipeModel.SwipeRecipeResponseModel;
import com.techease.grubsup.model.updateRatingModel.UpdateRecipeRatingResponseModel;
import com.techease.grubsup.model.verifyCodeModel.VerifyCodeResponseModel;
import com.techease.grubsup.model.contactUsModel.ContactUsResponseModel;
import com.techease.grubsup.model.deativeAccountModel.DeactiveResponseModel;
import com.techease.grubsup.model.getSuperMarket.GetSuperMarketResponseModel;
import com.techease.grubsup.model.notificationModel.NotificationResponseModel;
import com.techease.grubsup.model.postFavoriteModel.BaseResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by eapple on 29/08/2018.
 */

public interface

APIServices {


    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> userLogin(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("deviceToken") String deviceToken);


    @Multipart
    @POST("register")
    Call<SignUpResponseModel> userSignUp(@Part("name") RequestBody name,
                                         @Part("email") RequestBody email,
                                         @Part("password") RequestBody password,
                                         @Part("confirmPassword") RequestBody passwordConfirmation,
                                         @Part("phoneNumber") RequestBody phoneNumber,
                                         @Part("deviceType") RequestBody deviceType,
                                         @Part("deviceToken") RequestBody deviceToken,
                                         @Part("planId") RequestBody planId,
                                         @Part("adults") RequestBody adults,
                                         @Part("kidsUnder14") RequestBody kidsUder14,
                                         @Part MultipartBody.Part photo,
                                         @Part("profilePicture") RequestBody fileName);

    @GET("all-recipies")
    Call<GetAllRecipeResponseModel> allRecepies();


    @GET("recipe-detail?")
    Call<RecipeDetailResponseModel> recipeDetail(@Query("recipeId") int id);


    @GET("user-profile")
    Call<GetProfileResponseModel> profile();


    @GET("get-favouite-recipies")
    Call<GetFavouriteResponseModel> favourite();


    @FormUrlEncoded
    @POST("update-favouite-recipe-status")
    Call<BaseResponse> favoriteRecipe(@Field("recipeId") String recipeId,
                                      @Field("favouriteStatus") String favoriteStatus);


    @Multipart
    @POST("update-profile-picture")
    Call<ProfileUpdateResponseModel> updateProfilePic(@Part MultipartBody.Part photo,
                                                      @Part("profilePicture") RequestBody fileName);


    @FormUrlEncoded
    @POST("update-profile")
    Call<ProfileUpdateResponseModel> updateProfile(@Field("name") String name,
                                                   @Field("phoneNumber") String phoneNumber,
                                                   @Field("location") String location,
                                                   @Field("postalCode") String postalCode,
                                                   @Field("gender") String gender,
                                                   @Field("adults") String adults,
                                                   @Field("kidsUnder14") String kidsUnder14);


    @FormUrlEncoded
    @POST("update-profile")
    Call<ProfileUpdateResponseModel> updateProfilePlanId(@Field("name") String name,
                                                         @Field("location") String location,
                                                         @Field("postalCode") String postalCode,
                                                         @Field("gender") String gender,
                                                         @Field("adults") String adults,
                                                         @Field("kidsUnder14") String kidsUnder14,
                                                         @Field("planId") String planId);


    @FormUrlEncoded
    @POST("reset-password")
    Call<ForgotPasswordResponseModel> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset-password-verify")
    Call<VerifyCodeResponseModel> verifyCode(@Field("email") String email,
                                             @Field("code") String code);


    @FormUrlEncoded
    @POST("change-password")
    Call<ChangePasswordResponseModel> changePassword(@Field("newPassword") String newPassword,
                                                     @Field("confirmPassword") String confirmPassword);


    @FormUrlEncoded
    @POST("update-notification-status")
    Call<NotificationResponseModel> notificationStatus(@Field("status") String notificationStatus);


    @POST("deactive-account")
    Call<DeactiveResponseModel> deactiveAccount();


    @FormUrlEncoded
    @POST("contact-us")
    Call<ContactUsResponseModel> contactUs(@Field("description") String description);


    @GET("get-super-markets")
    Call<GetSuperMarketResponseModel> getSuperMarket();


    @GET("recipe-ingredients?")
    Call<GetSpecificRecipeIngredientsResponseModel> specificRecipeIngredients(@Query("recipeId") int id);


    @FormUrlEncoded
    @POST("shoping")
    Call<ShoppingResponseModel> shopping(@Field("superMarket") String superMarket,
                                         @Field("postalCode") String strPostalCode,
                                         @Field("shippingAddress") String shoppingAddress,
                                         @Field("items") String items);


    @FormUrlEncoded
    @POST("rating")
    Call<UpdateRecipeRatingResponseModel> updateRecipeRating(@Field("recipeId") int recipeId,
                                                             @Field("rating") float rating);


    @GET("orders")
    Call<GetOrderResponseModel> getMyOrder();


    @GET("ingredients-list")
    Call<GetAllIngredientsWithOutCategoryResponseModel> getSpecificIngredientsWithOutCategory();


    @GET("ingredients-list?")
    Call<GetSpecificIngredientsWithOutCategoryResponseModel> specificRecipeIngredientsWithOutCategory(@Query("recipeId") int id);


    @FormUrlEncoded
    @POST("swipe-or-delete-recipe")
    Call<SwipeRecipeResponseModel> swipeOrDelete(@Field("recipeId") int recipeId, @Field("type") String swipeType);


}
