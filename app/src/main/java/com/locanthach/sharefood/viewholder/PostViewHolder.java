package com.locanthach.sharefood.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.locanthach.sharefood.adapter.PostAdapter;
import com.locanthach.sharefood.databinding.ItemPostBinding;
import com.locanthach.sharefood.model.Post;

import org.greenrobot.eventbus.EventBus;

import static com.locanthach.sharefood.utils.BindingUtil.loadImage;

/**
 * Created by An Lee on 7/16/2017.
 */


public class PostViewHolder extends RecyclerView.ViewHolder {
    public final ItemPostBinding binding;

    public PostViewHolder(ItemPostBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    //    public PostViewHolder(LayoutInflater inflater, ViewGroup parent, int innerViewResId) {
//        super(inflater.inflate(R.layout.viewholder_shimmer, parent, false));
//        ShimmerFrameLayout layout = (ShimmerFrameLayout) itemView;
//        layout.setGroup(Integer.toString(innerViewResId));
//
//        View innerView = inflater.inflate(innerViewResId, layout, false);
//        layout.addView(innerView);
//        layout.setAutoStart(false);
//    }
//
    public void bind(Post post, Context context) {
//        ShimmerFrameLayout layout = (ShimmerFrameLayout) itemView;
//        layout.startShimmerAnimation();
        setUpLikeClick();
        loadImage(binding.imgPost, post.getPhotoUrl());
        binding.setPost(post);
    }

    private void setUpLikeClick() {
        binding.btnLike.setOnClickListener(v -> EventBus.getDefault()
                .post(new PostAdapter.PostEvent(binding.btnLike, binding.getPost())));
    }
}
