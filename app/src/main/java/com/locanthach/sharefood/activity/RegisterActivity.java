package com.locanthach.sharefood.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.locanthach.sharefood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtConfirmation)
    EditText edtConfirmation;
    @BindView(R.id.sign_up_button)
    Button sign_up_button;
    @BindView(R.id.btnLogin)
    TextView btnLogin;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setUpFireBase();
        handleClickEvent();
    }

    private void setUpFireBase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void handleClickEvent() {
        btnLogin.setOnClickListener(v -> {
            startActivity(LoginActivity.getIntent(this));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        sign_up_button.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmation = edtConfirmation.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showError("Email is empty");
            } else if (password.length() < 8) {
                showError("Password is too short");
            } else if (!TextUtils.equals(password, confirmation)) {
                showError("Confirmation is not match");
            } else {
                registerWithFirebase(email, password);
            }
        });
    }

    private void registerWithFirebase(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    startActivity(MainActivity.getIntent(this));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .addOnFailureListener(e -> showError(e.getMessage()));
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
