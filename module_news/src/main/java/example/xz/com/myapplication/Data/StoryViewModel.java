package example.xz.com.myapplication.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class StoryViewModel {
    private String title;
    private String ga_prefix;
    private int type;
    private int storyId;
    private String images;

    public StoryViewModel() {
    }

    public StoryViewModel(StoryModel storyModel) {
        this.title = storyModel.getTitle();
        this.ga_prefix = storyModel.getGa_prefix();
        this.type = storyModel.getType();
        this.storyId = storyModel.getStoryId();
        this.images = storyModel.getImages();
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

    public int getId() {
        return storyId;
    }

    public void setId(int id) {
        this.storyId = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
