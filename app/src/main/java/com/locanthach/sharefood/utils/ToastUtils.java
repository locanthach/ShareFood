package com.locanthach.sharefood.utils;

import android.support.annotation.ColorInt;

import es.dmoral.toasty.Toasty;

/**
 * Created by phant on 18-Jul-17.
 */

public class ToastUtils {
    public static void toastConfigSuccess(@ColorInt int successColor){
        Toasty.Config.getInstance()
                .setSuccessColor(successColor)
                .apply();
    }

    public static void resetConfig(){
        Toasty.Config.reset();
    }
}
