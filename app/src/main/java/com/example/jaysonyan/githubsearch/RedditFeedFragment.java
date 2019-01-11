package com.example.jaysonyan.githubsearch;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RedditFeedFragment extends Fragment {
    private UpdateFeed currActivity;
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

    public RedditFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UpdateFeed) {
            currActivity = (UpdateFeed) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reddit_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Progress spinner
        spinner = getView().findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //Recycler view
        feed = getView().findViewById(R.id.recyclerView);
        searchBar = getView().findViewById(R.id.searchView);

        feedLayoutManager = new LinearLayoutManager(this.getContext());
        feed.setLayoutManager(feedLayoutManager);

        feedAdaptor = new RecyclerViewAdaptor(new ArrayList<RedditPost>(), Glide.with(this));
        feed.setAdapter(feedAdaptor);

        feedAdaptor.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                currActivity.showPost(redditPostList.get(position));
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

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
            Data_ currPost = posts.get(i).data;
            newRedditList.add(new RedditPost(currPost.title, currPost.subredditNamePrefixed, currPost.author, currPost.url, currPost.permalink, currPost.selftext));
        }
        after = redditData.data.after.toString();
        if(append) {
            redditPostList.addAll(newRedditList);
        } else {
            redditPostList = newRedditList;
        }
        return redditPostList;
    }

    public interface UpdateFeed {
        void showPost(RedditPost currPost);
    }

}
