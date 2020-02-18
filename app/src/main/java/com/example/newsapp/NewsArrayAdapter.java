package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;


public class NewsArrayAdapter extends ArrayAdapter<News> {

    public NewsArrayAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.list_item_title_text_view);
        titleTextView.setText(currentNews.getTitle());

        TextView sectionTextView = listItemView.findViewById(R.id.list_item_section_text_view);
        sectionTextView.setText(currentNews.getSection());

        String fullDate = currentNews.getPublishedDate();
        String splitter = "T";
        String[] dateArray = fullDate.split(splitter);
        String dateToDisplay = dateArray[0];
        TextView dateTextView = listItemView.findViewById(R.id.list_item_published_date_text_view);
        dateTextView.setText(dateToDisplay);

        TextView authorTextView = listItemView.findViewById(R.id.list_item_author_text_view);
        String author = currentNews.getAuthor();
        if (author != null){
            String fullString = author;
            String splitter1 = "/";
            String[] splitArray = fullString.split(splitter1);
            author = splitArray[1];
            authorTextView.setText(author);
        } else {
            authorTextView.setText(" --- ");
        }

        return listItemView;
    }
}
