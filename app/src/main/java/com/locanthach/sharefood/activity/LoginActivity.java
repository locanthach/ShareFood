package com.locanthach.sharefood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.locanthach.sharefood.R;
import com.locanthach.sharefood.common.FireBaseConfig;
import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.util.StringUtils;
import com.locanthach.sharefood.util.ToastUtils;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtEmail) EditText edtEmail;
    @BindView(R.id.edtPassword) EditText edtPassword;
    @BindView(R.id.login_button) AppCompatButton login_button;
    @BindView(R.id.btnRegister) TextView btnRegister;
    @BindView(R.id.loader)
    MKLoader loader;
    @BindView(R.id.viewTrans)
    View viewTrans;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setUpView();
        handleClickEvent();
        setUpFireBase();
    }

    private void setUpView() {
        ToastUtils.toastConfigSuccess(getResources().getColor(R.color.teal700));
    }

    private void setUpFireBase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void handleClickEvent() {
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        login_button.setOnClickListener(v -> {
            loader.setVisibility(View.VISIBLE);
            viewTrans.setVisibility(View.VISIBLE);
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                loader.setVisibility(View.GONE);
                viewTrans.setVisibility(View.GONE);
                showError("Please enter email and password");
                return;
            }
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnFailureListener(e -> {
                        loader.setVisibility(View.GONE);
                        viewTrans.setVisibility(View.GONE);
                        showError(e.getMessage());
                    })
                    .addOnCompleteListener(this, task -> {
                        loader.setVisibility(View.GONE);
                        viewTrans.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        }
                    })
                    .addOnSuccessListener((AuthResult authResult) -> {
                        Toasty.success(this, "Signed in!", Toast.LENGTH_SHORT, true).show();
                        startActivity(MainActivity.getIntent(this));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    });
        });
    }

    private void showError(String error) {
        Toasty.error(this, error, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = StringUtils.usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        databaseReference.child(FireBaseConfig.USERS_CHILD).child(userId).setValue(user);
    }
    // [END basic_write]
}
