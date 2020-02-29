package com.example.a123.sharedbrain.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a123.sharedbrain.AppService;
import com.example.a123.sharedbrain.DataModel.InfoModel;
import com.example.a123.sharedbrain.DataModel.InfoType;
import com.example.a123.sharedbrain.DataModel.PictureModel;

import com.example.a123.sharedbrain.R;
import com.example.a123.sharedbrain.VideoPlayerActivity;
import com.example.a123.sharedbrain.activity.LookDetailActivity;
import com.example.a123.sharedbrain.adapter.CommonRecyclerAdapter;
import com.example.a123.sharedbrain.adapter.CommonRecyclerHolder;
import com.example.a123.sharedbrain.config.Consts;
import com.example.a123.sharedbrain.helper.event.NoticeEvent;
import com.example.a123.sharedbrain.utils.TimeUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 订单显示页面
 */
public class OrderFragment extends FragmentBase{

    private static final String TAG = "NoticeFragment";
    private XRecyclerView mRecyclerView;
    private CommonRecyclerAdapter<InfoModel> mAdapter;
    private List<InfoModel> mInfoModels;
    private int start = 0;
    private int count = 10;//设置一次获取的条目数
    private View footerView;
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        Log.e(TAG,"onStart");
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (mInfoModels != null){
            mInfoModels.clear();
            mInfoModels = null;
        }
    }

    //定义处理接收方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeEvent event) {
        Log.e(TAG,"通知收到:"+event.getInfoModel());
        if (event.getInfoModel() != null){
            mInfoModels.add(0,event.getInfoModel());
            mAdapter.notifyDataSetChanged();
        }else{
            //否则直接刷新
            mRecyclerView.setRefreshing(true);
        }
    }

    private void initView(View view) {
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.notice_recycler);
        mImageView = (ImageView) view.findViewById(R.id.no_content);

//        mNoticeModelList = new ArrayList<>();
        mInfoModels = new ArrayList<>();

        // 获取一些假数据
//        getSomeData();


        loadData(true);


        mAdapter = new CommonRecyclerAdapter<InfoModel>(getActivity(), mInfoModels, R.layout.layout_notice_item) {
            @Override
            public void convert(final CommonRecyclerHolder holder, final InfoModel item, final int position, boolean isScrolling) {
                if (TextUtils.isEmpty(item.user.avatar)) {
                    holder.setImageResource(R.id.notice_item_avatar, R.drawable.default_avatar);
                } else {
                    Log.e(TAG, item.user.avatar);
                    holder.setImageByUrl(R.id.notice_item_avatar, Consts.API_SERVICE_HOST+item.user.avatar);
                }
                holder.setText(R.id.notice_item_name, item.user.nickname);
                holder.setText(R.id.notice_item_time, TimeUtils.longToDateTime(item.time));
                holder.setText(R.id.notice_item_content, item.content);
                holder.setText(R.id.notice_item_like, "赞 " + item.praiseCount);
                holder.setText(R.id.notice_item_comment, "评论 " + item.commentCount);
                if (item.isIPraised){
                    holder.setTextColor(R.id.notice_item_like, ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.red));
                }else{
                    holder.setTextColor(R.id.notice_item_like, ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.gray));
                }


                Log.i("video", "convert: videoUrls："+item.videoUrl.size()+","+item.videoUrl );

                // 如果是视频
                if (item.videoUrl != null && item.videoUrl.size() != 0){
                    holder.setVisibility(R.id.videoImage,View.VISIBLE);
                    holder.setVisibility(R.id.community_nineGrid,View.GONE);
                    String imgUrl = Consts.API_SERVICE_HOST+item.picUrls.get(0).imageUrl;
                    Log.i("video", "convert: imgUrl:"+imgUrl );
                    holder.setImageByUrl(R.id.videoImage,imgUrl);
                    final String videoUrl = Consts.API_SERVICE_HOST+item.videoUrl.get(0).videoUrl;
                    Log.i("video", "convert: videoUrl:"+videoUrl );
                    holder.setOnClckListener(R.id.videoImage, new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), VideoPlayerActivity.class).putExtra(
                                    "path", videoUrl));
                        }
                    });
                }else{
                    Log.i("video", "convert: 不是视频");
                    holder.setVisibility(R.id.videoImage,View.GONE);
                    holder.setVisibility(R.id.community_nineGrid,View.VISIBLE);
                    ArrayList<ImageInfo> imageInfoList = new ArrayList<>();
                    List<PictureModel> picModels = item.picUrls;
                    if (picModels != null && picModels.size() != 0){
                        for (PictureModel picModel:picModels) {
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setThumbnailUrl(Consts.API_SERVICE_HOST+picModel.imageUrl);
                            imageInfo.setBigImageUrl(Consts.API_SERVICE_HOST+picModel.imageUrl);
                            imageInfoList.add(imageInfo);
                        }
                    }
                    holder.setNineGridAdapter(R.id.community_nineGrid,new NineGridViewClickAdapter(getActivity(), imageInfoList));
                }



                Log.e(TAG,item.mainid+","+item.isIPraised);
                holder.setOnRecyclerItemClickListener(R.id.notice_item_like, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertPraised(item,holder);

                    }
                });

                holder.setOnRecyclerItemClickListener(R.id.notice_item_comment, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "你点击了评论，将进入详情页面！", Toast.LENGTH_SHORT).show();
                        LookDetailActivity.start(getActivity(), mInfoModels.get(position),InfoType.ORDER);
                    }
                });
            }
        };


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);


        mRecyclerView.setLoadingListener(new LoadingListener() {
            @Override
            public void onRefresh() {
                loadData(true);
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
//                getSomeData();
                loadData(false);
                mRecyclerView.loadMoreComplete();
            }
        });

        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_not_more, (ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), false);
        mRecyclerView.addFootView(footerView);
//        footerView.setVisibility(View.GONE);

        // 设置下拉图片为自己的图片
        mRecyclerView.setArrowImageView(R.mipmap.refresh_icon);
    }



    /**
     * 把赞的信息提交到服务器
     */
    private void insertPraised(final InfoModel item,final CommonRecyclerHolder holder) {

        if (AppService.getInstance().getCurrentUser() == null) {

        }

    }


    private int lastMainId;

    /**
     * 加载数据
     */
    private void loadData(final boolean isRefresh) {
        if (isRefresh) {
            start = 0;
            lastMainId = Integer.MAX_VALUE;
        } else {
            start += count;
        }
        Log.e(TAG+"1", start +"");
        if (AppService.getInstance().getCurrentUser() != null) {
            int classId = AppService.getInstance().getCurrentUser().Majorid;
            String username = AppService.getInstance().getCurrentUser().username;
           // AppService.getInstance().getNoticeAsync(classId, username,InfoType.ORDER, start,count, lastMainId,new JsonCallback<LslResponse<List<InfoModel>>>() {}


        }
    }
}
