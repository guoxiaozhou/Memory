package com.example.xz.weiji.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.DataTable.Group;
import com.example.xz.weiji.DataTable.Note;
import com.example.xz.weiji.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xz on 2016/10/8.
 */

public class ClassifyActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private ExpandableListView expandableListView;
    //第一和第二层列表
    private ArrayList<String> firstlist = new ArrayList<String>();
    private List<List<Note>> secondlist = new ArrayList<List<Note>>();
    private Toolbar tb_classify;
    BmobUser author = BmobUser.getCurrentUser();
    private SwipeRefreshLayout swipe_classify;
    private Group groupone;
    private EditText et;
    private ExpandableAdapter expandableAdapter;
    private List<Group> gruopTestlist;
    String[] s;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_classify, null);
        setContentView(view);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(ClassifyActivity.this, ReFirestpageActivity.class);
//        BmobUser user = BmobUser.getCurrentUser();
//        i.putExtra("name", user.getUsername());
//        startActivity(i);
//        finish();
    }


    private void onRefresh() {


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        BmobQuery<Group> bmobQuery = new BmobQuery<Group>();
        bmobQuery.addWhereEqualTo("author", author.getObjectId());

        bmobQuery.findObjects(new FindListener<Group>() {
                                  @Override
                                  public void done(List<Group> list, BmobException e) {
                                      if (e == null) {
                                          for (Group group : list) {
                                              gruopTestlist = list;
                                              firstlist.add(group.getEveryclass());
                                              Log.i("firstlist", firstlist.toString());
                                          }
                                          BmobQuery<Note> query8 = new BmobQuery<Note>();
                                          query8.addWhereEqualTo("author", author.getObjectId());
                                          s = new String[firstlist.size()];
                                          for (int i = 0; i < firstlist.size(); i++) {
                                              s[i] = firstlist.get(i);
                                          }
                                          query8.addWhereContainedIn("sort", Arrays.asList(s));
                                          query8.findObjects(new FindListener<Note>() {
                                              @Override
                                              public void done(List<Note> list, BmobException e) {
                                                  if (e == null) {
                                                      //    Toast.makeText(ClassifyActivity.this, "测试查询成功", Toast.LENGTH_SHORT).show();
                                                    //  Toast.makeText(ClassifyActivity.this, "测试查询成功" + list.get(0).getNote(), Toast.LENGTH_SHORT).show();
                                                      List<List<Note>> threeList = new ArrayList<List<Note>>();
                                                      for (int i = 0; i < s.length; i++) {
                                                          List<Note> list1 = new ArrayList<Note>();
                                                          threeList.add(list1);
                                                          Log.i("测试", list1.toString());
                                                      }
                                                      Log.i("threeList", threeList.toString());
                                                      for (Note note : list) {
                                                          //List<Note> temp=new ArrayList<Note>();
                                                          Log.i("测试", note.getNote());
                                                          for (int i = 0; i < s.length; i++) {
                                                              Log.i("sort", note.getSort() + "  " + s[i]);
                                                              if (note.getSort().equals(s[i])) {
                                                                  threeList.get(i).add(note);
                                                                  Log.i("测试", note.getSort());
                                                                  Log.i("各自的分组", threeList.get(i).toString());
                                                              }


                                                          }
                                                          //  temp.add(note);

                                                      }
                                                      Log.i("threeList", threeList.toString());

                                                      secondlist = threeList;

                                                      expandableAdapter = new ExpandableAdapter(ClassifyActivity.this, firstlist, secondlist);
                                                      expandableListView.setGroupIndicator(null);
                                                      //expandableListView.setDivider(null);
                                                      expandableListView.setAdapter(expandableAdapter);
                                                      expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                          @Override
                                                          public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                              // Toast.makeText(ClassifyActivity.this, "点击有效", Toast.LENGTH_SHORT).show();
                                                              new AlertDialog.Builder(ClassifyActivity.this).setTitle("确认删除此分组？")
                                                                      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                                  @Override
                                                                                  public void onClick(DialogInterface dialog, int which) {
                                                                                      Group group = gruopTestlist.get(position);
                                                                                      group.delete(new UpdateListener() {
                                                                                                       @Override
                                                                                                       public void done(BmobException e) {
                                                                                                           if (e == null) {
                                                                                                               Toast.makeText(ClassifyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                                                                               Intent i = new Intent(ClassifyActivity.this,ClassifyActivity.class);
                                                                                                               startActivity(i);
                                                                                                               finish();
                                                                                                           } else
                                                                                                               Toast.makeText(ClassifyActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                                                                                       }
                                                                                                   }
                                                                                      );
                                                                                  }
                                                                              }
                                                                      )
                                                                      .setNegativeButton("取消", null)
                                                                      .show();

                                                              return true;
                                                          }
                                                      });
                                                      swipe_classify.setRefreshing(false);
                                                  } else {
                                                      Toast.makeText(ClassifyActivity.this, "测试查询失败", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          });


                                      } else
                                          Toast.makeText(ClassifyActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                                  }
                              }

        );


    }

    private void initView() {
        tb_classify = (Toolbar) findViewById(R.id.tb_classify);
        tb_classify.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //设置标题栏左边按钮点击事件
        tb_classify.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tb_classify.inflateMenu(R.menu.menu_main);
        tb_classify.setOnMenuItemClickListener(ClassifyActivity.this);
        swipe_classify = (SwipeRefreshLayout) findViewById(R.id.swipe_classify);
        swipe_classify.setColorSchemeResources(R.color.blue);
        swipe_classify.post(new Runnable() {
            @Override
            public void run() {
                swipe_classify.setRefreshing(true);
            }
        });
        swipe_classify.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                        swipe_classify.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        onRefresh();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            et = new EditText(ClassifyActivity.this);
           // et.setBackgroundResource(R.drawable.edittext_selector);
          //  LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)et.getLayoutParams();
          //  layoutParams.setMargins(10,10,10,0);
          //  et.setLayoutParams(layoutParams);

            new AlertDialog.Builder(this).setTitle("新建分类")
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String input = et.getText().toString();
                            if (input.equals("")) {
                                Toast.makeText(ClassifyActivity.this, "分组内容不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                groupone = new Group();
                                groupone.setEveryclass(input);
                                groupone.setAuthor(BmobUser.getCurrentUser());
                                groupone.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(ClassifyActivity.this, "新建" + input + "成功", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(ClassifyActivity.this,ClassifyActivity.class);
                                            finish();
                                            startActivity(i);



                                        } else
                                            Toast.makeText(ClassifyActivity.this, "新建" + input + "失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();


        }

        return true;
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {
        private Context context;
        private ArrayList<String> groups;
        private List<List<Note>> childs;

        public ExpandableAdapter(Context context, ArrayList<String> groups, List<List<Note>> childs) {
            this.context = context;
            this.groups = groups;
            this.childs = childs;
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childs.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childs.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String text = groups.get(groupPosition);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.list_group, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.textView01);
            textView.setText(text);
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.image_arrow_list);
            if (isExpanded) {
                imageView.setBackgroundResource(R.mipmap.ic_arrow_drop_down_black_24dp);
            } else {
                imageView.setBackgroundResource(R.mipmap.ic_arrow_drop_up_black_24dp);
            }
            return linearLayout;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String text = ((Note) getChild(groupPosition, childPosition)).getNote();
            final String date = ((Note) getChild(groupPosition, childPosition)).getUpdatedAt();
            final String title = ((Note) getChild(groupPosition, childPosition)).getTitle();
            Log.i("getChild",title);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.list_child, null);
            TextView tv = (TextView) linearLayout.findViewById(R.id.childTo);
            tv.setText(title);
            TextView textView = (TextView) linearLayout.findViewById(R.id.tv_classify_date);
            textView.setText(date);
            LinearLayout layout = (LinearLayout) linearLayout.findViewById(R.id.ll_classify_item);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditActivity.class);
                    i.putExtra("notestring", text);
                    // Bundle b=new Bundle();
                    i.putExtra("titlestring",title);
                    i.putExtra("note", (Note) getChild(groupPosition, childPosition));
                    startActivity(i);
                  //  finish();
                }
            });

            return linearLayout;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
