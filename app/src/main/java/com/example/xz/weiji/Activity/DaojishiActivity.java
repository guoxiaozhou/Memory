package com.example.xz.weiji.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Daojishi;
import com.example.xz.weiji.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xz on 2016/11/6.
 */

public class DaojishiActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private Toolbar tb_daojishi;
    private RecyclerView rclv_mdaojishilist;
    private SwipeRefreshLayout swipeLayout;
    private EditText et_daojishi_text;
    private DatePicker datepicker;
    private Button button;
    private View view;
    private int year;
    private int month;
    private int day;
    private BmobUser user;
    private String selecteddate;
    private Dialog dialog;
    private static Context context;
    private List<String> text1list=new ArrayList<String>();
    private List<String> text2list=new ArrayList<String>();
    private List<String> text3list=new ArrayList<String>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daojishi);
        context=DaojishiActivity.this;
        initView();
    }

    private void initView() {
        tb_daojishi = (Toolbar) findViewById(R.id.tb_daojishi);
        rclv_mdaojishilist = (RecyclerView) findViewById(R.id.rclv_mdaojishilist);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        tb_daojishi.inflateMenu(R.menu.menu_main);
        tb_daojishi.setOnMenuItemClickListener(DaojishiActivity.this);

        swipeLayout.setColorSchemeResources(R.color.blue);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        user = BmobUser.getCurrentUser();

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在登陆");
        progressDialog.setCancelable(true);

        onRefresh();
    }

    private void onRefresh() {
        BmobQuery<Daojishi> query=new BmobQuery<Daojishi>();
        query.addWhereEqualTo("user",user.getObjectId());
        query.findObjects(new FindListener<Daojishi>() {
            @Override
            public void done(List<Daojishi> list, BmobException e) {
                if(e==null){
                    for(Daojishi daojishi:list){
                        text1list.add(daojishi.getText());
                        text2list.add(daojishi.getResult());
                        text3list.add(daojishi.getLaterdate());
                    }
                    rclv_mdaojishilist.setLayoutManager(new GridLayoutManager(context,2));
                    DaojishiAdapter daojishiAdapter=new DaojishiAdapter(text1list,text2list,text3list);
                    rclv_mdaojishilist.setAdapter(daojishiAdapter);
                    swipeLayout.setRefreshing(false);
                }else {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            //Toast.makeText(DaojishiActivity.this, "发生错误退出", Toast.LENGTH_SHORT).show();
            view = LayoutInflater.from(DaojishiActivity.this).inflate(R.layout.dialog_daojishi, null);
            et_daojishi_text = (EditText) view.findViewById(R.id.et_daojishi_text);
            button = (Button) view.findViewById(R.id.btn_daojishi_commit);
            datepicker = (DatePicker) view.findViewById(R.id.datepicker);
            button.setOnClickListener(this);
            dialog=new AlertDialog.Builder(DaojishiActivity.this).setView(view).show();

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            datepicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear++;
                    selecteddate= year + "-" + monthOfYear + "-" + dayOfMonth ;
                    Toast.makeText(DaojishiActivity.this, "您已选择"+selecteddate, Toast.LENGTH_SHORT).show();
                    Log.i("倒计时日期", selecteddate);
                    // daojishi.setLaterdate(date);
                }
            });

        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_daojishi_commit:
                commit();

        }


    }

    private void commit() {
        progressDialog.show();
        final Daojishi daojishi = new Daojishi();
        String day=null;
        daojishi.setText(et_daojishi_text.getText().toString());
        daojishi.setLaterdate(selecteddate);
        Toast.makeText(DaojishiActivity.this, "您已选择"+selecteddate, Toast.LENGTH_SHORT).show();
        daojishi.setUser(user);

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=new Date();
        String s1=simpleDateFormat.format(date);

        String s2=selecteddate+" 12:00";
        Log.i("现在日期",s1);
        Log.i("设定日期",s2);

        try {
            long from=simpleDateFormat.parse(s1).getTime();
            long to=simpleDateFormat.parse(s2).getTime();
            int days=(int)((to-from)/(1000*60*60*24));
            day=days+"天";
            Log.i("天数",day);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        daojishi.setResult(day);


        if (TextUtils.isEmpty(et_daojishi_text.getText().toString())) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
           daojishi.save(new SaveListener<String>() {
               @Override
               public void done(String s, BmobException e) {
                   if(e==null){
                       Toast.makeText(DaojishiActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                   }else
                       Toast.makeText(DaojishiActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
               }
           });

         dialog.dismiss();
            startActivity(new Intent(context,DaojishiActivity.class));
            finish();
            progressDialog.cancel();
        }
    }

    public static class DaojishiAdapter extends RecyclerView.Adapter<DaojishiAdapter.DaojishiViewHolder>{
        private List<String> text1list;
        private List<String> text2list;
        private List<String> text3list;

        public DaojishiAdapter(List<String> text1list, List<String> text2list, List<String> text3list) {
            this.text1list = text1list;
            this.text2list = text2list;
            this.text3list = text3list;
        }

        @Override
        public DaojishiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DaojishiViewHolder daojishiViewHolder=new DaojishiViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daojishi,parent,false));

            return daojishiViewHolder;
        }

        @Override
        public void onBindViewHolder(DaojishiViewHolder holder, int position) {
           holder.text1.setText("距离"+text1list.get(position)+"还有");
            holder.text2.setText(text2list.get(position));
            holder.text3.setText(text3list.get(position));

        }

        @Override
        public int getItemCount() {
            return text1list.size();
        }

        class DaojishiViewHolder extends RecyclerView.ViewHolder{
            TextView text1;
            TextView text2;
            TextView text3;
            LinearLayout linearLayout;
            public DaojishiViewHolder(View itemView) {
                super(itemView);
                text1=(TextView)itemView.findViewById(R.id.tv_daojishi_one);
                text2=(TextView)itemView.findViewById(R.id.tv_daojishi_two);
                text3=(TextView)itemView.findViewById(R.id.tv_daojishi_three);
                linearLayout=(LinearLayout)itemView.findViewById(R.id.ll_daojishi_listitem);

            }
        }
    }

}