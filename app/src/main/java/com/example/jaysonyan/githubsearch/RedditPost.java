package com.example.jaysonyan.githubsearch;

import android.support.annotation.NonNull;

public class RedditPost {
    public String title;
    public String subReddit;
    public String author;
    public String imageURl;
    public String link;
    public String bodyText;

    public RedditPost(String title, String subReddit, String author, String imageURl, String link, String bodyText) {
        this.title = title;
        this.subReddit = subReddit;
        this.author = author;
        this.imageURl = imageURl;
        this.link = link;
        this.bodyText = bodyText;
    }
}
