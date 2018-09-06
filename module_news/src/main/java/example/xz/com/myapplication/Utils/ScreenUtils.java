package example.xz.com.myapplication.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import java.lang.reflect.Field;

import example.xz.com.myapplication.MyApplication;

/**
 * 屏幕管理工具类
 */
public class ScreenUtils {
	private static final String TAG = "CsbBank.ScreenUtil";
	
	private static double RATIO = 0.80;
	
	public static int screenWidth;
	public static int screenHeight;
	public static int screenMin;// 宽高中，小的一边
	public static int screenMax;// 宽高中，较大的值

	public static float density;
	public static float scaleDensity;
	public static float xdpi;
	public static float ydpi;
	public static int densityDpi;
	
	public static int dialogWidth;
	public static int statusbarheight;
	public static int navbarheight;

    static {
        init(MyApplication.getMyApplication());
    }
	
	public static int dip2px(float dipValue) {
		return (int) (dipValue * density + 0.5f);
	}

	public static int px2dip(float pxValue) {
		return (int) (pxValue / density + 0.5f);
	}

	public static int getDialogWidth() {
		dialogWidth = (int) (screenMin * RATIO);
		return dialogWidth;
	}

    public static void init(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;

        Log.d(TAG, "screenWidth=" + screenWidth + " screenHeight=" + screenHeight + " density=" + density);
    }

	public static int getDisplayWidth(){
		if(screenWidth == 0){
			GetInfo(MyApplication.getMyApplication());
		}
		return screenWidth;
	}

	public static int getDisplayHeight() {
		if(screenHeight == 0){
			GetInfo(MyApplication.getMyApplication());
		}
		return screenHeight;
	}

	public static void GetInfo(Context context) {
		if (null == context) {
			return;
		}
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
		screenMax = (screenWidth < screenHeight) ? screenHeight : screenWidth;
		density = dm.density;
		scaleDensity = dm.scaledDensity;
		xdpi = dm.xdpi;
		ydpi = dm.ydpi;
		densityDpi = dm.densityDpi;
		statusbarheight = getStatusBarHeight(context);
		navbarheight = getNavBarHeight(context);
		Log.d(TAG, "screenWidth=" + screenWidth + " screenHeight=" + screenHeight + " density=" + density);
	}

	//获取状态栏的高度
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception E) {
			E.printStackTrace();
		}
		return sbar;
	}

	// 获取导航栏的高度
	public static int getNavBarHeight(Context context){
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	/**
	 * 获取控件的高度或者宽度  isHeight=true则为测量该控件的高度，isHeight=false则为测量该控件的宽度
	 * @param view
	 * @param isHeight
	 * @return
	 */
	public static int getViewHeight(View view, boolean isHeight){
		int result;
		if(view==null)return 0;
		if(isHeight){
			int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			view.measure(h,0);
			result =view.getMeasuredHeight();
		}else{
			int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			view.measure(0,w);
			result =view.getMeasuredWidth();
		}
		return result;
	}
}
