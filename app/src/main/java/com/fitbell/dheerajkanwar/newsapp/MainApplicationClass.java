package com.fitbell.dheerajkanwar.newsapp;

import android.app.Activity;
import android.app.Application;

import com.fitbell.dheerajkanwar.newsapp.di.AppModule;
import com.fitbell.dheerajkanwar.newsapp.di.DaggerAppComponent;
import com.fitbell.dheerajkanwar.newsapp.di.NetModule;
import com.fitbell.dheerajkanwar.newsapp.utils.AppConstants;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class MainApplicationClass extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(AppConstants.REST_API_BASE_URL))
                .build().inject(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }


}
