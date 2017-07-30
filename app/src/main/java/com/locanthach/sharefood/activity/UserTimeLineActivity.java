package com.locanthach.sharefood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.adapter.UserTimeLineAdapter;
import com.locanthach.sharefood.common.Constant;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by phant on 26-Jul-17.
 */

public class UserTimeLineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView rvPost;
    @BindView(R.id.nvView)
    NavigationView nvView;
    @BindView(R.id.sign_out_button)
    LinearLayout sign_out_button;
    @BindView(R.id.profile_button)
    LinearLayout profile_button;
    @BindView(R.id.timeline_button)
    LinearLayout timeline_button;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_button)
    LinearLayout home_button;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    private MaterialEditText etRepost;
    private ImageView imgPost;
    private TextView tvUsernamePost;
    private TextView tvStatus;

    private UserTimeLineAdapter userTimeLineAdapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private List<Post> posts;
    private TextView toolbar_title;
    private User currentUser;
    //FIREBASE
    private DatabaseReference postsDBRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timeline);
        setUpFireBase();
        setUpViews();
        handleEventClick();
        fetchPosts();
        swipeToRefresh();
        setUpDrawerLayout();

    }

    private void swipeToRefresh() {
        swipeContainer.setOnRefreshListener(() -> fetchPosts());

        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_dark);
    }

    private void fetchPosts() {
        postsDBRef.child(FireBaseConfig.POSTS_CHILD)
                .orderByChild("uid")
                .startAt(mFirebaseUser.getUid())
                .endAt(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        posts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Post post = child.getValue(Post.class);
                            if (Integer.parseInt(post.getStatus()) != Constant.STATUS_DELETED) {
                                post.setId(child.getKey());
                                child.getValue();
                                posts.add(post);
                            }
                        }
                        Collections.reverse(posts);
                        userTimeLineAdapter.setUserPosts(posts);
                        rvPost.hideShimmerAdapter();
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Subscribe
    public void onEvent(UserTimeLineAdapter.GivenEvent event) {
        Post post = event.post;
        post.setStatus(String.valueOf(Constant.STATUS_GIVEN));
        String key = post.getId();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + post.getUid() + "/" + key, postValues);
        postsDBRef.updateChildren(childUpdates);
        posts.set(event.position, post);
        userTimeLineAdapter.notifyItemChanged(event.position);
    }

    @Subscribe
    public void onEvent(UserTimeLineAdapter.DeleteEvent event) {
        Post post = event.post;
        post.setStatus(String.valueOf(Constant.STATUS_DELETED));
        String key = post.getId();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + post.getUid() + "/" + key, postValues);
        postsDBRef.updateChildren(childUpdates);
        posts.remove(event.position);
        userTimeLineAdapter.notifyItemRemoved(event.position);
    }

    private void handleEventClick() {
        userTimeLineAdapter.setRepostListener(post -> {
            showRepostDialog(post);
        });

        profile_button.setOnClickListener(v -> {
            startActivity(new Intent(UserTimeLineActivity.this, UserDetailActitvity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        timeline_button.setOnClickListener(v -> {
            startActivity(new Intent(UserTimeLineActivity.this, UserTimeLineActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        home_button.setOnClickListener(v -> {
            startActivity(new Intent(UserTimeLineActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        sign_out_button.setOnClickListener(v -> new MaterialDialog.Builder(this)
                .content("Are you sure you want to log out")
                .positiveText("Log out")
                .onPositive((dialog, which) -> {
                    firebaseAuth.signOut();
                    startActivity(new Intent(UserTimeLineActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .neutralText("Cancel")
                .show());
    }

    private void showRepostDialog(Post post) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(UserTimeLineActivity.this)
                .backgroundColorRes(R.color.colorPrimary)
                .customView(R.layout.repost_dialog, wrapInScrollView)
                .backgroundColorRes(R.color.greyBackGround)
                .positiveText("Post")
                .positiveColorRes(R.color.colorPrimary)
                .cancelable(true)
                .build();

        setUpViewforDialog(dialog, post);
    }

    private void setUpViewforDialog(MaterialDialog dialog, Post post) {
        etRepost = (MaterialEditText) dialog.findViewById(R.id.etRepost);
        imgPost = (ImageView) dialog.findViewById(R.id.imgPost);
        tvUsernamePost = (TextView) dialog.findViewById(R.id.tvUsernamePost);
        tvStatus = (TextView) dialog.findViewById(R.id.tvStatus);

        Glide.with(this).load(post.getPhotoUrl()).override(56, 56).animate(R.anim.slide_in_left).into(imgPost);
        tvUsernamePost.setText(post.getAuthor());
        tvStatus.setText(post.getContent());

        dialog.show();
    }

    private void setUpFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();
        postsDBRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText(StringUtils.usernameFromEmail(mFirebaseUser.getEmail()));
        userTimeLineAdapter = new UserTimeLineAdapter(this);
        rvPost.setAdapter(userTimeLineAdapter);
        rvPost.setLayoutManager(new GridLayoutManager(this, 2));
        rvPost.showShimmerAdapter();
    }

    private void setUpDrawerLayout() {
        //setting Event Icon in NavigationView
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        setUpNavigationDrawer();
    }

    private void setUpNavigationDrawer() {
        postsDBRef.child(FireBaseConfig.USERS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            if (currentUser.getProfileImageUrl() != null) {
                                Glide.with(UserTimeLineActivity.this)
                                        .load(currentUser.getProfileImageUrl())
                                        .override(58, 58)
                                        .centerCrop()
                                        .into(imgUser);
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        tvUsername.setText(StringUtils.usernameFromEmail(firebaseAuth.getCurrentUser().getEmail()));
        tvEmail.setText(firebaseAuth.getCurrentUser().getEmail());

    }

    ;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
