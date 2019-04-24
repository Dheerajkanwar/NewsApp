package com.fitbell.dheerajkanwar.newsapp.viewmodel;

import com.fitbell.dheerajkanwar.newsapp.model.Article;
import com.fitbell.dheerajkanwar.newsapp.model.NewsDetailMain;
import com.fitbell.dheerajkanwar.newsapp.repository.NewsListRepository;
import com.fitbell.dheerajkanwar.newsapp.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Article>> mNewsLiveData = new MutableLiveData<>() ;
    private MutableLiveData<Boolean> mIsDataLoading = new MutableLiveData<>() ;
    private MutableLiveData<String> mMessageInfo = new MutableLiveData<>() ;
    private MutableLiveData<Integer> mAppConnectivityStatus = new MutableLiveData<>() ;

    private NewsListRepository mRepo ;

    private DisposableObserver<NewsDetailMain> disposableObserver ;
    private DisposableObserver<List<Article>> dbDisposableObserver ;

    private ArrayList<Article> articles = null ;
    private boolean isOnCreateCalledOnce = true ;
    private int totalPage, currentPage ;


    @Inject
    public NewsListViewModel(NewsListRepository mRepo) {
        this.mRepo = mRepo;
        init();
    }

    public void init() {
        if(isOnCreateCalledOnce) {
            this.totalPage = 1 ;
            this.currentPage = 1 ;
            if(articles == null) articles = new ArrayList<>();
        }
    }

    public LiveData<Integer> getAppConnectivityStatus() { return mAppConnectivityStatus; }

    public LiveData<String> getMessageInfo() { return mMessageInfo; }

    public LiveData<Boolean> getIsDataLoading() { return mIsDataLoading; }

    public LiveData<ArrayList<Article>> getLatestNews() {
        return mNewsLiveData ;
    }


    //To post internet connectivity back to activity 1 for ONLINE, 2 for OFFLINE
    public void onAppConnectivityChange(boolean status) {
        if (status) {
            mAppConnectivityStatus.postValue(1);
            mMessageInfo.postValue("Internet available");
        } else {
            mAppConnectivityStatus.postValue(2);
            mMessageInfo.postValue("Internet disconnected");
        }
    }

    //To get already loaded list of news articles
    public ArrayList<Article> getArticles() {
        return this.articles ;
    }

    //To get article on card click
    public Article getArticleWithPosition(int position) { return this.articles.get(position); }

    //To check if onCreate was called or not
    public boolean isOnCreateCalledOnce() {
        return isOnCreateCalledOnce;
    }


    //To switch app between Offline and Online Modes
    public void resetVals() {
        this.totalPage = 1 ;
        this.currentPage = 1 ;
        this.articles.clear();
    }


    //To load news from Server
    public void
    loadLatestNewsFromApi() {

        this.isOnCreateCalledOnce = false ;

        if(currentPage <= totalPage) {

            mIsDataLoading.postValue(true);
            disposableObserver = new DisposableObserver<NewsDetailMain>() {

                @Override
                public void onNext(NewsDetailMain newsDetailMain) {
                    currentPage += 1 ;
                    mIsDataLoading.postValue(false);
                    onNewsDataReceivedFromApi(newsDetailMain);
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            };

            mRepo.getNewsFromApi(this.currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .subscribe(disposableObserver);

        }

    }

    //To handle news received from Server
    private void onNewsDataReceivedFromApi(NewsDetailMain newsDetailMain) {

        if(newsDetailMain != null) {

            int totalResultsFound = newsDetailMain.getTotalResults() ;
            if(totalResultsFound != 0){

                //Update the page size and update the list
                this.totalPage = ( totalResultsFound / AppConstants.PAGE_SIZE ) + 1 ;
                this.articles.addAll(newsDetailMain.getArticles());

            }else {

                //Update the list
                this.articles.clear();
                mMessageInfo.postValue("No new article found !");

            }

            mNewsLiveData.postValue(articles);

        }else mMessageInfo.postValue("Oops ! Problem loading news");

    }



    //Load data from Database
    public void loadNewsFromDB() {

        this.isOnCreateCalledOnce = false ;

        mIsDataLoading.postValue(true);
        dbDisposableObserver = new DisposableObserver<List<Article>>() {

            @Override
            public void onNext(List<Article> articles) {
                currentPage += 1 ;
                mIsDataLoading.postValue(false);
                onNewsReceivedFromDb(articles);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        mRepo.getNewsFromDB(AppConstants.PAGE_SIZE, AppConstants.PAGE_SIZE * this.currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dbDisposableObserver);

    }

    //To handle news received from Database
    private void onNewsReceivedFromDb(List<Article> articles) {

        if(articles.size() != 0) this.articles.addAll(articles);
        else {
            this.articles.clear();
            mMessageInfo.postValue("No news stored !");
        }

        mNewsLiveData.postValue((ArrayList<Article>) articles);

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if(null != disposableObserver && !disposableObserver.isDisposed()) disposableObserver.dispose() ;
        if(null != dbDisposableObserver && !dbDisposableObserver.isDisposed()) dbDisposableObserver.dispose() ;
    }


}
