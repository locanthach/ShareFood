package com.locanthach.sharefood.viewholder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.locanthach.sharefood.R;
import com.locanthach.sharefood.adapter.PostAdapter;
import com.locanthach.sharefood.common.Constant;
import com.locanthach.sharefood.databinding.ItemPostBinding;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.User;

import org.greenrobot.eventbus.EventBus;

import static com.locanthach.sharefood.util.BindingUtil.loadImage;
import static com.locanthach.sharefood.util.BindingUtil.loadImageAva;

/**
 * Created by An Lee on 7/16/2017.
 */


public class PostViewHolder extends RecyclerView.ViewHolder {
    public final ItemPostBinding binding;
    private Context context;

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
    public void bind(Context context, Post post, User user, String currentUid) {
        this.context = context;
        binding.setPost(post);
        binding.setUser(user);
        //Deactive like icon
        binding.btnLike
                .getDrawable()
                .mutate()
                .setColorFilter(context.getResources().getColor(R.color.blueGrey800),
                        PorterDuff.Mode.SRC_IN);
        if (islike(post, currentUid)) {
            //If user liked post, active like icon
            binding.btnLike
                    .getDrawable()
                    .mutate()
                    .setColorFilter(context.getResources().getColor(R.color.red400),
                            PorterDuff.Mode.SRC_IN);
        }
        setUpStatusPost(post);
        setUpLikeClick();
        setImagePostClick();
        loadImageAva(binding.imgUser, user.getProfileImageUrl());
        loadImage(binding.imgPost, post.getPhotoUrl());
    }

    private void setUpStatusPost(Post post) {
        binding.stvStatus.setVisibility(View.GONE);
        if (Integer.parseInt(post.getStatus()) == Constant.STATUS_GIVEN) {
            binding.stvStatus.setText("Given").setVisibility(View.VISIBLE);
        }
    }

    private boolean islike(Post post, String currentUid) {
        return !post.getLikes().isEmpty() &&
                post.getLikes().containsKey(currentUid) &&
                post.getLikes().get(currentUid);
    }

    private void setImagePostClick() {
        binding.imgPost.setOnClickListener(v -> EventBus.getDefault()
                .post(new PostAdapter.ImagePostEvent(binding.getPost())));
    }

    private void setUpLikeClick() {
        binding.btnLike.setOnClickListener(v -> EventBus.getDefault()
                .post(new PostAdapter.LikeEvent(binding.btnLike, binding.getPost())));
    }
}