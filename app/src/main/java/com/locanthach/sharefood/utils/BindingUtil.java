package com.locanthach.sharefood.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by An Lee on 7/23/2017.
 */

public class BindingUtil {
    @BindingAdapter("bind:image")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
