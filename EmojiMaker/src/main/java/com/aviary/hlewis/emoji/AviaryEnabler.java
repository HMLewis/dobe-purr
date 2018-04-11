package com.aviary.hlewis.emoji;

import android.support.multidex.MultiDexApplication;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;

public class AviaryEnabler extends MultiDexApplication implements IAviaryClientCredentials {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return "0f87bed93d03425b877ffbf6b09223d1";
    }

    @Override
    public String getClientSecret() {
        return "4397617d-4420-4f42-b2cf-6b00980a5ed4";
    }
}
