package com.example.a123.sharedbrain.DataModel;

import java.io.Serializable;
import java.util.List;

public class InfoModel implements Serializable,IJsonModel {

    /**
     * 主贴id
     */
    public int mainid;
    /**
     * 专业id,可见性
     */
    public int majorid;
    /**
     * 用户名，用于获取用户资料信息
     */
    public String username;
    /**
     * 发起时间戳
     */
    public long time;
    /**
     * 信息类型，1为订单，2为博客，3为动态
     */
    public int infotype;
    /**
     * 信息内容
     */
    public String content;
    /**
     * 发布人信息
     */
    public UserInfoModel user;
    /**
     * 赞的数目
     */
    public int praiseCount;
    /**
     * 评论的数目
     */
    public int commentCount;
    /**
     * 我是否赞了该条信息
     */
    public boolean isIPraised;
    /**
     * 评论信息
     */
    public List<CommentInfoModel> commentInfo;
    /**
     * 最后获取的一条信息的id
     */
    public int lastid;

    /**
     * 社区图片Url组
     */
    public List<PictureModel> picUrls;

    /**
     * 社区视频Url
     */
    public List<VideoModel> videoUrl;
}
