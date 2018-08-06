package com.example.administrator.iweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import gson.City;
import gson.Sun_time;
import util.Utilty;

public class SearchActivity extends AppCompatActivity {

   AutoCompleteTextView completeTextView=null;
    GridView gridView=null;
    Button button=null;
    String intent_cityname=null;
     ActionBar actionBar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initData();
        initListener();
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
    public void initView()
    {

        actionBar=getSupportActionBar();
        actionBar.setTitle("选择城市");
        completeTextView=findViewById(R.id.multiAutoCompleteTextView);
        gridView=findViewById(R.id.gridview);
        button=findViewById(R.id.button);

    }
    public void initData() {
        //TODO:自动提示框的数据最后放上

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Citystr = sharedPreferences.getString("TopCitycontent", null);

        if (!isNetworkAvalible()) {
            Toast.makeText(getApplicationContext(), "获取天气信息失败", Toast.LENGTH_LONG);


        } else {
            if (Citystr == null) {
                requestTopCity();
            } else {

                City c = Utilty.handlergetCity(Citystr);


                setgridView(gridView, c);


            }



        }
    }
    public void initListener()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==button)
                {


                        if(completeTextView.getText().toString().equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"请输入城市名", Toast.LENGTH_LONG).show();
                        }
                        else{
                            String []tmp=completeTextView.getText().toString().split(",");
                            requestCity(tmp[0].trim());


                        }



                }
            }
        });
completeTextView.addTextChangedListener(new TextWatcher() {
    DBAdapter dbAdapter=new DBAdapter(SearchActivity.this);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        City city=null;
        dbAdapter.open();

        city=dbAdapter.query(s.toString());

        if(city!=null)
        {

            setListView(completeTextView,city);

        }


        dbAdapter.close();


    }

    @Override
    public void afterTextChanged(Editable s) {


    }
    });

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String ,Object > map1=( Map<String ,Object >)parent.getAdapter().getItem(position);
           String tmp=(String)map1.get("name");
            Log.i("fuckcityname","tmp=="+tmp);

            actionstart(SearchActivity.this,tmp);

        }
    });

    }

    public  static void actionstart(Context context,String data1)
    {
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("param1",data1);
        context.startActivity(intent);

    }
    public   void setListView(AutoCompleteTextView completeTextView,City c)
    {


        List<String> list=new ArrayList<>();

          for (int i=0;i<c.getBasic().size();i++)
        {


            list.add(c.getBasic().get(i).getLocation()+","+c.getBasic().get(i).getParent_city()+","
                    +c.getBasic().get(i).getCnty()+","+c.getBasic().get(i).getAdmin_area());



        }


        ArrayAdapter<String> adapter=new ArrayAdapter(SearchActivity.this,R.layout.support_simple_spinner_dropdown_item,list);

        completeTextView.setAdapter(adapter);

    }
    public void requestCity( String cityname)
    {

        String address= "https://search.heweather.com/find?location="+cityname+"&key=a08ca2fe2ee646588161419de2cd55f9&mode=equal";

        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"获取城市信息失败", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();

                    final City city= Utilty.handlergetCity(responseText);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                            if(city!=null&&"ok".equals(city.getStatus()))
                            {
                                DBAdapter adapter=new DBAdapter(SearchActivity.this);
                                adapter.open();
                                adapter.insert(city);

                                intent_cityname=city.getBasic().get(0).getLocation();
                                setListView(completeTextView,city);

                                 adapter.close();
                                if(intent_cityname!=null)
                                { actionstart(SearchActivity.this,intent_cityname);}


                            }else
                            {


                                Toast.makeText(getApplicationContext(),"请输入正确的城市名", Toast.LENGTH_LONG).show();
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
    public   void setgridView(GridView gridView,City c)//这个函数也许可以放在设置天气那个地方使用
    {



        List<Map<String ,Object >> list=new ArrayList<>();
        Map<String ,Object > map1=new HashMap<>();
        map1.put("name","当前位置");
        list.add(map1);
        for (int i=0;i<c.getBasic().size();i++)
        {
            Map<String ,Object > map2=new HashMap<>();
            map2.put("name",c.getBasic().get(i).getLocation());
            list.add(map2);

            }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,list,R.layout.gridviewlayout,new String[]{"name"},new int[]{R.id.textView9});
        gridView.setAdapter(simpleAdapter);

        }







    public void requestTopCity()
    {

        String address= "https://search.heweather.com/top?group=cn&key=a08ca2fe2ee646588161419de2cd55f9&number=20";

        try{
            OkHttpClient Client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"获取城市信息失败", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();

                    final City city=Utilty.handlergetCity(responseText);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                            if(city!=null&&"ok".equals(city.getStatus()))
                            {


                                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
                                SharedPreferences.Editor editor=sh.edit();
                                DBAdapter adapter=new DBAdapter(SearchActivity.this);
                                adapter.open();
                                adapter.insert(city);
                                adapter.close();
                                editor.putString("TopCitycontent", responseText);
                                editor.apply();

                                setgridView(gridView,city);


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
}
