package com.example.xz.weiji.DataTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by xz on 2016/11/6.
 */

public class Daojishi extends BmobObject {
    private String text;
    private String laterdate;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    private BmobUser user;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLaterdate() {
        return laterdate;
    }

    public void setLaterdate(String laterdate) {
        this.laterdate = laterdate;
    }
}
