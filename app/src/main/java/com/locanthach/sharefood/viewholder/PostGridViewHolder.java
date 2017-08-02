package com.locanthach.sharefood.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.locanthach.sharefood.adapter.PostAdapter;
import com.locanthach.sharefood.databinding.ItemPostImageBinding;
import com.locanthach.sharefood.model.Post;

import org.greenrobot.eventbus.EventBus;

import static com.locanthach.sharefood.util.BindingUtil.loadImage;

/**
 * Created by An Lee on 8/2/2017.
 */

public class PostGridViewHolder extends RecyclerView.ViewHolder {
    public final ItemPostImageBinding binding;
    private Context context;

    public PostGridViewHolder(ItemPostImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Context context, Post post) {
        this.context = context;
        binding.setPost(post);
        setImagePostClick();
        loadImage(binding.imgPost, post.getPhotoUrl());
    }

    private void setImagePostClick() {
        binding.imgPost.setOnClickListener(v -> EventBus.getDefault()
                .post(new PostAdapter.ImagePostEvent(binding.getPost())));
    }
}
