package com.example.jaysonyan.githubsearch;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jaysonyan.githubsearch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedditPostFragment extends Fragment {

    private String title;
    private String imageURL;
    private String bodyText;

    private ImageView imagePostView;
    private TextView titleTextView;
    private TextView bodyTextView;
    private ImageButton backButton;

    private RemovePost currActiviy;

    public RedditPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(getString(R.string.reddit_post_title));
            imageURL = args.getString(getString(R.string.reddit_post_imageURL));
            bodyText = args.getString(getString(R.string.reddit_post_bodyText));
        }
        if (context instanceof RemovePost) {
            currActiviy = (RemovePost) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reddit_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = getView().findViewById(R.id.post_title);
        imagePostView = getView().findViewById(R.id.post_image);
        bodyTextView = getView().findViewById(R.id.post_body);
        backButton = getView().findViewById(R.id.post_back_button);

        titleTextView.setText(title);
        bodyTextView.setText(bodyText);

        if (imageURL.contains(".jpg") || imageURL.contains(".png")) {
            Glide.with(this)
                    .load(imageURL)
                    .into(imagePostView);
        } else {
            imagePostView.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currActiviy.removePost();
            }
        });

    }

    public static RedditPostFragment newInstance(String title, String imageURL, String bodyText, Context context) {
        Bundle args = new Bundle();

        args.putString(context.getResources().getString(R.string.reddit_post_title), title);
        args.putString(context.getResources().getString(R.string.reddit_post_imageURL), imageURL);
        args.putString(context.getResources().getString(R.string.reddit_post_bodyText), bodyText);

        RedditPostFragment redditPostFragment = new RedditPostFragment();
        redditPostFragment.setArguments(args);
        return redditPostFragment;
    }

    public interface RemovePost {
        void removePost();
    }
}
