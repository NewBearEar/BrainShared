package com.example.a123.sharedbrain.net.okgo;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

    public int code;
    public String msg;

    public LslResponse toResponse() {
        LslResponse LslResponse = new LslResponse();
        LslResponse.code = code;
        LslResponse.msg = msg;
        return LslResponse;
    }
}