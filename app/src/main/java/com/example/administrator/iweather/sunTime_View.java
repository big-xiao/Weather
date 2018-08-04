package com.example.administrator.iweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.*;

/**
 * Created by Administrator on 2018/7/2.
 */

public class sunTime_View extends View {

    private Bitmap sun;
    private int bitmap_width;
    private int bitmap_height;
    private RectF rectF;
    private Paint mTextPaint;
    private Paint mArcPaint;
    private float leftmargin;
    private float  topextralwidth;
    private float textstrke;
    private float minitextstrke;
    private int width;
    private int height;
    private String Nowdate;
    private String firstTime_str;
    private String lastTime_str;
    private Date firstTime;
    private Date lastTime;
    private float radiu;
    private float mownerstrke;
    private float distance;
    private float Angle;
    private String ownerstr;
    private String NowTime_str;
    private Date NowTime;
    private float Angleper;
   private BaseAnimation change;
    private int TIME;

    public sunTime_View(Context context) {
        super(context);

    }

    public sunTime_View(Context context, @Nullable AttributeSet attrs) throws ParseException {
        super(context, attrs);
        init( );
    }

    public sunTime_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init( ) throws ParseException {
        sun= BitmapFactory.decodeResource(getResources(),R.drawable.sun);
        bitmap_width=sun.getWidth();
        bitmap_height=sun.getHeight();
        Angle=90;
        TIME=1000;
        Nowdate="2018-7-5";
        firstTime_str="7:00";
        lastTime_str="18:00";
        NowTime_str="10:37";
        firstTime=transfor_time(Nowdate+" "+firstTime_str);
        lastTime=transfor_time(Nowdate+" "+lastTime_str);
        NowTime=transfor_time(Nowdate+" "+NowTime_str);
        ownerstr="日出日落";
        mownerstrke=dip2px(getContext(),20);
        distance=dip2px(getContext(),15);
        minitextstrke=dip2px(getContext(),10);
        topextralwidth=dip2px(getContext(),80);
        leftmargin=dip2px(getContext(),30);
        rectF=new RectF();
        mArcPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(Color.BLACK);
        mArcPaint.setStrokeWidth(3);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));

       mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
       mTextPaint.setColor(Color.BLACK);
       mTextPaint.setStyle(Paint.Style.STROKE);
       mTextPaint.setStrokeWidth(3);
     change=new BaseAnimation();
     change.setDuration(TIME);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bound=new Rect();
        //偏移量
        float offset_x=bitmap_width/2;
        float offset_y=bitmap_height/2;
        float standar_x=rectF.centerX();
        float standar_y=rectF.centerY();
        float bitmap_x=(float)(standar_x-radiu*cos(Angle*PI/180));
        float bitmap_y=(float)(standar_y-radiu*sin(Angle*PI/180));
        float top=(height+topextralwidth)/2;
        float right=width;
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mArcPaint.setColor(Color.BLACK);
        canvas.drawArc(rectF,180,180,false,mArcPaint);
        canvas.drawLine(leftmargin,top,right-leftmargin,top,mTextPaint);
        mTextPaint.reset();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(minitextstrke);
        mTextPaint.getTextBounds(firstTime_str,0,firstTime_str.length(),bound);
        canvas.drawText(firstTime_str,standar_x-radiu,standar_y+bound.height()+distance,mTextPaint);
        canvas.drawText(lastTime_str,standar_x+radiu,standar_y+bound.height()+distance,mTextPaint);
        mArcPaint.setColor(Color.YELLOW);
        canvas.drawArc(rectF,180,Angle,false,mArcPaint);
       canvas.drawBitmap(sun,bitmap_x-offset_x,bitmap_y-offset_y,mArcPaint);

        mTextPaint.setTextSize(mownerstrke);

        canvas.drawText(ownerstr,distance,2*distance,mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         width=getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
         height=getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(width,height);
        int min= min(width,height);
        float baseheight=(topextralwidth+height)/2;
        radiu=width/2-2*leftmargin;//日出日落轨迹线的半径
        rectF.set(width/2-radiu,baseheight-radiu,width/2+radiu,baseheight+radiu);

    }


    public  void startBaseAnimation()
    {
        this.startAnimation(change);

    }
    public void setFirstTime(String firstTime) throws ParseException{
        this.firstTime=  transfor_time(firstTime);//通过外部把一整块字符串时间传入

    }


    public void setLastTime(String lastTime) throws ParseException {
        this.lastTime = transfor_time(lastTime);

    }

    public void setFirstTime_str(String firstTime_str) {
        this.firstTime_str = firstTime_str;
    }

    public void setLastTime_str(String lastTime_str) {
        this.lastTime_str = lastTime_str;
    }

    public void setNowTime(String nowTime)  throws ParseException{
        NowTime =transfor_time( nowTime);
    }
    public void calculate()
    { int through_time=timeBetween2(firstTime,NowTime);
        int all_time=timeBetween2(firstTime,lastTime);
        Log.i("suntime","firstTime"+firstTime+"NowTime"+NowTime);
        Log.i("suntime","through_time=="+through_time+"all_time"+all_time);
     if (through_time>=all_time)
     {
         Angle=180;
         Angleper=Angle;
         Log.i("suntime","angle=="+Angle);
     }else
     {
         Angle=180*through_time/all_time;
         Log.i("suntime","angle=="+Angle);
         Angleper=Angle;
     }


    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public Date  transfor_time(String time) throws ParseException {
        Date day=null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return day=df.parse(time);


    }
    public  int timeBetween2(Date startTime, Date endTime) {


        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;
        try{
            cal.setTime(startTime);
            time1 = cal.getTimeInMillis();
            cal.setTime(endTime);
            time2 = cal.getTimeInMillis();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        long between_minute=(time2-time1)/(1000*60);
        Log.i("suntime","starttime"+time1+"");
        Log.i("suntime","endtime"+time2+"");
        return Integer.parseInt(String.valueOf(between_minute));
    }
    class BaseAnimation extends Animation
    {
        public BaseAnimation() throws ParseException {
            super();

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);


            if (interpolatedTime<1)
            {
                Angle=interpolatedTime*Angleper;


                Log.i("tah","come here");
            }else
            {
                Angle=Angleper;


            }
            postInvalidate();
        }

    }

}
