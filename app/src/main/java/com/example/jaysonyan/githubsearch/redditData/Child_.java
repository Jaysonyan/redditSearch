package com.example.jaysonyan.githubsearch.redditData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child_ {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("data")
    @Expose
    public Data_ data;

}