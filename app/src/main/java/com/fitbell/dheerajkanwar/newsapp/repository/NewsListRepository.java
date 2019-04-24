package com.fitbell.dheerajkanwar.newsapp.repository;

import com.fitbell.dheerajkanwar.newsapp.model.Article;
import com.fitbell.dheerajkanwar.newsapp.model.NewsDetailMain;
import com.fitbell.dheerajkanwar.newsapp.source.api.RestApiInterface;
import com.fitbell.dheerajkanwar.newsapp.source.db.NewsArticleDao;
import com.fitbell.dheerajkanwar.newsapp.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public class NewsListRepository {

    private RestApiInterface restApiInterface ;
    private NewsArticleDao newsArticleDao ;

    @Inject
    public NewsListRepository(RestApiInterface restApiInterface, NewsArticleDao newsArticleDao) {
        this.restApiInterface = restApiInterface;
        this.newsArticleDao = newsArticleDao ;
    }


    public Observable<NewsDetailMain> getNewsFromApi(int page) {

        return restApiInterface.getLatestNews("top-headlines?country=us&apiKey=" + AppConstants.API_KEY + "&pageSize=" + AppConstants.PAGE_SIZE + "&page=" + page)
                .doOnNext(new Consumer<NewsDetailMain>() {
                    @Override
                    public void accept(NewsDetailMain newsDetailMain) throws Exception {
                        if(newsDetailMain != null) {
                            List<Article> articleList = newsDetailMain.getArticles() ;

                            if(articleList != null) {
                                for(Article article : articleList) newsArticleDao.inserArticle(article);
                            }
                        }
                    }
                });

    }

    public Observable<List<Article>> getNewsFromDB(int limit, int offset) {
        return newsArticleDao.getNewsArticles(limit, offset).toObservable();
    }


}
