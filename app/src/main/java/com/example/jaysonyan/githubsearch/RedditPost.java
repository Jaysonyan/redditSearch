package com.example.jaysonyan.githubsearch;

import android.support.annotation.NonNull;

public class RedditPost {
    public String title;
    public String subReddit;
    public String author;
    public String imageURl;
    public String link;

    public RedditPost(String title, String subReddit, String author, String imageURl, String link) {
        this.title = title;
        this.subReddit = subReddit;
        this.author = author;
        this.imageURl = imageURl;
        this.link = link;
    }
}
