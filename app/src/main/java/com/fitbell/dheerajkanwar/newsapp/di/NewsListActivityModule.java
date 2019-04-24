package com.fitbell.dheerajkanwar.newsapp.di;

import com.fitbell.dheerajkanwar.newsapp.view.NewsListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NewsListActivityModule {

    @ContributesAndroidInjector
    abstract NewsListActivity contributeActivityInjector();

}
