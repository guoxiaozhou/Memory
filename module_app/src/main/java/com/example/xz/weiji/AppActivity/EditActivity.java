package com.example.xz.weiji.AppActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.CommonUtil;
import com.example.xz.weiji.Utils.ImageUtils;
import com.example.xz.weiji.Utils.StringUtils;
import com.example.xz.weiji.Utils.Utils;
import com.jimmy.common.util.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sendtion.xrichtext.RichTextEditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xz on 2016/9/20.
 */

public class EditActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, RichTextEditor.OnDeleteImageListener {
    private Toolbar toolBar;
    private RichTextEditor et_text;
    private BmobUser user;
    private String isNote;
    private String isTitle;
    private Boolean isEmpty = false;
    private Note notebefore;
    private MaterialEditText et_notetitle;
    private Intent i;
    private Bitmap bitmap = null;
    private ProgressDialog loadingDialog;
    private Subscription subsLoading;
    private LinearLayout ll_editcontent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittext);
        initView();

    }

    // 标题栏右边按钮点击方法
    private void saveNote() {

        Note note = new Note();
        String s = getEditData();
        String title = et_notetitle.getText().toString();
        //submit();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
            note.setNote(s);
            note.setTitle(title);
            note.setAuthor(user);
            note.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        // Intent i = new Intent(EditActivity.this, NoteListActivity.class);
                        // i.putExtra("name", user.getUsername());
                        // startActivity(i);
                        EditActivity.this.setResult(0x11, i);
                        //   EditActivity.this.setResult(0x12,i);
                        finish();
                    } else
                        Toast.makeText(EditActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void initView() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolBar.getLayoutParams().height = Utils.getAppBarHeight(this);
            toolBar.setPadding(toolBar.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    toolBar.getPaddingRight(),
                    toolBar.getPaddingBottom());
        }
        //setSupportActionBar(toolBar);
        toolBar.setTitle("");

        toolBar.inflateMenu(R.menu.menu_second);
        toolBar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        //设置标题栏左边按钮点击事件
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
        et_text = (RichTextEditor) findViewById(R.id.et_text);
//        et_text.setBackgroundColor(getResources().getColor(R.color.white));
        //  et_text.setSelection(0);
        et_notetitle = (MaterialEditText) findViewById(R.id.et_notetitle);
        ll_editcontent = (LinearLayout) findViewById(R.id.ll_editcontent);

        //处理前先获得用户缓存数据
        user = BmobUser.getCurrentUser();

        i = getIntent();

        if (i != null) {

            if (i.getStringExtra("titlestring") != null) {


                et_notetitle.setText(i.getStringExtra("titlestring"));
                et_text.post(new Runnable() {
                    @Override
                    public void run() {
                        //showEditData(note.getContent());
                        et_text.clearAllLayout();
                        showDataSync(i.getStringExtra("notestring"));
                    }
                });
                isNote = i.getStringExtra("notestring");
                isTitle = i.getStringExtra("titlestring");
                notebefore = (Note) i.getSerializableExtra("note");
                isEmpty = false;
            } else {
                et_text.addEditTextAtIndex(et_text.getLastIndex(), "");
                Log.i("xiaozhou", "执行了addEditTextAtIndex");
                isEmpty = true;

            }
        }
        ll_editcontent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                et_text.getLastEditText().requestFocus();

                ((InputMethodManager)et_text.getLastEditText().getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE)).showSoftInput(et_text.getLastEditText(),
                        InputMethodManager.SHOW_IMPLICIT);

                return false;
            }
        });
    }


    //设置标题栏右边按钮点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                if (isEmpty) {
                    saveNote();
                } else {
                    updateNote();
                }
                break;
            case R.id.action_addImage:
                callGallery();

                break;
            default:
                break;
        }
        return true;
    }

    private void updateNote() {
        notebefore.setNote(getEditData());
        notebefore.setTitle(et_notetitle.getText().toString());
        notebefore.update(notebefore.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(EditActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    // Intent i = new Intent(EditActivity.this, NoteListActivity.class);
                    // i.putExtra("name", user.getUsername());
                    // startActivity(i);
                    EditActivity.this.setResult(0x11, i);
                    //   EditActivity.this.setResult(0x12,i);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent i=getIntent();
//        if(i.getStringExtra("类名")=="ReFirestpageActivity"){
//            Intent intent = new Intent(EditActivity.this, ReFirestpageActivity.class);
//            startActivity(intent);
//            finish();
//        }else if(i.getStringExtra("类名")=="NoteListActivity"){
        //super.onBackPressed();
        //  Intent i = new Intent(EditActivity.this, NoteListActivity.class);
        //  i.putExtra("name", user.getUsername());
        // startActivity(i);
        EditActivity.this.setResult(0x11, i);
        finish();
        //super.onBackPressed();

    }

    private void submit() {
        // validate
        String notetitle = et_notetitle.getText().toString().trim();
        if (TextUtils.isEmpty(notetitle)) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        }


        // TODO validate success, do something


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void callGallery() {
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);

        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//                Uri uri=data.getData();
//                try {
//                     bitmap= BitmapFactory.decodeStream(resolver
//                    .openInputStream(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                if(bitmap!=null){
//
//                       insertIntoEditText(getBitmapMime(resizeImage(bitmap,400,800),uri));
//                }else {
//                    Toast.makeText(this,"图片插入失败",Toast.LENGTH_SHORT).show();
//                }
            } else if (requestCode == PhotoPicker.REQUEST_CODE) {
                setImage(data);
            }
        }
    }

    public void setImage(final Intent data) {
        int width = CommonUtil.getScreenWidth(EditActivity.this);
        int height = CommonUtil.getScreenHeight(EditActivity.this);
        final ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);


        final ProgressDialog progressDialog = new ProgressDialog(EditActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setCancelable(true);


        int i = 0;
        //可以同时插入多张图片
        for (String imagePath : photos) {
            i++;
            progressDialog.setMessage("上传第" + i + "张图片中...");
            progressDialog.show();
            //Log.i("NewActivity", "###path=" + imagePath);
            Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
            Log.i("EditActivity", "###imagePath=" + imagePath);

            File file = bitmapToFile(bitmap, imagePath);


            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        et_text.insertImage(bmobFile.getFileUrl(), et_text.getMeasuredWidth());
                        Toast.makeText(EditActivity.this, "上传文件成功"
                                , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Log.i("EditActivity", getEditData());


                    } else {
                        Toast.makeText(EditActivity.this, "上传文件失败"
                                + e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    Log.i("path", value + "");
                    progressDialog.setProgress(value);
                }
            });
        }

        //et_text.addEditTextAtIndex(et_text.getLastIndex(), " ");

    }

    /**
     * 获取编辑数据
     *
     * @return
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_text.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }
        }
        return content.toString();
    }

    /**
     * 异步方式显示数据
     *
     * @param html
     */
    private void showDataSync(final String html) {

        subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        //在图片全部插入完毕后，再插入一个EditText，防止最后一张图片后无法插入文字
                        et_text.addEditTextAtIndex(et_text.getLastIndex(), "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        Toast.makeText(EditActivity.this, "解析错误：图片不存在或已损坏", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains("<img") && text.contains("src=")) {
                            //imagePath可能是本地路径，也可能是网络地址
                            String imagePath = StringUtils.getImgSrc(text);
                            //插入空的EditText，以便在图片前后插入文字
                            //et_text.addEditTextAtIndex(et_text.getLastIndex(), "");
                            et_text.addImageViewAtIndex(et_text.getLastIndex(), null, imagePath);
                        } else {
                            et_text.addEditTextAtIndex(et_text.getLastIndex(), text);
                        }
                    }
                });
    }

    /**
     * 显示数据
     */
    protected void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                subscriber.onNext(text);
            }
            subscriber.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    public File bitmapToFile(Bitmap bitmap, String filename) {

        File file = new File(filename);//将要保存图片的路径
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

    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        String path = uri.getPath();
        SpannableString ss = new SpannableString(path);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;

    }

    @Override
    public void onDeleteImage(String s) {

    }


}
