package com.example.xz.weiji.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Money;
import com.example.xz.weiji.Activity.MoneyActivity;
import com.example.xz.weiji.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xz on 2016/10/4.
 */

public class MoneyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private List<List<Map<String,Float>>> moneyList = new ArrayList<List<Map<String,Float>>>();
    private ArrayList<String> datelist = new ArrayList<String>();
    private List<List<Map<String,Float>>> eatList = new ArrayList<List<Map<String,Float>>>();
    private List<List<Map<String,Float>>> shopList = new ArrayList<List<Map<String,Float>>>();
    private List<List<Map<String,Float>>> otherList = new ArrayList<List<Map<String,Float>>>();
    private List<List<Map<String,Float>>> incomeList = new ArrayList<List<Map<String,Float>>>();
    private ArrayList<Money> moneys=new ArrayList<Money>();

    private SwipeRefreshLayout swipeLayout;
    private MoneyAdapter moneyAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_layout, container, false);
        Log.i("MoneyListFragment","执行了");
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
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
                },1000);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        onRefresh();
        Log.i("initView MoneyList","执行了");
    }

    @Override
    public void onRefresh() {
        BmobQuery<Money> query=new BmobQuery<Money>();
        BmobUser user=BmobUser.getCurrentUser();
        query.addWhereEqualTo("user",user.getObjectId());
        query.findObjects(new FindListener<Money>() {
            @Override
            public void done(List<Money> list, BmobException e) {
                if(e==null){
                    for(Money money: list){
                        moneyList.add(money.getAll());
                        eatList.add(money.getEat());
                        shopList.add(money.getShopping());
                        otherList.add(money.getOthers());
                        incomeList.add(money.getIncome());
                        datelist.add(money.getUpdatedAt());
                        Log.i("MoneyListItem",moneyList.toString());
                        moneys.add(money);
                    }
                    moneyAdapter=new MoneyAdapter(moneyList,eatList,shopList,otherList,incomeList,context,datelist);
                    moneyAdapter.setOnItemClickListener(new MoneyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            startMoneyActivity(position);
                        }
                    });
                    recyclerView.setAdapter(moneyAdapter);
                    swipeLayout.setRefreshing(false);
                }else {
                    Toast.makeText(context,"查询失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startMoneyActivity(int position) {
        Intent i=new Intent(context,MoneyActivity.class);
        i.putExtra("moneyItem",moneys.get(position));
        startActivity(i);
        getActivity().finish();

    }

    public static class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHoler>{
        private Context context;
        private List<List<Map<String,Float>>> moneyList;
        private List<List<Map<String,Float>>> eatList;
        private List<List<Map<String,Float>>> shopList;
        private List<List<Map<String,Float>>> otherList;
        private List<List<Map<String,Float>>> incomeList;
        private ArrayList<String> datelist;
        private OnItemClickListener onItemClickListener;

        public MoneyAdapter(List<List<Map<String, Float>>> moneyList, List<List<Map<String,Float>>> eatList,
                            List<List<Map<String,Float>>> shopList,List<List<Map<String,Float>>> otherList,
                            List<List<Map<String,Float>>> incomeList,
                            Context context, ArrayList<String> datelist) {
            this.moneyList = moneyList;
            this.context = context;
            this.eatList=eatList;
            this.datelist = datelist;
            this.shopList=shopList;
            this.otherList=otherList;
            this.incomeList=incomeList;
        }
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MoneyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
            MoneyViewHoler moneyViewHoler=new MoneyViewHoler(LayoutInflater.from(context).inflate(R.layout.item_money,
                    parent,false));
            return moneyViewHoler;
        }

        @Override
        public void onBindViewHolder(final MoneyViewHoler holder, final int position) {
            Float[] floats=new Float[5];
            Map<String,Float> eat = null,shop=null,other=null,income = null;
            String eatstring="",shopstring="",otherstring="",incomestring="";
            floats[0]=moneyList.get(position).get(0).get("吃饭");
            floats[1]=moneyList.get(position).get(1).get("网购");
            floats[2]=moneyList.get(position).get(2).get("收入");
            floats[3]=moneyList.get(position).get(3).get("其他");
            floats[4]=moneyList.get(position).get(4).get("支出");

            eatstring=resolvemap(eat,eatList,position);
            shopstring=resolvemap(shop,shopList,position);
            otherstring=resolvemap(other,otherList,position);
            incomestring=resolvemap(income,incomeList,position);
            Log.i("eat",eatstring);
            if(eatstring!="")
                eatstring+="\n";
            if(shopstring!="")
                shopstring+="\n";
            if(otherstring!="")
                otherstring+="\n";
            if(incomestring=="")
                otherstring=otherstring.trim();



            holder.money1.setText("餐饮:"+floats[0]+"  网购:"+floats[1]+"  其他:"+floats[3]+"\n"+"详细记录："+"\n"
                    +eatstring+shopstring+otherstring+incomestring);
            String s="支出:"+floats[4]+"      收入:"+floats[2];
            int bstart=s.indexOf(floats[4].toString());
            int bend=bstart+floats[4].toString().length();
            int fstart=s.indexOf(floats[2].toString());
            int fend=fstart+floats[2].toString().length();
            SpannableStringBuilder style=new SpannableStringBuilder(s);
            style.setSpan(new ForegroundColorSpan(Color.RED),bstart,bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.RED),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.money2.setText(style);
            holder.date.setText(datelist.get(position));
            if(onItemClickListener!=null){
                holder.ll_money_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView,position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return datelist.size();
        }

        class MoneyViewHoler extends RecyclerView.ViewHolder{
            TextView money1;
            TextView money2;
            TextView date;
            LinearLayout ll_money_item;
            public MoneyViewHoler(View itemView) {
                super(itemView);
                money1=(TextView)itemView.findViewById(R.id.tv_money1);
                money2=(TextView)itemView.findViewById(R.id.tv_money2);
                date=(TextView)itemView.findViewById(R.id.tv_money_date);
                ll_money_item=(LinearLayout)itemView.findViewById(R.id.ll_item_money);
            }
        }
    }
    public static String resolvemap(Map<String,Float> map,List<List<Map<String,Float>>> list,int position){
        String string="";
        for(int i=0;i<=3;i++){
            map=list.get(position).get(i);
            Set set=map.keySet();
            Iterator it=set.iterator();
            while (it.hasNext()){
                String key=(String)it.next();
                Float value=(Float) map.get(key);
                if(value==0.0)
                    break;
                if(key==""){
                    key="null";
                }
                string+=key+":"+value+"\n";

            }

        }
        string=string.trim();
        return  string;
    }
}
