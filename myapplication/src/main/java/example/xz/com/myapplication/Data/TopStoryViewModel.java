package example.xz.com.myapplication.Data;

/**
 * Created by Administrator on 2017/8/23.
 */

public class TopStoryViewModel {
    private String image;
    private int type;
    private int topId;
    private String ga_prefix;
    private String title;

    public TopStoryViewModel(TopStoryModel topStoryModel) {
        this.image = topStoryModel.getImage();
        this.type = topStoryModel.getType();
        this.topId = topStoryModel.getTopId();
        this.ga_prefix = topStoryModel.getGa_prefix();
        this.title = topStoryModel.getTitle();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int id) {
        this.topId = id;
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
}
