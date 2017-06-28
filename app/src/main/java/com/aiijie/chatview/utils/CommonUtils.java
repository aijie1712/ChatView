package com.aiijie.chatview.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.aiijie.chatview.CustomApplication;

/**
 * 作者：Android_AJ on 2017/4/7.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 通用工具类 1.给RecyclerView设置分割线，2.获取版本号，3.判断Service是否在运行
 */
public class CommonUtils {
    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    public static void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) CustomApplication.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
