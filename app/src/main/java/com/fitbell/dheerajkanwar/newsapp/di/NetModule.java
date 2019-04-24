package com.fitbell.dheerajkanwar.newsapp.di;

import android.app.Application;

import com.fitbell.dheerajkanwar.newsapp.source.api.RestApiInterface;
import com.fitbell.dheerajkanwar.newsapp.utils.CommonUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetModule {

    private final String baseUrl ;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl ;
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient() ;
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public RestApiInterface provideRestApiInterface(Retrofit retrofit) {
        return retrofit.create(RestApiInterface.class);
    }

    @Singleton
    @Provides
    public CommonUtils provideCommonUtils(Application application) {
        return new CommonUtils(application);
    }

}
