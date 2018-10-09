package com.kad.umengapplication;


import com.kad.umengapplication.ui.viewmodule.ViewModuleFragment;

public class ViewModuleActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.view_module_activity;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ViewModuleFragment.newInstance(getIntent().getStringExtra(ViewModuleFragment.PARAM_ARG)))
                .commitNow();
    }

    @Override
    protected void initData() {

    }
}
