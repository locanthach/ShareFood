package com.locanthach.sharefood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Comment implements Parcelable{
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

    protected Comment(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        content = in.readString();
        timeStamp = in.readString();
        liked = in.readByte() != 0;
        likeCount = in.readInt();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(content);
        dest.writeString(timeStamp);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeInt(likeCount);
    }
}
