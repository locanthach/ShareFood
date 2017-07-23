package com.locanthach.sharefood.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.adapter.PostAdapter;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.utils.BitmapScaler;
import com.locanthach.sharefood.utils.FileUtils;
import com.locanthach.sharefood.utils.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    private final static String MY_CURRENT_IMAGE_PATH = "MY_IMAGE_PATH";
    //Firebase variable
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsDBRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private PostAdapter postAdapter;
    private String mCurrentPhotoPath;
    private StorageReference mStorageReference;

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
                //user already logged in
                //SHOW TIMELINE
                fetchPosts();

            } else {
                setUpAppIntro();
            }
        };
    }

    private void setUpDrawerLayout() {
        //setting Event Icon in NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            //selectDrawerItem(item);
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
            case R.id.action_walk:
                title = "Walk";
                break;
            case R.id.action_car:
                title = "Car";
                break;
            case R.id.action_bus:
                title = "Bus";
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
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void setUpView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        postAdapter = new PostAdapter(this);
        rvPost.setAdapter(postAdapter);
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        rvPost.showShimmerAdapter();


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
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void fetchPosts() {
        postsDBRef.child(FireBaseConfig.POSTS_CHILD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Post> posts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Post post = child.getValue(Post.class);
                            post.setId(child.getKey());
                            child.getValue();
                            posts.add(post);
                        }
                        Collections.reverse(posts);
                        postAdapter.setData(posts);
                        rvPost.hideShimmerAdapter();
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
        }
    }

    private void handleImageTaken() {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        intent.putExtra(MY_CURRENT_IMAGE_PATH,mCurrentPhotoPath);
        startActivity(intent);
    }


}
