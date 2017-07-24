package com.locanthach.sharefood.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by phant on 24-Jul-17.
 */

public class BindingUtil {
    @BindingAdapter("bind:image")
    public static void loadImage(Context context, ImageView imageView, String url) {
//        PhotoUtils.takeImage(context, imageView, url);
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .animate(android.R.anim.slide_in_left)
//                .transform(new RotateTransformation(context, 90))
                .into(imageView);
    }
}
