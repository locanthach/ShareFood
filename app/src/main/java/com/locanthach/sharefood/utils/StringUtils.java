package com.locanthach.sharefood.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by An Lee on 7/16/2017.
 */

public class StringUtils {
    public static String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public static boolean validateForm(EditText mEmailField, EditText mPasswordField) {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }
}
