package com.locanthach.sharefood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.intro.SlideContent;
import com.locanthach.sharefood.intro.SlideHome;
import com.locanthach.sharefood.intro.SlideHowToUse;
import com.locanthach.sharefood.intro.SlideHowToUse2;

/**
 * Created by phant on 14-Jul-17.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(SlideHome.newInstance(R.layout.slide_intro_home));
        addSlide(SlideContent.newInstance(R.layout.slide_intro_content));
        addSlide(AppIntroFragment.newInstance("How to use", "ShareFood is really easy to use. Let's start!", R.drawable.how_to_use, ContextCompat.getColor(this,R.color.blueGrey)));
        addSlide(SlideHowToUse.newInstance(R.layout.slide_intro_to_use));
        addSlide(SlideHowToUse2.newInstance(R.layout.slide_intro_to_use_2));

        setDepthAnimation();
        showSkipButton(false);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        startActivity(new Intent(IntroActivity.this,NavigateSignInActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // animation
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        //Do Something when Slide Change
    }
}
