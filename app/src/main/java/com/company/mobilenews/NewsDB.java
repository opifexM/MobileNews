package com.company.mobilenews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsDB extends SQLiteOpenHelper {
    private static final int dbVersion = 1;
    private static NewsDB newsDB;

    public static synchronized NewsDB getMovieDB(Context context) {
        if (newsDB == null) {
            newsDB = new NewsDB(context.getApplicationContext());
        }
        return newsDB;
    }

    private NewsDB(Context context) {
        super(context, "NewsDB", null, dbVersion);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNewsTable = "CREATE TABLE NewsTable (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "source TEXT, " +
                "author TEXT, " +
                "title TEXT, " +
                "description TEXT, " +
                "url TEXT, " +
                "urlToImage TEXT, " +
                "publishedAt TEXT, " +
                "content TEXT)";
        db.execSQL(createNewsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addOrUpdateNews(News news) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("source", news.source);
        contentValues.put("author", news.author);
        contentValues.put("title", news.title);
        contentValues.put("description", news.description);
        contentValues.put("url", news.url);
        contentValues.put("urlToImage", news.urlToImage);
        contentValues.put("publishedAt", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(news.publishedAt));
        contentValues.put("content", news.content);
        db.insert("NewsTable", null, contentValues);
    }

    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, source, author, title, description, url, urlToImage, publishedAt, content FROM NewsTable",
                null);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if (cursor.moveToFirst()) {
                do {
                    News news = new News();
                    news.id = cursor.getInt(0);
                    news.source = cursor.getString(1);
                    news.author = cursor.getString(2);
                    news.title = cursor.getString(3);
                    news.description = cursor.getString(4);
                    news.url = cursor.getString(5);
                    news.urlToImage = cursor.getString(6);
                    news.publishedAt = sdf.parse(cursor.getString(7));
                    news.content = cursor.getString(8);
                    newsList.add(news);
                } while (cursor.moveToNext());
            }
        } catch (ParseException ex) {
            Log.d("NewsDB", "Error while trying to get news from database", ex);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        newsList.sort((news1, news2) -> {
            if (news1.publishedAt == null && news2.publishedAt == null) return 0;
            if (news1.publishedAt == null) return 1;
            if (news2.publishedAt == null) return -1;
            return news2.publishedAt.compareTo(news1.publishedAt);
        });

        return newsList;
    }

    public boolean newsExists(String title) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT id FROM NewsTable WHERE title = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{title});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
