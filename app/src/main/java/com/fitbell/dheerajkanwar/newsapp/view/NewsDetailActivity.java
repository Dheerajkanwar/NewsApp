package com.fitbell.dheerajkanwar.newsapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.fitbell.dheerajkanwar.newsapp.R;
import com.fitbell.dheerajkanwar.newsapp.databinding.NewsDetailActivityLayoutBinding;
import com.fitbell.dheerajkanwar.newsapp.model.Article;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class NewsDetailActivity extends AppCompatActivity {

    NewsDetailActivityLayoutBinding mBinding ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.news_detail_activity_layout);

        //To set toolbar from layout
        setSupportActionBar(mBinding.tbNewsDetail);
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        //To bind article received through intent
        Intent intent = getIntent() ;
        mBinding.setArticle((Article) intent.getSerializableExtra("article"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
