package com.example.jaysonyan.githubsearch;

import android.support.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {
    private SearchView searchBar;
    private RecyclerView feed;
    private LinearLayoutManager feedLayoutManager;
    private RecyclerViewAdaptor feedAdaptor;
    private RedditService service;
    private ProgressBar spinner;
    private String currSearch;
    private String after;
    private List<RedditPost> redditPostList = new ArrayList<>();
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Progress spinner
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //Recycler view
        feed = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchView);

        feedLayoutManager = new LinearLayoutManager(this);
        feed.setLayoutManager(feedLayoutManager);

        feedAdaptor = new RecyclerViewAdaptor(new ArrayList<RedditPost>(), Glide.with(this));
        feed.setAdapter(feedAdaptor);


        //Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RedditService.class);


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                spinner.setVisibility(View.VISIBLE);
                Log.d("myTags", query);
                final Call<Child> redditData = service.getRedditData(query, "");

                redditData.enqueue(new Callback<Child>() {
                    @Override
                    public void onResponse(Call<Child> call, Response<Child> response) {
                        spinner.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            currSearch = query;
                            feedAdaptor.setAllPosts(generatePostList(response.body(), false));
                        }
                    }

                    @Override
                    public void onFailure(Call<Child> call, Throwable t) {
                        spinner.setVisibility(View.GONE);
                        Log.d("myTags", "onFailure: " + t.getMessage());
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        feed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itemCount = feedLayoutManager.getItemCount();
                int childCount = feedLayoutManager.getChildCount();
                int position = feedLayoutManager.findFirstVisibleItemPosition();
                boolean closeToBottom = (childCount + position >= itemCount);
                if (!isLoading && closeToBottom) {
                    isLoading = true;
                    final Call<Child> redditData = service.getRedditData(currSearch, after);
                    redditData.enqueue(new Callback<Child>() {
                        @Override
                        public void onResponse(Call<Child> call, Response<Child> response) {
                            isLoading = false;
                            if (response.isSuccessful() && response.body().data.children.size() > 0) {
                                feedAdaptor.setAllPosts(generatePostList(response.body(), true));
                            }
                        }

                        @Override
                        public void onFailure(Call<Child> call, Throwable t) {
                            isLoading = false;
                            Log.d("mytags", "onFailure: " + t.getMessage());
                        }
                    });
                }
                Log.d("mytags", "onScrolled: item: "+ itemCount + " child: " + childCount + " position: " + position);

            }
        });



    }
    public List<RedditPost> generatePostList(Child redditData, boolean append) {

        List<RedditPost> newRedditList = new ArrayList<>();
        List<Child_> posts = redditData.data.children;
        for (int i = 0; i < posts.size(); i++) {
            Data_ currPost = redditData.data.children.get(i).data;
            newRedditList.add(new RedditPost(currPost.title, currPost.subredditNamePrefixed, currPost.author, currPost.url, currPost.permalink));
        }
        after = redditData.data.after.toString();
        if(append) {
            redditPostList.addAll(newRedditList);
        } else {
            redditPostList = newRedditList;
        }
        return redditPostList;
    }
}

