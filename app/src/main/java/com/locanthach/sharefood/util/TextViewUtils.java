package com.locanthach.sharefood.util;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by phant on 18-Jul-17.
 */

public class TextViewUtils {
    public static int getWidth(TextView textView){
        textView.measure(0,0);
        return textView.getMeasuredWidth();
    }

    public static int getHeight(TextView textView){
        textView.measure(0,0);
        return textView.getMeasuredHeight();
    }

    public static void setWidth(TextView textView, int width){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
    }

    public static void setMargin(TextView textView,int start){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(start,0,0,0);//setMargins(left, top, right, bottom);
        textView.setLayoutParams(params);
    }
}
