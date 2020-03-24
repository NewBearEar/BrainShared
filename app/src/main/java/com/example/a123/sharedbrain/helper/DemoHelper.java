package com.example.a123.sharedbrain.helper;


import com.example.a123.sharedbrain.DataModel.DemoModel;

public class DemoHelper {
    protected static final String TAG = "DemoHelper";
    private static DemoHelper instance = null;
    private DemoModel demoModel = null;
    private String username;

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    /**
     * get current user's id
     */
    public String getCurrentUserName(){
        if(username == null){
            //username = demoModel.getCurrentUsernName();
        }
        return username;
    }


}
