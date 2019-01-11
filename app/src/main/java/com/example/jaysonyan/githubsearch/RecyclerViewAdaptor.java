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
    private RequestManager glide;
    private ItemClickListener clickListener;
    private enum ViewType {
        IMAGE, NO_IMAGE
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView image;
        public TextView author;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            this.title = v.findViewById(R.id.feed_title);
            this.image = v.findViewById(R.id.feed_image);
            this.author = v.findViewById(R.id.feed_author);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecyclerViewAdaptor(List<RedditPost> dataSet, RequestManager glide) {
        this.allPosts = dataSet;
        this.glide = glide;
    }

    public void setAllPosts(List<RedditPost> allPosts) {
        this.allPosts = allPosts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        boolean isImage = allPosts.get(position).imageURl.contains(".jpg")|| allPosts.get(position).imageURl.contains(".png");
        return isImage ? ViewType.IMAGE.ordinal() : ViewType.NO_IMAGE.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, null, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String credits = "Posted by: " + allPosts.get(position).author + " on " + allPosts.get(position).subReddit;
        String link = "<a href='https://www.reddit.com"+ allPosts.get(position).link+"'>" + credits + " </a>";
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).title.setText(allPosts.get(position).title);
            ((ViewHolder) holder).title.setText(allPosts.get(position).title);
            ((ViewHolder) holder).author.setMovementMethod(LinkMovementMethod.getInstance());
            ((ViewHolder) holder).author.setText(Html.fromHtml((link)));
        }

        if (ViewType.values()[holder.getItemViewType()] == ViewType.IMAGE) {
            glide.load(allPosts.get(position).imageURl)
                    .into(((ViewHolder) holder).image);
        } else {
            ((ViewHolder) holder).image.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return allPosts.size();
    }

}
