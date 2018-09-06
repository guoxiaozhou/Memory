package example.xz.com.myapplication.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import example.xz.com.myapplication.Data.StoryModel;

/**
 * Created by Administrator on 2017/9/6.
 */

public class StoryDao extends BasicDao<StoryModel> {
    @Override
    public boolean save(StoryModel bean) {
        delete(bean);
        return bean.save();
    }

    @Override
    public int delete(StoryModel bean) {
        return DataSupport.deleteAll(StoryModel.class,"storyId=?",bean.getStoryId()+"");
    }


    @Override
    public List<StoryModel> find() {
        List<StoryModel> all=DataSupport.findAll(StoryModel.class);
        if(all==null)
            all=new ArrayList<StoryModel>();
        return all;
    }
}
