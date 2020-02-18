package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    private String newsURL = "https://content.guardianapis.com/search?&api-key=2d0c31d6-dd87-4e3c-a86c-f0e11c0cae78&page-size=30&show-references=author";
    NewsArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBarCircular = findViewById(R.id.progress_circular);
        TextView emptyTextView = findViewById(R.id.empty_text_view);
        TextView retryTextView = findViewById(R.id.retry_text_view);
        retryTextView.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        final LoaderManager loaderManager = getLoaderManager();
        if (isConnected){
            loaderManager.initLoader(0,null, MainActivity.this);
        }else {
            retryTextView.setVisibility(View.VISIBLE);
            progressBarCircular.setVisibility(View.GONE);
            emptyTextView.setText("Please check your internet connection.");
        }

        retryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaderManager.restartLoader(0,null, MainActivity.this);
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsAsyncTaskLoader(MainActivity.this, newsURL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {

        ListView mainListView = findViewById(R.id.list);

        TextView emptyTextView = findViewById(R.id.empty_text_view);
        emptyTextView.setText("No stories to show.");
        mainListView.setEmptyView(emptyTextView);

        ProgressBar progressBarCircular = findViewById(R.id.progress_circular);
        progressBarCircular.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        TextView retryTextView = findViewById(R.id.retry_text_view);
        if (isConnected){
            retryTextView.setVisibility(View.GONE);
        }

        adapter = new NewsArrayAdapter(MainActivity.this, news);
        if (news != null){
            mainListView.setAdapter(adapter);
        }

        final ArrayList<News> news1 = news;

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = news1.get(position);
                String webUrl = currentNews.getWebLink();
                openWebPage(webUrl);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        if (adapter != null){
            adapter.clear();
        }
    }

    public void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
