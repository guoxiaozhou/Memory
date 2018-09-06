package com.example.xz.weiji.DataTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by xz on 2016/10/18.
 */

public class Group extends BmobObject {
    private String everyclass;
    private BmobUser author;

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }

    public String getEveryclass() {
        return everyclass;
    }

    public void setEveryclass(String everyclass) {
        this.everyclass = everyclass;
    }
}
