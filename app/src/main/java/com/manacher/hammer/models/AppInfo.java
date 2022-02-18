package com.manacher.hammer.models;

public class AppInfo {

    private boolean adsActive;
    private int defaultChips;


    public AppInfo() {
    }

    public boolean isAdsActive() {
        return adsActive;
    }

    public void setAdsActive(boolean adsActive) {
        this.adsActive = adsActive;
    }

    public int getDefaultChips() {
        return defaultChips;
    }

    public void setDefaultChips(int defaultChips) {
        this.defaultChips = defaultChips;
    }
}
