package com.fitbell.dheerajkanwar.newsapp.di;

import android.app.Application;

import com.fitbell.dheerajkanwar.newsapp.repository.NewsListRepository;
import com.fitbell.dheerajkanwar.newsapp.source.api.RestApiInterface;
import com.fitbell.dheerajkanwar.newsapp.source.db.NewsArticleDao;
import com.fitbell.dheerajkanwar.newsapp.source.db.NewsArticleDatabase;
import com.fitbell.dheerajkanwar.newsapp.utils.AppConstants;
import com.fitbell.dheerajkanwar.newsapp.viewmodel.NewsListViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application app ;

    public AppModule(Application app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return app ;
    }

    @Singleton
    @Provides
    public NewsArticleDatabase provideNewsArticleDatabase(Application app) {
        return Room.databaseBuilder(
                app,
                NewsArticleDatabase.class,
                AppConstants.DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    public NewsArticleDao provideNewsArticleDao(NewsArticleDatabase database) {
        return database.getNewsArticleDao() ;
    }

    @Singleton
    @Provides
    public NewsListRepository provideNewsListRepository(RestApiInterface restApiInterface, NewsArticleDao newsArticleDao) {
        return new NewsListRepository(restApiInterface, newsArticleDao);
    }


    @Singleton
    @Provides
    public ViewModelProvider.Factory provideNewsViewModelFactory(NewsListRepository repository) {
        return new NewsListViewModelFactory(repository);
    }

}
