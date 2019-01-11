package com.example.jaysonyan.githubsearch;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.jaysonyan.githubsearch.redditData.Child;

public interface RedditService {
    @GET("search.json")
    Call<Child> getRedditData(@Query("q") String search, @Query("after") String after);
}
