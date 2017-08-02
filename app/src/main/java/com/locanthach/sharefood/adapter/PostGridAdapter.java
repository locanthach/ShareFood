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
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.viewholder.PostGridViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by An Lee on 8/2/2017.
 */

public class PostGridAdapter extends RecyclerView.Adapter<PostGridViewHolder> {
    private final ArrayList<Post> posts;
    private final ArrayList<User> users;
    private Context context;
    private String uid;
    private static final String STATE = "listState";

    public PostGridAdapter() {
        this.posts = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    @Override
    public PostGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new PostGridViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_post_image, parent, false));
    }

    @Override
    public void onBindViewHolder(PostGridViewHolder holder, int position) {
        User postOwner = new User();
        for (User user : users) {
            if (posts.get(position).getUid().equals(user.getId())) {
                postOwner = user;
                postOwner.setId(user.getId());
            }
        }
        holder.bind(context, posts.get(position), postOwner, uid);
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

    public void setUsers(List<User> data) {
        users.clear();
        users.addAll(data);
        notifyDataSetChanged();
    }

    public void setCurrentUid(String uid) {
        this.uid = uid;
    }

    public void setState(Bundle state) {
        state.putParcelableArrayList(STATE, (ArrayList<? extends Parcelable>) posts);
    }

    public List<Post> getStateList(Bundle state) {
        return state.getParcelableArrayList(STATE);
    }
}
