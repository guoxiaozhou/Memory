package example.xz.com.myapplication.Data;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class StoryModel extends DataSupport{

    /**
     * title : 中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）
     * ga_prefix : 052321
     * images : ["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"]
     * type : 0
     * id : 3930445
     */

    private String title;
    private String ga_prefix;
    private int type;
    private int storyId;
    private String image;

    public StoryModel() {
    }

    public StoryModel(Story story) {
        this.title = story.getTitle();
        this.ga_prefix = story.getGa_prefix();
        this.type = story.getType();
        this.storyId = story.getstoryid();
        this.image = story.getImages().get(0);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int id) {
        this.storyId = id;
    }

    public String getImages() {
        return image;
    }

    public void setImages(String images) {
        this.image = images;
    }
}
