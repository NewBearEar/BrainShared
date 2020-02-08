package com.example.a123.sharedbrain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a123.sharedbrain.R;

public class RegisterActivity extends Activity{
    public static RegisterActivity registerActivity;
    protected Button wBtnRegister;
    protected EditText wEtxtUser,wEtxtPassword,wEtxtPasswordAgain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        registerActivity = this;
        initView();
    }

    protected void initView(){
        wBtnRegister = (Button) findViewById(R.id.regiter_regiter);
        wBtnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                connectToRegister();
            }
        } );
        wEtxtPassword = findViewById(R.id.regiter_password);
        wEtxtPasswordAgain = findViewById(R.id.regiter_password_again);
        wEtxtUser = findViewById(R.id.regiter_user);

    }


    protected void goLogin(){
        Intent intent = new Intent();
        intent.setClass(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void connectToRegister(){
        String username = wEtxtUser.getText().toString();
        String password = wEtxtPassword.getText().toString();
        String password_again= wEtxtPasswordAgain.getText().toString();
        if (username.equals("")||password.equals("")||password_again.equals("")){
            Toast.makeText(RegisterActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(password_again)){
            Toast.makeText(RegisterActivity.this,"两次输入的密码不同",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            boolean isRegistSuccess = Network.toRegister(username,password); //调用Network
            if (isRegistSuccess){
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                goLogin();
            }
        }

    }
}
