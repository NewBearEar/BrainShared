package com.example.a123.sharedbrain;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initView();
    }
    protected void initView() {
        Button wBtnRegister = (Button) findViewById(R.id.wel_register_btn);
        Button wBtnLogin = (Button) findViewById(R.id.wel_login_btn);
        wBtnLogin.setOnClickListener(this);
        wBtnRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.wel_login_btn:
                Toast.makeText(WelcomeActivity.this,"Login",Toast.LENGTH_SHORT).show();
                goLoginActivity();
                break;
            case R.id.wel_register_btn:
                Toast.makeText(WelcomeActivity.this,"Register",Toast.LENGTH_SHORT).show();
                goRegisterActivity();
                break;

        }
    }

    protected  void goLoginActivity(){
        Intent intent = new Intent();
        intent.setClass(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    protected  void goRegisterActivity(){
        Intent intent = new Intent();
        intent.setClass(this,RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
