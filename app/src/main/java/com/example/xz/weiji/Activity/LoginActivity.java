package com.example.xz.weiji.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xz on 2016/9/14.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText lg_username;
    private MaterialEditText lg_password;
    private Button bt_login;
    private BmobUser user;
    private TextView tv_lg_rg;
    private BmobUser bmobUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "1ee478a204051c3c3c94372e0bb462b1","Bmob");
//        Boolean b=cn.bmob.statistics.AppStat.i("1ee478a204051c3c3c94372e0bb462b1",null);
//        Log.i("b的真假",b.toString());
        bmobUser = BmobUser.getCurrentUser();
        // Toast.makeText(LoginActivity.this,bmobUser.toString(),Toast.LENGTH_SHORT).show();
        if (bmobUser != null) {
            directLogin();
        }
        setContentView(R.layout.activity_login);
        initView();


        bt_login.setOnClickListener(this);
        tv_lg_rg.setOnClickListener(this);

        Intent i = getIntent();
        lg_username.setText(i.getStringExtra("username"));


    }

    private void directLogin() {

        Intent t = new Intent(LoginActivity.this, ReFirestpageActivity.class);
        t.putExtra("name", bmobUser.getUsername());
        startActivity(t);
        finish();

    }


    private void loginUser() {
        user = new BmobUser();
        user.setUsername(lg_username.getText().toString());
        user.setPassword(lg_password.getText().toString());
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent t = new Intent(LoginActivity.this, ReFirestpageActivity.class);
                    //t.putExtra("name", lg_username.getText().toString());
                    startActivity(t);
                    finish();
                    progressDialog.cancel();
                } else {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void initView() {

        lg_username = (MaterialEditText) findViewById(R.id.et_lg_username);
        lg_password = (MaterialEditText) findViewById(R.id.et_lg_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_lg_rg = (TextView) findViewById(R.id.tv_lg_register);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在登陆");
        progressDialog.setCancelable(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录按钮
            case R.id.bt_login:
                progressDialog.show();
                loginUser();
                break;
            //点击注册
            case R.id.tv_lg_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }
}
