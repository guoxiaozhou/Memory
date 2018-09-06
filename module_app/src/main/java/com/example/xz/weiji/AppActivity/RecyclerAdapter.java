package com.example.xz.weiji.AppActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rusan on 2017/5/15.
 */

public class RecyclerAdapter extends SecondaryListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder> {

    public interface OnLongClickListner{
        void onLongClick(int group_position);
    }
    private Context context;

    private OnLongClickListner onLongClickListner;
    private List<DataTree<String, Note>> dts = new ArrayList<>();

    public RecyclerAdapter(Context context,OnLongClickListner onLongClickListner ) {
        this.context = context;
        this.onLongClickListner=onLongClickListner;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {

        ((GroupItemViewHolder) holder).tvGroup.setText(dts.get(groupItemIndex).getGroupItem());

    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, final int groupItemIndex, final int subItemIndex) {

        final String text = ((Note)(dts.get(groupItemIndex).getSubItems().get(subItemIndex))).getNote();
        final String title = ((Note)(dts.get(groupItemIndex).getSubItems().get(subItemIndex))).getTitle();
        ((SubItemViewHolder) holder).tv.setText(title);
        ((SubItemViewHolder) holder).textView.setText(((Note)(dts.get(groupItemIndex).getSubItems().get(subItemIndex))).getUpdatedAt());
        ((SubItemViewHolder) holder).tv_note.setText(text);

    }

    @Override
    public void onGroupLongClick(GroupItemViewHolder holder, int groupItemIndex) {
        onLongClickListner.onLongClick(groupItemIndex);
    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

        ObjectAnimator animator;
        if(!isExpand){
             animator= ObjectAnimator.ofFloat(((GroupItemViewHolder)holder).imageView,
                    "rotation", 0f, 90.0f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());//不停顿
//            animator.setRepeatCount(1);//设置动画重复次数

            animator.start();//开始动画
        }else {
            animator= ObjectAnimator.ofFloat(((GroupItemViewHolder)holder).imageView,
                    "rotation", 90.0f, 0f);
            animator.setDuration(300);
            animator.setInterpolator(new LinearInterpolator());//不停顿

            animator.start();//开始动画
        }

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        Intent i = new Intent(context, EditActivity.class);
        i.putExtra("notestring", ((Note)(dts.get(groupItemIndex).getSubItems().get(subItemIndex))).getNote());
        // Bundle b=new Bundle();
        i.putExtra("titlestring",((Note)(dts.get(groupItemIndex).getSubItems().get(subItemIndex))).getTitle());
        i.putExtra("note", (Note) dts.get(groupItemIndex).getSubItems().get(subItemIndex));
        context.startActivity(i);
        //  finish();

    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;
        ImageView imageView;
        public GroupItemViewHolder(View itemView) {
            super(itemView);

           tvGroup = (TextView) itemView.findViewById(R.id.textView01);
           imageView=(ImageView)itemView.findViewById(R.id.iv_right);

        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvSub;
        TextView tv;
        TextView tv_note;
        TextView textView;
        LinearLayout layout;
        public SubItemViewHolder(View itemView) {
            super(itemView);



//            Log.i("getChild",title);
//            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.list_child, null);
            tv = (TextView) itemView.findViewById(R.id.childTo);
            tv_note = (TextView) itemView.findViewById(R.id.tv_classify_note);
            textView = (TextView) itemView.findViewById(R.id.tv_classify_date);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_classify_item);
;
        }
    }


}

