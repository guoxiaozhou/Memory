package example.xz.com.myapplication.Utils;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.tablemanager.Connector;

/**
 * Created by Administrator on 2017/8/28.
 */

public class DBUtil {
    private volatile static DBUtil instance;
    private SQLiteDatabase db;

    public DBUtil() {
        db= Connector.getDatabase();
    }
    public static DBUtil getInstance(){
        if(instance==null){
            synchronized (DBUtil.class){
                if(instance==null)
                    instance=new DBUtil();
            }
        }
        return instance;
    }
    public void closeDB(){
        if(db!=null){
            db.close();
        }
    }
}
