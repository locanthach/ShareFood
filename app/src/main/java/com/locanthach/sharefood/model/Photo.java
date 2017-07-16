package com.locanthach.sharefood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by An Lee on 7/16/2017.
 */

public class Photo implements Parcelable{
    private String id;
    private String url;

    public Photo() {
    }

    public Photo(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected Photo(Parcel in) {
        id = in.readString();
        url = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
    }
}
