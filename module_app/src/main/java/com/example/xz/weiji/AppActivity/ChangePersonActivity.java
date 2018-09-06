package com.example.xz.weiji.AppActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.User;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xz on 2016/10/9.
 */

public class ChangePersonActivity extends BaseActivity implements View.OnClickListener {
    private EditText tv_changeperson;
    private Button btn_changeperson;
    private Toolbar toolBar_changeperson;
    String downloadPath;
    private CircleImageView iv_head;
    private RelativeLayout rl_head;
    public static final String SDPath=Environment.getExternalStorageDirectory()+"/bmob/";
    ProgressDialog progressDialog;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeperson);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadHead();
    }

    @Override
    public void onResume() {
        super.onResume();

    }




    private void initView() {
        tv_changeperson = (EditText) findViewById(R.id.tv_changeperson);

        tv_changeperson.setText(BmobUser.getCurrentUser().getUsername());
        btn_changeperson = (Button) findViewById(R.id.btn_changeperson);
        btn_changeperson.setOnClickListener(this);
        toolBar_changeperson = (Toolbar) findViewById(R.id.toolBar_changeperson);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolBar_changeperson.getLayoutParams().height = Utils.getAppBarHeight(this);
            toolBar_changeperson.setPadding(toolBar_changeperson.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    toolBar_changeperson.getPaddingRight(),
                    toolBar_changeperson.getPaddingBottom());
        }
        toolBar_changeperson.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        toolBar_changeperson.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(ChangePersonActivity.this,ReFirestpageActivity.class);
//                startActivity(i);
//                finish();
                onBackPressed();
            }
        });
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        rl_head.setOnClickListener(this);


//            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_changeperson:
                submit();
                BmobUser user = BmobUser.getCurrentUser();
                BmobUser newUser = new BmobUser();
                newUser.setUsername(tv_changeperson.getText().toString());
                newUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ChangePersonActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            // Intent t = new Intent(ChangePersonActivity.this, ReFirestpageActivity.class);
                            // t.putExtra("name", newUser.getUsername());
                            finish();
                            //    startActivity(t);


                        } else
                            Toast.makeText(ChangePersonActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;



            case R.id.rl_head:
                Intent intent1=new Intent(ChangePersonActivity.this,HeadActivity.class);
                intent1.putExtra("path",downloadPath);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 下载头像
     */
    public void downloadHead() {
        progressDialog = new ProgressDialog(ChangePersonActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Waiting...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(BmobUser.getCurrentUser().getObjectId(),
                new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            BmobFile head = user.getHead();

                            File saveFile=new File(Environment.getExternalStorageDirectory()+"/bmob/",
                                    head.getFilename());

                            Log.i("path", head.getFileUrl());
                            if (head != null) {
                                head.download(saveFile,new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Log.i("path", s);
                                            iv_head.setImageURI(Uri.fromFile(new File(s)));
                                            downloadPath=s;


                                        }
                                        progressDialog.dismiss();
                                    }


                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            }
                            }else {
                            iv_head.setImageResource(R.mipmap.icon);
                            progressDialog.dismiss();
                        }

                    }

                });

    }


}
