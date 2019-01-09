package com.example.jaysonyan.githubsearch;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RedditPost> allPosts;
    private boolean isLoading = false;
    private RequestManager glide;

    public static class ViewHolderImage extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView author;

        public ViewHolderImage(View v) {
            super(v);
            this.title = v.findViewById(R.id.post_title);
            this.image = v.findViewById(R.id.post_image);
            this.author = v.findViewById(R.id.post_author);
        }
    }

    public static class ViewHolderNoImage extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;

        public ViewHolderNoImage(View v) {
            super(v);
            this.title = v.findViewById(R.id.post_title_no_image);
            this.author = v.findViewById(R.id.post_author_no_image);
        }
    }


    public RecyclerViewAdaptor(List<RedditPost> dataSet, RequestManager glide) {
        this.allPosts = dataSet;
        this.glide = glide;
    }

    public void setAllPosts(List<RedditPost> allPosts) {
        this.allPosts = allPosts;
        notifyDataSetChanged();
    }

    public void appendPosts(List<RedditPost> morePosts) { //Maybe change this if not smooth
        this.allPosts.addAll(morePosts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        // 0 is no image
        // 1 is image
        boolean isImage = allPosts.get(position).imageURl.contains(".jpg")|| allPosts.get(position).imageURl.contains(".png");
        return isImage ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item, null, false);
                return new ViewHolderImage(v);
            default:
                v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_no_image, null, false);
                return new ViewHolderNoImage(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String credits = "Posted by: " + allPosts.get(position).author + " on " + allPosts.get(position).subReddit;
        String link = "<a href='https://www.reddit.com"+ allPosts.get(position).link+"'>" + credits + " </a>";
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderImage viewHolderImage = (ViewHolderImage)holder;
                viewHolderImage.title.setText(allPosts.get(position).title);
                viewHolderImage.author.setMovementMethod(LinkMovementMethod.getInstance());
                viewHolderImage.author.setText(Html.fromHtml(link));
                glide.load(allPosts.get(position).imageURl)
                        .into(viewHolderImage.image);
                break;
            case 1:
                ViewHolderNoImage viewHolderNoImage = (ViewHolderNoImage)holder;
                viewHolderNoImage.author.setMovementMethod(LinkMovementMethod.getInstance());
                viewHolderNoImage.title.setText(allPosts.get(position).title);
                viewHolderNoImage.author.setText(Html.fromHtml(link));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return allPosts.size();
    }

}
