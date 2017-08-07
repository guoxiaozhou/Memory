package com.example.xz.weiji.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Daojishi;
import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.example.xz.weiji.View.LeftSwipeMenuRecyclerView;
import com.example.xz.weiji.View.OnItemActionListener;

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
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/11/6.
 */

public class DaojishiActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private Toolbar tb_daojishi;
    private LeftSwipeMenuRecyclerView rclv_mdaojishilist;
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
    private List<String> text1list;
    private List<String> text2list;
    private List<String> text3list;
    private List<Daojishi> daojishilist;
    private DaojishiAdapter daojishiAdapter;
    private boolean isResume=false;
   // private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daojishi);
        context=DaojishiActivity.this;
        initView();


    }


    private void initView() {
        tb_daojishi = (Toolbar) findViewById(R.id.tb_daojishi);
        rclv_mdaojishilist = (LeftSwipeMenuRecyclerView) findViewById(R.id.rclv_mdaojishilist);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        tb_daojishi.inflateMenu(R.menu.menu_main);
        tb_daojishi.setOnMenuItemClickListener(DaojishiActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            tb_daojishi.getLayoutParams().height = Utils.getAppBarHeight(this);
            tb_daojishi.setPadding(tb_daojishi.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    tb_daojishi.getPaddingRight(),
                    tb_daojishi.getPaddingBottom());
        }



//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("正在登陆");
//        progressDialog.setCancelable(true);


    }

    private void onRefresh() {
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

        text1list=new ArrayList<String>();
        text2list=new ArrayList<String>();
        text3list=new ArrayList<String>();
        daojishilist=new ArrayList<Daojishi>();
        BmobQuery<Daojishi> query=new BmobQuery<Daojishi>();
        query.addWhereEqualTo("user",user.getObjectId());
        query.findObjects(new FindListener<Daojishi>() {
            @Override
            public void done(List<Daojishi> list, BmobException e) {
                if(e==null){
                    for(Daojishi daojishi:list){
                        text1list.add(daojishi.getText());
                        text2list.add(getDay(daojishi.getLaterdate()));
                        text3list.add(daojishi.getLaterdate());
                        daojishilist.add(daojishi);
                    }
                    rclv_mdaojishilist.setItemAnimator(new DefaultItemAnimator());
                    rclv_mdaojishilist.setLayoutManager(new GridLayoutManager(context,2));
                    daojishiAdapter=new DaojishiAdapter(text1list,text2list,text3list);
                    rclv_mdaojishilist.setAdapter(daojishiAdapter);
                    rclv_mdaojishilist.setOnItemActionListener(new OnItemActionListener() {
                        @Override
                        public void OnItemClick(int position) {
                            Toast.makeText(DaojishiActivity.this,"点击了倒计日",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnItemTop(int position) {

                        }

                        @Override
                        public void OnItemDelete(int position) {
                            deleteDaojiri(position);
                             //Toast.makeText(DaojishiActivity.this,"点击了删除按钮",Toast.LENGTH_SHORT).show();
                        }
                    });
                    swipeLayout.setRefreshing(false);
                   // Toast.makeText(context, "调用了onRefresh方法", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //删除倒计日
    public void deleteDaojiri(final int position){


        Daojishi daojishi = daojishilist.get(position);
        daojishi.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    text1list.remove(position);
                    text2list.remove(position);
                    text3list.remove(position);

                    daojishiAdapter.notifyItemRemoved(position);



                } else
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
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
//            Date date=new Date();
//            long time=date.getTime();

            datepicker.setMinDate(System.currentTimeMillis()-1000);
            button.setOnClickListener(this);
            dialog=new AlertDialog.Builder(DaojishiActivity.this).setView(view).show();

            WindowManager.LayoutParams layoutParams=dialog.getWindow().getAttributes();
            layoutParams.width= LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height= LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
            Window window=dialog.getWindow();
            window.setWindowAnimations(R.style.mydialog);


            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            datepicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear++;
                    selecteddate= year + "-" + monthOfYear + "-" + dayOfMonth ;
                  //  Toast.makeText(DaojishiActivity.this, "您已选择"+selecteddate, Toast.LENGTH_SHORT).show();
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
                isResume=true;
                commit();

        }


    }

    private void commit() {

//        progressDialog.show();
        final Daojishi daojishi = new Daojishi();
      //  String day=null;
        daojishi.setText(et_daojishi_text.getText().toString());
        daojishi.setLaterdate(selecteddate);
      //  Toast.makeText(DaojishiActivity.this, "您已选择"+selecteddate, Toast.LENGTH_SHORT).show();
        daojishi.setUser(user);




        if (TextUtils.isEmpty(et_daojishi_text.getText().toString())) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
           daojishi.save(new SaveListener<String>() {
               @Override
               public void done(String s, BmobException e) {
                   if(e==null){
                       Toast.makeText(DaojishiActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                       dialog.dismiss();
                       onResume();
                   }else {
                       Toast.makeText(DaojishiActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                       dialog.dismiss();
                   }
               }
           });


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }




    public String getDay(String selecteddate){
        String day=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=new Date();
        String s1=simpleDateFormat.format(date);

        String s2=selecteddate+" 00:00";
        Log.i("现在日期",s1);
        Log.i("设定日期",s2);

        try {
            long from=simpleDateFormat.parse(s1).getTime();
            long to=simpleDateFormat.parse(s2).getTime();
            int days=(int)((to-from)/(1000*60*60*24))+1;
            day=days+"天";
            Log.i("天数",day);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public  class DaojishiAdapter extends RecyclerView.Adapter<DaojishiAdapter.DaojishiViewHolder>{
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
           holder.text1.setText("距  "+text1list.get(position));
            holder.text2.setText(text2list.get(position));
            holder.text3.setText(text3list.get(position));

        }

        @Override
        public int getItemCount() {
            return text1list.size();
        }

        public class DaojishiViewHolder extends RecyclerView.ViewHolder{
            TextView text1;
            TextView text2;
            TextView text3;
            public LinearLayout linearLayout;
            public LinearLayout ll_delete;
            public LinearLayout ll_star;
            public DaojishiViewHolder(View itemView) {
                super(itemView);
                text1=(TextView)itemView.findViewById(R.id.tv_daojishi_one);
                text2=(TextView)itemView.findViewById(R.id.tv_daojishi_two);
                text3=(TextView)itemView.findViewById(R.id.tv_daojishi_three);
                linearLayout=(LinearLayout)itemView.findViewById(R.id.ll_daojishi_listitem);
                ll_delete=(LinearLayout)itemView.findViewById(R.id.ll__daojishi_delete);
                ll_star=(LinearLayout)itemView.findViewById(R.id.ll_daojishi_star);
            }
        }
    }

}