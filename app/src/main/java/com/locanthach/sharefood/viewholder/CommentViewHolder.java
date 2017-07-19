package com.locanthach.sharefood.viewholder;

import android.support.v7.widget.RecyclerView;

import com.locanthach.sharefood.databinding.ItemCommentBinding;
import com.locanthach.sharefood.model.Comment;

import static com.locanthach.sharefood.utils.ParseRelativeData.getRelativeTimeAgo;

/**
 * Created by An Lee on 7/19/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public final ItemCommentBinding binding;

    public CommentViewHolder(ItemCommentBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Comment comment) {
        comment.setTime(getRelativeTimeAgo(comment.getTime()));
        binding.setComment(comment);
    }
}
