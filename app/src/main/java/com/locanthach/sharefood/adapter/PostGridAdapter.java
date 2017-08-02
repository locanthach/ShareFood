package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.locanthach.sharefood.R;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.viewholder.PostGridViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by An Lee on 8/2/2017.
 */

public class PostGridAdapter extends RecyclerView.Adapter<PostGridViewHolder> {
    private final ArrayList<Post> posts;
    private Context context;
    private static final String STATE = "listState";

    public PostGridAdapter() {
        this.posts = new ArrayList<>();
    }

    @Override
    public PostGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new PostGridViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_post_image, parent, false));
    }

    @Override
    public void onBindViewHolder(PostGridViewHolder holder, int position) {
        holder.bind(context, posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addPost(Post post) {
        posts.add(0, post);
        notifyItemInserted(0);
    }

    public void setPosts(List<Post> data) {
        posts.clear();
        posts.addAll(data);
        notifyDataSetChanged();
    }

    public void setState(Bundle state) {
        state.putParcelableArrayList(STATE, (ArrayList<? extends Parcelable>) posts);
    }

    public List<Post> getStateList(Bundle state) {
        return state.getParcelableArrayList(STATE);
    }
}
