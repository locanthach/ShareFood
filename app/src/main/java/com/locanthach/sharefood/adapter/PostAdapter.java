package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.locanthach.sharefood.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by An Lee on 7/16/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Post> posts;
    private final Context context;

    public PostAdapter(Context context) {
        this.posts = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setData(List<Post> data) {
        posts.clear();
        posts.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<Post> newPosts) {
        int nextPos = posts.size();
        posts.addAll(nextPos, newPosts);
        notifyItemRangeChanged(nextPos, newPosts.size());
    }
}
