package com.example.administrator.iweather;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/6/27.
 */

public class BetterScrollView extends ScrollView{
    public BetterScrollView(Context context) {
        super(context);
    }

    public BetterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BetterScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY*2);
    }
}
