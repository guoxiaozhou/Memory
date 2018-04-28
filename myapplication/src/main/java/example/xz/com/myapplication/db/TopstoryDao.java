package example.xz.com.myapplication.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryModel;

/**
 * Created by Administrator on 2017/8/28.
 */

public class TopstoryDao extends BasicDao<TopStoryModel> {
    @Override
    public boolean save(TopStoryModel bean) {
        delete(bean);
        return bean.save();
    }

    @Override
    public int delete(TopStoryModel bean) {
        return DataSupport.deleteAll(TopStoryModel.class,"topId=?",bean.getTopId()+"");
    }



    @Override
    public List<TopStoryModel> find() {
        List<TopStoryModel> all=DataSupport.findAll(TopStoryModel.class);
        if(all==null)
            all=new ArrayList<TopStoryModel>();
        return all;
    }
}
