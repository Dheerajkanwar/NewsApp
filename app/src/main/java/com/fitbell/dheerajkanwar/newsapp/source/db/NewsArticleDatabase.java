package com.fitbell.dheerajkanwar.newsapp.source.db;

import com.fitbell.dheerajkanwar.newsapp.model.Article;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Article.class}, version = 1,exportSchema = false)
public abstract class NewsArticleDatabase extends RoomDatabase {

    public abstract NewsArticleDao getNewsArticleDao() ;

}
