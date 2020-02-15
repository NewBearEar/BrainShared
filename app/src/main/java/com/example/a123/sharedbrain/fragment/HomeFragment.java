package com.example.a123.sharedbrain.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.a123.sharedbrain.AppService;
import com.example.a123.sharedbrain.R;
import com.example.a123.sharedbrain.activity.ReleaseActivity;
import com.example.a123.sharedbrain.adapter.MyPagerAdapter;
import com.example.a123.sharedbrain.config.AddConfig;
import com.example.a123.sharedbrain.config.Consts;
import com.example.a123.sharedbrain.utils.CircularAnimUtil;
import com.example.a123.sharedbrain.utils.UIUtil;
import com.example.a123.sharedbrain.view.TitleView;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class HomeFragment extends FragmentBase {
    private static final String TAG = "HomeFragment";
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TitleView mTitleBar;
    private String mName = AddConfig.ORDER;
    private List<String> nameList;
    private List<Fragment> list;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nameList != null){
            nameList.clear();
            nameList = null;
        }
        if (list != null){
            list.clear();
            list = null;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        bindView(view);
        return view;
    }
    private void bindView(View view) {
        mTitleBar = (TitleView) view.findViewById(R.id.home_titleBar);
        mTitleBar.setTitle("首页");

        mTabLayout = (TabLayout) view.findViewById(R.id.home_tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.home_vp);

        mFab = (FloatingActionButton) view.findViewById(R.id.home_fab);

        if (AppService.getInstance().getCurrentUser() != null){
            int type = AppService.getInstance().getCurrentUser().type;
            Log.e(TAG, "bindView: type:"+type );
            if (type == Consts.USER_TYPE_STUDENT){//用户是学生
                //mFab.setVisibility(View.VISIBLE);
                mFab.show();
            }
        }

        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppService.getInstance().getCurrentUser().type != Consts.USER_TYPE_Admin){
                    UIUtil.showToast("非相关人员暂时不允许发公告！");
                    return;
                }

                Intent intent = new Intent(getActivity(), ReleaseActivity.class);
                intent.putExtra("name", mName);
                Log.e(TAG,mName);
                CircularAnimUtil.startActivity((AppCompatActivity) getActivity(), intent, mFab,
                        R.color.main_bg_color1);
            }
        });

        nameList = new ArrayList<>();
        nameList.add("订单");
        nameList.add("博客");
//        nameList.add("课表");
//        nameList.add("社区");

        list = new ArrayList<>();
        list.add(new OrderFragment());
        list.add(new BlogFragment());
//        list.add(new CommunityFragment());

        mViewPager.setAdapter(new MyPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),nameList,list));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int type = AppService.getInstance().getCurrentUser().type;
                Log.e(TAG, "onPageSelected: type:"+ type);
                //mFab.setVisibility(View.GONE);
                mFab.hide();
                if (position == 0 && type == Consts.USER_TYPE_Admin){
                    mFab.show();
                    mName = AddConfig.ORDER;
                }
                if (position == 1 && type == Consts.USER_TYPE_Admin){
                    mFab.show();
                    mName = AddConfig.BLOG;
                }
                if (position == 2){
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        MyBehavior behavior = MyBehavior.from(mFab);
//        behavior.setOnStateChangedListener(mOnStateChangedListener);


    }
}
