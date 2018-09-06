package com.example.xz.weiji.AppActivity;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xz.weiji.DataTable.User;
import com.example.xz.weiji.Fragment.MyFragment;
import com.example.xz.weiji.Fragment.NoteListFragment;
import com.example.xz.weiji.R;
import com.example.xz.weiji.Utils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new NoteListFragment(), new MyFragment()};
    //Tab数目
    private final int COUNT = TAB_TITLES.length;
    private TextView tv_user;
    private CircleImageView mIvFirstpageHead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage_withleftmenu);
        initView();
    }



    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.getLayoutParams().height = Utils.getAppBarHeight(this);
            toolbar.setPadding(toolbar.getPaddingLeft(),
                    Utils.getStatusBarHeight(this),
                    toolbar.getPaddingRight(),
                    toolbar.getPaddingBottom());
        }
        //   drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  toolbar.setTitle("记忆+");
        //  toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ReFirestpageActivity.this,"开发者不想要侧滑栏了..",Toast.LENGTH_SHORT).show();
//            }
//        });
        //   setSupportActionBar(toolbar);
        //       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.action_ok, R.string.action_sign_in);
//        drawerToggle.syncState();
//        drawer_layout.setScrimColor(Color.TRANSPARENT);
//        drawer_layout.setDrawerListener(drawerToggle);


        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        setTabs(tab_layout, this.getLayoutInflater(), TAB_TITLES, TAB_IMgs);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(myViewPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(view_pager));


        tv_user = (TextView) findViewById(R.id.tv_user);
        Log.i("空指针值：", BmobUser.getCurrentUser().getTableName());

        mIvFirstpageHead = (CircleImageView) findViewById(R.id.iv_firstpage_head);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE},
                    1
            );
        }
    }




    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            // tabLayout.setBackgroundResource(R.mipmap.zhuyetwo);
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

    public void downloadHead() {
//        final ProgressDialog progressDialog = new ProgressDialog(ReFirestpageActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Waiting...");
//        progressDialog.setCancelable(true);
//        //progressDialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(BmobUser.getCurrentUser().getObjectId(),
                new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            BmobFile head = user.getHead();

                            File saveFile=new File(Environment.getExternalStorageDirectory()+"/bmob/",
                                    head.getFilename());

                            Log.i("path", head.getFileUrl());
                            if (head != null) {
                                head.download(saveFile,new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Log.i("path", s);
                                            mIvFirstpageHead.setImageURI(Uri.fromFile(new File(s)));
                                          //  progressDialog.dismiss();

                                        }
                                    }


                                    @Override
                                    public void onProgress(Integer integer, long l) {
                                          //  progressDialog.dismiss();
                                    }
                                });
                            }
                        }

                    }

                });

    }
    @Override
    public void onResume() {
        super.onResume();
        downloadHead();
        tv_user.setText(BmobUser.getCurrentUser().getUsername());
        MobclickAgent.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
