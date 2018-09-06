package example.xz.com.myapplication.NoHttp;

import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2017/8/23.
 */

public interface ResponseCallback<T> {
    void onSucceed(int what, Response<T> response );
    void onFailed(int what,Object tag,Exception exception,long networkMills);
}
