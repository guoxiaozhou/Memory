package com.example.xz.weiji.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.xz.weiji.AppActivity.DaojishiActivity;

/**
 * Created by Administrator on 2016/8/16.
 */
public class LeftSwipeMenuRecyclerView extends RecyclerView {
    private LinearLayout llDelete;
    private LinearLayout llStar;
    //置顶按钮
    private TextView tvTop;
    //删除按钮
    private ImageView tvDelete;
    //item相应的布局
    private LinearLayout mItemLayout;
    //菜单的最大宽度
    private int mMaxLength;

    //上一次触摸行为的x坐标
    private int mLastX;
    //上一次触摸行为的y坐标
    private int mLastY;

    //当前触摸的item的位置
    private int mPosition;

    //是否在垂直滑动列表
    private boolean isDragging;
    //item是在否跟随手指移动
    private boolean isItemMoving;
    //item是否开始自动滑动
    private boolean   isStartScroll;

    //左滑菜单状态   0：关闭 1：将要关闭 2：将要打开 3：打开
    private int mMenuState;
    private static int MENU_CLOSED = 0;
    private static int MENU_WILL_CLOSED = 1;
    private static int MENU_OPEN = 2;
    private static int MENU_WILL_OPEN = 3;

    //实现弹性滑动，恢复
    private Scroller mScroller;

    //item的事件监听
    private OnItemActionListener mListener;
    private Boolean isItemViewNull;

    public LeftSwipeMenuRecyclerView(Context context) {
        this(context, null);
    }

    public LeftSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context, new LinearInterpolator());
        //LinearInterpolator以常量速率改变
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mMenuState == MENU_CLOSED) {
                    //根据坐标获得view
                    View view = findChildViewUnder(x, y);
                    /**点击到列表的空白区域时，将触摸事件交由父容器处理，下一次ActionUp事件还会从此处开始处理
                     而如果直接返回false则，下一次事件直接返回false?
                     点击事件会有三个触摸事件传递：down->move->up
                     *
                     */

                    isItemViewNull=false;
                    if (view == null) {
                        isItemViewNull=true;
                        break;
                    }
                    String s=view.toString();
                    String string=s.substring(s.lastIndexOf("/")+1,s.lastIndexOf("/")+8);
                    Log.i("View","string为"+string+";");
                    //获得这个view的ViewHolder
                    if(string.equals("ll_item")){
                        RVAdapter.Holder holder = (RVAdapter.Holder) getChildViewHolder(view);
                        //获得这个view的position
                        mPosition = holder.getAdapterPosition();
                        //获得这个view的整个布局
                        mItemLayout = holder.llLayout;

                        llDelete=holder.llDelete;
                        llStar=holder.llStar;
                    }else {
                       DaojishiActivity.DaojishiAdapter.DaojishiViewHolder holder
                               =(DaojishiActivity.DaojishiAdapter.DaojishiViewHolder)getChildViewHolder(view);
                        //获得这个view的position
                        mPosition = holder.getAdapterPosition();
                        //获得这个view的整个布局
                        mItemLayout = holder.linearLayout;

                        llDelete=holder.ll_delete;
                        llStar=holder.ll_star;
                    }



//                    //获得这个view的删除按钮
//                    tvDelete = holder.tvDelete;
//                    //这个view的整个置顶按钮
//                    tvTop = holder.tvTop;
                    //两个按钮的宽度
                    mMaxLength = llDelete.getWidth() + llStar.getWidth();

                    //设置删除按钮点击监听
                    llDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemLayout.scrollTo(0, 0);
                            mMenuState =MENU_CLOSED;
                            mListener.OnItemDelete(mPosition);
                        }
                    });
                    //设置置顶按钮点击监听
                    llStar.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemLayout.scrollTo(0, 0);
                            mMenuState =MENU_CLOSED;
                            mListener.OnItemTop(mPosition);
                        }
                    });
                    //如果是打开状态，点击其他就把当前menu关闭掉
                } else if (mMenuState == MENU_OPEN) {
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200);  //(startX,startY,dx,dy,duration),FinalX=startX+dx=0;
                    invalidate();
                    mMenuState = MENU_CLOSED;
                    //该点击无效
                    return false;
                }
                if (isItemMoving){
                    mLastX = x;
                    mLastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                int dx = mLastX - x;
                int dy = mLastY - y;
                //上一点滑动的x
                //action_on break后，mItemlayout有可能为空
                if(mItemLayout==null){
                    break;
                }
                int scrollx = mItemLayout.getScrollX();

                if (Math.abs(dx) > Math.abs(dy)&&Math.abs(dx)>10) {

                    isItemMoving = true;
                    //超出左边界则始终保持不动
                    Log.i("scrollx",scrollx+"");
                    Log.i("dx",dx+"");
                    if (scrollx + dx <= 0) {
                        mItemLayout.scrollTo(0, 0);
                        //滑动无效
                        return false;
                    //超出右边界则始终保持不动
                    } else if (scrollx + dx >= mMaxLength) {
                        mItemLayout.scrollTo(mMaxLength, 0);
                        //滑动无效
                        return false;
                    }
                    //菜单随着手指移动
                    mItemLayout.scrollBy(dx, 0);
                //如果水平移动距离大于30像素的话，recyclerView不会上下滑动
                }
//                else  if (Math.abs(dx) > 30){
//                    return true;
//                    //触摸事件已经在此处消费，父View不会监听到此事件
//                }
                //如果菜单正在打开就不能上下滑动
                if (isItemMoving){
                    mLastX = x;
                    mLastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //与action_move同上
                if(mItemLayout==null){
                    break;
                }
                //手指抬起时判断是否点击,静止且有Listener才能点击
                if (!isItemMoving && !isDragging && mListener != null&&!isItemViewNull) {
                    mListener.OnItemClick(mPosition);
                }



                //等一下要移动的距离
                int deltaX = 0;
                //手指抬起来后的偏移位置
                int upScrollx = mItemLayout.getScrollX();
                //滑动距离大于1/2menu长度就自动展开，否则就关掉
                if (upScrollx >= mMaxLength / 2) {
                    deltaX = mMaxLength - upScrollx;
                    mMenuState = MENU_WILL_OPEN;
                } else if (upScrollx < mMaxLength / 2) {
                    deltaX = -upScrollx;
                    mMenuState = MENU_WILL_CLOSED;
                }
                //知道我们为什么不直接把mMenuState赋值为MENU_OPEN或者MENU_CLOSED吗？因为滑动时有时间的，我们可以在滑动完成时才把状态改为已经完成
                mScroller.startScroll(upScrollx, 0, deltaX, 0, 400);
                isStartScroll = true;
                //该方法导致View重绘，在onDraw()方法中调用computeScroll()，该方法在View中为空，
                //由我们重写，computeScroll()会判断去向Scroller获取当前的CurrX,CurrY，如果还没完成滑动
                //则通过scrollTo()滑动，接着调用postInvalidate()继续重绘，循环此过程
                invalidate();
                break;
        }
        //只有更新mLastX，mLastY数据才会准确
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        //判断scroller是否完成滑动
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //这个很重要，刷新界面View,pop掉旧View
            invalidate();
        //如果已经完成就改变状态
        } else if (isStartScroll) {
            isStartScroll = false;
            isItemMoving = false;
            if (mMenuState == MENU_WILL_CLOSED) {
                mMenuState = MENU_CLOSED;
            }
            if (mMenuState == MENU_WILL_OPEN) {
                mMenuState = MENU_OPEN;
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //是否在上下滑动
        isDragging = state == SCROLL_STATE_DRAGGING;
    }
    //设置Listener
    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.mListener = onItemActionListener;
    }
}
