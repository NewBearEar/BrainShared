package com.example.a123.sharedbrain.activity;

import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.a123.sharedbrain.App;
import com.example.a123.sharedbrain.AppService;
import com.example.a123.sharedbrain.DataModel.InfoType;
import com.example.a123.sharedbrain.R;
import com.example.a123.sharedbrain.adapter.ImagePickerAdapter;
import com.example.a123.sharedbrain.config.AddConfig;
import com.example.a123.sharedbrain.utils.ScreenUtil;
import com.example.a123.sharedbrain.utils.UIUtil;
import com.example.a123.sharedbrain.view.TitleView;
import com.example.a123.sharedbrain.view.WavyLineView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nanchen.compresshelper.CompressHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 发布信息专用的Activity
 */
public class ReleaseActivity extends BaseActivity
        implements ImagePickerAdapter.OnRecyclerViewItemClickListener
{
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数


    private static final String TAG = "ReleaseActivity";
    private TitleView mTitleBar;
    private String mFrom;
    private WavyLineView mWavyLine;
    private EditText mEditText;
    private int infoType;

    private List<File> mFiles;
    private List<String> mSmallUrls;
    private boolean isUploadPics;
    private int reqWidth = 0;
    private int reqHeight = 0;
    private Point point;
    private CompressHelper mCompressor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        mCompressor = new CompressHelper.Builder(this)
                .setMaxHeight(960)
                .setMaxWidth(720)
                .setQuality(80)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build();

        updatePixel();
        bindView();
    }
    private void updatePixel() {
        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Log.e(TAG,"宽:"+point.x+",高："+point.y);
        reqWidth = point.x ;
        reqHeight = point.y ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFiles != null){
            mFiles.clear();
            mFiles = null;
        }
        if (mSmallUrls != null){
            mSmallUrls.clear();
            mSmallUrls = null;
        }
    }

    private void bindView() {
        mFiles = new ArrayList<>();
        mSmallUrls = new ArrayList<>();

        mFrom = getIntent().getStringExtra("name");
        mTitleBar = (TitleView) findViewById(R.id.release_title);
        mTitleBar.setLeftButtonAsFinish(this);
        Log.e(TAG, mFrom);



        switch (mFrom) {
            case AddConfig.ORDER:
                mTitleBar.setTitle("发布订单");
                infoType = InfoType.ORDER;
                break;
            case AddConfig.BLOG:
                mTitleBar.setTitle("发布博客");
                infoType = InfoType.BLOG;
                break;
            default:
                mTitleBar.setTitle("发布动态");
                infoType = InfoType.COMMUNITY;
                break;
        }
        mTitleBar.changeRightButtonTextColor(getColor(R.color.white3));
        mTitleBar.setRightButtonText(getResources().getString(R.string.send_back_right));
        mTitleBar.setRightButtonTextSize(25);
        mTitleBar.setFixRightButtonPadingTop();
        mTitleBar.setRightButtonOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryDecodeSmallImg2();
            }
        });


        // 波浪线设置
        mWavyLine = (WavyLineView) findViewById(R.id.release_wavyLine);
        int initStrokeWidth = 1;
        int initAmplitude = 5;
        float initPeriod = (float) (2 * Math.PI / 60);
        mWavyLine.setPeriod(initPeriod);
        mWavyLine.setAmplitude(initAmplitude);
        mWavyLine.setStrokeWidth(ScreenUtil.dp2px(initStrokeWidth));


        mEditText = (EditText) findViewById(R.id.release_edit);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.release_recycler);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    /**
     * 压缩图片为小图片
     */
    private void tryDecodeSmallImg2() {
        mFiles.clear();// 避免重复点击多次上传图片
        //showLoading(this);
        for (int i = 0; i < selImageList.size(); i++) {
            Log.e(TAG,"第"+i+"个图片宽:"+selImageList.get(i).width);
            Log.e(TAG,"第"+i+"个图片高:"+selImageList.get(i).height);
            String filePath = selImageList.get(i).path;

            final File oldFile = new File(filePath);
            File newFile = mCompressor.compressToFile(oldFile);
            mFiles.add(newFile);

            /* 这里假设只对 200k 以上 并且 宽高小的像素 > 400的图片进行裁剪*/
//            reqHeight = selImageList.get(i).height;
//            reqWidth = selImageList.get(i).width;
//            int minSize = Math.min(reqHeight,reqWidth);
//            int size = (int) (selImageList.get(i).size/1024);//当前图片的大小
//            Log.e(TAG,"图片size:"+size+"KB");
//            while (minSize > 500 && size >= 200){
//                reqWidth /= 2;
//                reqHeight /= 2;
//                minSize = Math.min(reqHeight,reqWidth);
//            }
//
//            if (reqWidth == 0 || reqHeight == 0){ //拍照返回的宽高为0，这里避免异常
//                reqWidth = 480;
//                reqHeight = 640;
//            }
//            Log.e(TAG,"第"+i+"个图片压缩后宽："+reqWidth);
//            Log.e(TAG,"第"+i+"个图片压缩后高："+reqHeight);
//            // 对图片压缩尺寸为原来的八分之一
//            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(filePath,reqWidth,reqHeight);
//            Log.e(TAG,"第"+i+"个图片大小:"+bitmap.getByteCount()/1024+"kb");
//            saveBitmapFile(bitmap,filePath);
        }
        uploadPic();  // 图片压缩完毕开始上传图片
    }


    /**
     * 把bitmap转换为file
     * @param bitmap    bitmap源
     * @param filePath  文件路径
     */
    public void saveBitmapFile(Bitmap bitmap,String filePath) {
        if(bitmap==null){
            return;
        }
        Log.e(TAG,"文件路径:"+filePath);
        File file = new File(filePath);
        try {
            FileOutputStream bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            mFiles.add(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"失败："+e.getMessage());
        }
    }



    /**
     * 把图片上传上去
     */
    private void uploadPic() {
        Log.e(TAG,"需要上传图片的集合size:"+mFiles.size());
        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            UIUtil.showToast("发布内容不能为空！");
            //stopLoading();
            return;
        }

        if (mFiles.size() == 0){
            sendInfo();
            return;
        }
       //判断图片上传是否成功，调用函数发送信息到服务器
    }

    /**
     * 发送信息到服务器
     */
    private void sendInfo() {
        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            UIUtil.showToast("发布内容不能为空！");
            //stopLoading();
            return;
        }
        // 把图片的地址上传上去
        for (int i = 0; i < mFiles.size(); i++) {
            mSmallUrls.add(mFiles.get(i).getName());
            Log.e(TAG,"第"+(i+1)+"个图片名字:"+mFiles.get(i).getName());
        }
        Log.e(TAG,mSmallUrls.size()+" **** ");
        if(AppService.getInstance().getCurrentUser() == null){
            UIUtil.showToast("未知错误，请重新登录后操作");
            //stopLoading();
            return;
        }
        final int majorId = AppService.getInstance().getCurrentUser().Majorid;
        String username = AppService.getInstance().getCurrentUser().username;
        //信息发布具体实现
    }

    private void sendMsgToOthers(int classId,int infoType) {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                new MaterialDialog.Builder(this)
                        .items(R.array.release)
                        .itemsCallback(new ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (text.equals("相片")){
                                    //打开选择,本次允许选择的数量
                                    ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                    Intent intent1 = new Intent(ReleaseActivity.this, com.lzy.imagepicker.ui.ImageGridActivity.class);
                                    startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                }else{
                                    // 打开微视频

                                    // 存一个文件，以便于让后面发微视频知道发到哪里
                                    getSharedPreferences("send.tmp",MODE_PRIVATE).edit().putString("infoType",mFrom)
                                            .putString("content",mEditText.getText().toString().trim()).apply();


                                    ReleaseActivity.this.finish();
                                }
                            }
                        }).show();

                //打开选择,本次允许选择的数量
//               ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
//               Intent intent = new Intent(ReleaseActivity.this, com.lzy.imagepicker.ui.ImageGridActivity.class);
//               startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
//                for (int i = 0; i < selImageList.size(); i++) {
//                    mFiles.add(new File(selImageList.get(i).path));
//                }

                    //鲁班压缩
//                compressWithLs(new File(selImageList.get(0).path));
                    adapter.setImages(selImageList);
                }

            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                @SuppressWarnings("unchecked")
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
