package com.locanthach.sharefood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.locanthach.sharefood.common.Constant;
import com.locanthach.sharefood.util.ParseRelativeData;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.locanthach.sharefood.util.ParseRelativeData.getRelativeTimeAgo;
import static com.locanthach.sharefood.util.ParseRelativeData.getStringCount;

/**
 * Created by An Lee on 7/16/2017.
 */

@IgnoreExtraProperties
public class Post implements Parcelable {
    private String id;
    private String uid;
    private String author;
    private String content;
    private String photoUrl;
    private String location;
    private String time;
    private String likeCount;
    private String status;
    private String viewCount;
    private Map<String, Boolean> likes = new HashMap<>();

    public Post() {
    }

    public Post(String uid, String author, String title, String photoUrl, String location) {
        this.uid = uid;
        this.author = author;
        this.content = title;
        this.photoUrl = photoUrl;
        this.location = location;
        this.time = DateFormat.getDateTimeInstance().format(new Date());
        this.status = String.valueOf(Constant.STATUS_AVAILABLE);
        this.likeCount = String.valueOf(0);
        this.viewCount = String.valueOf(0);
    }

    public Post(String id, String uid, String author, String content, String photoUrl, String location, String time, String likeCount, String status, String viewCount, Map<String, Boolean> likes) {
        this.id = id;
        this.uid = uid;
        this.author = author;
        this.content = content;
        this.photoUrl = photoUrl;
        this.location = location;
        this.time = time;
        this.likeCount = likeCount;
        this.status = status;
        this.viewCount = viewCount;
        this.likes = likes;
    }

    protected Post(Parcel in) {
        final int N = in.readInt();
        for (int i = 0; i < N; i++) {
            String key = in.readString();
            Boolean value = Boolean.parseBoolean(in.readString());
            likes.put(key, value);
        }
        id = in.readString();
        uid = in.readString();
        author = in.readString();
        content = in.readString();
        photoUrl = in.readString();
        location = in.readString();
        time = in.readString();
        likeCount = in.readString();
        status = in.readString();
        viewCount = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("content", content);
        result.put("photoUrl", photoUrl);
        result.put("location", location);
        result.put("time", time);
        result.put("likeCount", likeCount);
        result.put("viewCount", viewCount);
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

    public String getRelativeTime() {
        return getRelativeTimeAgo(time);
    }

    public String getLikeString() {
        return ParseRelativeData.getLikeCount(likeCount);
    }

    public String getViewString() {
        return getStringCount(viewCount);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final int N = likes.size();
        dest.writeInt(N);
        if (N > 0) {
            for (Map.Entry<String, Boolean> entry : likes.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue().toString());
            }
        }
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(photoUrl);
        dest.writeString(location);
        dest.writeString(time);
        dest.writeString(likeCount);
        dest.writeString(status);
        dest.writeString(viewCount);
    }
}
