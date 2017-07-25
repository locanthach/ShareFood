package com.locanthach.sharefood.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by phant on 25-Jul-17.
 */

public class SlantedView extends View {
    private Context mContext;
    Paint paint ;
    Path path;

    public SlantedView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth(), h = getHeight();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0,0);
        path.lineTo(0,h);
        path.lineTo(w,h);
        path.close();
        canvas.drawPath(path, paint);
    }
}
