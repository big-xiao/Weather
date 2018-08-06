package com.example.administrator.iweather;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button button=null;
    Button button2=null;
    Switch aSwitch=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        button=findViewById(R.id.about_weather);
        button2=findViewById(R.id.rate_time);
        aSwitch=findViewById(R.id.switch1);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isupdate=sh.getBoolean("isUpdate",false);
        aSwitch.setChecked(isupdate);
        button2.setEnabled(isupdate);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false)
                {
                    button2.setEnabled(false);

                    editor.putBoolean("isUpdate",false);
                    editor.apply();
                }
                else
                {
                    button2.setEnabled(true);

                    editor.putBoolean("isUpdate",true);
                    editor.apply();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,WeatherinfoActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
            SharedPreferences.Editor editor = sh.edit();
            int num=sh.getInt("select_num",0);
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("请选择间隔时间");
                final String []time={ "2小时","6小时","12小时","24小时"};

                builder.setSingleChoiceItems(time, num, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which)
                        {


                            case 0:editor.putInt("updatetime",120);
                                editor.putInt("select_num",0);

                                break;
                            case 1:editor.putInt("updatetime",360);
                                editor.putInt("select_num",1);

                                break;
                            case 2:editor.putInt("updatetime",720);
                                editor.putInt("select_num",2);

                                break;
                            case 3:editor.putInt("updatetime",1440);
                                editor.putInt("select_num",3);

                                break;
                            default:


                        }
                        editor.apply();


                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(SettingActivity.this,updateService.class);
                        startService(intent);
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();




                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
    }
}
