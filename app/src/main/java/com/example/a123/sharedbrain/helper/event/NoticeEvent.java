package com.example.a123.sharedbrain.helper.event;

import com.example.a123.sharedbrain.DataModel.InfoModel;

public class NoticeEvent {
    private InfoModel mInfoModel;
    private int mCommentCount;
    public NoticeEvent(InfoModel infoModel,int commentCount){
        this(infoModel);
        this.mCommentCount = commentCount;
    }

    public NoticeEvent(InfoModel infoModel){
        this.mInfoModel = infoModel;
    }

    public NoticeEvent(){}

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public InfoModel getInfoModel() {
        return mInfoModel;
    }

    public void setInfoModel(InfoModel infoModel) {
        mInfoModel = infoModel;
    }
}
