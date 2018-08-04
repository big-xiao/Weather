package com.example.administrator.iweather;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gson.Airquality;
import gson.Sun_time;
import gson.Weather;
import util.Utilty;

public class MainActivity extends AppCompatActivity {
ListView listView=null;
    String  currentPosition=null;
SwipeRefreshLayout swipeRefreshLayout=null;
    RecyclerView recyclerView=null;
    LocationClient mlocationClient=null;
    RecyclerView recyclerView1=null;
    AirQI_Progress airQIProgress=null;
    ScrollView scrollView=null;
    windmillView wind=null;
    sunTime_View sunview=null;
    BetterScrollView betterScrollView=null;
    comfortLev_Progress cfLev=null;
    Toolbar toolbar=null;
    ImageButton button=null;
    TextView toolbartitle=null;
    TextView t1=null;//目前温度
    TextView t2=null;//目前天气
    TextView t3=null;//最大和最小温度
    TextView t4=null;//空气质量


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
           switch (requestCode) {
               case 1:
               if (grantResults.length > 0) {
                   for (int result : grantResults) {
                       if (result != PackageManager.PERMISSION_GRANTED) {
                        System.exit(0);
                       }
                   }
               } else {
                   showError();
                   ;
               }
           }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

          initView();
          initData();
          initListenner();


  final  Handler myhandler=new windmillView.MyHandler(this,wind);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                myhandler.sendEmptyMessage(0x123);
            }
        }, 33, 33);




    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    public void initView()
    {

        List<String> list=new ArrayList<>();
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        cfLev=findViewById(R.id.myview2);
        airQIProgress =findViewById(R.id.myview);
        betterScrollView=findViewById(R.id.scrollView);
        scrollView=findViewById(R.id.scrollView);
        wind=findViewById(R.id.myview3);
        sunview=findViewById(R.id.myview4);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView1=findViewById(R.id.recyclerview2);
        swipeRefreshLayout=findViewById(R.id.main_srl);
        toolbartitle=findViewById(R.id.textView2);
        button=findViewById(R.id.imageButton2);
        t1=findViewById(R.id.textView);
        t2=findViewById(R.id.textView4);
        t3=findViewById(R.id.textView5);
        t4=findViewById(R.id.textView6);

        recyclerView1.setNestedScrollingEnabled(false);
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocatinListener());
         /*TODO:权限处理*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            list.add(Manifest.permission.READ_EXTERNAL_STORAGE) ;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED)
        {

            list.add(Manifest.permission.ACCESS_NETWORK_STATE) ;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(!list.isEmpty())
        {
            String[] tmp=list.toArray(new String[list.size()]);

            ActivityCompat.requestPermissions(this,tmp,1);
        }

    }
    public boolean isNetworkAvalible()
    {
        NetworkInfo info;
        ConnectivityManager manager= (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        info=manager.getActiveNetworkInfo();
        if(info==null||!info.isAvailable())
        {
            return false;
        }else
            return true;

    }
    public void initData()
    {
/*todo 网络请求的数据*/

        String cityname = getIntent().getStringExtra("param1");
        Log.i("button","come from searchActivity button"+cityname);
        if (!TextUtils.isEmpty(cityname)) {
            if(cityname.equals("当前位置"))
            {
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);              // 获得详细地址
                mlocationClient.setLocOption(option);
                mlocationClient.start();
            }else{
                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor=sh.edit();
                editor.putString("cityname",cityname);
                editor.apply();
                requestweather(cityname);
                requestAQI(cityname);
                requestSun_time(cityname);
            }


        }
            else
            {



                if (!isNetworkAvalible())
                {
                    Snackbar.make(swipeRefreshLayout, "当前无网络，无法刷新 %>_<% ",Snackbar.LENGTH_LONG)
                            .setAction("去设置网络", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            }).show();


                }else
                {    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
                    String weatherstr=sharedPreferences.getString("weathercontent",null);
                    String AQIstr=sharedPreferences.getString("AQIcontent",null);
                    String Sun_timestr=sharedPreferences.getString("Sun_timecontent",null);
                    currentPosition=sharedPreferences.getString("cityname",null);

                    if(weatherstr==null&&AQIstr==null&&Sun_timestr==null)//第一次进入
                    {
                        Log.i("dingwei","come initData() ");
                        LocationClientOption option = new LocationClientOption();
                        option.setIsNeedAddress(true);              // 获得详细地址
                        mlocationClient.setLocOption(option);
                        mlocationClient.start();

                        requestweather(currentPosition);
                        requestAQI(currentPosition);
                        requestSun_time(currentPosition);

                    }
                    else
                    {

                        Weather w=Utilty.handlergetWeather(weatherstr);
                        Airquality a=Utilty.handlergetAQI(AQIstr);
                        Sun_time s=Utilty.handlergetSun_time(Sun_timestr);
                        Log.i("responseText","填充数据  "+Sun_timestr);
                        setweatherinfo(w);
                        setAQIinfo(a);

                        setSun_timeinfo(s);


                    }



                }



            }




    }
    public void initListenner()
    {
        betterScrollView.setListenner(new BetterScrollView.ScrollViewListenner() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt,MotionEvent e) {


                int dp2=px2dp(MainActivity.this,t);
                switch (e.getAction()) {
                    case MotionEvent.ACTION_SCROLL:
                        break;
                    default:
                        if (dp2 > 200 && dp2 < 250) {
                            airQIProgress.startBaseAnimation();
                        } else if (dp2 > 400 && dp2 < 450) {
                            cfLev.startBaseAnimation();

                        } else if (dp2 > 900 && dp2 < 950) {
                            sunview.startBaseAnimation();
                        }
                }
            }
        });
/*todo 这里还有swiperefresh的监听器*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                         if (!isNetworkAvalible())
                         {
                             Snackbar.make(swipeRefreshLayout, "当前无网络，无法刷新 %>_<% ",Snackbar.LENGTH_LONG).setAction("去设置网络", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                     startActivity(intent);
                                 }
                             }).show();
                             swipeRefreshLayout.setRefreshing(false);
                         }else{
                             SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                             currentPosition=sh.getString("cityname",null);


                             if (currentPosition!=null)
                             {
                                 requestweather(currentPosition);
                                 requestAQI(currentPosition);
                                 requestSun_time(currentPosition);


                             }else
                             {
                               mlocationClient.restart();

                                 requestweather(currentPosition);
                                 requestAQI(currentPosition);
                                 requestSun_time(currentPosition);
                             }
                             swipeRefreshLayout.setRefreshing(false);

                         }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==button)
                {
                    Intent i=new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(i);
                }
            }
        });


    }
public  void  showError()
{
    AlertDialog.Builder builder=new AlertDialog.Builder(this);
    builder.setMessage("权限未授予");
    builder.setCancelable(false);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent=new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);

            System.exit(0);
        }
    });
    builder.show();



}
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
    public   int px2dp(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue/scale + 0.5f);
    }
    public void setListView(RecyclerView recyclerView, Weather w)//可能还需要一个参数,可以百度一下listview的优化
    {
        ArrayList<Integer> ID=new ArrayList<>();
        ArrayList<String> TIME=new ArrayList<>();
        ArrayList<String> TMP=new ArrayList<>();
        if(w.getHourly()==null)
        {
            Log.w("worry","这个类为空1");
            return;
        }
        for (Weather.HourlyBean bean:w.getHourly())
        {
            String weatherCode = "weather_"+bean.getCond_code();
            int resId = getResources().getIdentifier(weatherCode, "drawable", this.getPackageName());
            if (resId != 0){
                ID.add(resId);

            }
            TIME.add(bean.getTime());
            TMP.add(bean.getTmp()+"℃");
        }
        int id[]=new int[ID.size()];
        for(int i=0;i<ID.size();i++)
        {
            id[i]=ID.get(i);
        }



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        GalleryAdapter galleryAdapter=new GalleryAdapter(this,TIME,TMP,id);
         recyclerView.setAdapter(galleryAdapter);



    }
    public void setnormalListView(RecyclerView recyclerView,Weather w)//这个函数也许可以放在设置天气那个地方使用
    {
        ArrayList<Integer> ID=new ArrayList<>();
        ArrayList<String> DATE=new ArrayList<>();
        ArrayList<String> TMP=new ArrayList<>();
        if(w.getDaily_forecast()==null)
        {
            Log.w("worry","这个类为空2");
            return;
        }
        Weather.DailyForecastBean bean=null;
        for (int i=0;i<4;i++)
        {
            bean=w.getDaily_forecast().get(i);
            String weatherCode = "weather_"+bean.getCond_code_d();
            int resId = getResources().getIdentifier(weatherCode, "drawable", this.getPackageName());
            if (resId != 0){
                ID.add(resId);

            }
            DATE.add(bean.getDate());
            TMP.add(bean.getTmp_max()+"℃"+"/"+bean.getTmp_min()+"℃");
        }
        int id[]=new int[4];
        for(int i=0;i<ID.size();i++)
        {
            id[i]=ID.get(i);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
       FishAdapter galleryAdapter=new FishAdapter(this,DATE,TMP,id);
        recyclerView.setAdapter(galleryAdapter);

    }
  public void requestweather(String cityname)
    {

        String address="https://free-api.heweather.com/s6/weather?location="+cityname+"&key=a08ca2fe2ee646588161419de2cd55f9";

        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG);
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();

                   final Weather weather= Utilty.handlergetWeather(responseText);



runOnUiThread(new Runnable() {
    @Override
    public void run() {

        //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
        if(weather!=null&&"ok".equals(weather.getStatus()))
        {

            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor=sh.edit();

            editor.putString("weathercontent", responseText);

            editor.apply();

            setweatherinfo(weather);

        }else
        {
            Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG);
        }
    }
});

                }
            });

        }catch (JsonSyntaxException e)
        {
            e.printStackTrace();

        }
    }
    public void requestAQI(String cityname)
    {

        String address="https://free-api.heweather.com/s6/air/now?location="+cityname+"&key=a08ca2fe2ee646588161419de2cd55f9";

        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG);
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();

                    final Airquality airquality=Utilty.handlergetAQI(responseText);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                            if(airquality!=null&&"ok".equals(airquality.getStatus()))
                            {

                                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor=sh.edit();

                                editor.putString("AQIcontent", responseText);
                                editor.apply();

                                setAQIinfo(airquality);

                            }else
                            {
                                Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG);
                            }
                        }
                    });

                }
            });

        }catch (JsonSyntaxException e)
        {
            e.printStackTrace();

        }
    }
    public void requestSun_time(String cityname)
    {

        String address= "https://free-api.heweather.com/s6/solar/sunrise-sunset?location="+cityname+"&key=a08ca2fe2ee646588161419de2cd55f9";

        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();
                    Log.i("responseText","请求  "+responseText);
                    final Sun_time sun_time=Utilty.handlergetSun_time(responseText);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                            if(sun_time!=null&&"ok".equals(sun_time.getStatus()))
                            {

                                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor=sh.edit();

                                editor.putString("Sun_timecontent", responseText);
                                editor.apply();
                                Log.i("responseText","存储文件  "+sh.getString("AQIcontent",null));
                               setSun_timeinfo(sun_time);

                            }else
                            {
                                Toast.makeText(getApplicationContext(),"获取天气信息失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });

        }catch (JsonSyntaxException e)
        {
            e.printStackTrace();

        }
    }
public void setweatherinfo(Weather w)
{
toolbartitle.setText(w.getBasic().getLocation());
t1.setText(w.getNow().getTmp());
t2.setText(w.getNow().getCond_txt());
t3.setText(w.getDaily_forecast().get(0).getTmp_max()+"℃/"+w.getDaily_forecast().get(0).getTmp_min()+"℃");
   setListView(recyclerView,w);//这部分是在获得了天气之后设置的
    setnormalListView(recyclerView1,w);//用资源获取的方式
    cfLev.setFl(w.getNow().getFl());
    cfLev.setHum(w.getNow().getHum());
    cfLev.setUv(w.getDaily_forecast().get(0).getUv_index());

    wind.setWind(w.getNow().getWind_dir());
    wind.setWindlev(w.getNow().getWind_sc());


//TODO:将图片资源找到放入


}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stop();
    }

    public void setAQIinfo(Airquality a)
    {

        airQIProgress.setPm_10(a.getAir_now_city().getPm10());
        airQIProgress.setPm_25(a.getAir_now_city().getPm25());
        airQIProgress.setNo_2(a.getAir_now_city().getNo2());
        airQIProgress.setSo_2(a.getAir_now_city().getSo2());
        airQIProgress.setCo(a.getAir_now_city().getCo());
        airQIProgress.setO_3(a.getAir_now_city().getO3());
        airQIProgress.setNumstr(a.getAir_now_city().getAqi());
        airQIProgress.setSituationstr(a.getAir_now_city().getQlty());



    }
    public void setSun_timeinfo(Sun_time s)
    {
        String tmp;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date day=new Date(System.currentTimeMillis());

        tmp=s.getSun().get(0).getDate();
        try {

          sunview.setNowTime(df.format(day));
            sunview.setFirstTime(tmp + " "+s.getSun().get(0).getSr());
            sunview.setLastTime(tmp + " "+s.getSun().get(0).getSs());
            sunview.setFirstTime_str(s.getSun().get(0).getSr());
            sunview.setLastTime_str(s.getSun().get(0).getSs());
            sunview.calculate();
            Log.i("responseText","填充数据  "+s.getSun().get(0).getSs());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public class MyLocatinListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor=sh.edit();

           currentPosition = bdLocation.getCity();
            if (currentPosition ==null)
            {
                int type =mlocationClient.requestLocation();
                Log.i("baidujianshiqi",""+currentPosition);
            }
           Log.i("baidujianshiqi","come onReceibeLocation"+currentPosition);
            editor.putString("cityname",currentPosition);
            editor.apply();
            requestweather(currentPosition);
            requestSun_time(currentPosition);
            requestAQI(currentPosition);
            Toast.makeText(getApplicationContext(),"定位成功", Toast.LENGTH_LONG).show();
        }
    }

}
