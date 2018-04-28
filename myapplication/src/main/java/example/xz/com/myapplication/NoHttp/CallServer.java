package example.xz.com.myapplication.NoHttp;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by Administrator on 2017/8/23.
 */

public class CallServer {
    private static CallServer instance;
    public static CallServer getInstance(){
        if(instance==null){
            synchronized (CallServer.class){
                if(instance==null)
                    instance=new CallServer();
            }

        }
        return instance;
    }
    private RequestQueue queue;
    private CallServer(){
        queue= NoHttp.newRequestQueue(5);
    }
    public <T> void request(int what, Request<T> request,ResponseCallback<T> callback){
        queue.add(what,request,new ResponseListner<T>(callback));
    }
    public void stop(){
        queue.stop();
    }
}
