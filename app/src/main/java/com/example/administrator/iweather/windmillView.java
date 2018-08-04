package com.example.administrator.iweather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.TextView;


/**
 * Created by Administrator on 2018/6/29.
 */

public class windmillView extends View{
  Paint mCirclePaint;
  Paint mTextPaint;private  Paint mownerPaint;
  private RectF rectF=new RectF();
  private float  radius;
  private float textstroke;
  private float  radiu;
  private  float strokewidth;
  private float extrawidth;
  private float topextrawidth;
  private float ownertroke;
  private float multiple;
  private static float Angleper=0;
  private float distance;
  private String ownerstr;
  private String wind;
    private String windlev;
    public windmillView(Context context) {
        super(context);

    }



    public windmillView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init( );
    }


    public windmillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( );
    }

   private  void init( )
   {
       extrawidth=dip2px(getContext(),10);
       strokewidth=dip2px(getContext(),30);
       topextrawidth=dip2px(getContext(),50);
       textstroke=dip2px(getContext(),15);
       distance=dip2px(getContext(),10);
       ownertroke=dip2px(getContext(),20);

   mCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
   mCirclePaint.setColor(Color.BLACK);
   mCirclePaint.setStrokeWidth(1f);
   mCirclePaint.setStyle(Paint.Style.FILL);

   mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
   mTextPaint.setColor(Color.BLACK);
   mTextPaint.setTextSize(textstroke);
   mTextPaint.setStyle(Paint.Style.FILL);


   mownerPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
   mownerPaint.setColor(Color.BLACK);
   mownerPaint.setStyle(Paint.Style.FILL);
   mownerPaint.setTextSize(ownertroke);
   multiple=0.6f;
   wind="";
   windlev="";
   ownerstr="风向风力";
   }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float temp_x;
        float temp_y;
        Rect bound=new Rect();
        Rect bound1=new Rect();
        Path path=new Path();
        float diameter=2f*radiu;
        float Angle=120;
        float miniradiu=diameter/100;
        float miniheight=diameter/2;
       canvas.drawCircle(rectF.centerX(),rectF.centerY(),miniradiu,mCirclePaint);//这部分先绘制出各个角度的风车
        drawwindmill(rectF.centerX(),rectF.centerY(),path,miniheight,miniradiu);
        canvas.rotate(Angleper,rectF.centerX(),rectF.centerY());
        canvas.drawPath(path,mCirclePaint);
        canvas.rotate(Angle,rectF.centerX(),rectF.centerY());
        canvas.drawPath(path,mCirclePaint);
        canvas.rotate(Angle,rectF.centerX(),rectF.centerY());
        canvas.drawPath(path,mCirclePaint);
        path.reset();
        canvas.rotate(Angle-Angleper,rectF.centerX(),rectF.centerY());
        drawstick(rectF.centerX(),rectF.centerY(),path,miniheight,miniradiu);
        canvas.drawPath(path,mCirclePaint);
        path.reset();
        diameter=1.5f*radiu;
       miniradiu=diameter/100;
       miniheight=diameter/2;

        float new_x=rectF.centerX()+2*miniheight/4;
        float new_y=rectF.centerY()+miniheight/6+2*miniradiu;
        canvas.drawCircle(new_x,new_y,miniradiu,mCirclePaint);

        drawwindmill(new_x,new_y,path,miniheight,miniradiu);
        canvas.rotate(Angleper,new_x,new_y);
        canvas.drawPath(path,mCirclePaint);
        canvas.rotate(Angle, new_x,new_y);
        canvas.drawPath(path,mCirclePaint);
        canvas.rotate(Angle, new_x,new_y);
        canvas.drawPath(path,mCirclePaint);
        path.reset();
        canvas.rotate(Angle-Angleper, new_x,new_y);


       drawstick(new_x,new_y,path,miniheight,miniradiu);
        canvas.drawPath(path,mCirclePaint);
        path.reset();
        mownerPaint.getTextBounds(ownerstr,0,ownerstr.length(),bound1);
        canvas.drawText(ownerstr,distance,bound1.height()+distance,mownerPaint);
        temp_x=rectF.centerX()+multiple*radiu/2+4*distance;
        temp_y=rectF.centerY();
        mTextPaint.getTextBounds("风向:",0,2,bound);
        canvas.drawText("风向:",rectF.centerX()+temp_x,temp_y,mTextPaint);
        canvas.drawText(wind,rectF.centerX()+temp_x+mTextPaint.measureText("风向:"),temp_y,mTextPaint);

        canvas.drawText("风力:",rectF.centerX()+temp_x,temp_y+2*distance+bound.height(),mTextPaint);
        canvas.drawText(windlev+"级",rectF.centerX()+temp_x+mTextPaint.measureText("风力:"),temp_y+2*distance+bound.height(),mTextPaint);



    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setWindlev(String windlev) {
        this.windlev = windlev;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        int height=getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(width,height);
        int min=Math.min(width,height);
        radius=min-strokewidth-extrawidth;
        radiu=radius-strokewidth-extrawidth;
        rectF.set(multiple*(strokewidth+extrawidth),topextrawidth,
                multiple*radius,multiple*radiu+topextrawidth);

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    private void drawstick(float  x,float  y,Path path,float miniheight,float miniradiu)//绘画风车杆子的路径
    {
        float standar_x=x+miniradiu;
        float standar_y=y+4*miniradiu;
        path.moveTo(standar_x,standar_y);
        path.quadTo(x,y+miniradiu,standar_x-2*miniradiu,standar_y);
        path.lineTo(standar_x-2*miniradiu,standar_y+miniheight/2);
        path.quadTo(x,y+miniheight/2+6*miniradiu,standar_x,standar_y+miniheight/2);
        path.close();
    }
    private void drawwindmill(float x,float y,Path path,float miniheight,float miniradiu)//绘画风车的路径
    {
        path.moveTo(x,y-miniradiu);
        path.cubicTo(x+miniheight/32,y-miniheight/16,
                x+miniheight/32,y-miniheight/8,x,y-miniheight/3);

        path.quadTo(x-miniheight/16,y-miniheight/16,x,y);

    }
   static final class MyHandler extends Handler
    {
        WeakReference<Activity>  mActivityReference;
        View view;
        public MyHandler(Activity activity,View view) {
            mActivityReference=new WeakReference<Activity>(activity);
            this.view=view;
        }


      @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivityReference.get();
            if (activity!=null&msg.what==0x123)
            {
                Angleper+=1f;
                if(Angleper>120)
                {
                    Angleper=0;
                }
                  view.invalidate();;
            }

            }



    }
}
