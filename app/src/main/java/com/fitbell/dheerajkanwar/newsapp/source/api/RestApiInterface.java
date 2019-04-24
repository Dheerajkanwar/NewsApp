package com.fitbell.dheerajkanwar.newsapp.source.api;



import com.fitbell.dheerajkanwar.newsapp.model.NewsDetailMain;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RestApiInterface {

    @GET
    Observable<NewsDetailMain> getLatestNews(@Url String url);

}


