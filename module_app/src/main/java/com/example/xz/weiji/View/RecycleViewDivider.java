package com.example.xz.weiji.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.xz.weiji.R;

/**
 * Created by xz on 2016/9/23.
 */

public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    public RecycleViewDivider(Context context) {
        final TypedArray array = context.obtainStyledAttributes(ATTRS);
        mDivider=array.getDrawable(0);
        //mDivider=context.getResources().getDrawable(R.drawable.divider);
        array.recycle();
    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        drawVertical(c,parent);
//        drawHorizontal(c,parent);
//    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
      //  drawVertical(c,parent);
        drawHorizontal(c,parent);
       // drawLeftVertical(c,parent);
       // drawUpHorizontal(c,parent);
    }

    // 水平线
    public void drawHorizontal(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        // 在每一个子控件的底部画线
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int left = child.getLeft() + child.getPaddingLeft();
            final int right = child.getWidth() + child.getLeft() - child.getPaddingRight();
            final int top = child.getBottom() - mDivider.getIntrinsicHeight() - child.getPaddingBottom();
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    public void drawUpHorizontal(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        // 在每一个子控件的底部画线
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int left = child.getLeft() + child.getPaddingLeft();
            final int right = child.getWidth() + child.getLeft() - child.getPaddingRight();
            final int bottom = child.getTop() + mDivider.getIntrinsicHeight() + child.getPaddingTop();
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    // 竖直线
    public void drawVertical(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        // 在每一个子控件的右侧画线
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int right = child.getRight() - child.getPaddingRight();
            int left = right - mDivider.getIntrinsicWidth();
            final int top = child.getTop() + child.getPaddingTop();
            final int bottom = child.getTop() + child.getHeight() - child.getPaddingBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    public void drawLeftVertical(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        // 在每一个子控件的右侧画线
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int left = child.getLeft() - child.getPaddingLeft();
            int right = left + mDivider.getIntrinsicWidth();
            final int top = child.getTop() + child.getPaddingTop();
            final int bottom = child.getTop() + child.getHeight() - child.getPaddingBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.set(0,0,mDivider.getIntrinsicWidth(),mDivider.getIntrinsicHeight());
//    }
}
