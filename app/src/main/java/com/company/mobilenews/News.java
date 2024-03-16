package com.company.mobilenews;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Comparable<News> {
    public int id;
    public String source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public Date publishedAt;
    public String content;

    public News(int id, String source, String author, String title, String description, String url, String urlToImage, Date publishedAt, String content) {
        this.id = id;
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public News() {
    }

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
        String formattedDate = publishedAt != null ? sdf.format(publishedAt) : "Unknown Date";
        return "📌 " + title + " (" + formattedDate + ')';
    }

    @Override
    public int compareTo(News o) {
        return toString().compareTo(o.toString());
    }
}

