package com.aiijie.chatview;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aiijie.chatview.base.BaseActivity;
import com.aiijie.chatview.utils.SizeUtils;
import com.aiijie.chatview.widget.MyChatView;
import com.rockerhieu.emojicon.EmojiconCustomGridFragment;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.custom.ui.EmojiKeyboard;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class MainActivity extends BaseActivity implements
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,
        EmojiconCustomGridFragment.EventListener {

    private MyChatView myChatView;

    private DisplayMetrics dm = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main);
        myChatView = (MyChatView) findViewById(R.id.myChatView);

        myChatView.setOnSendClickListener(new MyChatView.OnSendClickListener() {
            @Override
            public void onSendClick(String content) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (v != null && v.getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                }
                // 隐藏emoji区域
                myChatView.setCurrentState(MyChatView.STATE_SHOW_NO);
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }

        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (myChatView != null) {
            RectF rect = SizeUtils.calcViewScreenLocation(myChatView);
            float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
            float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
            return !rect.contains(x, y);
        }
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();

            if (event.getX() < dm.widthPixels && event.getY() > top) {
                return false;
            } else return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public void onEmojiSelected(String res) {
        EmojiKeyboard.input(myChatView.getChat_input_et(), res);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(myChatView.getChat_input_et(), emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(myChatView.getChat_input_et());
    }
}
