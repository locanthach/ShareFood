package com.locanthach.sharefood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haozhang.lib.SlantedTextView;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.model.Post;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.locanthach.sharefood.common.Constant.STATUS_GIVEN;

/**
 * Created by phant on 26-Jul-17.
 */

public class UserTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Post> posts;
    private Context context;
    private RepostListener repostListener;

    public interface RepostListener {
        void onItemClick(Post post);
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
        //Set up given button on click listener
        viewHolder.giveAway_button.setOnClickListener(v -> {
            EventBus.getDefault()
                    .post(new GivenEvent(post, position));
            viewHolder.stvStatus.setText("Given").setVisibility(View.VISIBLE);
        });
        //Set up delete button on click listener
//        viewHolder.delete_button.setOnClickListener(v -> EventBus.getDefault()
//                .post(new DeleteEvent(post, position)));
        //Set up repost button on click listener
        viewHolder.repost_button.setOnClickListener(v -> {
            if (repostListener != null) {
                repostListener.onItemClick(post);
            }
        });
        //Set on long click listener
        viewHolder.imgFood.setOnLongClickListener(v -> {
            // TODO Something
            return true;
        });
        viewHolder.stvStatus.setVisibility(View.GONE);
        //If post is given ...
        if (Integer.parseInt(post.getStatus()) == STATUS_GIVEN) {
            // TODO Something
            viewHolder.stvStatus.setText("Given").setVisibility(View.VISIBLE);
        }
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

        @BindView(R.id.stvStatus)
        SlantedTextView stvStatus;
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

    public static class GivenEvent {
        public final Post post;
        public final int position;

        public GivenEvent(Post post, int position) {
            this.post = post;
            this.position = position;
        }
    }

    public static class DeleteEvent {
        public final Post post;
        public final int position;

        public DeleteEvent(Post post, int position) {
            this.post = post;
            this.position = position;
        }
    }
}
