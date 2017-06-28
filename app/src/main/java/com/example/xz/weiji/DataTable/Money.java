package com.example.xz.weiji.DataTable;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by xz on 2016/10/3.
 */

public class Money extends BmobObject {
     private BmobUser user;
    private List<Map<String,Float>> eat;
    private List<Map<String,Float>> shopping;
    private List<Map<String,Float>> income;
    private List<Map<String,Float>> others;

    public List<Map<String, Float>> getAll() {
        return all;
    }

    public void setAll(List<Map<String, Float>> all) {
        this.all = all;
    }

    private List<Map<String,Float>> all;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public List<Map<String, Float>> getEat() {
        return eat;
    }

    public void setEat(List<Map<String, Float>> eat) {
        this.eat = eat;
    }

    public List<Map<String, Float>> getShopping() {
        return shopping;
    }

    public void setShopping(List<Map<String, Float>> shopping) {
        this.shopping = shopping;
    }

    public List<Map<String, Float>> getIncome() {
        return income;
    }

    public void setIncome(List<Map<String, Float>> income) {
        this.income = income;
    }

    public List<Map<String, Float>> getOthers() {
        return others;
    }

    public void setOthers(List<Map<String, Float>> others) {
        this.others = others;
    }
}
