package com.locanthach.sharefood.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by phant on 24-Jul-17.
 */

public class BindingUtil {
    @BindingAdapter({"image"})
    public static void loadImage( ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .animate(android.R.anim.slide_in_left)
                .into(imageView);
    }
}
