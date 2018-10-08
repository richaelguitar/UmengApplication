package com.kad.kumeng.callback;

/**
 * 注册回调
 * @since  2018-09-28
 * @author  xww
 */
public interface IPushRegisterCallback {

    void onSuccess(String deviceToken);

    void onFailure(String deviceToken,String errorMsg);
}
