package com.kuruvatech.pipelinerecorder;

import android.app.Application;

import java.util.Locale;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setLocale(Locale newLocale) {
        Locale.setDefault(newLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = newLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }


}
