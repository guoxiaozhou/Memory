package com.example.xz.weiji.AppActivity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.Fragment.MyFragment;
import com.example.xz.weiji.Fragment.NoteListFragment;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;

/**
 * Created by xz on 2016/9/20.
 */

public class NoteActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    // private LinearLayout head_layout;
    private Toolbar toolbar;
    private ViewPager view_pager;
    //  private AppBarLayout app_bar_layout;
    private NoteListFragment noteListFragment1;
    private MyFragment myFragment;
    private PagerAdapter pagerAdapter;
    //  Bitmap bitmap;
    private TabLayout tab_layout;
    //  String[] arrayAlert = {"笔 记", "记 账"};
    //   private TextView nick_name;
    private TextView date;
    private DrawerLayout drawer_layout;
    private LinearLayout ll_user;
    private LinearLayout ll_classify;
    private LinearLayout ll_money;
    private FloatingActionButtonPlus FabPlus;
    private long exitTime = 0;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage_withleftmenu);
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次  退出记忆+", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        // head_layout = (LinearLayout) findViewById(R.id.head_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            toolbar.getLayoutParams().height = Utils.getAppBarHeight(this);
            toolbar.setPadding(toolbar.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    toolbar.getPaddingRight(),
                    toolbar.getPaddingBottom());
        }
    //    view_pager = (ViewPager) findViewById(R.id.view_pager);
        //  app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
   //     tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        // nick_name = (TextView) findViewById(R.id.nick_name);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FabPlus = (FloatingActionButtonPlus) findViewById(R.id.FabPlus);
        FabPlus.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener() {
            private Button bt_beiwang;
            private EditText et_beiwang;

            @Override
            public void onItemClick(FabTagLayout tagView, int position) {
                if (position == 0) {
                    startActivity(new Intent(NoteActivity.this, EditActivity.class));
                    finish();
                } else if (position == 1) {
                    startActivity(new Intent(NoteActivity.this, MoneyActivity.class));
                    finish();
                } else if (position == 2) {
                    View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_layout, null);
                    bt_beiwang = (Button) view.findViewById(R.id.bt_beiwang);
                    et_beiwang = (EditText) view.findViewById(R.id.et_beiwang);
                    et_beiwang.setSelection(0);
                    dialog = new AlertDialog.Builder(NoteActivity.this).setView(view)
                            .show();
                    bt_beiwang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(NoteActivity.this,"点击按钮"+et_beiwang.getText().toString(),Toast.LENGTH_SHORT).show();
                            notifyTongzhilan(et_beiwang.getText().toString());
                        }
                    });
                }


            }
        });
        //设置传值过来的昵称
        //Intent t = getIntent();
        // nick_name.setText(BmobUser.getCurrentUser().getUsername());
        // bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.profile_bg);
        // final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.color.blue);
        //设置viewpager缓冲页面
     //   view_pager.setOffscreenPageLimit(2);
        //   head_layout.setBackgroundDrawable(new BitmapDrawable(bitmap));
        //设置标题栏及其导航菜单,菜单点击事件
        toolbar.setTitle("记忆+");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // toolbar.inflateMenu(R.menu.menu_settings);
        /**     toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        onBackPressed();
        }
        });**/
        //设置左边侧滑栏
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.action_ok, R.string.action_ok);
        toggle.syncState();
        drawer_layout.setDrawerListener(toggle);

        drawer_layout.setScrimColor(Color.TRANSPARENT);

       // toolbar.setOnMenuItemClickListener(this);

        //设置滑动
//        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
//      //  collapsingToolbarLayout.setContentScrim(new BitmapDrawable(bitmap)); //设置收缩时的背景颜色
//      //  collapsingToolbarLayout.setStatusBarScrim(new BitmapDrawable(bitmap));
//        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset <= -head_layout.getHeight() / 3) {
//                    collapsingToolbarLayout.setTitle("记忆+");
//                  //  collapsingToolbarLayout.s(new BitmapDrawable(bitmap1));
//                    //  collapsingToolbarLayout.setCollapsedTitleTextAppearance();
//                  //  collapsingToolbarLayout.setScrimsShown(false);
//                } else {
//                    collapsingToolbarLayout.setTitle("");
//                }
//            }
//        });
      //  initFragment();

        //左边侧滑栏点击事件
//        ll_user = (LinearLayout) findViewById(R.id.ll_user);
//        ll_user.setOnClickListener(this);
//        ll_classify = (LinearLayout) findViewById(R.id.ll_classify);
//        ll_classify.setOnClickListener(this);
//        ll_money = (LinearLayout) findViewById(R.id.ll_money);
//        ll_money.setOnClickListener(this);

    }


    private void initFragment() {
        if (noteListFragment1 == null) {
            noteListFragment1 = new NoteListFragment();
        }
        if (myFragment == null) {
            myFragment = new MyFragment();
        }
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(pagerAdapter);

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(view_pager));

        tab_layout.setupWithViewPager(view_pager);
        tab_layout.getTabAt(0).setIcon(R.drawable.icon1_selector);
        tab_layout.getTabAt(0).setText("笔 记");
        tab_layout.getTabAt(1).setIcon(R.drawable.icon2_seclector);
        tab_layout.getTabAt(1).setText("账 本");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(NoteActivity.this, "等待开发者开发...", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_user:
//                Intent i = new Intent(NoteActivity.this, ChangePersonActivity.class);
//                //i.putExtra("name", nick_name.getText().toString());
//                startActivity(i);
//                finish();
//                break;
//            case R.id.ll_classify:
//                // Toast.makeText(this,"笔记分类",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(NoteActivity.this, ClassifyActivity.class));
//                finish();
//                break;
//            case R.id.ll_money:
//                startActivity(new Intent(NoteActivity.this, EachMonthActivity.class));
//                finish();
//                break;
//            case R.id.FabPlus:
//                break;
//        }
    }


    //viewpager适配器
    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return noteListFragment1;
            } else if (position == 1) {
                return myFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notifyTongzhilan(String s) {
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
        } else {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int icon = R.mipmap.ic_event_note_blue_300_24dp;
            long time = System.currentTimeMillis();

            Intent notificationIntent = new Intent(this, NoteActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            Notification notification = new Notification.Builder(this).setContentTitle("记忆+")
                    .setContentText(s).setSmallIcon(icon).setWhen(time).setContentIntent(contentIntent)
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            manager.notify(0, notification);

            dialog.dismiss();


        }


    }


}
