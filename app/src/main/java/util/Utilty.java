package util;


import android.util.Log;

import com.example.administrator.iweather.AirQI_Progress;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import gson.Airquality;
import gson.City;
import gson.Sun_time;
import gson.Weather;

public class Utilty {
    public Weather weather;
    public Airquality airquality;

    public static Weather handlergetWeather(String responetext) {
        try {
            Log.i("Utilty","here");
            JSONObject jsonObject = new JSONObject(responetext);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Airquality handlergetAQI(String responetext) {
        try {
            Log.i("Utilty","here");
            JSONObject jsonObject = new JSONObject(responetext);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String AQIrContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(AQIrContent, Airquality.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Sun_time handlergetSun_time(String responetext) {
        try {

            JSONObject jsonObject = new JSONObject(responetext);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String AQIrContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(AQIrContent, Sun_time.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static City handlergetCity(String responetext) {
        try {

            JSONObject jsonObject = new JSONObject(responetext);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String AQIrContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(AQIrContent, City.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}