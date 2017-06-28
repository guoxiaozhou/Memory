package com.example.xz.weiji.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/9/20.
 */

public class EditActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private Toolbar toolBar;
    private EditText et_text;
    private BmobUser user;
    private String isNote;
    private String isTitle;
    private Boolean isEmpty = false;
    private Note notebefore;
    private MaterialEditText et_notetitle;
    private Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittext);
        initView();

    }

    //标题栏右边按钮点击方法
    private void saveNote() {

        Note note = new Note();
        String s = et_text.getText().toString();
        String title=et_notetitle.getText().toString();
        //submit();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        }else {
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
                        EditActivity.this.setResult(0x11,i);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
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
        et_text = (EditText) findViewById(R.id.et_text);
        et_text.setSelection(0);
        et_notetitle = (MaterialEditText) findViewById(R.id.et_notetitle);
        //处理前先获得用户缓存数据
        user = BmobUser.getCurrentUser();

        i = getIntent();
        if (i != null) {
            isNote = i.getStringExtra("notestring");
            isTitle=i.getStringExtra("titlestring");
            notebefore = (Note) i.getSerializableExtra("note");
            if (isNote == null) {
                isEmpty = true;
            } else {
                isEmpty = false;
                et_text.setText(isNote);
                et_notetitle.setText(isTitle);
                //et_notetitle.setSelection(isTitle.length());
                //et_text.setSelection(isNote.length());
            }

        }



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
        }
        return true;
    }

    private void updateNote() {
        notebefore.setNote(et_text.getText().toString());
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
                    EditActivity.this.setResult(0x11,i);
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
        EditActivity.this.setResult(0x11,i);
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

/** private void submit() {
 // validate
 String text = et_text.getText().toString().trim();
 if (TextUtils.isEmpty(text)) {
 Toast.makeText(this, "text不能为空", Toast.LENGTH_SHORT).show();
 return;
 }else
 Toast.makeText(this, et_text.getText().toString(), Toast.LENGTH_SHORT).show();

 // TODO validate success, do something


 }**/
}
