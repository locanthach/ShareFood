package com.locanthach.sharefood.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

/**
 * Created by phant on 24-Jul-17.
 */

public class RotateTransformation extends BitmapTransformation {
    private int rotateRotationAngle = 0;
    public RotateTransformation(Context context,  int rotateRotationAngle) {
        super(context);
        this.rotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int exifOrientationDegrees = getExifOrientationDegrees(rotateRotationAngle);
        return TransformationUtils.rotateImageExif(toTransform, pool, exifOrientationDegrees);
    }
    private int getExifOrientationDegrees(int orientation) {
        int exifInt;
        switch (orientation) {
            case 90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
//more cases
            default:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
        }
        return exifInt;
    }
    @Override
    public String getId() {
        return "rotate" + rotateRotationAngle;
    }
}
