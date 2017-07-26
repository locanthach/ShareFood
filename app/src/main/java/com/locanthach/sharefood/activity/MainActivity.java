package com.locanthach.sharefood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.adapter.PostAdapter;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.utils.FileUtils;
import com.locanthach.sharefood.utils.PermissionUtils;
import com.locanthach.sharefood.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.locanthach.sharefood.activity.PostDetailActivity.EXTRA_POST;

public class MainActivity extends AppCompatActivity {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    private final static String MY_CURRENT_IMAGE_PATH = "MY_IMAGE_PATH";
    private final int REQUEST_POST_CODE = 20;
    private final static String NEW_POST = "NEW_POST";
    private String KEEP_IMAGE_PATH = "KEEP_IMAGE_PATH";
    private boolean CHECK_FIRSTIME_USER_LOGIN = false;
    //Firebase variable
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsDBRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private PostAdapter postAdapter;
    private String mCurrentPhotoPath = null;
    private List<Post> posts;

<<<<<<< HEAD
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.nvView) NavigationView navigationView;
    @BindView(R.id.shimmer_recycler_view) ShimmerRecyclerView rvPost;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.sign_out_button) LinearLayout sign_out_button;
    @BindView(R.id.profile_button) LinearLayout profile_button;
    @BindView(R.id.home_button) LinearLayout home_button;
    @BindView(R.id.imgUser) CircleImageView imgUser;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvEmail) TextView tvEmail;
=======
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView rvPost;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.sign_out_button)
    LinearLayout sign_out_button;
    @BindView(R.id.profile_button)
    LinearLayout profile_button;
>>>>>>> 3a8f98e6b244ff669e2ec8a12d6815706d0a37c9

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
        setUpFireBase();
        setUpDrawerLayout();

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                fetchPosts();
                setUpNavigationDrawer();
                CHECK_FIRSTIME_USER_LOGIN = true;

            } else if ((user == null) && (CHECK_FIRSTIME_USER_LOGIN == false)) {
                setUpAppIntro();
            }
        };
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


    }

    private void setUpDrawerLayout() {
        //setting Event Icon in NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private void selectDrawerItem(MenuItem item) {
        //Sample Code
        String title;
        switch (item.getItemId()) {
            case R.id.home_button:
                title = "Home";
                break;
            case R.id.signOut_button:
                title = "Sign out";
                break;
            default:
                title = "None";
                break;
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content, SimpleFragment.newInstance(title))
//                .commit();
        drawerLayout.closeDrawers();
        toolbar.setTitle(item.getTitle());
    }

    private void setUpFireBase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        postsDBRef = firebaseDatabase.getReference();
    }

    private void setUpView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        postAdapter = new PostAdapter(this);
        rvPost.setAdapter(postAdapter);
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        rvPost.showShimmerAdapter();

        setUpRefresh();
        handleClickEvent();


    }

    private void setUpNavigationDrawer() {
        if(firebaseAuth.getCurrentUser().getPhotoUrl()!= null){
            Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl())
                    .placeholder(R.drawable.ic_placeholder_user)
                    .override(58,58)
                    .centerCrop()
                    .into(imgUser);
        }
        tvUsername.setText(StringUtils.usernameFromEmail(firebaseAuth.getCurrentUser().getEmail()));
        tvEmail.setText(firebaseAuth.getCurrentUser().getEmail());

    }

    private void setUpRefresh() {
        swipeToRefresh();
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_dark);
    }

    private void handleClickEvent() {
        sign_out_button.setOnClickListener(v -> new MaterialDialog.Builder(this)
                .content("Are you sure you want to log out")
                .positiveText("Log out")
                .onPositive((dialog, which) -> {
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .neutralText("Cancel")
                .show());

        profile_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserDetailActitvity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        home_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserTimeLineActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void swipeToRefresh() {
        swipeContainer.setOnRefreshListener(() -> fetchPosts());
    }

    private void setUpAppIntro() {
        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void fetchPosts() {
        postsDBRef.child(FireBaseConfig.POSTS_CHILD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        posts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Post post = child.getValue(Post.class);
                            post.setId(child.getKey());
                            child.getValue();
                            posts.add(post);
                        }
                        Collections.reverse(posts);
                        postAdapter.setData(posts);
                        rvPost.hideShimmerAdapter();
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

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

    private void showError(String error) {
        Toasty.error(this, error, Toast.LENGTH_SHORT, true).show();
    }

    public void fab_button(View view) {
//        startActivity(new Intent(MainActivity.this, PostActivity.class));
        openCamera(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void openCamera(int requestCode) {
        if (PermissionUtils.checkExternal(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            File file = FileUtils.createPhotoFile(this);
            mCurrentPhotoPath = file.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.fromFile(this, file));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, requestCode);
            }
        } else {
            PermissionUtils.requestExternal(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                handleImageTaken();
            }
            if (requestCode == REQUEST_POST_CODE) {
                Post post = data.getExtras().getParcelable(NEW_POST);
                if (post != null) {
                    postAdapter.addPost(post);
                    rvPost.smoothScrollToPosition(0);
                }
            }
        }
    }

    private void handleImageTaken() {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        intent.putExtra(MY_CURRENT_IMAGE_PATH, mCurrentPhotoPath);
        startActivityForResult(intent, REQUEST_POST_CODE);
    }

    @Subscribe
    public void onEvent(PostAdapter.ImagePostEvent event) {
        Intent intent = new Intent(MainActivity.this, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST, event.post);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(PostAdapter.LikeEvent event) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        setUpLike(userId, event);
    }

    private void setUpLike(String userId, PostAdapter.LikeEvent event) {
        Post post = event.post;
        String key = post.getId();
        if (isLiked(post, userId)) {
            post = disLike(userId, post);
            event.btnLike.setBackgroundResource(R.color.blueGrey800);
        } else {
            post = like(userId, post);
            event.btnLike.setBackgroundResource(R.color.red400);
=======
        setUpLike(userId, event.post);
    }

    private void setUpLike(String userId, Post post) {
        String key = post.getId();
        if (isLiked(post, userId)) {
            post = disLike(userId, post);
        } else {
            post = like(userId, post);
>>>>>>> 3a8f98e6b244ff669e2ec8a12d6815706d0a37c9
        }
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + post.getUid() + "/" + key, postValues);

        postsDBRef.updateChildren(childUpdates);
        postAdapter.updatePost(post);
    }

    private Post like(String userId, Post post) {
        Post temp = post;
        Map<String, Boolean> likes = temp.getLikes();
        likes.put(userId, true);
        int likeCount = Integer.parseInt(temp.getLikeCount()) + 1;
        temp.setLikeCount(String.valueOf(likeCount));
        temp.setLikes(likes);
        Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
        return temp;
    }

    private Post disLike(String userId, Post post) {
        Post temp = post;
        Map<String, Boolean> likes = temp.getLikes();
        likes.put(userId, false);
        int likeCount = Integer.parseInt(temp.getLikeCount()) - 1;
        temp.setLikeCount(String.valueOf(likeCount));
        temp.setLikes(likes);
        Toast.makeText(this, "dislike", Toast.LENGTH_SHORT).show();
        return temp;
    }

    private boolean isLiked(Post post, String userId) {
        Map<String, Boolean> likes = post.getLikes();
        if (!likes.containsKey(userId)) {
            likes.put(userId, false);
        }
        return likes.get(userId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        postAdapter.setState(outState);
        outState.putString(KEEP_IMAGE_PATH, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentPhotoPath = savedInstanceState.getString(KEEP_IMAGE_PATH);
        List<Post> statePostList = postAdapter.getStateList(savedInstanceState);
        postAdapter.setData(statePostList);
    }
}
