package com.example.xz.weiji.AppActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xz on 2016/9/23.
 */

public class TestActivity extends AppCompatActivity {

    private TextView textView;
    private String s=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        query();

    }

    private void query() {
        BmobQuery<Note> query = new BmobQuery<Note>();
        query.addWhereEqualTo("author", "59aa3dd08f");
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> object, BmobException e) {
                if (e == null) {
                    Toast.makeText(TestActivity.this, "查询成功;共" + object.size() + "条数据", Toast.LENGTH_SHORT).show();
                    for (Note note : object) {
                        Log.i("Note的值为", note.getNote());
                       // arrayList.add(note.getNote());
                        s=note.getNote();
                        Log.i("s的值为",s);
                        textView.setText(s);
                    }
                } else {
                    Toast.makeText(TestActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
    }
}
