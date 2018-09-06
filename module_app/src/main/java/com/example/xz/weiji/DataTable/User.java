package com.example.xz.weiji.DataTable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by xz on 2016/9/14.
 */

public class User extends BmobUser {
     private BmobFile head;

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }
}
