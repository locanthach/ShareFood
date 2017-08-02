package com.locanthach.sharefood.util;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phant on 18-Jul-17.
 */

public class ParseRelativeData {
    public static String getRelativeTimeAgo(String date) {
        String twitterFormat = "MMM dd, yyyy h:mm:ss aa";
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

    public static long getRelativeTime(String date) {
        String twitterFormat = "MMM dd, yyyy h:mm:ss aa";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        long relative = 0;
        try {
            long dateMillis = sf.parse(date).getTime();
            long now = sf.parse(DateFormat.getDateTimeInstance().format(new Date())).getTime();
            relative = now - dateMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relative;
    }

    public static String getLikeCount(String likeCount) {
        String count = likeCount + " likes";
        if (hasOne(likeCount)) {
            count = likeCount + " like";
        }
        return count;
    }

    public static String getStringCount(String viewCount) {
        String count = viewCount + " views";
        if (hasOne(viewCount)) {
            count = viewCount + " view";
        }
        return count;
    }

    public static boolean hasOne(String count) {
        return Integer.parseInt(count) <= 1;
    }
}
