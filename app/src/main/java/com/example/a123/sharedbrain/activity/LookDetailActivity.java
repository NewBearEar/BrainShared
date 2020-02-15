package com.example.a123.sharedbrain.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.a123.sharedbrain.DataModel.CommentInfoModel;
import com.example.a123.sharedbrain.DataModel.InfoModel;

import com.example.a123.sharedbrain.R;

import com.example.a123.sharedbrain.view.TitleView;


import java.util.ArrayList;
import java.util.List;


public class LookDetailActivity extends BaseActivity {

    private static final String TAG = "LookDetailActivity";
    private TitleView mTitleBar;
    private EditText mEditText;
   // private IcomoonTextView mTextSend;
    private TextView mTextView;
    private boolean isReply = false;

    private static final String EXTRA_KEY = "v";
    private ListView mListView;
    private InfoModel mInfoModel;

   // private CommonAdapter<CommentInfoModel> mAdapter;
    private List<CommentInfoModel> mCommentInfoModels;

    private String commentText = "";
    private String replyId;
    private static int infoType;
    private int commentCount;


    public static void start(Context context, InfoModel noticeModel,int type) {
        Intent intent = new Intent(context, LookDetailActivity.class);
        intent.putExtra(EXTRA_KEY, noticeModel);
        infoType = type;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_detail);

        if (getIntent() == null) {
            return;
        }
        mInfoModel = (InfoModel) getIntent().getSerializableExtra(EXTRA_KEY);
        commentCount = mInfoModel.commentCount;
        initList();
        //bindView();

    }

    private void initList() {
        mCommentInfoModels = new ArrayList<>();

        if (mInfoModel.commentInfo != null && mInfoModel.commentInfo.size() != 0){
            mCommentInfoModels.addAll(mInfoModel.commentInfo);
        }
    }


    private void showKeyboard() {

    }


}
