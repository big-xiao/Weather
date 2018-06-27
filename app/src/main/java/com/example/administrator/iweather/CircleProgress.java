package com.example.administrator.iweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2018/6/23.
 */

public class CircleProgress extends View{

       private   int TIME;
        private  Paint mDeafaultPaint;
        private  Paint mColorPaint;
        private  Paint mtextPaint;//某些文字字体
        private  Paint mnumPaint;//数值字体
        private  Paint mownerPaint;//绘画组件的使用者和数值字体
        private  Paint airPaint;//绘画某些气体的字体

        private RectF rectF=new RectF();

        private float Angleper;
        private float Angle;
        private float  radius;//为了绘制矩形区域特地编写的，和半径撤了一些特定的zhi
        private float  radiu;//真正的半径
        private  float strokewidth;
        private float extrawidth;
        private float textstroke;
        private  float numstroke;
        private  float ownertroke;
        private  float airnumstroke;
        private String typestr;
        private String situationstr;
        private String numstr;
        private String ownerstr;
        private String startnum;
        private String endnum;
        private String tmpstr;
        private float distance;
        private float topextrawidth;
        private float multiple;
        private String pm_10;
        private String pm_25;
        private String no_2;;
        private String so_2;
        private String co;
        private String o_3;
        private BaseAnimation change;
    public CircleProgress(Context context) {
        super(context);
        init(null,0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,0);
    }
    private void init(AttributeSet attrs, int defStyleAttr)
    {
             Angle=90;
             multiple=0.6f;
             airnumstroke=dip2px(getContext(),15);
             distance=dip2px(getContext(),10);
             extrawidth=dip2px(getContext(),10);
             topextrawidth=dip2px(getContext(),80);
             strokewidth=dip2px(getContext(),30);
             ownertroke=dip2px(getContext(),20);
             textstroke=dip2px(getContext(),15);
             numstroke=dip2px(getContext(),25);
             mDeafaultPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mDeafaultPaint.setColor(Color.BLACK);
             mDeafaultPaint.setStyle(Paint.Style.STROKE);
             mDeafaultPaint.setStrokeWidth(30);
             mDeafaultPaint.setStrokeCap(Paint.Cap.ROUND);

             mColorPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mColorPaint.setColor(Color.RED);
             mColorPaint.setStyle(Paint.Style.STROKE);
             mColorPaint.setStrokeWidth(30);
             mColorPaint.setStrokeCap(Paint.Cap.ROUND);

             mtextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mtextPaint.setColor(Color.BLACK);
             mtextPaint.setStyle(Paint.Style.FILL);
             mtextPaint.setTextSize(textstroke);

             mownerPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mownerPaint.setColor(Color.BLACK);
             mownerPaint.setStyle(Paint.Style.FILL);
             mownerPaint.setTextSize(ownertroke);

             mnumPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mnumPaint.setColor(Color.BLACK);
             mnumPaint.setStyle(Paint.Style.FILL);
             mnumPaint.setTextSize(numstroke);

             airPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
             airPaint.setColor(Color.BLUE);
             airPaint.setStyle(Paint.Style.FILL);
             airPaint.setTextSize(airnumstroke);
             typestr="污染情况";
             situationstr="良好";
             numstr="50";
             tmpstr=numstr;
             ownerstr="空气质量";
             startnum="0";
             endnum="500";
             pm_10="30";
             pm_25="36";
             so_2="22";
             no_2="15";
             co="26";
             o_3="12";
             TIME=1000;
             Angle=Float.parseFloat(numstr)/(Float.parseFloat(endnum)-Float.parseFloat(startnum))*300f;
             Angleper=Angle;
        change=new BaseAnimation();
        change.setDuration(TIME);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         float standar_y;
        float standar_x;
        float tempvalue;
        canvas.drawArc(rectF,120,300,false,mDeafaultPaint);
        canvas.drawArc(rectF,120,Angle,false,mColorPaint);
        Rect textbound1=new Rect();
        Rect textbound2=new Rect();
        Rect textbound3=new Rect();
        Rect textbound4=new Rect();
        Rect textbound5=new Rect();
        Rect numbound=new Rect();
        Rect airbound=new Rect();
     mtextPaint.getTextBounds(typestr,0,typestr.length(),textbound1);
       mtextPaint.getTextBounds(situationstr,0,situationstr.length(),textbound2);
        mtextPaint.getTextBounds(ownerstr,0,ownerstr.length(),textbound3);
        mnumPaint.getTextBounds(numstr,0,numstr.length(),numbound);

        Log.i("tag",textbound1.toString());
        canvas.drawText(ownerstr,distance,textbound3.height()+distance,mownerPaint);
     canvas.drawText(typestr,rectF.centerX()-(mtextPaint.measureText(typestr)/2),
                rectF.centerY()-textbound2.height()-multiple*radiu/2-distance,mtextPaint);

     standar_x=standar_y=rectF.centerY()-textbound2.height()-multiple*radiu/2-distance;
     standar_x=multiple*radiu/2;//圆环的半径

     canvas.drawText(situationstr,rectF.centerX()-(mtextPaint.measureText(situationstr)/2),
                rectF.centerY()-textbound2.height(),mtextPaint);
        canvas.drawText(numstr,rectF.centerX()-(mnumPaint.measureText(numstr)/2),
                rectF.centerY()+numbound.height(),mnumPaint);
        mownerPaint.setStrokeWidth(10);
        mownerPaint.getTextBounds(startnum,0,startnum.length(),textbound4);
        mownerPaint.getTextBounds(endnum,0,endnum.length(),textbound5);
        canvas.drawText(startnum,rectF.centerX()-multiple*radiu/4-mownerPaint.measureText(startnum)/2
                ,rectF.centerY()+multiple*radiu/2+textbound4.height(),mownerPaint);
        canvas.drawText(endnum,rectF.centerX()+multiple*radiu/4-mownerPaint.measureText(startnum)
                ,rectF.centerY()+multiple*radiu/2+textbound4.height(),mownerPaint);

        canvas.drawText("PM10:",rectF.centerX()+standar_x+4*distance,standar_y,mtextPaint);
        canvas.drawText(pm_10,rectF.centerX()+standar_x+4*distance+airPaint.measureText("PM10:"),standar_y,airPaint);

        airPaint.getTextBounds(pm_25,0,pm_25.length(),airbound);
        tempvalue=standar_y+2*distance+airbound.height();
        canvas.drawText("PM25:",rectF.centerX()+standar_x+4*distance,tempvalue,mtextPaint);
        canvas.drawText(pm_25,rectF.centerX()+standar_x+4*distance+airPaint.measureText("PM25:"),
                tempvalue,airPaint);

        airPaint.getTextBounds(no_2,0,no_2.length(),airbound);
        tempvalue=tempvalue+2*distance+airbound.height();
        canvas.drawText("NO₂:",rectF.centerX()+standar_x+4*distance,tempvalue,mtextPaint);
        canvas.drawText(no_2,rectF.centerX()+standar_x+4*distance+airPaint.measureText("NO₂:"),
                tempvalue,airPaint);

        airPaint.getTextBounds(so_2,0,so_2.length(),airbound);
        tempvalue=tempvalue+2*distance+airbound.height();
        canvas.drawText("SO₂:",rectF.centerX()+standar_x+4*distance,tempvalue,mtextPaint);
        canvas.drawText(so_2,rectF.centerX()+standar_x+4*distance+airPaint.measureText("SO₂:"),
                tempvalue,airPaint);

        airPaint.getTextBounds(co,0,co.length(),airbound);
        tempvalue=tempvalue+2*distance+airbound.height();
        canvas.drawText("CO:",rectF.centerX()+standar_x+4*distance,tempvalue,mtextPaint);
        canvas.drawText(so_2,rectF.centerX()+standar_x+4*distance+airPaint.measureText("CO:"),
                tempvalue,airPaint);

        airPaint.getTextBounds(o_3,0,o_3.length(),airbound);
        tempvalue=tempvalue+2*distance+airbound.height();
        canvas.drawText("O₃:",rectF.centerX()+standar_x+4*distance,tempvalue,mtextPaint);
        canvas.drawText(o_3,rectF.centerX()+standar_x+4*distance+airPaint.measureText("O₃:"),
                tempvalue,airPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getDefaultSize(getMinimumWidth(),widthMeasureSpec);
        int height=getDefaultSize(getMinimumHeight(),heightMeasureSpec);
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
    public void setNumstr(String i)
    {
      numstr=i;

    }
    public void startBaseAnimation()
    {
this.startAnimation(change);

    }
class BaseAnimation extends Animation
{
    public BaseAnimation() {
        super();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        int count;
        if (interpolatedTime<1)
        {
            Angle=interpolatedTime*Angleper;
            count=(int)(interpolatedTime*Integer.parseInt(tmpstr));
            numstr=String .valueOf(count);
            Log.i("tah","come here");
        }else
        {
            Angle=Angleper;
            count=Integer.parseInt(tmpstr);
            numstr=String .valueOf(count);

        }
        postInvalidate();
    }
}
}
