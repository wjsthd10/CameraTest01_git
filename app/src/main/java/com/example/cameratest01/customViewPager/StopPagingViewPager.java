package com.example.cameratest01.customViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;

public class StopPagingViewPager extends ViewPager {

    public boolean enabled;

    public StopPagingViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StopPagingViewPager(@NonNull Context context) {
        super(context);
        this.enabled=true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (enabled) {
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (enabled) {
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }



    public void setPagingEnabled(boolean enabled){
        this.enabled=enabled;
    }

}
