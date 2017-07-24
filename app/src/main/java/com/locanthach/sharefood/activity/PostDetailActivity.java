package com.locanthach.sharefood.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.adapter.CommentAdapter;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.databinding.ActivityPostDetailBinding;
import com.locanthach.sharefood.model.Comment;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.User;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.HashMap;
import java.util.Map;

import static com.locanthach.sharefood.utils.BindingUtil.loadImage;

public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private Post mPost;


    private ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);
        // Get post key from intent
        mPost = getIntent().getExtras().getParcelable(EXTRA_POST_KEY);
        mPostKey = mPost.getId();
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        initDatabase();
        setUpView();
        increaseViewCount(mPost);
    }

    private void setUpView() {
        binding.rvComments.setLayoutManager(new LinearLayoutManager(this));
        binding.btnComment.setOnClickListener(v -> {
            postComment();
        });
    }

    private void initDatabase() {
        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                binding.setPost(post);
                loadImage(PostDetailActivity.this,binding.ivCover, mPost.getPhotoUrl());
                setFont();
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        binding.rvComments.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    private void postComment() {
        final String uid = FireBaseConfig.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String author = user.getName();

                        // Create new comment object
                        String content = binding.etComment.getText().toString();
                        Comment comment = new Comment(uid, author, content);

                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);

                        // Clear the field
                        binding.etComment.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setFont() {
        binding.tvAuthor.setTypeface(EasyFonts.robotoBold(PostDetailActivity.this));
        binding.tvContent.setTypeface(EasyFonts.robotoLight(PostDetailActivity.this));
        binding.tvLocation.setTypeface(EasyFonts.robotoBold(PostDetailActivity.this));
        binding.tvLikeCount.setTypeface(EasyFonts.robotoBold(PostDetailActivity.this));
        binding.tvViews.setTypeface(EasyFonts.robotoBold(PostDetailActivity.this));
        binding.tvTime.setTypeface(EasyFonts.robotoLight(PostDetailActivity.this));
    }

    private void increaseViewCount(Post post) {
        String key = post.getId();
        int count = Integer.parseInt(post.getViewCount()) + 1;
        post.setViewCount(String.valueOf(count));
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + post.getUid() + "/" + key, postValues);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }
}
