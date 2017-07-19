package com.locanthach.sharefood.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.locanthach.sharefood.common.Constant;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by An Lee on 7/16/2017.
 */

@IgnoreExtraProperties
public class Post implements Serializable {
    private String id;
    private String uid;
    private String author;
    private String content;
    private String location;
    private String time;
    private String likeCount;
    private String status;
    private Map<String, Boolean> likes = new HashMap<>();

    public Post() {
    }

    public Post(String uid, String author, String title, String location) {
        this.uid = uid;
        this.author = author;
        this.content = title;
        this.location = location;
        this.time = DateFormat.getDateTimeInstance().format(new Date());
        this.status = String.valueOf(Constant.STATUS_AVAIABLE);
        this.likeCount = String.valueOf(0);
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("content", content);
        result.put("location", location);
        result.put("time", time);
        result.put("likeCount", likeCount);
        result.put("likes", likes);
        result.put("status", status);

        return result;
    }
    // [END post_to_map]

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
