package com.kad.umengapplication;


import com.kad.umengapplication.ui.viewmodule.ViewModuleFragment;

public class ViewModuleActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.view_module_activity;
    }

    @Override
    public void initView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ViewModuleFragment.newInstance(getIntent().getStringExtra(ViewModuleFragment.PARAM_ARG)))
                .commitNow();
    }

    @Override
    public void initData() {

    }

    @Override
    public String getPageName() {
        return ViewModuleActivity.class.getName();
    }
}
