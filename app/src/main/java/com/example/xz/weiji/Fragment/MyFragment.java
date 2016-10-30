package com.example.xz.weiji.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.xz.weiji.Activity.ChangePersonActivity;
import com.example.xz.weiji.Activity.EachMonthActivity;
import com.example.xz.weiji.R;

/**
 * Created by xz on 2016/10/4.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private RelativeLayout rl_eachmonth;
    private RelativeLayout rl_changeperson;
    private RelativeLayout rl_settings;
    private RelativeLayout rl_aboutus;
    private RelativeLayout rl_logout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wode, container, false);
        Log.i("MyFragment", "执行了");
        initView(view);
        return view;
    }


    private void initView(View view) {
        rl_eachmonth = (RelativeLayout) view.findViewById(R.id.rl_eachmonth);
        rl_eachmonth.setOnClickListener(this);
        rl_changeperson = (RelativeLayout) view.findViewById(R.id.rl_changeperson);
        rl_changeperson.setOnClickListener(this);
        rl_settings = (RelativeLayout) view.findViewById(R.id.rl_settings);
        rl_settings.setOnClickListener(this);
        rl_aboutus = (RelativeLayout) view.findViewById(R.id.rl_aboutus);
        rl_aboutus.setOnClickListener(this);
        rl_logout = (RelativeLayout) view.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_eachmonth:
                startActivity(new Intent(context, EachMonthActivity.class));
                break;
            case R.id.rl_changeperson:
                startActivity(new Intent(context, ChangePersonActivity.class));
                getActivity().finish();
                break;
            case R.id.rl_aboutus:
                break;
            case R.id.rl_logout:
                break;
            case R.id.rl_settings:
                break;
        }
    }
}
