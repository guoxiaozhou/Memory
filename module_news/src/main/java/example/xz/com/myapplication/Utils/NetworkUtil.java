package example.xz.com.myapplication.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/8/25.
 */

public class NetworkUtil {
    public static boolean isNetworkConnected(Context context){
        if(context!=null){
            ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if(networkInfo!=null){
                return networkInfo.isAvailable();
            }

        }
        return false;
    }
    public static boolean isWifiConnected(Context context){
        if(context!=null){
            ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                 return networkInfo.isAvailable();
            }
        }
        return false;
    }
    public static boolean isMobileConnected(Context context){
        if(context!=null){
            ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
