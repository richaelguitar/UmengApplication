package com.kad.umengapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kad.kumeng.utils.KUmengUtils;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        KUmengUtils.with(this).onAppStart();
        initView();
        initData();
    }

    public abstract int getLayoutId();
    public abstract void initView();
    public abstract void initData();

    public abstract String getPageName();


    @Override
    protected void onResume() {
        super.onResume();
        KUmengUtils.with(this)
                .onPageStart(getPageName())//1、统计页面路径
                .onResume(this);//2、统计页面时长

    }


    @Override
    protected void onPause() {
        super.onPause();
        KUmengUtils.with(this)
                .onPageEnd(getPageName())//1、统计页面路径
                .onPause(this);//2、统计页面时长
    }
}
