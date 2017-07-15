package com.locanthach.sharefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigateSignInActivity extends AppCompatActivity {

    @BindView(R.id.google_button)
    Button google_button;
    @BindView(R.id.email_button)
    Button email_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        handleButtonClickEvent();
    }

    private void handleButtonClickEvent() {
        google_button.setOnClickListener(v -> {
            startActivity(new Intent(NavigateSignInActivity.this,LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // animation
        });
        email_button.setOnClickListener(v -> {
            startActivity(new Intent(NavigateSignInActivity.this,LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // animation
        });
    }
}
