package com.example.xz.weiji.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xz.weiji.AppActivity.DaojishiActivity;
import com.example.xz.weiji.DataTable.Daojishi;
import com.example.xz.weiji.R;

import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/9/6.
 */

public class TestDialog extends Dialog {



    public Context context;
    private EditText et_daojishi_text;
    private Button button;
    private DatePicker datepicker;
    private ImageView imageView;
    private int year;
    private int month;
    private int day;
    private String selecteddate;

    public TestDialog(@NonNull Context context) {
        super(context,R.style.mydialog);
        this.context=context;
        // 拿到Dialog的Window, 修改Window的属性
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height=WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM ;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        window.setWindowAnimations(R.style.daojishiDialog);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_daojishi);
        initView();
        initDatePicker();

    }



    private void initView() {
        et_daojishi_text = (EditText)findViewById(R.id.et_daojishi_text);
        button = (Button) findViewById(R.id.btn_daojishi_commit);
        datepicker = (DatePicker)findViewById(R.id.datepicker);
        imageView=(ImageView)findViewById(R.id.iv_daojishi_close);

        datepicker.setMinDate(System.currentTimeMillis()-1000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commit();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDialog.this.dismiss();
            }
        });

    }
    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        int tempMonth=month+1;
        selecteddate= year + "-" + tempMonth + "-" + day ;
        Log.i("DaojishiActivity","selecteddate:"+selecteddate);
        datepicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                selecteddate= year + "-" + monthOfYear + "-" + dayOfMonth ;
                //  Toast.makeText(DaojishiActivity.this, "您已选择"+selecteddate, Toast.LENGTH_SHORT).show();
                Log.i("DaojishiActivity", "点击日期时selecteddate："+selecteddate);
                // daojishi.setLaterdate(date);
            }
        });


    }

    private void commit() {

        final Daojishi daojishi = new Daojishi();
        daojishi.setText(et_daojishi_text.getText().toString());
        daojishi.setLaterdate(selecteddate);
        daojishi.setUser(BmobUser.getCurrentUser());




        if (TextUtils.isEmpty(et_daojishi_text.getText().toString())) {
            Toast.makeText(context, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
            daojishi.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                        TestDialog.this.dismiss();
                        ((DaojishiActivity)context).onResume();
                    }else {
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                        TestDialog.this.dismiss();
                    }
                }
            });


        }
    }
}
