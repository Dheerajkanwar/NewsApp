package com.fitbell.dheerajkanwar.newsapp.source.db;

import com.fitbell.dheerajkanwar.newsapp.model.Article;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface NewsArticleDao {

    @Query("SELECT * FROM articles ORDER BY id limit :limit offset :offset")
    Single<List<Article>> getNewsArticles(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserArticle(Article article);

}
