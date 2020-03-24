package com.example.a123.sharedbrain.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 头像改变的广播接收器，防止用户在个人信息更改图片后主页面头像未更新的问题
 */
public class AvatarReceiver extends BroadcastReceiver {
    private static final String TAG = "AvatarReceiver";

    public static final String AVATAR_ACTION = "com.nanchen.android.AVATAR_ACTION";


    private AvatarCallback mAvatarCallback;

    public AvatarReceiver(AvatarCallback avatarCallback){
        mAvatarCallback = avatarCallback;
    }

    public AvatarReceiver(){}


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG,action);
        // 如果是正确的action
        if (AVATAR_ACTION.equals(action)){
            if (mAvatarCallback != null){
                mAvatarCallback.onAvatarChanged();
            }
        }
    }

    public interface AvatarCallback{
        /**
         * 头像更改时调用
         */
        void onAvatarChanged();
    }
}
