package com.locanthach.sharefood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.loader)
    MKLoader loader;
    @BindView(R.id.viewTrans)
    View viewTrans;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setUpView();
        setUpFireBase();
        handleClickEvent();
    }

    private void setUpView() {
        ToastUtils.toastConfigSuccess(getResources().getColor(R.color.teal700));
    }

    private void setUpFireBase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                loader.setVisibility(View.VISIBLE);
                viewTrans.setVisibility(View.VISIBLE);
                registerWithFirebase(email, password);
            }
        });
    }

    private void registerWithFirebase(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    loader.setVisibility(View.GONE);
                    viewTrans.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    }
                })
                .addOnSuccessListener(authResult -> {
                    Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .addOnFailureListener(e -> {
                    loader.setVisibility(View.GONE);
                    viewTrans.setVisibility(View.GONE);
                    showError(e.getMessage());
                });
    }

    private void showError(String error) {
        Toasty.error(this, error, Toast.LENGTH_SHORT, true).show();
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
