package com.f10company.barcodewidgetf10;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;

public class ForViewPagerSize extends ViewPager {

    public ForViewPagerSize(Context context) {
        super(context);
    }

    public ForViewPagerSize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }

            height = (int)((float)488/(float)1920*getResources().getDisplayMetrics().heightPixels); // 휴대폰 세로길이의 1920분의 488픽셀만큼 top margin

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
