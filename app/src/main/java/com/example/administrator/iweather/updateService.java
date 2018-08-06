package com.example.administrator.iweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import gson.Airquality;
import gson.Sun_time;
import gson.Weather;
import util.Utilty;

public class updateService extends Service {
    public updateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isupdate = sh.getBoolean("isUpdate", false);
        if (isupdate) {

            updatetweather();
            updatetAQI();
            updateSun_time();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            int updatetime = sh.getInt("updatetime", 60);
            int hourtime = updatetime * 1000 * 60;
            long triggerAtTime = SystemClock.elapsedRealtime() + hourtime;
            Intent i = new Intent(this, updateService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        }
        return super.onStartCommand(intent, flags, startId);

    }

    public void updatetweather() {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
        String cityname = sh.getString("cityname", null);
        String address = "https://free-api.heweather.com/s6/weather?location=" + cityname + "&key=a08ca2fe2ee646588161419de2cd55f9";

        try {
            OkHttpClient Client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utilty.handlergetWeather(responseText);
                    //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                    if (weather != null && "ok".equals(weather.getStatus())) {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString("weathercontent", responseText);
                        editor.apply();
                    }
                }
            });

        } catch (JsonSyntaxException e) {
            e.printStackTrace();

        }
    }

    public void updatetAQI() {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
        String cityname = sh.getString("cityname", null);
        String address = "https://free-api.heweather.com/s6/air/now?location=" + cityname + "&key=a08ca2fe2ee646588161419de2cd55f9";

        try {
            OkHttpClient Client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Airquality airquality = Utilty.handlergetAQI(responseText);
                    //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                    if (airquality != null && "ok".equals(airquality.getStatus())) {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString("AQIcontent", responseText);
                        editor.apply();
                    }
                }
            });

        } catch (JsonSyntaxException e) {
            e.printStackTrace();

        }
    }

    public void updateSun_time() {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
        String cityname = sh.getString("cityname", null);
        String address = "https://free-api.heweather.com/s6/solar/sunrise-sunset?location=" + cityname + "&key=a08ca2fe2ee646588161419de2cd55f9";

        try {
            OkHttpClient Client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }


                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseText = response.body().string();

                    final Sun_time sun_time = Utilty.handlergetSun_time(responseText);
                    //先向配置文件存储，然后设置天气信息,城市也应该保存，但是现在没有写到城市选择的页面，先默认湛江
                    if (sun_time != null && "ok".equals(sun_time.getStatus())) {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(updateService.this);
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString("Sun_timecontent", responseText);
                        editor.apply();
                    }

                }


            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();

        }

    }
}
