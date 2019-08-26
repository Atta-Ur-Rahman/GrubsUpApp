package com.techease.grubsup.views.fragments.yourPreferencesFragments;

public class PlanDataModel {
    int img;

    String planTitle, planText, planId;


    public PlanDataModel(int img, String planTitle, String planText, String planId) {
        this.img = img;
        this.planTitle = planTitle;
        this.planText = planText;
        this.planId = planId;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanText() {
        return planText;
    }

    public void setPlanText(String planText) {
        this.planText = planText;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
