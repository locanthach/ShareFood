package com.locanthach.sharefood.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.locanthach.sharefood.activity.MainActivity;
import com.locanthach.sharefood.activity.PostActivity;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

/**
 * Created by phant on 23-Jul-17.
 */

public class PhotoUtils {
    public static void takeImage(Context context, ImageView imageView, String mCurrentPhotoPath) {
        Bitmap rotatedBitmap = FileUtils.rotateBitmapOrientation(mCurrentPhotoPath);
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rotatedBitmap, 500);
        try {
            FileUtils.store(resizedBitmap, mCurrentPhotoPath);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        imageView.setImageBitmap(resizedBitmap);
    }

//    private void uploadImage() {
//        Uri file = FileUtils.fromFile(this, mCurrentPhotoPath);
//        mStorageReference.child("images/" + file.getLastPathSegment())
//                .putFile(file)
//                .addOnFailureListener(e -> {
//                    showError(e.getMessage());
//                })
//                .addOnSuccessListener(taskSnapshot -> {
//                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
//                    intent.putExtra(MY_CURRENT_IMAGE_URI,taskSnapshot.getDownloadUrl());
//                    startActivity(intent);
//                });
//    }
}
