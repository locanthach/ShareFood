package com.locanthach.sharefood.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
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
import com.locanthach.sharefood.adapter.PostGridAdapter;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.dao.PostDAO;
import com.locanthach.sharefood.dao.UserDAO;
import com.locanthach.sharefood.fragment.MyDialogFragment;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.util.FileUtils;
import com.locanthach.sharefood.util.NetworkUtils;
import com.locanthach.sharefood.util.PermissionUtils;
import com.locanthach.sharefood.util.StringUtils;

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
import io.realm.Realm;

import static com.locanthach.sharefood.activity.PostDetailActivity.EXTRA_POST;
import static com.locanthach.sharefood.common.Constant.GRID_LAYOUT;
import static com.locanthach.sharefood.common.Constant.LINEAR_LAYOUT;

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
    private DatabaseReference usersDBRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private PostAdapter postAdapter;
    private PostGridAdapter postGridAdapter;
    private String mCurrentPhotoPath = null;
    private StorageReference mStorageReference;
    private List<Post> mPosts;
    private List<User> mUsers;
    private User currentUser;
    private PostDAO postDAO = new PostDAO();
    private UserDAO userDAO = new UserDAO();
    private int mCurrentView;
    private BottomSheetBehavior mBottomSheetBehavior;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.nvView) NavigationView navigationView;
    @BindView(R.id.shimmer_recycler_view) RecyclerView rvPost;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.sign_out_button) LinearLayout sign_out_button;
    @BindView(R.id.profile_button) LinearLayout profile_button;
    @BindView(R.id.home_button) LinearLayout home_button;
    @BindView(R.id.timeline_button) LinearLayout timeline_button;
    @BindView(R.id.imgUser) CircleImageView imgUser;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvEmail) TextView tvEmail;
    @BindView(R.id.progress_bar) ProgressBar progress_bar;
    @BindView(R.id.btnViewType) TextView btnViewType;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        setUpView();
        setUpFireBase();
        setUpDrawerLayout();

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //user already logged in
                //SHOW TIMELINE
                postAdapter.setCurrentUid(user.getUid());
                loadData();
                setUpNavigationDrawer();
                CHECK_FIRSTIME_USER_LOGIN = true;

            } else if ((user == null) && (CHECK_FIRSTIME_USER_LOGIN == false)) {
                setUpAppIntro();
            }
        };
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        setUpListenDataChange();
        setUpViewTypeButton();
    }

    private void setUpDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    private void setUpFireBase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        postsDBRef = firebaseDatabase.getReference();
        usersDBRef = firebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void setUpView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        postAdapter = new PostAdapter();
        postGridAdapter = new PostGridAdapter();
        linearLayout(true);

        setUpRefresh();
        handleClickEvent();

        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
    }

    private void setUpNavigationDrawer() {
        postsDBRef.child(FireBaseConfig.USERS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        currentUser = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            if (currentUser.getProfileImageUrl() != null) {
                                Glide.with(MainActivity.this)
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
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .neutralText("Cancel")
                .show());

        profile_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserDetailActitvity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        timeline_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserTimeLineActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        home_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void swipeToRefresh() {
        swipeContainer.setOnRefreshListener(() -> {
            loadData();
            swipeContainer.setRefreshing(false);
        });
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
        firebaseAuth.removeAuthStateListener(authStateListener);
        super.onPause();
    }

    private void setUpListenDataChange() {
        postsDBRef.child(FireBaseConfig.POSTS_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        if (hasNewPost(count) && postAdapter.getItemCount() > 0) {
                            Post newPost = new Post();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                newPost = child.getValue(Post.class);
                            }
                            sendNotification(newPost.getAuthor() + " shared food at "
                                    + newPost.getLocation());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Share Food Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private boolean hasNewPost(long count) {
        return count > postAdapter.getItemCount();
    }

    private void fetchPosts() {
        postsDBRef.child(FireBaseConfig.POSTS_CHILD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mPosts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Post post = child.getValue(Post.class);
                            post.setId(child.getKey());
                            child.getValue();
                            mPosts.add(post);
                        }
                        // Save data to Realm
                        postDAO.clearAll();
                        postDAO.storePosts(mPosts);

                        Collections.reverse(mPosts);
                        postAdapter.setPosts(mPosts);
                        postGridAdapter.setPosts(mPosts);
                        progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fetchUsers() {
        postsDBRef.child(FireBaseConfig.USERS_CHILD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUsers = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            user.setId(child.getKey());
                            child.getValue();
                            mUsers.add(user);
                        }
                        // Save data to Realm
                        userDAO.clearAll();
                        userDAO.storeUsers(mUsers);

                        postAdapter.setUsers(mUsers);
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
                    postGridAdapter.addPost(post);
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

    //Image on click listener
    @Subscribe
    public void onEvent(PostAdapter.ImagePostEvent event) {
        Intent intent = new Intent(MainActivity.this, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST, event.post);
        startActivity(intent);
    }

    //Like on click listener
    @Subscribe
    public void onEvent(PostAdapter.LikeEvent event) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        setUpLike(userId, event);
    }

    //Comment on click listener
    @Subscribe
    public void onEvent(PostAdapter.CommentClickEvent event) {
        //Todo something
        Post post = event.post;
//        Toast.makeText(this, "Comment clicked !!", Toast.LENGTH_SHORT).show();
        new MyDialogFragment();
        BottomSheetDialogFragment bottomSheetDialogFragment = MyDialogFragment.newInstance(post);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void setUpLike(String userId, PostAdapter.LikeEvent event) {
        Post post = event.post;
        String key = post.getId();
        if (isLiked(post, userId)) {
            post = disLike(userId, post);
            event.btnLike
                    .getDrawable()
                    .mutate()
                    .setColorFilter(this.getResources().getColor(R.color.blueGrey800),
                            PorterDuff.Mode.SRC_IN);
        } else {
            post = like(userId, post);
            event.btnLike
                    .getDrawable()
                    .mutate()
                    .setColorFilter(this.getResources().getColor(R.color.red400),
                            PorterDuff.Mode.SRC_IN);
        }
        event.tvLikeCount.setText(post.getLikeString());
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + post.getUid() + "/" + key, postValues);
        postsDBRef.updateChildren(childUpdates);
//        postAdapter.updatePost(post);
    }

    private Post like(String userId, Post post) {
        Post temp = post;
        Map<String, Boolean> likes = temp.getLikes();
        likes.put(userId, true);
        int likeCount = Integer.parseInt(temp.getLikeCount()) + 1;
        temp.setLikeCount(String.valueOf(likeCount));
        temp.setLikes(likes);
        return temp;
    }

    private Post disLike(String userId, Post post) {
        Post temp = post;
        Map<String, Boolean> likes = temp.getLikes();
        likes.put(userId, false);
        int likeCount = Integer.parseInt(temp.getLikeCount()) - 1;
        temp.setLikeCount(String.valueOf(likeCount));
        temp.setLikes(likes);
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
        postGridAdapter.setState(outState);
        outState.putString(KEEP_IMAGE_PATH, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentPhotoPath = savedInstanceState.getString(KEEP_IMAGE_PATH);
        List<Post> statePostList = postAdapter.getStateList(savedInstanceState);
        List<Post> statePostGridList = postGridAdapter.getStateList(savedInstanceState);
        postAdapter.setPosts(statePostList);
        postGridAdapter.setPosts(statePostGridList);
    }

    private void loadData() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            getDataOnline();
        } else {
            getDataOffline();
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataOnline() {
        fetchPosts();
        fetchUsers();
    }

    private void getDataOffline() {
        mPosts = postDAO.getAll();
        mUsers = userDAO.getAll();
        postAdapter.setPosts(mPosts);
        postAdapter.setUsers(mUsers);
        postGridAdapter.setPosts(mPosts);
    }

    private void setUpViewTypeButton() {
        btnViewType.setOnClickListener(v -> {
            if (mCurrentView == LINEAR_LAYOUT)
                linearLayout(false);
            else {
                linearLayout(true);
            }
        });
    }

    private void linearLayout(boolean type) {
        if (type) {
            rvPost.setAdapter(postAdapter);
            rvPost.setLayoutManager(new LinearLayoutManager(this));
            //set icon
            btnViewType.setBackground(this.getDrawable(R.drawable.ic_grid));
            mCurrentView = LINEAR_LAYOUT;
        } else {
            rvPost.setAdapter(postGridAdapter);
            rvPost.setLayoutManager(new GridLayoutManager(this, 2));
            //set icon
            btnViewType.setBackground(this.getDrawable(R.drawable.ic_linear));
            mCurrentView = GRID_LAYOUT;
        }
    }
}
