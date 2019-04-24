package com.fitbell.dheerajkanwar.newsapp.viewmodel;

import com.fitbell.dheerajkanwar.newsapp.repository.NewsListRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsListViewModelFactory implements ViewModelProvider.Factory {

    private final NewsListRepository repository ;

    @Inject
    public NewsListViewModelFactory(NewsListRepository repository) {
        this.repository = repository ;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(NewsListViewModel.class)) return (T) new NewsListViewModel(repository) ;
        else {
            throw new IllegalArgumentException() ;
        }
    }
}
