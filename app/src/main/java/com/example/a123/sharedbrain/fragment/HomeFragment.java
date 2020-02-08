package com.example.a123.sharedbrain.fragment;

import android.support.v4.view.ViewPager;

import com.marshalchen.ultimaterecyclerview.ui.floatingactionbutton.FloatingActionButton;

import java.util.List;
import android.support.v4.app.Fragment;

public class HomeFragment extends FragmentBase {
    private static final String TAG = "HomeFragment";
    private FloatingActionButton mFab;
    private ViewPager mViewPager;
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
}
