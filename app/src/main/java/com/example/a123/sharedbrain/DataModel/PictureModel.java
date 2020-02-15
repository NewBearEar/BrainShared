package com.example.a123.sharedbrain.DataModel;

import java.io.Serializable;

public class PictureModel implements Serializable,IJsonModel{

    /**
     * 图片id
     */
    public int picid;
    /**
     * 图片地址
     */
    public String imageUrl;
}
