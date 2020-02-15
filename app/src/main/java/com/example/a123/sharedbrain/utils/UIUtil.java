package com.example.a123.sharedbrain.utils;

import android.widget.Toast;
import android.content.Context;
import com.example.a123.sharedbrain.App;

import java.lang.ref.WeakReference;

/**
 * 页面显示工具类
 */
public final class UIUtil {

    private static Toast toast;


    /**
     * 显示一个短时间的Toast
     * @param context   上下文
     * @param desc      显示文本
     */
    public static void showToast(Context context,String desc){
        // 使用弱引用，防止内存泄漏
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);// 把context设置为弱引用
        if (toast == null){
            toast = Toast.makeText(contextWeakReference.get(),desc,Toast.LENGTH_SHORT);
        }else {
            toast.setText(desc);
        }
        toast.show();
    }

    /**
     * 显示一个长时间的Toast
     * @param context   上下文
     * @param desc      显示文本
     */
    public static void showToast2(Context context,String desc){
        // 使用弱引用，防止内存泄漏
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);// 把context设置为弱引用
        if (toast == null){
            toast = Toast.makeText(contextWeakReference.get(),desc,Toast.LENGTH_LONG);
        }else {
            toast.setText(desc);
        }
        toast.show();
    }


    /**
     * 显示一个短时间的Toast
     * @param desc      显示文本
     */
    public static void showToast(String desc){
        if (toast == null){
            toast = Toast.makeText(App.getAppContext(),desc,Toast.LENGTH_SHORT);
        }else {
            toast.setText(desc);
        }
        toast.show();
    }

    /**
     * 显示一个长时间的Toast
     * @param desc      显示文本
     */
    public static void showToast2(String desc){
        // 使用弱引用，防止内存泄漏
        if (toast == null){
            toast = Toast.makeText(App.getAppContext(),desc,Toast.LENGTH_LONG);
        }else {
            toast.setText(desc);
        }
        toast.show();
    }
}
