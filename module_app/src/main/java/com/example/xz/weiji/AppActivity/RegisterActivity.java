package com.example.xz.weiji.AppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.User;
import com.example.xz.weiji.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xz on 2016/9/16.
 */

public class RegisterActivity extends BaseActivity {
    private MaterialEditText rg_username;
    private MaterialEditText rg_password;
    private MaterialEditText rg_email;
    private Button bt_rg;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        user=new User();
        bt_rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });






    }

    private void registerUser() {
        user.setUsername(rg_username.getText().toString());
        user.setPassword(rg_password.getText().toString());
        user.setEmail(rg_email.getText().toString());
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null) {
                    Toast.makeText(RegisterActivity.this, "注册成功  点击返回即可登陆", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        rg_username=(MaterialEditText)findViewById(R.id.et_rg_username);
        rg_password=(MaterialEditText)findViewById(R.id.et_rg_password);
        rg_email=(MaterialEditText)findViewById(R.id.et_rg_mail);
        bt_rg=(Button)findViewById(R.id.bt_rg);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
        i.putExtra("username",user.getUsername());
        startActivity(i);
    }
}
