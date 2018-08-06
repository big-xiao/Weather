package com.example.administrator.iweather;

import android.content.Context;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/6/27.
 */

public class BetterScrollView extends ScrollView {
    private ScrollViewListenner listenner;
    MotionEvent ev;

    private int downX, downY;
    private int mTouchSlop;


    public BetterScrollView(Context context) {
        super(context);


    }

    public BetterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public BetterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }


public static interface ScrollViewListenner
{
   public void scrollOritention(int l, int t, int oldl, int oldt,MotionEvent e);

}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //返回false，则把事件交给子控件的onInterceptTouchEvent()处理
        int action = e.getAction();
        ev=e;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                int moveX=(int) e.getRawX();
                // 判断是否滑动，若滑动就拦截事件

                if ( Math.abs(moveX - downX) >Math.abs(moveY - downY)) {
                    return false;
                }
                else if (Math.abs(moveY - downY) > mTouchSlop)
                {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
    ev=e;

        return super.onTouchEvent(e);
      
    }
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY );//这里设置滑动的速度
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listenner!=null&&ev!=null)
        {
            listenner.scrollOritention(l, t, oldl,  oldt,ev);

        }
    }

    public void setListenner(ScrollViewListenner listenner) {
        this.listenner = listenner;
    }
}

