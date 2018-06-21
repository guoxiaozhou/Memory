package com.example.xz.weiji;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Administrator on 2018/4/7.
 */

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yanzhenjie.nohttp.NoHttp;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import example.xz.com.myapplication.Utils.DBUtil;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyApplication extends LitePalApplication {


    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Application","MyApplication");
        NoHttp.initialize(this);
        myApplication=this;

        BGASwipeBackManager.getInstance().init(this);

        LitePal.initialize(this);
        DBUtil.getInstance();

        //初始化友盟
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5b021f49f43e48045a0000ac");
        //友盟设置场景模式
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //使用友盟的集成测试
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setDebugMode(true);

        //android 7.0调用系统相机报错
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
