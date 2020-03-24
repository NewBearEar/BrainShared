package com.example.a123.sharedbrain.activity;

import android.content.Context;
import android.app.Dialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.content.DialogInterface.OnCancelListener;
import com.example.a123.sharedbrain.view.Loading;
import com.example.a123.sharedbrain.view.Loading.OnReturnListener;

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

            return super.isDestroyed();

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
    public void showLoading(Context context){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,null,null,Loading.LOGOSTYLE);
    }

    public void showLoading(Context context,String text){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,text,null,Loading.LOGOSTYLE);
    }

    public void showLoading(Context context,String text,OnReturnListener listener){
        Loading loading = new Loading();
        mDialog = loading.showLoading(context,text,listener,Loading.LOGOSTYLE);
    }

    public void stopLoading(){
        if (canUpdateUI()){
            Loading loading = new Loading();
            loading.dialogDismiss(mDialog);
        }
    }

    public void setOnCancelListener(OnCancelListener cancelListener){
        if (mDialog != null){
            mDialog.setOnCancelListener(cancelListener);
        }
    }

    /**
     * 检测是否是双击退出应用程序
     * @return true - 快速双击，间隔不少于1秒  false 不是快速双击
     */
    public synchronized static boolean isFastClick(){
        long time = System.currentTimeMillis();
        if (time - mLastClickTime < 1000){
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    protected InputMethodManager inputMethodManager;

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    private void parseIntent() {



    }

    private void consultService() {
        // 启动客服聊天界面

    }


}
