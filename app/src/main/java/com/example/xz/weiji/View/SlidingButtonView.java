package com.example.xz.weiji.View;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xz.weiji.R;

public class SlidingButtonView extends HorizontalScrollView {

    //删除按钮
    private TextView mTextView_Delete;


    private LinearLayout ll_content;

    private TextView text;

    //记录滚动条滚动的距离
    private int mScrollWidth;

    //在onMeasure中只执行一次的判断
    private Boolean once = false;

    public SlidingButtonView(Context context) {
        this(context, null);
    }

    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    //    this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!once) {
            //只需要执行一次
            mTextView_Delete = (TextView) findViewById(R.id.tv_delete);
         //   rbtn = (RadioButton) findViewById(R.id.rbtn);
            text = (TextView) findViewById(R.id.text);
            ll_content=(LinearLayout)findViewById(R.id.layout_content);
            once = true;

        }
    }

    //使Item在每次变更布局大小时回到初始位置，并且获取滚动条的可移动距离
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            mScrollWidth = mTextView_Delete.getWidth();
            this.smoothScrollTo(0,0);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
         int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //mIonSlidingButtonListener.onDownOrMove(this);
               break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //滚动监听，为了让删除按钮显示在项的背后的效果
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void changeScrollx() {
        if (getScrollX() >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth , 0);
        } else {
            this.smoothScrollTo(0, 0);
        }
    }


}
