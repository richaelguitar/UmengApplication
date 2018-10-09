package com.kad.umengapplication.ui.viewmodule;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kad.umengapplication.BaseFragment;
import com.kad.umengapplication.R;

public class ViewModuleFragment extends BaseFragment {

    private ViewModuleViewModel mViewModel;
    public static final String PARAM_ARG = "TEXT";

    public static ViewModuleFragment newInstance(String text) {
        ViewModuleFragment fragment = new ViewModuleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ARG,text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_module_fragment;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initData() {
        mViewModel = ViewModelProviders.of(this).get(ViewModuleViewModel.class);
        // TODO: Use the ViewModel
        String content = getArguments().getString(PARAM_ARG,"妈卖批");
        Toast.makeText(getActivity(),"content="+content,Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPageName() {
        return ViewModuleFragment.class.getName();
    }

}
