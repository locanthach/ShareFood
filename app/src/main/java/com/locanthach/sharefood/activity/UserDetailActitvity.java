package com.locanthach.sharefood.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.ui.NumberAnimTextView;
import com.locanthach.sharefood.util.ParseRelativeData;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by phant on 25-Jul-17.
 */

public class UserDetailActitvity extends AppCompatActivity {

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.add_image_button)
    ImageView add_image_button;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.like_count)
    NumberAnimTextView like_count;
    @BindView(R.id.post_count)
    NumberAnimTextView post_count;
    @BindView(R.id.view_count)
    NumberAnimTextView view_count;

    public final static int PICK_PHOTO_CODE = 1046;
    private String userPhotoURL = null;
    private User user;
    private int mTotalLike = 0;
    private int mTotalView = 0;
    private int mTotalPost = 0;

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        setUpview();
        setUpFireBase();
        getCurrentUser();
        countTotalLike();
        countTotalView();
        countTotalPost();
        eventClickButton();
    }

    private void countTotalLike() {
        databaseReference.child(FireBaseConfig.USER_POSTS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int likeCount = Integer.parseInt(child.child("likeCount")
                                    .getValue(String.class));
                            mTotalLike = mTotalLike + likeCount;
                        }
                        if (ParseRelativeData.hasOne(String.valueOf(mTotalLike))) {
                            like_count.setText(String.valueOf(mTotalLike) + " like");
                        } else {
                            like_count.setText(String.valueOf(mTotalLike) + " likes");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void countTotalView() {
        databaseReference.child(FireBaseConfig.USER_POSTS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int viewCount = Integer.parseInt(child.child("viewCount")
                                    .getValue(String.class));
                            mTotalView = mTotalView + viewCount;
                        }
                        if (ParseRelativeData.hasOne(String.valueOf(mTotalView))) {
                            view_count.setText(String.valueOf(mTotalView) + " view");
                        } else {
                            view_count.setText(String.valueOf(mTotalView) + " views");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void countTotalPost() {
        databaseReference.child(FireBaseConfig.USER_POSTS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mTotalPost = (int) dataSnapshot.getChildrenCount();
                        if (ParseRelativeData.hasOne(String.valueOf(mTotalPost))) {
                            post_count.setText(String.valueOf(mTotalPost) + " post");
                        } else {
                            post_count.setText(String.valueOf(mTotalPost) + " posts");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getCurrentUser() {
//        String uID = FireBaseConfig.getUid();
        databaseReference.child(FireBaseConfig.USERS_CHILD)
                .child(FireBaseConfig.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            tvUsername.setText(user.getName());
                            if (user.getProfileImageUrl() != null) {
                                Glide.with(UserDetailActitvity.this)
                                        .load(user.getProfileImageUrl()).override(104, 104).fitCenter().into(imgUser);
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void eventClickButton() {
        add_image_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PICK_PHOTO_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            storageReference.child("images/" + photoUri.getLastPathSegment())
                    .putFile(photoUri)
                    .addOnFailureListener(e -> {
                        showError(e.getMessage());
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        userPhotoURL = String.valueOf(taskSnapshot.getDownloadUrl());
                        updatePhotoUser(userPhotoURL);
                    });
        }
    }

    private void updatePhotoUser(String userPhotoURL) {
        String userID = FireBaseConfig.getUid();
        User newUser = new User(user.getName(), user.getEmail(), userPhotoURL);
        Map<String, Object> updates = new HashMap<>();
        updates.put(userID, newUser);

        databaseReference.child(FireBaseConfig.USERS_CHILD)
                .updateChildren(updates, (databaseError, databaseReference1) -> {
                    if (databaseError == null) {
                        databaseReference.child(FireBaseConfig.USERS_CHILD).child(userID).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Get user value
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user != null) {
                                            tvUsername.setText(user.getName());
                                            if (user.getProfileImageUrl() != null) {
                                                Glide.with(UserDetailActitvity.this)
                                                        .load(user.getProfileImageUrl()).override(104, 104).fitCenter().into(imgUser);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                    }
                });
    }

    private void setUpFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void setUpview() {
        ButterKnife.bind(this);
    }

    private void showError(String error) {
        Toasty.error(this, error, Toast.LENGTH_SHORT, true).show();
    }
}
