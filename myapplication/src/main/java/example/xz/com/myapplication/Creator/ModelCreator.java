package example.xz.com.myapplication.Creator;

import example.xz.com.myapplication.Data.Story;
import example.xz.com.myapplication.Data.StoryModel;
import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryModel;
import example.xz.com.myapplication.MyApplication;
import example.xz.com.myapplication.db.StoryDao;
import example.xz.com.myapplication.db.TopstoryDao;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ModelCreator {
    private static ModelCreator modelCreator;
    private MyApplication myApplication;

    public ModelCreator(MyApplication myApplication) {
        this.myApplication = myApplication;
    }
    public static synchronized ModelCreator getInstance(MyApplication application){
        if(modelCreator==null)
            modelCreator=new ModelCreator(application);
        return modelCreator;
    }
    public TopStoryModel setTopstoryModel(TopStory topStory){
        TopStoryModel topStoryModel=new TopStoryModel(topStory);
        TopstoryDao topstoryDao=new TopstoryDao();
        topstoryDao.save(topStoryModel);
        return topStoryModel;
    }
    public StoryModel setStoryModel(Story story){
        StoryModel storyModel=new StoryModel(story);
        StoryDao storyDao=new StoryDao();
        storyDao.save(storyModel);
        return  storyModel;
    }

}
