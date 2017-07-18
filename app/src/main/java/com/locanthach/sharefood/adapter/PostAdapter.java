package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerViewHolder;
import com.locanthach.sharefood.R;
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
        holder.bind(posts.get(position), context);
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

//    public void setLayoutReference(int layoutReference) {
//        this.mLayoutReference = layoutReference;
//    }
}
