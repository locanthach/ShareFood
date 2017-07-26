package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.utils.ParseRelativeData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phant on 26-Jul-17.
 */

public class UserTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Post> posts;
    private Context context;
    private GiveAwayListener giveAwayListener;
    private RepostListener repostListener;

    public interface GiveAwayListener {
        void onItemClick(Post post);
    }
    public interface RepostListener {
        void onItemClick(Post post);
    }

    public void setGiveAwayListener(GiveAwayListener listener) {
        this.giveAwayListener = listener;
    }
    public void setRepostListener(RepostListener listener) {
        this.repostListener = listener;
    }

    public UserTimeLineAdapter(Context context) {
        this.posts = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_post, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Post post = posts.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context).load(post.getPhotoUrl()).placeholder(R.drawable.ic_placeholder_food_image).animate(R.anim.slide_in_right).centerCrop().into(viewHolder.imgFood);
        viewHolder.tvTime.setText(post.getRelativeTime());
        viewHolder.giveAway_button.setOnClickListener(v -> {
            if (giveAwayListener != null) {
                giveAwayListener.onItemClick(post);
            }
        });
        viewHolder.repost_button.setOnClickListener(v -> {
            if (repostListener != null) {
                repostListener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setUserPosts(List<Post> data) {
        posts.clear();
        posts.addAll(data);
        notifyDataSetChanged();
    }

    public void updateUserPost(Post data) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId().equals(data.getId())) {
                posts.set(i, data);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void addUserPost(Post post) {
        posts.add(0, post);
        notifyItemInserted(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgFood)
        ImageView imgFood;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.giveAway_button)
        TextView giveAway_button;
        @BindView(R.id.repost_button)
        TextView repost_button;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
