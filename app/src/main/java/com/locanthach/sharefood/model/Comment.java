package com.locanthach.sharefood.model;

import com.locanthach.sharefood.utils.ParseRelativeData;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Comment implements Serializable{

    private String uid;
    private String author;
    private String content;
    private String time;

    public Comment() {
    }

    public Comment(String uid, String author, String content) {
        this.uid = uid;
        this.author = author;
        this.content = content;
        this.time = DateFormat.getDateTimeInstance().format(new Date());
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRelativeTime() {
        return ParseRelativeData.getRelativeTimeAgo(time);
    }
}
