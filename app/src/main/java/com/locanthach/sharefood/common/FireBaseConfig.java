package com.locanthach.sharefood.common;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by phant on 16-Jul-17.
 */

public class FireBaseConfig {
    public static final String POSTS_CHILD = "posts";
    public static final String USERS_CHILD = "users";

    public static String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
