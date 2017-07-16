package com.locanthach.sharefood.model;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Comment {

    private User user;
    private String content;
    private String timeStamp;
    private boolean liked;
    private int likeCount;

    public Comment() {
    }

    public Comment(User user, String content, String timeStamp, boolean liked, int likeCount) {
        this.user = user;
        this.content = content;
        this.timeStamp = timeStamp;
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
