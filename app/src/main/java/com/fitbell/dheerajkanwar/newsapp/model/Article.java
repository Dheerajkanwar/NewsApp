package com.fitbell.dheerajkanwar.newsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles")
public class Article implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id ;

    @SerializedName("author")
    @Expose
    @ColumnInfo(name = "author")
    private String author;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "url")
    private String url;

    @SerializedName("urlToImage")
    @Expose
    @ColumnInfo(name = "urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    @Expose
    @ColumnInfo(name = "publishedAt")
    private String publishedAt;

    @SerializedName("content")
    @Expose
    @ColumnInfo(name = "content")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
return author;
}

public void setAuthor(String author) {
this.author = author;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public String getUrlToImage() {
return urlToImage;
}

public void setUrlToImage(String urlToImage) {
this.urlToImage = urlToImage;
}

public String getPublishedAt() {
return publishedAt;
}

public void setPublishedAt(String publishedAt) {
this.publishedAt = publishedAt;
}

public String getContent() {
return content;
}

public void setContent(String content) {
this.content = content;
}

}