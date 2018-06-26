package com.example.administrator.iweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gson.Weather;
import util.Utilty;

public class MainActivity extends AppCompatActivity {
ListView listView=null;
    CircleProgress circleProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
      //  circleProgress=findViewById(R.id.myview);




        RecyclerView recyclerView=findViewById(R.id.recyclerview);
          setListView(recyclerView);
        RecyclerView recyclerView1=findViewById(R.id.recyclerview2);
        setnormalListView(recyclerView1);
    }
    public void setListView(RecyclerView recyclerView)//可能还需要一个参数,可以百度一下listview的优化
    {
        String time[]=new String[]{"18:00","21:00","00:00"};
        int id[]=new int[]{R.drawable.w100,R.drawable.w100,R.drawable.w100};
        String tmp[]=new String[]{"32℃","30℃","26℃"};



        List< String>  listitem1=new ArrayList<>();
        List< String >  listitem2=new ArrayList<>();
        for (int i=0;i<time.length;i++)
        {

            listitem1.add(time[i]);
            listitem2.add(tmp[i]);

        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        GalleryAdapter galleryAdapter=new GalleryAdapter(this,listitem1,listitem2,id);
         recyclerView.setAdapter(galleryAdapter);



    }
    public void setnormalListView(RecyclerView recyclerView)
    {
        int id[]=new int[]{R.drawable.w100,R.drawable.w100,R.drawable.w100};
        String date[]=new String[]{"6-23","6-24","6-25"};
        String mtmp[]=new String[]{"32℃/37℃","30℃/32℃","26℃/32℃"};
        List< String>  listitem1=new ArrayList<>();
        List< String >  listitem2=new ArrayList<>();
        for (int i=0;i<date.length;i++)
        {

            listitem1.add(date[i]);
            listitem2.add(mtmp[i]);

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
       FishAdapter galleryAdapter=new FishAdapter(this,listitem1,listitem2,id);
        recyclerView.setAdapter(galleryAdapter);

    }
  /*  public void requestweather(String cityname)
    {
        String address="https://free-api.heweather.com/s6/weather/now?location="+cityname+"&key=3235c6302ec54ead9e20eef9ded364c4";
        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(getApplicationContext(),"网络请求失败",Toast.LENGTH_LONG);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();
                   final Weather weather= Utilty.handlergetWeather(responseText);
                  Log.i("tag",weather.now.temperature);

                }
            });

        }catch (JsonSyntaxException e)
        {
            e.printStackTrace();

        }
    }*/

  /*  public void request(View view) {
        requestweather("zhanjiang");
    }*/
}
