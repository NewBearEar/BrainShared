package com.example.a123.sharedbrain.activity;
import com.example.a123.sharedbrain.R;
import com.example.a123.sharedbrain.activity.BaseActivity;
import android.os.Bundle;

/**
 * 发布信息专用的Activity
 */
public class ReleaseActivity extends BaseActivity
        //implements ImagePickerAdapter.OnRecyclerViewItemClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
    }
}
