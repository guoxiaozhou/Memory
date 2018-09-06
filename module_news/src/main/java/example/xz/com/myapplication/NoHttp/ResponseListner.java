package example.xz.com.myapplication.NoHttp;

import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import example.xz.com.myapplication.NoHttp.ResponseCallback;

/**
 * Created by Administrator on 2017/8/23.
 */

public class ResponseListner<T> implements OnResponseListener<T> {

    private ResponseCallback<T> callback;

    public ResponseListner(ResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<T> response) {
        if(this.callback!=null){
            callback.onSucceed(what,response);
        }
    }

    @Override
    public void onFailed(int what, Response<T> response) {
        if(this.callback!=null){
            this.callback.onFailed(what,response.getTag(),response.getException(),response.getNetworkMillis());
        }
    }

    @Override
    public void onFinish(int what) {

    }
}
