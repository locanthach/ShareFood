package com.locanthach.sharefood.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.locanthach.sharefood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phant on 15-Jul-17.
 */

public class SlideHowToUse extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    @BindView(R.id.imgTakePhoto)
    ImageView imgTakePhoto;

    public static SlideHowToUse newInstance(int layoutResId) {
        SlideHowToUse slideHowToUse = new SlideHowToUse();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        slideHowToUse.setArguments(args);

        return slideHowToUse;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResId, container, false);
        ButterKnife.bind(this, view);
        YoYo.with(Techniques.Flash)
                .duration(5000)
                .repeat(-1)
                .playOn(imgTakePhoto);
        return view;
    }
}
