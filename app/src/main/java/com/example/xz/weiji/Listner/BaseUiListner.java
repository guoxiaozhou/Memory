package com.example.xz.weiji.Listner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.xz.weiji.AppActivity.LoginActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/10.
 */

public class BaseUiListner implements IUiListener {

    Context context;
    Tencent tencent;

    String openid;
    String access_token;
    String expires_in;
    Handler handler;

    public BaseUiListner(Context context, Tencent tencent, Handler handler) {
        this.context=context;
        this.tencent=tencent;
        this.handler=handler;
    }

    @Override
    public void onComplete(Object o) {
        JSONObject response=(JSONObject)o;
        Log.i("QQui",response.toString());
      //  Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();

        try {
            openid=response.getString("openid");
            access_token=response.getString("access_token");
            expires_in=response.getString("expires_in");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("openid",openid);
        tencent.setOpenId(openid);
        tencent.setAccessToken(access_token,expires_in);
        QQToken qqToken=tencent.getQQToken();
        UserInfo info=new UserInfo(context,qqToken);

        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Message msg=new Message();
                JSONObject response=(JSONObject)o;
                Log.i("QQui response",response.toString());
                try {
                int ret=response.getInt("ret");
                if(ret==100030){
                    Runnable r=new Runnable() {
                        @Override
                        public void run() {
                            tencent.reAuth((LoginActivity) context, "all", new IUiListener() {
                                @Override
                                public void onComplete(Object o) {

                                }

                                @Override
                                public void onError(UiError uiError) {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    };
                    ((LoginActivity)context).runOnUiThread(r);
                }

                    String name=response.getString("nickname");
                    Log.i("QQui name",name);
                    msg.what=0;
                    msg.obj=name;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(context,uiError.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context,"取消获取基础信息",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onError(UiError uiError) {
        Log.i("QQui", uiError.errorMessage);
        Toast.makeText(context,uiError.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(context,"授权取消",Toast.LENGTH_LONG).show();
    }
}
