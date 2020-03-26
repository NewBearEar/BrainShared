package com.example.a123.sharedbrain.utils;
import com.example.a123.sharedbrain.App;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DisplayMetricsUtil extends Activity {
    public static Resources resourcesInstance;
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static Resources GetResources(Context context) {
    // TODO Auto-generated method stub
    Resources mResources = null;
    mResources = context.getResources();
    return mResources;
    }

    public static int dip2px(float dpValue,Context context) {

        resourcesInstance=GetResources(context);
        final float scale =resourcesInstance.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Objects.requireNonNull(App.getAppContext()).getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 宽度 像素
     */
    public static int getWidthPixels(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 高度  像素
     */
    public static int getHeightPixels(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static String convertDateToString(long timeStamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(timeStamp * 1000));
    }
}
