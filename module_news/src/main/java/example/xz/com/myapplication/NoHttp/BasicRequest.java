package example.xz.com.myapplication.NoHttp;

import android.util.Log;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import example.xz.com.myapplication.Data.BasicBean;

/**
 * Created by Administrator on 2017/8/23.
 */

public class BasicRequest<T> extends RestRequest<T> {

    //要解析的JavaBean的class
    private Class<T> clazz;

    public BasicRequest(String url,Class<T> clazz) {
        this(url, RequestMethod.GET,clazz);
    }
    public BasicRequest(String url,RequestMethod requestMethod,Class<T> clazz) {
        super(url, RequestMethod.GET);
        this.clazz=clazz;
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String response= StringRequest.parseResponseString(responseHeaders,responseBody);
            Log.i("response:",response);
            return new Gson().fromJson(response,clazz);
    }
}
