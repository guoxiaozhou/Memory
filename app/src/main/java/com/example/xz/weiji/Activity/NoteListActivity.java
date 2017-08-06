package com.example.xz.weiji.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Group;
import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.example.xz.weiji.View.LeftSwipeMenuRecyclerView;
import com.example.xz.weiji.View.OnItemActionListener;
import com.example.xz.weiji.View.RVAdapter;
import com.example.xz.weiji.View.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/10/28.
 */

public class NoteListActivity extends AppCompatActivity {

    private Toolbar tb_notelist;
    private LeftSwipeMenuRecyclerView rclv_list;
    private SwipeRefreshLayout swipeLayout;
    ArrayList<String> arrayList;
    ArrayList<String> datelist ;
    ArrayList<String> titlelist;
    ArrayList<Note> noteList ;
    ArrayList<String> groupList;
    BmobUser user;
    private RVAdapter noteAdapter;

    static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=NoteListActivity.this;
        setContentView(R.layout.activity_notelist);
        initView();
    }

    private void initView() {
        tb_notelist = (Toolbar) findViewById(R.id.tb_notelist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            tb_notelist.getLayoutParams().height = Utils.getAppBarHeight(this);
            tb_notelist.setPadding(tb_notelist.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    tb_notelist.getPaddingRight(),
                    tb_notelist.getPaddingBottom());
        }
        rclv_list = (LeftSwipeMenuRecyclerView) findViewById(R.id.rclv_list);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        tb_notelist.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        tb_notelist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swipeLayout.setColorSchemeResources(R.color.blue);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //1秒的定时器，1秒过后刷新轮停止
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        onRefresh();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(context, "执行了onResume方法且refresh", Toast.LENGTH_SHORT).show();
//        onRefresh();
//    }


    private void onRefresh() {
        arrayList = new ArrayList<String>();
        datelist = new ArrayList<String>();
        titlelist = new ArrayList<String>();
        noteList= new ArrayList<Note>();
        BmobQuery<Note> query = new BmobQuery<Note>();
        user=BmobUser.getCurrentUser();
        query.addWhereEqualTo("author", user.getObjectId());
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> object, BmobException e) {
                if (e == null) {
                    Log.i("NoteList", object.toString());
                    //  Toast.makeText(context, "查询成功;共" + object.size() + "条数据", Toast.LENGTH_SHORT).show();
                    for (Note note : object) {

                        Log.i("Note的值为", note.getNote());
                        arrayList.add(note.getNote());
                        datelist.add(note.getUpdatedAt());
                        titlelist.add(note.getTitle());
                        Log.i("titleList", titlelist.toString());
                        // Toast.makeText(context, "时间：" + datelist.toString() , Toast.LENGTH_SHORT).show();
                        noteList.add(note);
                    }

                    //设置适配器
                    rclv_list.setLayoutManager(new LinearLayoutManager(NoteListActivity.this));
                    noteAdapter = new RVAdapter(NoteListActivity.this,arrayList, datelist, titlelist);
//                    noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            //Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
//                            startEditActivity(position);
//                           // finish();
//                        }
//
//                    });
//                    //列表设置长按点击效果
//                    noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
//
//                        @Override
//                        public void onItemLongClick(View view, int position) {
//                            queryGroups(position);
//                        }
//                    });
//                    noteAdapter.setmIDeleteBtnClickListener(new NoteAdapter.IonSlidingViewClickListener() {
//                        @Override
//                        public void onDeleteBtnCilck(View view, int position) {
//                          //  Toast.makeText(context,"删除了"+position,Toast.LENGTH_SHORT).show();
//                            deleteNote(position);
//                        }
//                    });

                    rclv_list.setAdapter(noteAdapter);
                    rclv_list.setOnItemActionListener(new OnItemActionListener() {
                        @Override
                        public void OnItemClick(int position) {
                            startEditActivity(position);
                        }

                        @Override
                        public void OnItemTop(int position) {
                            queryGroups(position);
                        }

                        @Override
                        public void OnItemDelete(int position) {
                            deleteNote(position);
                        }
                    });
                    rclv_list.addItemDecoration(new RecycleViewDivider(NoteListActivity.this));
                    rclv_list.setItemAnimator(new DefaultItemAnimator());
                    swipeLayout.setRefreshing(false);
                } else {
                    Toast.makeText(NoteListActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteNote(final int position){


        Note note = noteList.get(position);
        note.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "删除笔记成功", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    datelist.remove(position);
                    titlelist.remove(position);
                    noteAdapter.notifyItemRemoved(position);
                   // onRefresh();


                } else
                    Toast.makeText(context, "删除笔记失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //弹出对话框显示当前已有分组列表
    private void queryGroups(final int position) {
        groupList = new ArrayList<String>();
        BmobQuery<Group> bmobQuery = new BmobQuery<Group>();
        bmobQuery.addWhereEqualTo("author", user.getObjectId());
        bmobQuery.findObjects(new FindListener<Group>() {
            @Override
            public void done(final List<Group> list, BmobException e) {
                if (e == null) {
                    for (Group group : list) {

                        groupList.add(group.getEveryclass());
                        Log.i("分组详情显示：", groupList.toString());
                    }
                    //将集合转化为数组
                    final String[] groupString = new String[groupList.size()];
                    for (int i = 0; i < groupList.size(); i++) {
                        groupString[i] = groupList.get(i);
                        Log.i("各分组", groupString[i]);
                    }

                                    new AlertDialog.Builder(context).setTitle("添加至")
                                            .setItems(groupString, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //记得更改信息或者笔记、账本以后要记得保存
                                                    Note note = noteList.get(position);
                                                    note.setSort(groupString[which]);
                                                    Log.i("notelist现在的值为", note.getNote());
                                                    //    Log.i("note现在的gruop",note.getGroup().getEveryclass());
                                                    note.update(note.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            Toast.makeText(context, "设置分组成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            })
                                            .show();


                    // Toast.makeText(context, "长按成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "分组显示失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startEditActivity(int position) {
        Intent i = new Intent(NoteListActivity.this, EditActivity.class);
        i.putExtra("notestring", arrayList.get(position));
        // Bundle b=new Bundle();
        i.putExtra("note", noteList.get(position));
        i.putExtra("titlestring", titlelist.get(position));
        String s="NoteListActivity";
        i.putExtra("类名",s);
        startActivityForResult(i,0x11);
       // finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11&&resultCode==0x11){
         //   Toast.makeText(NoteListActivity.this,"执行了onActivityResult",Toast.LENGTH_SHORT).show();



            onRefresh();
            //noteAdapter.notifyDataSetChanged();
        }
    }

//    public static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
//        private ArrayList<String> noteList;
//        private ArrayList<String> dateList;
//        private ArrayList<String> titleList;
//        private OnItemClickListener onItemClickListener;
//        private OnItemLongClickListener onItemLongClickListener;
//        public NoteAdapter(ArrayList<String> noteList, ArrayList<String> dateList, ArrayList<String> titleList) {
//            this.noteList = noteList;
//            this.dateList = dateList;
//            this.titleList = titleList;
//        }
//        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//            this.onItemClickListener = onItemClickListener;
//        }
//
//        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
//            this.onItemLongClickListener = onItemLongClickListener;
//        }
//        public interface OnItemClickListener {
//            void onItemClick(View view, int position);
//        }
//
//        public interface OnItemLongClickListener {
//            void onItemLongClick(View view, int position);
//        }
//        @Override
//        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            NoteViewHolder noteViewHolder=new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notelist
//                    ,parent,false));
//            return noteViewHolder;
//        }
//        @Override
//        public void onBindViewHolder(final NoteViewHolder holder, final int position) {
//            holder.title.setText(titleList.get(position));
//            holder.note.setText(noteList.get(position));
//            holder.date.setText(dateList.get(position));
//            if (onItemClickListener != null) {
//                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onItemClickListener.onItemClick(holder.itemView,position);
//                    }
//                });
//            }
//            if (onItemLongClickListener != null) {
//                holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        onItemLongClickListener.onItemLongClick(holder.itemView, position);
//                        return true;
//                    }
//                });
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return noteList.size();
//        }
//
//        class NoteViewHolder extends RecyclerView.ViewHolder{
//            TextView title;
//            TextView note;
//            TextView date;
//            LinearLayout linearLayout;
//            public NoteViewHolder(View itemView) {
//                super(itemView);
//                title=(TextView)itemView.findViewById(R.id.tv_notetitle);
//                note=(TextView)itemView.findViewById(R.id.tv_note);
//                date=(TextView)itemView.findViewById(R.id.tv_date);
//                linearLayout=(LinearLayout)itemView.findViewById(R.id.ll_item);
//            }
//        }
//    }


}

