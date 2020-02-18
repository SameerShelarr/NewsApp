package com.example.newsapp;

public class News {

    private String mTitle;
    private String mSection;
    private String mPublishedDate;
    private String mAuthor;
    private String mWebLink;

    public News(String title, String section, String publishedDate, String author, String webLink){
        mTitle = title;
        mSection = section;
        mPublishedDate = publishedDate;
        mAuthor = author;
        mWebLink = webLink;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getWebLink() {
        return mWebLink;
    }
}
