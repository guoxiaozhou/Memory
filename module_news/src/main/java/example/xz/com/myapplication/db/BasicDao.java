package example.xz.com.myapplication.db;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */

public abstract class BasicDao<T> {
    public boolean save(T bean){
        return false;
    }
    public int delete(T bean){
        return -1;
    }
    public int update(T bean){
        return -1;
    }
    public List<T> find(){
        return null;
    }


}
