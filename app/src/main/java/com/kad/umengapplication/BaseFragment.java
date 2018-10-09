package com.kad.umengapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kad.kumeng.utils.KUmengUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public  abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract int getLayoutId();
    public abstract void initView(View rootView);
    public abstract void initData();

    public abstract String getPageName();


    @Override
    public void onResume() {
        super.onResume();
        KUmengUtils.with(getActivity())
                .onPageStart(getPageName());//1、统计页面路径


    }


    @Override
    public void onPause() {
        super.onPause();
        KUmengUtils.with(getActivity())
                .onPageEnd(getPageName());//1、统计页面路径

    }

}
