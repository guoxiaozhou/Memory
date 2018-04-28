package com.example.xz.weiji.DataTable;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by xz on 2016/9/14.
 */

public class Note extends BmobObject {
    private String note;
    private BmobUser author;
    private String sort;
    private String title;

    public List<BmobFile> getImages() {
        return images;
    }

    public void setImages(List<BmobFile> images) {
        this.images = images;
    }

    private List<BmobFile> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }
}
