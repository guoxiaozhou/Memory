package example.xz.com.myapplication.Utils;

import android.widget.Toast;

import example.xz.com.myapplication.MyApplication;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ToastUtils {

    private static Toast toast=null;
    public static void showShort(String s){
        if(toast==null){
            toast=Toast.makeText(MyApplication.getMyApplication(),s,Toast.LENGTH_SHORT);
        }
        else{
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(s);
        }

        toast.show();
    }
    public static void showLong(String s){

        if(toast==null){
            toast=Toast.makeText(MyApplication.getMyApplication(),s,Toast.LENGTH_LONG);
        }
        else{
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(s);
        }

        toast.show();
    }
}
