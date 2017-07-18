package com.locanthach.sharefood.model;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Comment {

    private String uid;
    private String content;
    private String time;

    public Comment() {
    }

    public Comment(String uid, String content, String time) {
        this.uid = uid;
        this.content = content;
        this.time = time;
    }
}
