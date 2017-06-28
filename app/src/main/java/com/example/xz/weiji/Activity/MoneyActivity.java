package com.example.xz.weiji.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Money;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/10/3.
 */

public class MoneyActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolBar;
    private EditText et_string_eat1;
    private EditText et_float_eat1;
    private EditText et_string_eat2;
    private EditText et_float_eat2;
    private EditText et_string_eat3;
    private EditText et_float_eat3;
    private EditText et_string_eat4;
    private EditText et_float_eat4;
    private EditText et_string_shop1;
    private EditText et_float_shop1;
    private EditText et_string_shop2;
    private EditText et_float_shop2;
    private EditText et_string_shop3;
    private EditText et_float_shop3;
    private EditText et_string_shop4;
    private EditText et_float_shop4;
    private EditText et_string_income1;
    private EditText et_float_income1;
    private EditText et_string_income2;
    private EditText et_float_income2;
    private EditText et_string_income3;
    private EditText et_float_income3;
    private EditText et_string_income4;
    private EditText et_float_income4;
    private EditText et_string_others1;
    private EditText et_float_others1;
    private EditText et_string_others2;
    private EditText et_float_others2;
    private EditText et_string_others3;
    private EditText et_float_others3;
    private EditText et_string_others4;
    private EditText et_float_others4;
    private Button bt_save;
    private List<Map<String, Float>> eat=new ArrayList<Map<String, Float>>();
    private List<Map<String, Float>> shopping=new ArrayList<Map<String, Float>>();
    private List<Map<String, Float>> income=new ArrayList<Map<String, Float>>();
    private List<Map<String, Float>> others=new ArrayList<Map<String, Float>>();
    private List<Map<String,Float>> all=new ArrayList<Map<String, Float>>();
    private boolean isEmpty=true;
    private Money moneybefore;
    Map<String, Float> data;
    private BmobUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jizhang);
        initView();
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
        toolBar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        et_string_eat1 = (EditText) findViewById(R.id.et_string_eat1);
        et_float_eat1 = (EditText) findViewById(R.id.et_float_eat1);
        et_string_eat2 = (EditText) findViewById(R.id.et_string_eat2);
        et_float_eat2 = (EditText) findViewById(R.id.et_float_eat2);
        et_string_eat3 = (EditText) findViewById(R.id.et_string_eat3);
        et_float_eat3 = (EditText) findViewById(R.id.et_float_eat3);
        et_string_eat4 = (EditText) findViewById(R.id.et_string_eat4);
        et_float_eat4 = (EditText) findViewById(R.id.et_float_eat4);
        et_string_shop1 = (EditText) findViewById(R.id.et_string_shop1);
        et_float_shop1 = (EditText) findViewById(R.id.et_float_shop1);
        et_string_shop2 = (EditText) findViewById(R.id.et_string_shop2);
        et_float_shop2 = (EditText) findViewById(R.id.et_float_shop2);
        et_string_shop3 = (EditText) findViewById(R.id.et_string_shop3);
        et_float_shop3 = (EditText) findViewById(R.id.et_float_shop3);
        et_string_shop4 = (EditText) findViewById(R.id.et_string_shop4);
        et_float_shop4 = (EditText) findViewById(R.id.et_float_shop4);
        et_string_income1 = (EditText) findViewById(R.id.et_string_income1);
        et_float_income1 = (EditText) findViewById(R.id.et_float_income1);
        et_string_income2 = (EditText) findViewById(R.id.et_string_income2);
        et_float_income2 = (EditText) findViewById(R.id.et_float_income2);
        et_string_income3 = (EditText) findViewById(R.id.et_string_income3);
        et_float_income3 = (EditText) findViewById(R.id.et_float_income3);
        et_string_income4 = (EditText) findViewById(R.id.et_string_income4);
        et_float_income4 = (EditText) findViewById(R.id.et_float_income4);
        et_string_others1 = (EditText) findViewById(R.id.et_string_others1);
        et_float_others1 = (EditText) findViewById(R.id.et_float_others1);
        et_string_others2 = (EditText) findViewById(R.id.et_string_others2);
        et_float_others2 = (EditText) findViewById(R.id.et_float_others2);
        et_string_others3 = (EditText) findViewById(R.id.et_string_others3);
        et_float_others3 = (EditText) findViewById(R.id.et_float_others3);
        et_string_others4 = (EditText) findViewById(R.id.et_string_others4);
        et_float_others4 = (EditText) findViewById(R.id.et_float_others4);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        user=BmobUser.getCurrentUser();

        Intent i=getIntent();
        if(i!=null){
            moneybefore= (Money) i.getSerializableExtra("moneyItem");
            if(moneybefore==null){
                isEmpty=true;
            }else {
                isEmpty=false;
                //设置EditText显示已有数据
                solveMoney(moneybefore);
            }

        }
    }

    private void solveMoney(Money moneybefore) {
        Map<String,Float> map;
        map=moneybefore.getEat().get(0);
        for (String key : map.keySet()) {
            et_string_eat1.setText(key);
            if(map.get(key)==0.0)
                et_float_eat1.setText("");
            else
                et_float_eat1.setText(map.get(key)+"");
        }
        map=moneybefore.getEat().get(1);
        for (String key : map.keySet()) {
            et_string_eat2.setText(key);
            if(map.get(key)==0.0)
                et_float_eat2.setText("");
            else
                et_float_eat2.setText(map.get(key)+"");
        }
        map=moneybefore.getEat().get(2);
        for (String key : map.keySet()) {
            et_string_eat3.setText(key);
            if(map.get(key)==0.0)
                et_float_eat3.setText("");
            else
                et_float_eat3.setText(map.get(key)+"");
        }
        map=moneybefore.getEat().get(3);
        for (String key : map.keySet()) {
            et_string_eat4.setText(key);
            if(map.get(key)==0.0)
                et_float_eat4.setText("");
            else
                et_float_eat4.setText(map.get(key)+"");
        }
        map=moneybefore.getShopping().get(0);
        for (String key : map.keySet()) {
            et_string_shop1.setText(key);
            if(map.get(key)==0.0)
                et_float_shop1.setText("");
            else
                et_float_shop1.setText(map.get(key)+"");
        }
        map=moneybefore.getShopping().get(1);
        for (String key : map.keySet()) {
            et_string_shop2.setText(key);
            if(map.get(key)==0.0)
                et_float_shop2.setText("");
            else
                et_float_shop2.setText(map.get(key)+"");
        }
        map=moneybefore.getShopping().get(2);
        for (String key : map.keySet()) {
            et_string_shop3.setText(key);
            if(map.get(key)==0.0)
                et_float_shop3.setText("");
            else
                et_float_shop3.setText(map.get(key)+"");
        }
        map=moneybefore.getShopping().get(3);
        for (String key : map.keySet()) {
            et_string_shop4.setText(key);
            if(map.get(key)==0.0)
                et_float_shop4.setText("");
            else
                et_float_shop4.setText(map.get(key)+"");
        }
        map=moneybefore.getIncome().get(0);
        for (String key : map.keySet()) {
            et_string_income1.setText(key);
            if(map.get(key)==0.0)
                et_float_income1.setText("");
            else
                et_float_income1.setText(map.get(key)+"");
        }
        map=moneybefore.getIncome().get(1);
        for (String key : map.keySet()) {
            et_string_income2.setText(key);
            if(map.get(key)==0.0)
                et_float_income2.setText("");
            else
                et_float_income2.setText(map.get(key)+"");
        }
        map=moneybefore.getIncome().get(2);
        for (String key : map.keySet()) {
            et_string_income3.setText(key);
            et_float_income3.setText(map.get(key)+"");
        }
        map=moneybefore.getIncome().get(3);
        for (String key : map.keySet()) {
            et_string_income4.setText(key);
            if(map.get(key)==0.0)
                et_float_income4.setText("");
            else
                et_float_income4.setText(map.get(key)+"");
        }
        map=moneybefore.getOthers().get(0);
        for (String key : map.keySet()) {
            et_string_others1.setText(key);
            if(map.get(key)==0.0)
                et_float_others1.setText("");
            else
                et_float_others1.setText(map.get(key)+"");
        }
        map=moneybefore.getOthers().get(1);
        for (String key : map.keySet()) {
            et_string_others2.setText(key);
            if(map.get(key)==0.0)
                et_float_others2.setText("");
            else
                et_float_others2.setText(map.get(key)+"");
        }
        map=moneybefore.getOthers().get(2);
        for (String key : map.keySet()) {
            et_string_others3.setText(key);
            if(map.get(key)==0.0)
                et_float_others3.setText("");
            else
                et_float_others3.setText(map.get(key)+"");
        }
        map=moneybefore.getOthers().get(3);
        for (String key : map.keySet()) {
            et_string_others4.setText(key);
            if(map.get(key)==0.0)
                et_float_eat2.setText("");
            else
                et_float_others4.setText(map.get(key)+"");
        }
        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                if(isEmpty){
                    saveMoney();
                }else {
                    updateMoney();
                }
                break;
        }
    }

    private void updateMoney() {
        setMoney();
        moneybefore.setEat(eat);
        moneybefore.setShopping(shopping);
        moneybefore.setIncome(income);
        moneybefore.setOthers(others);
        moneybefore.setAll(all);
        moneybefore.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(MoneyActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MoneyActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MoneyActivity.this,ZhangbenActivity.class);
                   // i.putExtra("name",user.getUsername());
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(MoneyActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveMoney() {
        Money money = new Money();

        setMoney();
        money.setUser(user);
        money.setEat(eat);
        money.setShopping(shopping);
        money.setIncome(income);
        money.setOthers(others);
        money.setAll(all);
        money.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null)
                    Toast.makeText(MoneyActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MoneyActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(MoneyActivity.this,ZhangbenActivity.class));
        finish();


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MoneyActivity.this,ZhangbenActivity.class));
        finish();
    }

    private void setMoney() {
        data = new HashMap<String, Float>();
        data.put(et_string_eat1.getText().toString(), solveFloat(et_float_eat1.getText().toString()));
        eat.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_eat2.getText().toString(), solveFloat(et_float_eat2.getText().toString()));
        eat.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_eat3.getText().toString(), solveFloat(et_float_eat3.getText().toString()));
        eat.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_eat4.getText().toString(), solveFloat(et_float_eat4.getText().toString()));
        eat.add(data);

        data=new HashMap<String, Float>();
        Float eat=solveFloat(et_float_eat1.getText().toString())+solveFloat(et_float_eat2.getText().toString())
                +solveFloat(et_float_eat3.getText().toString())+solveFloat(et_float_eat4.getText().toString());
        data.put("吃饭",eat);
        all.add(data);

        data = new HashMap<String, Float>();
        data.put(et_string_shop1.getText().toString(), solveFloat(et_float_shop1.getText().toString()));
        shopping.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_shop2.getText().toString(), solveFloat(et_float_shop2.getText().toString()));
        shopping.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_shop3.getText().toString(), solveFloat(et_float_shop3.getText().toString()));
        shopping.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_shop4.getText().toString(), solveFloat(et_float_shop4.getText().toString()));
        shopping.add(data);

        data=new HashMap<String, Float>();
        Float shopping=solveFloat(et_float_shop1.getText().toString())+solveFloat(et_float_shop2.getText().toString())
                +solveFloat(et_float_shop3.getText().toString())+solveFloat(et_float_shop4.getText().toString());
        data.put("网购",shopping);
        all.add(data);

        data = new HashMap<String, Float>();
        data.put(et_string_income1.getText().toString(), solveFloat(et_float_income1.getText().toString()));
        income.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_income2.getText().toString(), solveFloat(et_float_income2.getText().toString()));
        income.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_income3.getText().toString(), solveFloat(et_float_income3.getText().toString()));
        income.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_income4.getText().toString(), solveFloat(et_float_income4.getText().toString()));
        income.add(data);

        data=new HashMap<String, Float>();
        Float income=solveFloat(et_float_income1.getText().toString())+solveFloat(et_float_income2.getText().toString())
                +solveFloat(et_float_income3.getText().toString())+solveFloat(et_float_income4.getText().toString());
        data.put("收入",income);
        all.add(data);

        data = new HashMap<String, Float>();
        data.put(et_string_others1.getText().toString(), solveFloat(et_float_others1.getText().toString()));
        others.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_others2.getText().toString(), solveFloat(et_float_others2.getText().toString()));
        others.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_others3.getText().toString(), solveFloat(et_float_others3.getText().toString()));
        others.add(data);
        data = new HashMap<String, Float>();
        data.put(et_string_others4.getText().toString(), solveFloat(et_float_others4.getText().toString()));
        others.add(data);

        data=new HashMap<String, Float>();
        Float others=solveFloat(et_float_others1.getText().toString())+solveFloat(et_float_others2.getText().toString())
                +solveFloat(et_float_others3.getText().toString())+solveFloat(et_float_others4.getText().toString());
        data.put("其他",others);
        all.add(data);

        data=new HashMap<String, Float>();
        Float allpay=eat+shopping+others;
        data.put("支出",allpay);
        all.add(data);
    }
    private float solveFloat(String s){
        if(s.equals(""))
            return 0;
        else
            return Float.parseFloat(s);
    }
}
