package example.xz.com.myapplication.Creator;

import java.util.ArrayList;
import java.util.List;

import example.xz.com.myapplication.Data.Story;
import example.xz.com.myapplication.Data.StoryModel;
import example.xz.com.myapplication.Data.StoryViewModel;
import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryModel;
import example.xz.com.myapplication.Data.TopStoryViewModel;
import example.xz.com.myapplication.MyApplication;
import example.xz.com.myapplication.db.StoryDao;
import example.xz.com.myapplication.db.TopstoryDao;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ViewModelCreator {
    private static ViewModelCreator viewModelCreator;
    private ModelCreator modelCreator;
    private MyApplication application;

    public ViewModelCreator(MyApplication application) {
        this.application = application;
        this.modelCreator=ModelCreator.getInstance(application);
    }
    public static synchronized ViewModelCreator getInstance(MyApplication application){
        if(viewModelCreator==null)
            viewModelCreator=new ViewModelCreator(application);
        return viewModelCreator;
    }
    //保存topstory
    public List<TopStoryViewModel> setTopstoryModels(List<TopStory> topStories){
        List<TopStoryViewModel> topStoryViewModels=new ArrayList<TopStoryViewModel>();
        for(TopStory topStory:topStories){
            //将topstory->topstorymodel然后将此存进本地数据库，再将topstorymodel转成topstoryviewmodel准备给View显示
            TopStoryModel topStoryModel=modelCreator.setTopstoryModel(topStory);
            topStoryViewModels.add(new TopStoryViewModel(topStoryModel));
        }
        return topStoryViewModels;
    }
    public List<TopStoryViewModel> getTopstoryModels(){
        List<TopStoryViewModel> topStoryViewModels=new ArrayList<TopStoryViewModel>();
        List<TopStoryModel> topStoryModels=new TopstoryDao().find();
        if(topStoryModels!=null){
            for(TopStoryModel topStoryModel:topStoryModels){
                topStoryViewModels.add(new TopStoryViewModel(topStoryModel));
            }
        }
        return topStoryViewModels;
    }

    public List<StoryViewModel> setStoryModels(List<Story> stories){
        List<StoryViewModel> storyViewModels=new ArrayList<StoryViewModel>();
        for(Story story:stories){
            StoryModel storyModel=modelCreator.setStoryModel(story);
            storyViewModels.add(new StoryViewModel(storyModel));
        }
        return storyViewModels;
    }
    public List<StoryViewModel> getStoryModel(){
        List<StoryViewModel> storyViewModels=new ArrayList<StoryViewModel>();
        List<StoryModel> storyModels=new StoryDao().find();
        if(storyModels!=null){
            for(StoryModel storyModel:storyModels){
                storyViewModels.add(new StoryViewModel(storyModel));
            }
        }
        return storyViewModels;
    }
}
