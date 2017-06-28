package com.aiijie.chatview.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.aiijie.chatview.CustomApplication;

/**
 * @author YH
 */
public class UiUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable mRunnable = new Runnable() {
        public void run() {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;              // toast 隐藏后，将其置为 null
            }
        }
    };

    /**
     * 显示toast信息
     *
     * @param message
     */
    public static void showToast(final String message) {
        showToast(CustomApplication.getApplication(), message);
    }

    public static void showToast(final Context context, final String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
                } else {
                    mToast.setText(message);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        }, 100);
    }

    public static void showToast(int res) {
        showToast(CustomApplication.getApplication().getString(res));
    }

    public static void showToast(Context context, int res) {
        showToast(context, context.getString(res));
    }

    /**
     * 显示toast信息
     *
     * @param message
     */
    public static void showToastLong(String message) {
        showToastLong(CustomApplication.getApplication(), message);
    }

    /**
     * 显示toast信息
     *
     * @param message
     */
    public static void showToastLong(Context context, String message) {
        mHandler.removeCallbacks(mRunnable);
        if (mToast == null) {           // 只有 mToast == null 时才重新创建，否则只需更改提示文字
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
        mToast.setText(message);
        mHandler.postDelayed(mRunnable, 2000);  // 延迟 duration 事件隐藏 toast
        mToast.show();
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) CustomApplication.getApplication()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     */
    public static String paste() {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) CustomApplication.getApplication()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().toString().trim();
    }

    public static String numToString(int num) {
        String numString = "";
        switch (num) {
            case 0:
                numString = "零";
                break;
            case 1:
                numString = "一";
                break;
            case 2:
                numString = "二";
                break;
            case 3:
                numString = "三";
                break;
            case 4:
                numString = "四";
                break;
            case 5:
                numString = "五";
                break;
            case 6:
                numString = "六";
                break;
            case 7:
                numString = "七";
                break;
            case 8:
                numString = "八";
                break;
            case 9:
                numString = "九";
                break;
            case 10:
                numString = "十";
                break;
            default:
                break;
        }
        return numString;
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", CustomApplication.getApplication().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", CustomApplication.getApplication().getPackageName());
        }
        return localIntent;
    }
}
