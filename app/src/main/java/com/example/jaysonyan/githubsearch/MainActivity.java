package com.example.jaysonyan.githubsearch;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.example.jaysonyan.githubsearch.redditData.Child;
import com.example.jaysonyan.githubsearch.redditData.Child_;
import com.example.jaysonyan.githubsearch.redditData.Data_;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RedditFeedFragment.UpdateFeed, RedditPostFragment.RemovePost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RedditFeedFragment feedFragment = new RedditFeedFragment();
        feedFragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, feedFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void showPost(RedditPost currPost) {
        Fragment redditPostFragment = RedditPostFragment.newInstance(currPost.title, currPost.imageURl, currPost.bodyText, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, redditPostFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void removePost() {
        getSupportFragmentManager().popBackStackImmediate();
    }
}

