package com.example.administrator.iweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gson.City;

/**
 * Created by Administrator on 2018/7/27.
 */
public class DBAdapter  {
    private   final Context context;
    private static final String DB_NAME="city.db";
    private static final String DB_TABLE=" Cityinfo";
    private static final String CITY_ID="city_id";
    private static final String CITY_NAME="city_name";
    private static final String  PARENT_CITY="parent_city";
    private static final String ADMIN_AREA="admin_area";
    private static final String CNTY="cnty";
    CitySqliteHelper dbHelper;
    SQLiteDatabase db;
    public DBAdapter(Context _context) {
    context=_context;
    }
    public void open() throws SQLException {
        dbHelper = new CitySqliteHelper(context, DB_NAME, null, 1);
        try {
            db = dbHelper.getWritableDatabase();

        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }
    public void insert(City c) {
        ContentValues newvalues = new ContentValues();
        for (City.BasicBean bean : c.getBasic()) {
            newvalues.put("city_id", bean.getCid());
            newvalues.put("city_name", bean.getLocation());
            newvalues.put("parent_city", bean.getParent_city());
            newvalues.put("admin_area", bean.getAdmin_area());
            newvalues.put("cnty", bean.getCnty());
            if(querybyid(bean.getCid())==null)
            {

                Log.i("change","use insert");
                db.insert(DB_TABLE,null,newvalues);
            }

        }



    }
    public City query(String cityname)
    {

        Cursor result=db.query(DB_TABLE,new String[]{CITY_ID,CITY_NAME,PARENT_CITY,
                ADMIN_AREA,CNTY},CITY_NAME+" like '%"+cityname+"%'",null,null,null,null);


        if (result.getCount()==0||!result.moveToFirst())
        {  Log.i("change","result count=="+result.getCount());
            return null;
        }
           return ConvertToCity(result);

    }
    public City querybyid(String cityid)
    {
        Cursor result=db.query(DB_TABLE,new String[]{CITY_ID,CITY_NAME,PARENT_CITY,
                        ADMIN_AREA,CNTY},CITY_ID+"="+"'"+cityid+"'",null,
                null,null,null);
        if (result.getCount()==0||!result.moveToFirst())
        {return null;}
        return ConvertToCity(result);

    }
    private City ConvertToCity(Cursor cursor)
    {

        int count=cursor.getCount();
        if(count==0||!cursor.moveToFirst())
        {
            return null;
        }
        List<City.BasicBean> beans=new ArrayList<>();
        City.BasicBean basic=new City.BasicBean();
        City c=new City();
        for(int i=0;i<count;i++)
        {
            basic.setCid(cursor.getString(cursor.getColumnIndex(CITY_ID)));
            basic.setLocation(cursor.getString(cursor.getColumnIndex(CITY_NAME)));
            basic.setParent_city(cursor.getString(cursor.getColumnIndex(PARENT_CITY)));
            basic.setAdmin_area(cursor.getString(cursor.getColumnIndex(ADMIN_AREA)));
            basic.setCnty(cursor.getString(cursor.getColumnIndex(CNTY)));
            beans.add(basic);
            cursor.moveToNext();
        }
        c.setBasic(beans);
        return c;

    }
    public static class CitySqliteHelper extends SQLiteOpenHelper {


        public CitySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_City = "create table Cityinfo(id integer primary key,city_id text not null," +
                "city_name text not null,parent_city text,admin_area text,cnty text)";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_City);
        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS Cityinfo");
            onCreate(db);
        }


//TODO:数据库大概这样，接下来第二部，搜索的步骤
    }
}