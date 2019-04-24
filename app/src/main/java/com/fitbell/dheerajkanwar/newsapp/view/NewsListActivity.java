package com.fitbell.dheerajkanwar.newsapp.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.fitbell.dheerajkanwar.newsapp.R;
import com.fitbell.dheerajkanwar.newsapp.utils.RecyclerItemClickListener;
import com.fitbell.dheerajkanwar.newsapp.adapter.NewsCardAdapter;
import com.fitbell.dheerajkanwar.newsapp.databinding.NewsListActivityLayoutBinding;
import com.fitbell.dheerajkanwar.newsapp.model.Article;
import com.fitbell.dheerajkanwar.newsapp.utils.CommonUtils;
import com.fitbell.dheerajkanwar.newsapp.viewmodel.NewsListViewModel;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class NewsListActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory ;
    @Inject
    CommonUtils commonUtils ;

    private NewsListActivityLayoutBinding mBinding ;
    private NewsListViewModel mViewModel ;
    private NewsCardAdapter newsCardAdapter ;
    private RecyclerView recyclerNewsList ;
    private LinearLayoutManager layoutManager ;
    private ProgressBar progressBar ;

    private Disposable internetDisposable ;

    private boolean isLoadingPerformed = false ;
    private int lastVisibleItem, totalItemCount, appConnectivityStatus ;
    private final int VISIBLE_THRESHOLD = 1, APP_ONLINE = 1, APP_OFFLINE = 2 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        mBinding = DataBindingUtil.setContentView(NewsListActivity.this, R.layout.news_list_activity_layout);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsListViewModel.class);
        mViewModel.init();
        init();

    }

    private void init() {

        newsCardAdapter = new NewsCardAdapter(mViewModel.getArticles());
        layoutManager = new LinearLayoutManager(this);

        progressBar = mBinding.progress ;
        recyclerNewsList = mBinding.recyclerNewsList ;

        //Setting toolbar
        setSupportActionBar(mBinding.tbNewsList);


        //Setting recycler
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerNewsList.setLayoutManager(layoutManager);
        recyclerNewsList.setAdapter(newsCardAdapter);
        setUpLoadMoreListener();
        addOnItemClickListener();

        //To prevent Api calling on orientation change
        if(mViewModel.isOnCreateCalledOnce()){
            if(commonUtils.isInternetAvailable()) appConnectivityStatus = 1 ;
            else appConnectivityStatus = 2 ;
            loadData();
        }

        mViewModel.getIsDataLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                handleProgressBar(aBoolean);
            }
        });

        mViewModel.getMessageInfo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showSnackBar(s);
            }
        });

        mViewModel.getLatestNews().observe(this, new Observer<ArrayList<Article>>() {
            @Override
            public void onChanged(ArrayList<Article> articles) {
                isLoadingPerformed = false ;
                inflateLatestNews();
            }
        });

        mViewModel.getAppConnectivityStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appConnectivityStatus = integer ;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        //Checks for Internet connectivity
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        //To check if ONLINE or OFFLINE, snackbar shows up only on connectivity changes
                        if ((appConnectivityStatus == 1 && !aBoolean) || (appConnectivityStatus == 2 && aBoolean)) mViewModel.onAppConnectivityChange(aBoolean);
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    //To safely dispose observable
    private void safelyDispose(Disposable disposable) {
        if(disposable != null && !disposable.isDisposed()) disposable.dispose();
    }


    //To set up custom scroll end listener on recycler
    private void setUpLoadMoreListener() {
        recyclerNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoadingPerformed && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    isLoadingPerformed = true;
                    loadData();
                }
            }
        });
    }

    //To set up custom click listener
    private void addOnItemClickListener() {
        recyclerNewsList.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerNewsList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(NewsListActivity.this, view.findViewById(R.id.img_article), "news_pic");
                Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra("article", mViewModel.getArticleWithPosition(position));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) startActivity(intent, options.toBundle());
                else startActivity(intent);
            }
        }));
    }

    //To inflate adapter from news articles in Model class
    private void inflateLatestNews() {
        newsCardAdapter.addDatatoAdapter();
    }


    //To provide visual clues regarding ERROR, SUCCESS and ACTION
    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mBinding.lytCoordinatorNewsList, message, Snackbar.LENGTH_INDEFINITE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.resetVals();
                loadData();
            }
        };

        switch (appConnectivityStatus) {
            case APP_ONLINE : snackbar.setAction(R.string.btn_go_online,listener);
                break;

            case APP_OFFLINE : snackbar.setAction(R.string.btn_go_offline,listener);
                break;

        }
        snackbar.show();
    }


    //To handle progress bar visibility
    private void handleProgressBar(boolean visibility) {
        if(visibility) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }


    //To load data from Server or DB
    private void loadData() {
        if(appConnectivityStatus == 1) mViewModel.loadLatestNewsFromApi();
        else mViewModel.loadNewsFromDB();
    }

}
