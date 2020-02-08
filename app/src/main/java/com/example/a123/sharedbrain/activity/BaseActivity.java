package com.example.a123.sharedbrain.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
public class BaseActivity extends AppCompatActivity {
    private Dialog mDialog;
    private static long mLastClickTime;
    private boolean isDestroyed = false;
    private static final String TAG = "ActivityBase";
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        isDestroyed = false;
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState, persistentState);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();

        if (mDialog != null){
            mDialog.cancel();
            mDialog = null;
        }

    }
    @Override
    public boolean isDestroyed() {
        if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR1){
            return isDestroyed;
        }else {
            return super.isDestroyed();
        }
    }
    /**
     * 是否可以对UI进行操作，比如更新UI控件，显示/消失对话框等
     * 由于Activity中存在大量的异步网络操作，若异步回调时，Activity已经被销毁，则不可以对UI进行更新操作
     *
     *  @return true - Activity未被销毁，可更新UI  false - Activity已被销毁，不可更新UI
     */
    public boolean canUpdateUI(){
        return (!isFinishing()) && (!isDestroyed());
    }


}
