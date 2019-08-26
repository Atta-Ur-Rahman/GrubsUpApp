package com.techease.grubsup.views.fragments.yourPreferencesFragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.NextWeekRecipeAdapter;
import com.techease.grubsup.dataBase.Grubs_Up_CURD;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeDataModel;
import com.techease.grubsup.model.getAllRecipeModel.GetAllRecipeResponseModel;
import com.techease.grubsup.model.profileUpdateModel.ProfileUpdateResponseModel;
import com.techease.grubsup.model.signUpDataModel.SignUpResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.LoginActivity;
import com.techease.grubsup.views.activities.NavigationTabActivity;
import com.techease.grubsup.views.ui.signup.SignUpFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techease.grubsup.views.activities.NavigationTabActivity.arrayListRecipeId;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strConfirmPassword;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strDeviceToken;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strDeviceType;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strEmailAddress;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strFullName;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strPassword;
import static com.techease.grubsup.views.ui.signup.SignUpFragment.strPhone;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourPreferencesFragment extends Fragment implements View.OnClickListener {

    View parentView;

    @BindView(R.id.btn_subscription)
    Button btnSubscription;

    @BindView(R.id.iv_up_arrow_adult_servings)
    ImageView ivAdultUpArrow;
    @BindView(R.id.iv_down_arrow_adult_servings)
    ImageView ivAdultDownArrow;

    @BindView(R.id.iv_up_arrow_child_servings)
    ImageView ivChildUpArrow;
    @BindView(R.id.iv_down_arrow_child_servings)
    ImageView ivChildDownArrow;


    @BindView(R.id.tv_adult_servings)
    TextView tvAdultServings;
    @BindView(R.id.tv_child_servings)
    TextView tvChildServings;

/*
    @BindView(R.id.ll_circle_bargain)
    LinearLayout llBargain;
    @BindView(R.id.ll_circle_family)
    LinearLayout llFamily;
    @BindView(R.id.ll_circle_ready)
    LinearLayout llReady;


    @BindView(R.id.iv_checked_bargain)
    ImageView ivCheckedBargain;
    @BindView(R.id.iv_checked_family)
    ImageView ivCheckedFamily;
    @BindView(R.id.iv_checked_ready)
    ImageView ivCheckedReady;


    @BindView(R.id.tv_bargain_learn_more)
    TextView tvBargain;
    @BindView(R.id.tv_family_favourite_learn_more)
    TextView tvFavouriteFamily;
    @BindView(R.id.tv_ready_learn_more)
    TextView tvReadyLearnMore;


    @BindView(R.id.ll_circle_weight_loss)
    LinearLayout llCircleWeightLoss;
    @BindView(R.id.tv_weight_loss_learn_more)
    TextView tvWeightLossLearMore;
    @BindView(R.id.iv_checked_weight_loss)
    ImageView ivCheckedWeightLoss;


    @BindView(R.id.ll_circle_vegetarian)
    LinearLayout llVegetarian;
    @BindView(R.id.tv_vegetarian_learn_more)
    TextView tvVegetarianLearnMore;
    @BindView(R.id.iv_checked__vegetarian)
    ImageView ivCheckedVegetarian;*/


    int anIntAdultServings = 1;
    int anIntChildServings = 1;


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_preference_title)
    TextView tvPreferenceTitle;

    @BindView(R.id.ll_family_size)
    LinearLayout llFamilySize;

    Dialog alertDialog;
    String strPlanId, strPlanIdBackLearnMore;


    ArrayList<PlanDataModel> planArrayList = new ArrayList<>();

    ArrayList<NextWeeKDataModel> nextWeeKDataModelArrayList = new ArrayList<>();

    @BindView(R.id.rv_dinner_plan)
    RecyclerView rvDinnerPlan;

    PlanItemAdapter planItemAdapter;


    List<GetAllRecipeDataModel> allRecipeDataModelList = new ArrayList<>();
    NextWeekRecipeAdapter nextWeekRecipeAdapter;

    @BindView(R.id.rv_next_week_recipe)
    RecyclerView rvNextWeekRecipe;

    LinearLayoutManager linearLayoutManager;

    Grubs_Up_CURD grubs_up_curd;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_your_preferences, container, false);

        ButterKnife.bind(this, parentView);
        grubs_up_curd = new Grubs_Up_CURD(getActivity());

        alertDialog = AlertUtils.createProgressDialog(getActivity());
        plan();
        iniLiseters();


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvDinnerPlan.setLayoutManager(linearLayoutManager);


        rvNextWeekRecipe.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        planArrayList.add(new PlanDataModel(R.drawable.family_favourties, "Family Favourites", "Classic meals the whole family will love.", "2"));
        planArrayList.add(new PlanDataModel(R.drawable.weight_loss, "Weight Loss", "Yummy, easy food under 500 calories per portion", "4"));
        planArrayList.add(new PlanDataModel(R.drawable.bargain, "Bargain", "Feed a family for $120 a week", "1"));
        planArrayList.add(new PlanDataModel(R.drawable.ready_in_30, "Ready in 30", "Prepped, cooked and served in under 30 minutes", "3"));
        planArrayList.add(new PlanDataModel(R.drawable.plant, "Plant Based", "Scrummy vegeterian meals that_s good for the planet and for you ", "5"));

        planItemAdapter = new PlanItemAdapter(getContext(), planArrayList, rvDinnerPlan);

        rvDinnerPlan.setAdapter(planItemAdapter);

        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvDinnerPlan);


        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain1));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain2));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain3));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain4));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain5));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain6));
        nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain7));

        nextWeekRecipeAdapter = new NextWeekRecipeAdapter(getActivity(), nextWeeKDataModelArrayList);
        rvNextWeekRecipe.setAdapter(nextWeekRecipeAdapter);
//        getReciepes();


        strPlanId = "1";

        rvDinnerPlan.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(centerView);

                    strPlanId = planArrayList.get(pos).getPlanId();

                    changeNextWeekRecipe(strPlanId);
                    Log.e("Snapped Item Position:", "" + strPlanId);
                }
            }
        });


        if (GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("adultsSize", "").equals("")) {
        } else {

            tvAdultServings.setText(GeneralUtills.FormatterPeopleSize(Integer.parseInt(GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("adultsSize", ""))));
            tvChildServings.setText(GeneralUtills.FormatterPeopleSize(Integer.parseInt(GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getString("kidsUnder14Size", ""))));
        }

        if (GeneralUtills.getSharedPreferences(getActivity()).getBoolean("isSetting", false)) {
            tvPreferenceTitle.setText(getString(R.string.change_preference));
            llFamilySize.setVisibility(View.GONE);
        }
        return parentView;
    }

    private void changeNextWeekRecipe(String planId) {


        if (planId.equals("1")) {

            nextWeeKDataModelArrayList.clear();
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain1));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain2));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain3));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain4));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain5));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain6));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.bargain7));
            nextWeekRecipeAdapter.notifyDataSetChanged();


        }

        if (planId.equals("4")) {
            nextWeeKDataModelArrayList.clear();
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss1));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss2));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss3));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss4));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss5));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss6));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.weightloss7));
            nextWeekRecipeAdapter.notifyDataSetChanged();

        }

        if (planId.equals("2")) {
            nextWeeKDataModelArrayList.clear();
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite1));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite2));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite3));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite4));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite5));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite6));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.family_favourite7));
            nextWeekRecipeAdapter.notifyDataSetChanged();
        }

        if (planId.equals("3")) {
            nextWeeKDataModelArrayList.clear();
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.ready1));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.ready2));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.ready3));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.ready4));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.ready5));
            nextWeekRecipeAdapter.notifyDataSetChanged();
        }


        if (planId.equals("5")) {
            nextWeeKDataModelArrayList.clear();
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant1));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant2));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant3));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant4));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant5));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant6));
            nextWeeKDataModelArrayList.add(new NextWeeKDataModel(R.drawable.plant7));
            nextWeekRecipeAdapter.notifyDataSetChanged();

        }
    }

    private void plan() {
      /*  strPlanId = GeneralUtills.getSharedPreferences(getActivity()).getString("planId", "");
        strPlanIdBackLearnMore = GeneralUtills.getSharedPreferences(getActivity()).getString("plan", "");

        assert strPlanIdBackLearnMore != null;
        if (strPlanIdBackLearnMore.length() == 0) {
            planChecked();
        } else {
            strPlanId = strPlanIdBackLearnMore;
            GeneralUtills.putStringValueInEditor(getActivity(), "plan", "");
            planChecked();
        }*/
    }

    private void iniLiseters() {
        btnSubscription.setOnClickListener(this);

        ivAdultUpArrow.setOnClickListener(this);
        ivAdultDownArrow.setOnClickListener(this);

        ivChildUpArrow.setOnClickListener(this);
        ivChildDownArrow.setOnClickListener(this);
        ivBack.setOnClickListener(this);

     /*   llBargain.setOnClickListener(this);
        llFamily.setOnClickListener(this);
        llReady.setOnClickListener(this);
        llCircleWeightLoss.setOnClickListener(this);
        llVegetarian.setOnClickListener(this);
        ivBack.setOnClickListener(this);


        tvBargain.setOnClickListener(this);
        tvFavouriteFamily.setOnClickListener(this);
        tvReadyLearnMore.setOnClickListener(this);
        tvWeightLossLearMore.setOnClickListener(this);
        tvVegetarianLearnMore.setOnClickListener(this);*/

    }

 /*   private void planChecked() {

        if (strPlanId.equals("1")) {
            ivCheckedBargain.setVisibility(View.VISIBLE);
        } else if (strPlanId.equals("2")) {
            ivCheckedFamily.setVisibility(View.VISIBLE);
        } else if (strPlanId.equals("3")) {
            ivCheckedReady.setVisibility(View.VISIBLE);
        } else if (strPlanId.equals("4")) {
            ivCheckedWeightLoss.setVisibility(View.VISIBLE);
        } else if (strPlanId.equals("5")) {
            ivCheckedVegetarian.setVisibility(View.VISIBLE);
        }

    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.btn_subscription:
/*
                if (ivCheckedBargain.getVisibility() == View.VISIBLE) {
                    strPlanId = "1";
                    apiCallRegistration();
                } else if (ivCheckedFamily.getVisibility() == View.VISIBLE) {
                    strPlanId = "2";
                    apiCallRegistration();
                } else if (ivCheckedReady.getVisibility() == View.VISIBLE) {
                    strPlanId = "3";
                    apiCallRegistration();
                } else if (ivCheckedWeightLoss.getVisibility() == View.VISIBLE) {
                    strPlanId = "4";
                    apiCallRegistration();
                } else if (ivCheckedVegetarian.getVisibility() == View.VISIBLE) {
                    strPlanId = "5";
                    apiCallRegistration();
                } else {
                    Toast.makeText(getActivity(), "please select your plan", Toast.LENGTH_SHORT).show();
                }*/


                apiCallRegistration();


                break;

            case R.id.iv_up_arrow_adult_servings:


                anIntAdultServings = anIntAdultServings + 1;
                tvAdultServings.setText(GeneralUtills.FormatterPeopleSize(anIntAdultServings));

                break;
            case R.id.iv_down_arrow_adult_servings:

                if (anIntAdultServings > 1) {
                    anIntAdultServings = anIntAdultServings - 1;
                    tvAdultServings.setText(GeneralUtills.FormatterPeopleSize(anIntAdultServings));
                }
                break;

            case R.id.iv_up_arrow_child_servings:

                anIntChildServings = anIntChildServings + 1;
                tvChildServings.setText(GeneralUtills.FormatterPeopleSize(anIntChildServings));

                break;

            case R.id.iv_down_arrow_child_servings:
                if (anIntChildServings > 1) {
                    anIntChildServings = anIntChildServings - 1;
                    tvChildServings.setText(GeneralUtills.FormatterPeopleSize(anIntChildServings));
                }
//
//                break;
//            case R.id.ll_circle_bargain:
//                ivCheckedBargain.setVisibility(View.VISIBLE);
//                ivCheckedFamily.setVisibility(View.GONE);
//                ivCheckedReady.setVisibility(View.GONE);
//                ivCheckedWeightLoss.setVisibility(View.GONE);
//                ivCheckedVegetarian.setVisibility(View.GONE);
//
//                strPlanId = "1";
//                break;
//            case R.id.ll_circle_family:
//                ivCheckedBargain.setVisibility(View.GONE);
//                ivCheckedFamily.setVisibility(View.VISIBLE);
//                ivCheckedReady.setVisibility(View.GONE);
//                ivCheckedWeightLoss.setVisibility(View.GONE);
//                ivCheckedVegetarian.setVisibility(View.GONE);
//                strPlanId = "1";
//
//                break;
//            case R.id.ll_circle_ready:
//                ivCheckedBargain.setVisibility(View.GONE);
//                ivCheckedFamily.setVisibility(View.GONE);
//                ivCheckedReady.setVisibility(View.VISIBLE);
//                ivCheckedWeightLoss.setVisibility(View.GONE);
//                ivCheckedVegetarian.setVisibility(View.GONE);
//                strPlanId = "1";
//
//                break;
//
//            case R.id.ll_circle_weight_loss:
//
//                ivCheckedBargain.setVisibility(View.GONE);
//                ivCheckedFamily.setVisibility(View.GONE);
//                ivCheckedReady.setVisibility(View.GONE);
//                ivCheckedWeightLoss.setVisibility(View.VISIBLE);
//                ivCheckedVegetarian.setVisibility(View.GONE);
//                strPlanId = "1";
//
//
//                break;
//
//            case R.id.ll_circle_vegetarian:
//
//                ivCheckedBargain.setVisibility(View.GONE);
//                ivCheckedFamily.setVisibility(View.GONE);
//                ivCheckedReady.setVisibility(View.GONE);
//                ivCheckedWeightLoss.setVisibility(View.GONE);
//                ivCheckedVegetarian.setVisibility(View.VISIBLE);
//                strPlanId = "1";
//
//                break;
/*

            case R.id.tv_bargain_learn_more:
                GeneralUtills.connectFragment(getActivity(), new BargainFragment());
                break;
            case R.id.tv_family_favourite_learn_more:
                GeneralUtills.connectFragment(getActivity(), new FamilyFavouriteFragment());
                break;
            case R.id.tv_ready_learn_more:
                GeneralUtills.connectFragment(getActivity(), new ReadyIn30Fragment());
                break;

            case R.id.tv_weight_loss_learn_more:
                GeneralUtills.connectFragment(getActivity(), new WeightLossFragment());
                break;

            case R.id.tv_vegetarian_learn_more:
                GeneralUtills.connectFragment(getActivity(), new VegetarianFragment());
                break;

*/

        }
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void apiCallRegistration() {

        if (GeneralUtills.getSharedPreferences(Objects.requireNonNull(getActivity())).getBoolean("setting_screen", false)) {
            updateProfilePlanId();
        } else {
            SignUpRegistration();
        }


    }

    private void SignUpRegistration() {


        alertDialog.show();
        strDeviceType = "AndroidMobile";
        APIServices services = APIClient.getApiClient().create(APIServices.class);

        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), strFullName);
        RequestBody emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), strEmailAddress);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("multipart/form-data"), strPassword);
        RequestBody confirmPasswordBody = RequestBody.create(MediaType.parse("multipart/form-data"), strConfirmPassword);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("multipart/form-data"), strPhone);
        RequestBody deviceToken = RequestBody.create(MediaType.parse("multipart/form-data"), strDeviceToken);
        RequestBody deviceTypeBody = RequestBody.create(MediaType.parse("multipart/form-data"), strDeviceType);
        RequestBody planId = RequestBody.create(MediaType.parse("multipart/form-data"), strPlanId);
        RequestBody adults = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(anIntAdultServings));
        RequestBody kidsUnder14 = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(anIntChildServings));


        final Call<SignUpResponseModel> registration = services.userSignUp(nameBody, emailBody, passwordBody, confirmPasswordBody, phoneBody, deviceTypeBody, deviceToken, planId, adults, kidsUnder14, null, null);
        registration.enqueue(new Callback<SignUpResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                alertDialog.dismiss();
                if (response.body() == null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getStatus()) {
                    alertDialog.dismiss();

                    if (SignUpFragment.isRemember) {
                        GeneralUtills.putBooleanValueInEditor(getContext(), "isLogin", true);
                    }
                    Toast.makeText(getActivity(), "registration successfull", Toast.LENGTH_SHORT).show();
                    GeneralUtills.putStringValueInEditor(getActivity(), "api_token", response.body().getData().getToken());
                    GeneralUtills.putStringValueInEditor(getActivity(), "planId", response.body().getData().getPlanId());
                    GeneralUtills.putStringValueInEditor(getActivity(), "adultsSize", String.valueOf(response.body().getData().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "kidsUnder14Size", String.valueOf(response.body().getData().getKidsUnder14()));

                    Objects.requireNonNull(getActivity()).finishAffinity();
                    startActivity(new Intent(getActivity(), NavigationTabActivity.class));

                }

            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error", t.getMessage());
                alertDialog.dismiss();

            }
        });

    }


    private void updateProfilePlanId() {
        alertDialog.show();
        Call<ProfileUpdateResponseModel> updateResponse = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity()))
                .updateProfilePlanId(GeneralUtills.getSharedPreferences(getActivity()).getString("name", ""),
                        GeneralUtills.getSharedPreferences(getActivity()).getString("location", ""),
                        GeneralUtills.getSharedPreferences(getActivity()).getString("postal_code", ""),
                        GeneralUtills.getSharedPreferences(getActivity()).getString("gender", ""),
                        tvAdultServings.getText().toString(),
                        tvChildServings.getText().toString(),
                        strPlanId);
        updateResponse.enqueue(new Callback<ProfileUpdateResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ProfileUpdateResponseModel> call, Response<ProfileUpdateResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    grubs_up_curd.ClearAllTables();

                    Toast.makeText(getActivity(), "your plan has been changed", Toast.LENGTH_SHORT).show();
                    GeneralUtills.putStringValueInEditor(getActivity(), "planId", strPlanId);
                    assert response.body() != null;
                    GeneralUtills.putStringValueInEditor(getActivity(), "adultsSize", String.valueOf(response.body().getData().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "kidsUnder14Size", String.valueOf(response.body().getData().getKidsUnder14()));
                    GeneralUtills.hideKeyboardFrom(Objects.requireNonNull(getActivity()), parentView);
                    GeneralUtills.hideKeyboard(getActivity());
                    getActivity().startActivity(new Intent(getActivity(), NavigationTabActivity.class));

                } else {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponseModel> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
    }


    private void getReciepes() {

        alertDialog.show();
        arrayListRecipeId.clear();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<GetAllRecipeResponseModel> getRecipiesResponseModelCall = services.allRecepies();
        getRecipiesResponseModelCall.enqueue(new Callback<GetAllRecipeResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<GetAllRecipeResponseModel> call, Response<GetAllRecipeResponseModel> response) {


                Log.d("response", response.message());
                if (response.isSuccessful()) {
                    alertDialog.dismiss();
                    assert response.body() != null;


                    nextWeekRecipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetAllRecipeResponseModel> call, Throwable t) {

                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();

                Log.d("errorAllRecipe", String.valueOf(t));


            }
        });
    }
}

