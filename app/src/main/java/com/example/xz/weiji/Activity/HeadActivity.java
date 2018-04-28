package com.example.xz.weiji.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.User;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/7/27.
 */

public class HeadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_head;
    private Toolbar toolBar_head;
    private ImageView iv_more;
    private ImageView iv_back;
    private Dialog dialog;
    public ProgressDialog progressDialog;
    String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        initView();

    }

    private void initView() {
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_head.setOnClickListener(this);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        toolBar_head = (Toolbar) findViewById(R.id.toolBar_head);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolBar_head.getLayoutParams().height = Utils.getAppBarHeight(this);
            toolBar_head.setPadding(toolBar_head.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    toolBar_head.getPaddingRight(),
                    toolBar_head.getPaddingBottom());
        }
        Intent intent = getIntent();
        String downloadPath = intent.getStringExtra("path");
        if(TextUtils.isEmpty(downloadPath))
            iv_head.setImageResource(R.mipmap.icon);
        else
            iv_head.setImageURI(Uri.fromFile(new File(downloadPath)));


        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head:
                showDialog();
                break;
            case R.id.iv_more:
                showDialog();
                break;
            case R.id.tv_selectphoto:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                dialog.dismiss();
                break;
            case R.id.tv_takephoto:
                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Uri uri=Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/temp.jpg"));
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,uri);


                startActivityForResult(intent1,3);
                dialog.dismiss();
                break;
            case R.id.tv_canclephoto:
                dialog.dismiss();
                //onBackPressed();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    /**
     * 弹出对话框
     */
    public void showDialog() {
         dialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.diaglog_head, null);
        root.findViewById(R.id.tv_takephoto).setOnClickListener(this);
        root.findViewById(R.id.tv_selectphoto).setOnClickListener(this);
        root.findViewById(R.id.tv_canclephoto).setOnClickListener(this);
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                Log.i("path", path);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*"); //表示Intent要打开的图片
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, 2);
            }
        }else if(requestCode==2&&resultCode==RESULT_OK){


            Bitmap bitmap = data.getParcelableExtra("data");

            String filename=path.substring(path.lastIndexOf("/")+1);

            Log.i("path-file",filename);
            File file=bitmapToFile(bitmap,filename);

            uploadHead(file);
            iv_head.setImageBitmap(bitmap);


        }else if(requestCode==3&&resultCode==RESULT_OK) {
            /**    Bundle bundle=data.getExtras();
            Bitmap bitmap=(Bitmap)bundle.get("data");
            Uri uriPath=null;
            if(data.getData()!=null){
                uriPath=data.getData();
            }else {
                uriPath=Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,null,null));
            }
            Log.i("path",uriPath.toString());
           **/
            File file=new File(Environment.getExternalStorageDirectory()+"/temp.jpg");
            Uri uriPath=Uri.fromFile(file);
            path=Environment.getExternalStorageDirectory()+"/temp.jpg";
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uriPath, "image/*"); //表示Intent要打开的图片
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 2);
        }
    }

    /**
     * 上传头像
     * @param file
     */
    private void uploadHead(File file){

        progressDialog = new ProgressDialog(HeadActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("上传中...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        final BmobUser user1 = BmobUser.getCurrentUser();
        final User newUser1 = new User();


        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    newUser1.setObjectId(user1.getObjectId());
                    newUser1.setSessionToken(user1.getSessionToken());
                    //Toast.makeText(HeadActivity.this, "上传文件成功"
                     //       + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    newUser1.setHead(bmobFile);
                    newUser1.update(user1.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(HeadActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(HeadActivity.this, "修改头像失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("Headexception", e.toString());
                                progressDialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(HeadActivity.this, "上传文件失败"
                            + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value) {
                Log.i("path", value + "");
                progressDialog.setProgress(value);
            }
        });
    }
    public File bitmapToFile(Bitmap bitmap,String filename){

        File file=new File(Environment.getExternalStorageDirectory()+"/bmob/",
                filename);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file=new File(Environment.getExternalStorageDirectory()+"/temp.jpg");
        if(file.exists()){
            file.delete();
        }
    }
}
