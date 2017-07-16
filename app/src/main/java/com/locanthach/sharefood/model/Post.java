package com.locanthach.sharefood.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Post implements Parcelable{
    private String id;
    private String title;
    private String location;
    private String timeStamp;
    private List<Photo> photos;
    private List<Comment> comments;
    private int likeCount;
    private boolean liked;
    private User user;

    public Post() {
    }

    public Post(String id, String title, String location, String timeStamp, List<Photo> photos,
                List<Comment> comments, int likeCount, boolean liked, User user) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.timeStamp = timeStamp;
        this.photos = photos;
        this.comments = comments;
        this.likeCount = likeCount;
        this.liked = liked;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected Post(Parcel in) {
        id = in.readString();
        title = in.readString();
        location = in.readString();
        timeStamp = in.readString();
        photos = in.createTypedArrayList(Photo.CREATOR);
        comments = in.createTypedArrayList(Comment.CREATOR);
        likeCount = in.readInt();
        liked = in.readByte() != 0;
        user = in.readParcelable(User.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(timeStamp);
        dest.writeTypedList(photos);
        dest.writeTypedList(comments);
        dest.writeInt(likeCount);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeParcelable(user, flags);
    }
}
