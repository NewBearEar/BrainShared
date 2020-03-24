package com.example.a123.sharedbrain.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123.sharedbrain.App;
import com.example.a123.sharedbrain.AppService;
import com.example.a123.sharedbrain.DataModel.User;
import com.example.a123.sharedbrain.R;
import com.example.a123.sharedbrain.activity.AboutActivity;
import com.example.a123.sharedbrain.activity.BaseActivity;
import com.example.a123.sharedbrain.activity.LoginActivity;
import com.example.a123.sharedbrain.activity.MainActivity;
import com.example.a123.sharedbrain.activity.PersonalInfoActivity;
import com.example.a123.sharedbrain.config.Consts;
import com.example.a123.sharedbrain.helper.AvatarReceiver;
import com.example.a123.sharedbrain.helper.AvatarReceiver.AvatarCallback;
import com.example.a123.sharedbrain.helper.DemoHelper;
import com.example.a123.sharedbrain.helper.event.UpdateUserEvent;
import com.example.a123.sharedbrain.net.okgo.LslResponse;
import com.example.a123.sharedbrain.utils.IntentUtil;
import com.example.a123.sharedbrain.utils.UIUtil;
import com.example.a123.sharedbrain.view.LinearLayoutListItemView;
import com.example.a123.sharedbrain.view.OnLinearLayoutListItemClickListener;
import com.example.a123.sharedbrain.view.SelectDialog;
import com.example.a123.sharedbrain.view.SelectDialog.SelectDialogListener;
import com.example.a123.sharedbrain.view.TitleView;
import com.hyphenate.EMCallBack;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import android.view.View.OnClickListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends FragmentBase {
    private TitleView mTitleBar;
    private CircleImageView mHeadImage;
    private LinearLayoutListItemView mMenuReviseData;
    private LinearLayoutListItemView mMenuMyRoom;
    private LinearLayoutListItemView mMenuAbout;
    private Button mBtnExit;
    private LinearLayoutListItemView mItemServer;
    private static final String TAG = "MineFragment";
    private AvatarReceiver mAvatarReceiver;
    private IntentFilter mIntentFilter;
    private TextView mUsername;
    private TextView mAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        bindView(view);
        return view;
    }

    @Override
    public void onStart() {
        Log.e(TAG,"onStart");
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private Crouton mCrouton;

    private void showCrouton(String desc) {
        if (mCrouton != null){
            mCrouton.cancel();
        }
        mCrouton = Crouton.makeText(getActivity(), desc, Style.ALERT);
        mCrouton.show();
    }



    private void bindView(View view) {

        mUsername = (TextView) view.findViewById(R.id.mine_name);
        mAddress = (TextView) view.findViewById(R.id.mine_city);

        mTitleBar = (TitleView) view.findViewById(R.id.mine_titleBar);
        mTitleBar.setTitle("我的");

        mHeadImage = (CircleImageView) view.findViewById(R.id.mine_image);

        if (AppService.getInstance().getCurrentUser() != null){
            String iconUrl = AppService.getInstance().getCurrentUser().icon;
            Log.e(TAG,iconUrl+"  ********** ");
            if (!TextUtils.isEmpty(iconUrl) && !iconUrl.equals("null")){
                // 设置picasso不允许缓存，以免头像更新后不能动态更新
                Picasso.get().load(iconUrl)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(mHeadImage);
            }
        }


        mHeadImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });


        mMenuReviseData = (LinearLayoutListItemView) view.findViewById(R.id.mine_revise_data);
        mMenuMyRoom = (LinearLayoutListItemView) view.findViewById(R.id.mine_my_room);

        mMenuAbout = (LinearLayoutListItemView) view.findViewById(R.id.mine_about);
        mBtnExit = (Button) view.findViewById(R.id.mine_exit_btn);
        mBtnExit.setText("退出登录("+DemoHelper.getInstance().getCurrentUserName()+")");

        mBtnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mMenuReviseData.setOnLinearLayoutListItemClickListener(new OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
//                Crouton.makeText(getActivity(),"你点击了个人信息", Style.ALERT).show();
                IntentUtil.newIntent(getActivity(), PersonalInfoActivity.class);

                /* 由于在个人信息页面采取广播和Service方式都会报空指针异常，可行性有待考证
                 * 所以暂时采用打开个人信息页面的时候关闭主页面，返回的时候重新start主页面*/
                getActivity().finish();
            }
        });

        mMenuMyRoom.setOnLinearLayoutListItemClickListener(new OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                showCrouton("你点击了我的订单");
            }
        });



        mMenuAbout.setOnLinearLayoutListItemClickListener(new OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                IntentUtil.newIntent(getActivity(), AboutActivity.class);
            }
        });


        mItemServer = (LinearLayoutListItemView) view.findViewById(R.id.mine_cloud_server);
        mItemServer.setOnLinearLayoutListItemClickListener(new OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                // 跳转到云客服

            }
        });



        // 注册广播，千万记得销毁的时候关闭广播，否则造成内存泄漏
        mAvatarReceiver = new AvatarReceiver(new AvatarCallback() {
            @Override
            public void onAvatarChanged() {
                String iconUrl = AppService.getInstance().getCurrentUser().icon;
                Log.e(TAG,iconUrl+"  ********** ");
                if (!TextUtils.isEmpty(iconUrl)){
                    // 设置picasso不允许缓存，以免头像更新后不能动态更新
                    Picasso.get().load(iconUrl)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(mHeadImage);
                }
            }
        });
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(AvatarReceiver.AVATAR_ACTION);
        getActivity().registerReceiver(mAvatarReceiver,mIntentFilter);

        updateUserInfo();
    }

    private void logout() {
        ((MainActivity)getActivity()).showLoading(getActivity());



    }


    private final int PHOTO_PICKED_FROM_CAMERA = 1; // 用来标识头像来自系统拍照
    private final int PHOTO_PICKED_FROM_FILE = 2; // 用来标识从相册获取头像
    private final int CROP_FROM_CAMERA = 3;

    private void getIconFromPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICKED_FROM_FILE);
    }

    /**
     * 弹出选择图片或者拍照
     */
    private void selectPhoto() {
        List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("相册");
        showDialog(new SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getIconFromCamera();
                        break;
                    case 1:
                        getIconFromPhoto(); // 从系统相册获取
                        break;
                    default:
                        break;
                }
            }
        },list);

    }

    private Uri imgUri; // 由于android手机的图片基本都会很大，所以建议用Uri，而不用Bitmap

    /**
     * 调用系统相机拍照
     */
    private void getIconFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "avatar_"+String.valueOf(System.currentTimeMillis())+".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
        startActivityForResult(intent,PHOTO_PICKED_FROM_CAMERA);
    }

    private SelectDialog showDialog(SelectDialogListener listener, List<String> list){
        SelectDialog dialog = new SelectDialog(getActivity(),
                R.style.transparentFrameWindowStyle,listener,list);
        if (((BaseActivity)getActivity()).canUpdateUI()){
            dialog.show();
        }
        return dialog;
    }


    /**
     * 尝试裁剪图片
     */
    private void doCrop(){

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_FROM_CAMERA:
                doCrop();
                break;
            case PHOTO_PICKED_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (data != null){
                    setCropImg(data);
                }
                break;
            default:
                break;
        }
    }

    private void setCropImg(Intent picData){
        Bundle bundle = picData.getExtras();
        if (bundle != null){
            Bitmap mBitmap = bundle.getParcelable("data");
            mHeadImage.setImageBitmap(mBitmap);
            String fileName = Environment.getExternalStorageDirectory() + "/"
                    +DemoHelper.getInstance().getCurrentUserName() + ".png";
            saveBitmap(fileName,mBitmap);

        }
    }

    private void saveBitmap(String fileName,Bitmap bitmap){
        File file = new File(fileName);
        FileOutputStream fout = null;
        try {
            file.createNewFile();
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,60,fout);
            fout.flush();
            uploadAvatar(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        } finally {
            try {
                assert fout != null;
                fout.close();
                bitmap.recycle();
                UIUtil.showToast(getActivity(),"保存成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 上传头像
     * @param file
     */
    private void uploadAvatar(File file) {
        ((MainActivity)getActivity()).showLoading(getActivity());



    }

    /**
     * 把头像的url加到数据库中去
     */
    private void updateAvatarUrl() {
        final User user = AppService.getInstance().getCurrentUser();
        if (TextUtils.isEmpty(user.icon)){ // 如果当前头像地址为null,所以数据库还未存有,则把此url插入到数据库中
            final String iconUrl = "/user/avatar/"+user.username+".png";


        }else {// 否则不用插入，图片已经成功替换
            UIUtil.showToast("图片替换成功！");
           // ((MainActivity)getActivity()).stopLoading();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mAvatarReceiver);
        EventBus.getDefault().unregister(this);
        if (mCrouton != null){
            mCrouton.cancel();
            mCrouton = null;
        }
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo(){
        if (AppService.getInstance().getCurrentUser() != null){
            String address = AppService.getInstance().getCurrentUser().address;
            String nickname = AppService.getInstance().getCurrentUser().nickname;
            if (TextUtils.isEmpty(nickname)){
                mUsername.setText("请设置昵称");
            }else{
                mUsername.setText(nickname);
            }
            if (TextUtils.isEmpty(address)){
                mAddress.setText("请设置地址");
            }else{
                mAddress.setText(address);
            }
        }
    }


    //定义处理接收方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateUserEvent event) {
        Log.e(TAG,"通知收到:");
        updateUserInfo();
    }
}
