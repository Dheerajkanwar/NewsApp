package com.fitbell.dheerajkanwar.newsapp.di;

import com.fitbell.dheerajkanwar.newsapp.MainApplicationClass;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class,NetModule.class,AppModule.class, BuildersModule.class})
public interface AppComponent {

    void inject(MainApplicationClass app);

}
