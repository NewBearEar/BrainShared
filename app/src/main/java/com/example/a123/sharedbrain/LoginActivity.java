package com.example.a123.sharedbrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {
    protected Button wBtnLogin;
    protected Button wBtnRegister;
    protected EditText wEtxtUser, wEtxtPassword;
    public Network mNetwork;
    //public boolean isLoginSuccess;
    public static LoginActivity loginActivity;  //用于在非Activity类中调用Toast

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginActivity = this;
        initView();
    }
    protected void initView(){
        wBtnLogin = (Button) findViewById(R.id.login_login);
        wBtnLogin.setOnClickListener(this);
        wBtnRegister = (Button) findViewById(R.id.login_register);
        wBtnRegister.setOnClickListener(this);

        wEtxtUser = (EditText) findViewById(R.id.login_user);
        wEtxtPassword = (EditText) findViewById(R.id.login_password);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_login:
                connectToLogin();
                break;
            case R.id.login_register:
                goRegister();
                break;
        }
    }

    private void goRegister(){
        Intent intent = new Intent();
        intent.setClass(this,RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //跳转到主界面(这里跳转到MAP）
    private void goMain(){
        goBaiduMap();
    }
    private void connectToLogin(){
        String username = wEtxtUser.getText().toString();
        String password = wEtxtPassword.getText().toString();
        if(username.equals("")||password.equals("")){
            Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isLoginSuccess = Network.toLogin(username,password);
        if(isLoginSuccess)
            goMain();

    }
    //跳转到MAP界面
    private void goBaiduMap(){
        Intent intent2 = new Intent();
        intent2.setClass(LoginActivity.this,MapActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
    }
}
