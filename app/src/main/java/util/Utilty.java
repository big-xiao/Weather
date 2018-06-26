package util;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import gson.Weather;

public class Utilty {
    public Weather weather;

    public static Weather handlergetWeather(String responetext) {
        try {
            JSONObject jsonObject = new JSONObject(responetext);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}