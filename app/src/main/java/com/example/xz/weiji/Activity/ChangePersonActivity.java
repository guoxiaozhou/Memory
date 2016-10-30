package com.example.xz.weiji.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xz.weiji.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/10/9.
 */

public class ChangePersonActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tv_changeperson;
    private Button btn_changeperson;
    private Toolbar toolBar_changeperson;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeperson);
        initView();
    }

    private void initView() {
        tv_changeperson = (EditText) findViewById(R.id.tv_changeperson);

        tv_changeperson.setText(BmobUser.getCurrentUser().getUsername());
        btn_changeperson = (Button) findViewById(R.id.btn_changeperson);
        btn_changeperson.setOnClickListener(this);
        toolBar_changeperson = (Toolbar) findViewById(R.id.toolBar_changeperson);
        toolBar_changeperson.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        toolBar_changeperson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChangePersonActivity.this,NoteActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void submit() {
        // validate
        String changeperson = tv_changeperson.getText().toString().trim();
        if (TextUtils.isEmpty(changeperson)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent i=new Intent(ChangePersonActivity.this,ReFirestpageActivity.class);
        BmobUser user=BmobUser.getCurrentUser();
        i.putExtra("name",user.getUsername());
        startActivity(i);
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_changeperson:
                submit();
                BmobUser user = BmobUser.getCurrentUser();
                BmobUser newUser=new BmobUser();
                newUser.setUsername(tv_changeperson.getText().toString());
                newUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null)
                            Toast.makeText(ChangePersonActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ChangePersonActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent t = new Intent(ChangePersonActivity.this, NoteActivity.class);
               // t.putExtra("name", newUser.getUsername());
                finish();
                startActivity(t);
                break;
        }
    }
}
