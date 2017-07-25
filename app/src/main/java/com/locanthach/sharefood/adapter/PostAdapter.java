package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.activity.PostDetailActivity;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.viewholder.PostViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by An Lee on 7/16/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<Post> posts;
    private final Context context;
    private static final String STATE = "listState";

    public PostAdapter(Context context) {
        this.posts = new ArrayList<>();
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new PostViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(posts.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, posts.get(position));
            context.startActivity(intent);
        });
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

    public void updatePost(Post data) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId().equals(data.getId())) {
                posts.set(i, data);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void addPost(Post post) {
        posts.add(0, post);
        notifyItemInserted(0);
    }

    public void appendData(List<Post> newPosts) {
        int nextPos = posts.size();
        posts.addAll(nextPos, newPosts);
        notifyItemRangeChanged(nextPos, newPosts.size());
    }

    public void setState(Bundle state) {
        state.putParcelableArrayList(STATE, (ArrayList<? extends Parcelable>) posts);
    }

    public List<Post> getStateList(Bundle state) {
        return state.getParcelableArrayList(STATE);
    }
//    private int scaleItemHeightImage(Article.Media media) {
//        int height = media.getHeight();
//        int width = media.getWidth();
//        int itemWidth = UiUtils.getScreenWitdh() / 2;
//        int itemHeight = itemWidth * height / width;
//        return itemHeight;
//    }

//    public void setLayoutReference(int layoutReference) {
//        this.mLayoutReference = layoutReference;
//    }

    public static class PostEvent {
        public final LikeButton btnLike;
        public final Post post;

        public PostEvent(LikeButton btnLike, Post post) {
            this.btnLike = btnLike;
            this.post = post;
        }
    }
}