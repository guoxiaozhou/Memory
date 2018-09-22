package com.example.xz.weiji.Fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.AppActivity.ClassifyActivity;
import com.example.xz.weiji.AppActivity.DaojishiActivity;
import com.example.xz.weiji.AppActivity.EditActivity;
import com.example.xz.weiji.AppActivity.NoteActivity;
import com.example.xz.weiji.AppActivity.NoteListActivity;
import com.example.xz.weiji.AppActivity.ReFirestpageActivity;
import com.example.xz.weiji.DataTable.Group;
import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;
import com.example.xz.weiji.View.RecycleViewDivider;
import com.jeek.calendar.activity.MainActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/9/20.
 */

public class NoteListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    Context context;
    private View view;
    private RecyclerView list_view;
    private SwipeRefreshLayout swipeLayout;
    //存放笔记信息数据的集合
    private ListAdapter listAdapter;
    ArrayList<String> arrayList=new ArrayList<String>();
    ArrayList<String> datelist=new ArrayList<String>();
    ArrayList<String> titlelist=new ArrayList<String>();
    ArrayList<Note> noteList=new ArrayList<Note>();
    BmobUser user;
    ArrayList<String> groupList;
    private LinearLayout ll_jibiji;
    private LinearLayout ll_fenlei;
    private LinearLayout ll_zhangben;
    private LinearLayout ll_beiwang;
    private LinearLayout ll_daojishi;
    private Dialog dialog;
    private RelativeLayout rl_allnote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Log.i("onCreate", "执行了");

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NoteListFragment");

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        onRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("onCreateView", "执行了");
        view = inflater.inflate(R.layout.fragment_shouye, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        user = BmobUser.getCurrentUser();
        //初始化各控件
        Log.i("initView", "执行了");
        list_view = (RecyclerView) view.findViewById(R.id.list_view);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        ll_jibiji = (LinearLayout) view.findViewById(R.id.ll_jibiji);
        ll_jibiji.setOnClickListener(this);
        ll_fenlei = (LinearLayout) view.findViewById(R.id.ll_fenlei);
        ll_fenlei.setOnClickListener(this);
//        ll_zhangben = (LinearLayout) view.findViewById(R.id.ll_zhangben);
//        ll_zhangben.setOnClickListener(this);
        ll_beiwang = (LinearLayout) view.findViewById(R.id.ll_beiwang);
        ll_beiwang.setOnClickListener(this);
        ll_daojishi = (LinearLayout) view.findViewById(R.id.ll_daojishi);
        ll_daojishi.setOnClickListener(this);
        rl_allnote = (RelativeLayout) view.findViewById(R.id.rl_allnote);
        rl_allnote.setOnClickListener(this);
    }




    @Override
    public void onRefresh() {

        BmobQuery<Note> query = new BmobQuery<Note>();

        query.addWhereEqualTo("author", user.getObjectId());
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> object, BmobException e) {
                if (e == null) {
                    Log.i("NoteList", object.toString());
                    noteList.clear();
                    arrayList.clear();
                    datelist.clear();
                    titlelist.clear();
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
                    list_view.setLayoutManager(new LinearLayoutManager(context));
                    listAdapter = new ListAdapter(arrayList, datelist, titlelist, context);
                    listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
                            startEditActivity(position);
                        }

                    });
                    //列表设置长按点击效果
//                    listAdapter.setOnItemLongClickListener(new ListAdapter.OnItemLongClickListener() {
//
//                        @Override
//                        public void onItemLongClick(View view, int position) {
//                            queryGroups(position);
//                        }
//                    });
                    list_view.setAdapter(listAdapter);
                    list_view.addItemDecoration(new RecycleViewDivider(context));
                    list_view.setItemAnimator(new DefaultItemAnimator());
                    swipeLayout.setRefreshing(false);
                } else {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
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
                    new AlertDialog.Builder(context).setItems(new String[]{"删除", "添加至"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Note note = noteList.get(position);
                                    note.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(context, "删除笔记成功", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(context, NoteActivity.class);
                                                i.putExtra("name", BmobUser.getCurrentUser().getUsername());
                                                startActivity(i);
                                                getActivity().finish();
                                            } else
                                                Toast.makeText(context, "删除笔记失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case 1:
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
                                    break;
                            }
                        }
                    }).show();

                    // Toast.makeText(context, "长按成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "分组显示失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startEditActivity(int position) {
        Intent i = new Intent(context, EditActivity.class);
        i.putExtra("notestring", arrayList.get(position));
        // Bundle b=new Bundle();
        i.putExtra("note", noteList.get(position));
        i.putExtra("titlestring", titlelist.get(position));
        String s="ReFirestpageActivity";
        i.putExtra("类名",s);
        startActivityForResult(i,0x12);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x12&&resultCode==0x12){
          //  Toast.makeText(context,"首页刷新",Toast.LENGTH_SHORT).show();
           // onRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_jibiji:
                Intent i=new Intent(context, EditActivity.class);
                startActivityForResult(i,0x12);
               // getActivity().finish();
                break;
            case R.id.ll_fenlei:
                startActivity(new Intent(context, ClassifyActivity.class));
              //  getActivity().finish();
                break;
//            case R.id.ll_zhangben:
//                startActivity(new Intent(context, ZhangbenActivity.class));
//                //getActivity().finish();
//                break;
            case R.id.ll_beiwang:
//                startActivity(new Intent(context, MainActivity.class));
                startDialog();
                break;
            case R.id.ll_daojishi:
                startActivity(new Intent(context,DaojishiActivity.class));
                break;
            case R.id.rl_allnote:
                startActivity(new Intent(context,NoteListActivity.class));
                break;
        }
    }

    private void startDialog() {
        Button bt_beiwang;
        final EditText et_beiwang;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        bt_beiwang = (Button) view.findViewById(R.id.bt_beiwang);
        et_beiwang = (EditText) view.findViewById(R.id.et_beiwang);
        et_beiwang.setSelection(0);
        dialog = new AlertDialog.Builder(context).setView(view)
                .show();
        bt_beiwang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NoteActivity.this,"点击按钮"+et_beiwang.getText().toString(),Toast.LENGTH_SHORT).show();
                notifyTongzhilan(et_beiwang.getText().toString());
            }
        });
    }


    //发送备忘到通知栏
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notifyTongzhilan(String s) {
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT).show();
        } else {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int icon = R.mipmap.ic_event_note_blue_300_24dp;
            long time = System.currentTimeMillis();

            Intent notificationIntent = new Intent(context, ReFirestpageActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            Notification notification = new Notification.Builder(context).setContentTitle("记忆+")
                    .setContentText(s).setSmallIcon(icon).setWhen(time).setContentIntent(contentIntent)
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            manager.notify(0, notification);

            dialog.dismiss();


        }


    }



    public static class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
        private ArrayList<String> noteList;
        private ArrayList<String> dateList;
        private ArrayList<String> titleList;
        private Context context;
        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;
        //测试
        private List<Integer> mHeights;

        public ListAdapter(ArrayList<String> noteList, ArrayList<String> dateList,
                           ArrayList<String> titleList, Context context) {
            this.noteList = noteList;
            this.dateList = dateList;
            this.titleList = titleList;
            this.context = context;
            mHeights = new ArrayList<>();
        }

        //设置适配器的监听事件
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        public interface OnItemLongClickListener {
            void onItemLongClick(View view, int position);
        }

        //加载xml布局
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_note, parent, false));
            return holder;
        }

        //设置需要改变布局的显示
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//            if(mHeights.size()<=position){
//                mHeights.add((int)(180+Math.random()*100));
//            }
//            ViewGroup.LayoutParams lp=holder.tv_note.getLayoutParams();
//            lp.height=mHeights.get(position);
//            holder.tv_note.setLayoutParams(lp);
            // holder.tv_note.setText(noteList.get(position));
            holder.tv_notetitle.setText(titleList.get(position));
            Log.i("onBindViewHolder", noteList.get(position));
            holder.tv_date.setText(dateList.get(position));
            if (onItemClickListener != null) {
                holder.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
            if (onItemLongClickListener != null) {
                holder.ll_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemLongClickListener.onItemLongClick(holder.itemView, position);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return noteList.size();

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_notetitle;
            TextView tv_note;
            TextView tv_date;
            LinearLayout ll_item;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_notetitle = (TextView) itemView.findViewById(R.id.tv_notetitle);
                tv_note = (TextView) itemView.findViewById(R.id.tv_note);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NoteListFragment");
    }
}
