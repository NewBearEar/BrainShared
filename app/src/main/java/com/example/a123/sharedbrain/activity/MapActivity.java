package com.example.a123.sharedbrain.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.a123.sharedbrain.R;

import android.support.v7.app.AppCompatActivity;

public class MapActivity extends Activity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private Context context;
    private LatLng latLng;//记录经纬度
    private boolean isFirstLoc = true;

    private MyLocationConfiguration.LocationMode locationMode;//定位图层显示方式
    public MyLocationListener mListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在setContentView使用控件之前初始化context信息
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumap);
        this.context = this;
        //初始化布局控件
        initView();
        //初始化地图
        initMap();



    }
    private void initView(){
        mMapView = (MapView)findViewById(R.id.bmapView);


    }
    private void initMap(){
        mBaiduMap = mMapView.getMap();
        //MAP_TYPE_NORMAL	普通地图（包含3D地图）
        //MAP_TYPE_SATELLITE	卫星图
        //MAP_TYPE_NONE	    空白地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //显示定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //判断是否为Android 6.0 以上的系统版本，如果是，需要动态添加权限
        /*if (Build.VERSION.SDK_INT >= 23) {
            //showLocMap();
        } else {
            initLocation();//initLocation为定位方法
        }*/

        initLocation();//initLocation定位方法
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(mListener);
        //开启地图定位图层
        mLocationClient.start();

    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MapActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MapActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MapActivity.this, "请检查手机模式是否正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initLocation(){
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        LocationClientOption option = new LocationClientOption();

        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 1000;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        /*
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        */

    }
    //管理地图生命周期
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }
}
