package com.example.xz.weiji.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xz.weiji.Fragment.MoneyListFragment;
import com.example.xz.weiji.Fragment.NoteListFragment;
import com.example.xz.weiji.R;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;

/**
 * Created by xz on 2016/10/28.
 */

public class ReFirestpageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private MyViewPagerAdapter myViewPagerAdapter;
    //Tab文字,Tab图片，Fragment数组
    private final int[] TAB_TITLES = new int[]{R.string.shouye, R.string.wode};
    private final int[] TAB_IMgs = new int[]{R.drawable.icon1_selector, R.drawable.icon2_seclector};
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new NoteListFragment(), new MoneyListFragment()};
    //Tab数目
    private final int COUNT = TAB_TITLES.length;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage_withleftmenu);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setTitle("记忆+");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.action_ok, R.string.action_sign_in);
        drawerToggle.syncState();
        drawer_layout.setScrimColor(Color.TRANSPARENT);
        drawer_layout.setDrawerListener(drawerToggle);


        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        setTabs(tab_layout, this.getLayoutInflater(), TAB_TITLES, TAB_IMgs);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(myViewPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(view_pager));



    }




    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tabLayout.setBackgroundResource(R.mipmap.zhuyetwo);
            View view = inflater.inflate(R.layout.tab_custom, null);
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);

        }
    }


    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }
}
