package com.example.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewsAsyncTaskLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String mURL;

    public NewsAsyncTaskLoader(@NonNull Context context, String URL) {
        super(context);
        mURL = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<News> loadInBackground() {
        return QueryUtils.fetchNewsData(mURL);
    }
}
