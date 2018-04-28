package example.xz.com.myapplication.Data;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Story {
    private List<String> images;
    private int type;
    @SerializedName("id")
    private int storyid;
    private String ga_prefix;

    public Story() {
    }

    public Story(List<String> images, int type, int storyid, String ga_prefix, String title) {
        this.images = images;
        this.type = type;
        this.storyid = storyid;
        this.ga_prefix = ga_prefix;
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getstoryid() {
        return storyid;
    }

    public void setstoryid(int storyid) {
        this.storyid = storyid;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
