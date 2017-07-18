package com.locanthach.sharefood.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.locanthach.sharefood.common.Constant;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by An Lee on 7/16/2017.
 */

@IgnoreExtraProperties
public class Post implements Serializable {
    public String uid;
    public String author;
    public String content;
    public String location;
    public String time;
    public String likeCount;
    public String status;
    public Map<String, Boolean> likes = new HashMap<>();

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
        result.put("id", uid);
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

    public String getRelativeTimeAgo(String date) {
        String twitterFormat = "MMM dd, YYYY H:mm:ss aa";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(date).getTime();
            relativeDate = DateUtils
                    .getRelativeTimeSpanString(dateMillis,
                            sf.parse(DateFormat.getDateTimeInstance().format(new Date())).getTime(),
                            DateUtils.SECOND_IN_MILLIS)
                    .toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getTime() {
        return getRelativeTimeAgo(time);
    }

    public String getLikeCount() {
        String likeCount = this.likeCount + " likes";
        if (hasOneLike()) {
            likeCount = this.likeCount + " like";
        }
        return likeCount;
    }

    private boolean hasOneLike() {
        return Integer.parseInt(likeCount) <= 1;
    }
}
