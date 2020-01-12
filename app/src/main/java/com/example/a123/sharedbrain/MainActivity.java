package com.example.a123.sharedbrain;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.Map;

public class MainActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }
    protected void initView(){

    }
    //管理地图生命周期
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    protected void onPause(){
        super.onPause();

    }
}
