package com.locanthach.sharefood.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.service.MyFirebaseMessagingService;
import com.locanthach.sharefood.utils.FileUtils;
import com.locanthach.sharefood.utils.PhotoUtils;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    private final static String MY_CURRENT_IMAGE_PATH = "MY_IMAGE_PATH";
    private final static String NEW_POST = "NEW_POST";

    //FIREBASE VARIABLES
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private MyFirebaseMessagingService fmService;

    //MY VARIABLES
    private String mCurrentPhotoUri = null;
    private String mCurrentPhotoPath = null;

    @BindView(R.id.edtStatus) EditText mTitle;
    @BindView(R.id.edtLocation) EditText mLocation;
    @BindView(R.id.share_button) FrameLayout mSubmitButton;
    @BindView(R.id.imgUpload) ImageView imgUpload;
    @BindView(R.id.back_button) FrameLayout back_button;
    @BindView(R.id.progressBar) CardView progressBar;
    @BindView(R.id.progressBarTitle) TextView progressBarTitle;
    @BindView(R.id.transparentView) View transparentView;
    @BindView(R.id.materialText) MaterialTextField materialText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        getCurrentPhotoPath();
        setupFireBase();
        setUpViews();
        submitButton();
        backButton();

    }

    private void uploadImage(String userId, User user, String title, String location) {
        Uri file = FileUtils.fromFile(this, mCurrentPhotoPath);
        storageReference.child("images/" + file.getLastPathSegment())
                .putFile(file)
                .addOnFailureListener(e -> {
                    showError(e.getMessage());
                })
                .addOnSuccessListener(taskSnapshot -> {
                    mCurrentPhotoUri = String.valueOf(taskSnapshot.getDownloadUrl());
                    writeNewPost(userId, user.getName(), title, location, mCurrentPhotoUri);
                });
    }

    private void submitPost() {
        showPrgoressBar();
        mLocation.setEnabled(false);
        materialText.reduce();
        final String title = mTitle.getText().toString();
        final String location = mLocation.getText().toString();
        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitle.setError(REQUIRED);
            hideProgressBar();
            return;
        }
        // Body is required
        if (TextUtils.isEmpty(location)) {
            mLocation.setError(REQUIRED);
            hideProgressBar();
            return;
        }

        // [START single_value_read]
        final String userId = FireBaseConfig.getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(PostActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            uploadImage(userId, user, title, location);
                        }
                        // Finish this Activity, back to the stream
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
//                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String location, String photoUri) {
        //new post

        String key = mDatabase.child(FireBaseConfig.POSTS_CHILD).push().getKey();
        Post post = new Post(userId, username, title, photoUri, location);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        mDatabase.child(FireBaseConfig.POSTS_CHILD)
                .child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post newPost = dataSnapshot.getValue(Post.class);
                        newPost.setId(dataSnapshot.getKey());
                        // send notification to Firebase Message
                        sendNotification(newPost.getAuthor(), newPost.getLocation());
                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                        intent.putExtra(NEW_POST, newPost);
                        setResult(RESULT_OK, intent);
                        hideProgressBar();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    //send notification to Firebase Message
    private void sendNotification(String author, String location) {
        String message = author + " has shared food at " + location;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Share Food Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void showError(String error) {
        Toasty.error(this, error, Toast.LENGTH_SHORT, true).show();
    }

    private void hideProgressBar() {
        transparentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        progressBarTitle.setVisibility(View.GONE);
    }

    private void showPrgoressBar() {
        transparentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progressBarTitle.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        PhotoUtils.takeImage(this, imgUpload, mCurrentPhotoPath);
        progressBarTitle.setTypeface(EasyFonts.robotoLight(this));
        hideProgressBar();

    }

    private void getCurrentPhotoPath() {
        mCurrentPhotoPath = getIntent().getStringExtra(MY_CURRENT_IMAGE_PATH);
    }

    private void backButton() {
        back_button.setOnClickListener(v -> {
            finish();
        });
    }

    private void submitButton() {
        mSubmitButton.setOnClickListener(v -> submitPost());
    }

    private void setupFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
}
