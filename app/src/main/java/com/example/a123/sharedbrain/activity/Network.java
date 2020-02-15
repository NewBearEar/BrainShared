package com.example.a123.sharedbrain.activity;

import android.widget.Toast;



public class Network {
    //必写为静态static方法，直接调用类方法
    public static boolean toLogin(String username, String password){

        if(username.equals("admin")&&password.equals("123456")){
            //管理员后门账号和密码
            Toast.makeText(LoginActivity.loginActivity,"登录成功",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(false){
            //向服务器发送数据并验证
            return true;
        }
        else {
            Toast.makeText(LoginActivity.loginActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean toRegister(String username, String password){
        return true;
    }
}
