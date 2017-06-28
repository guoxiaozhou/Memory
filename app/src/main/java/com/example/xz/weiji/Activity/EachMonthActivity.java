package com.example.xz.weiji.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Money;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.example.xz.weiji.View.RecycleViewDivider;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xz on 2016/10/6.
 */

public class EachMonthActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private EachMonthAdapter adapter;
    private PieData pieData;
    private float[] result = new float[4];
    private Toolbar tb_eachmonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eachmonth);
        Toast.makeText(EachMonthActivity.this,"此功能还不完善  敬请见谅",Toast.LENGTH_SHORT).show();
        initView();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i=new Intent(EachMonthActivity.this,NoteActivity.class);
//        BmobUser user=BmobUser.getCurrentUser();
//        i.putExtra("name",user.getUsername());
//        startActivity(i);
//        finish();
    }
    private void onRefresh() {
        final List<Money> somelist = new ArrayList<Money>();

        BmobUser user = BmobUser.getCurrentUser();
        BmobQuery<Money> query = new BmobQuery<Money>();
        query.addWhereEqualTo("user", user.getObjectId());
        query.findObjects(new FindListener<Money>() {
            @Override
            public void done(List<Money> list, BmobException e) {
                if (e == null) {
                    for (Money money : list) {
                        String s = money.getUpdatedAt();
                        if (s.contains("2016-10")) {
                            somelist.add(money);
                        }else if(s.contains("2016-09")){

                        }
                    }
                    result = caculate(somelist);
                    pieData = getPieData(4, 100, result);

                    recyclerView.setLayoutManager(new LinearLayoutManager(EachMonthActivity.this));
                    adapter = new EachMonthAdapter();
                    recyclerView.setAdapter(adapter);
                    //recyclerView.addItemDecoration(new RecycleViewDivider(EachMonthActivity.this));
                    swipeLayout.setRefreshing(false);
                } else {
                    Toast.makeText(EachMonthActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("异常捕获", e.toString());
                }
            }
        });
    }
    //计算每月的平均值
    private float[] caculate(List<Money> list) {
        float[] floats = new float[4];
        float eat = 0, shop = 0, income = 0, others = 0, pay = 0;
        // Float eatPercent = null,shopPercent=null,incomePercent=null,othersPercent=null;
        for (Money money : list) {
            eat += money.getAll().get(0).get("吃饭");
            shop += money.getAll().get(1).get("网购");
            income += money.getAll().get(2).get("收入");
            others += money.getAll().get(3).get("其他");
            pay += money.getAll().get(4).get("支出");
        }
        float all = income + pay;
        floats[0] = eat / all;
        floats[1] = shop / all;
        floats[2] = income / all;
        floats[3] = others / all;
        Log.i("各百分比", floats.toString());

        return floats;
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        tb_eachmonth = (Toolbar) findViewById(R.id.tb_eachmonth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            tb_eachmonth.getLayoutParams().height = Utils.getAppBarHeight(this);
            tb_eachmonth.setPadding(tb_eachmonth.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    tb_eachmonth.getPaddingRight(),
                    tb_eachmonth.getPaddingBottom());
        }
        tb_eachmonth.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        tb_eachmonth.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(EachMonthActivity.this,NoteActivity.class);
//                BmobUser user=BmobUser.getCurrentUser();
//                i.putExtra("name",user.getUsername());
//                startActivity(i);
//                finish();
                onBackPressed();
            }
        });
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

        onRefresh();
    }

    //设置适配器

    private PieData getPieData(int count, float range, float[] result) {
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = result[0];
        float quarterly2 = result[1];
        float quarterly3 = result[2];
        float quarterly4 = result[3];

        yValues.add(new PieEntry(quarterly1, "吃饭"));
        yValues.add(new PieEntry(quarterly2, "网购"));
        yValues.add(new PieEntry(quarterly3, "收入"));
        yValues.add(new PieEntry(quarterly4, "其他"));

        //y轴的集合
        /*显示在比例图上*/
        PieDataSet pieDataSet = new PieDataSet(yValues, "大学生月支出");
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));

        pieDataSet.setSliceSpace(2f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.Red));
        colors.add(getResources().getColor(R.color.Green));
        colors.add(getResources().getColor(R.color.Yellow));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 12 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        //设置百分比字体大小
     //   pieDataSet.setValueTextSize(15);
        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

    private void showChart(PieChart pieChart, PieData pieData) {

        pieChart.setHoleRadius(50f);  //半径
        pieChart.setTransparentCircleRadius(68f); // 半透明圈
        pieChart.setDescription(null);

        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setCenterText("大学生月消费");  //饼状图中间的文字
        pieChart.setDescriptionColor(getResources().getColor(R.color.blue));
        pieData.getDataSet().setValueTextSize(15);
        //设置数据
        pieChart.setData(pieData);

     //   pieChart.setEntryLabelTextSize(10f);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setDescriptionTextSize(30);
        pieChart.setCenterTextSize(15);
        pieChart.setCenterTextColor(getResources().getColor(R.color.Grey));
        pieChart.setEntryLabelColor(getResources().getColor(R.color.white));
        pieChart.animateXY(1000, 1000);  //设置动画
    }

    private class EachMonthAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(EachMonthActivity.this).inflate(R.layout.item_eachmonth
                    , parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText("2015年10月");

            showChart(holder.pieChart, pieData);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        PieChart pieChart;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_eachmonth_time);
            pieChart = (PieChart) itemView.findViewById(R.id.pieChart);
        }
    }
}
