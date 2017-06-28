package com.aiijie.chatview.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiijie.chatview.R;
import com.aiijie.chatview.utils.CommonUtils;
import com.aiijie.chatview.utils.Filter;
import com.aiijie.chatview.utils.LogUtils;
import com.aiijie.chatview.utils.SharedPreferencesUtil;
import com.aiijie.chatview.utils.SizeUtils;
import com.aiijie.chatview.utils.UiUtil;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconsFragment;

/**
 * Created by klx on 2017/5/16.
 * 聊天控件
 */
public class MyChatView extends RelativeLayout implements View.OnClickListener {
    private final long delayTime = 20;
    public static final int STATE_SHOW_NO = 0;  // 什么都不显示
    public static final int STATE_SHOW_EMOJI = 1;  // 显示emoji表情
    public static final int STATE_SHOW_KEYBOARD = 2;  // 显示软键盘
    public static final int STATE_SHOW_MORE = 3;  // 显示更多

    private ViewGroup mRootView;
    private ImageView iv_more; // 显示更多的
    private EmojiconEditText chat_input_et; // 输入框
    private ImageView iv_emoji;  // 显示emoji的按钮
    private TextView tv_send; // 发送按钮
    private FrameLayout frameLayout;  // 显示表情的区域
    private LinearLayout layout_more; // 显示更多的区域

    private LinearLayout ll_take_pic;  // 拍照
    private LinearLayout ll_choose_pic;  // 选择图片

    private int currentState = STATE_SHOW_NO;
    private int maxInputLength = 10000;
    Handler handler = new Handler();
    private Activity activity;
    private Window mWindow;
    InputMethodManager mImm;

    private boolean mShowSoftInput = false;
    private boolean mIsShowMoreMenu = false;
    private boolean isExpression = false;

    public MyChatView(Context context) {
        this(context, null);
    }

    public MyChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEmojiEditText);
        a.recycle();
        this.activity = (Activity) context;
        this.mWindow = activity.getWindow();
        this.mImm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
    }

    private void initView() {
        mRootView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_view_chat, this);
        iv_more = (ImageView) mRootView.findViewById(R.id.iv_more);
        iv_emoji = (ImageView) mRootView.findViewById(R.id.iv_emoji);
        chat_input_et = (EmojiconEditText) mRootView.findViewById(R.id.chat_input_et);
        tv_send = (TextView) mRootView.findViewById(R.id.tv_send);
        frameLayout = (FrameLayout) mRootView.findViewById(R.id.id_emoji_emojicons);
        layout_more = (LinearLayout) mRootView.findViewById(R.id.layout_more);
        ll_take_pic = (LinearLayout) layout_more.findViewById(R.id.ll_take_pic);
        ll_choose_pic = (LinearLayout) layout_more.findViewById(R.id.ll_choose_pic);

        setExpLayoutHeight();
        setMoreLayoutHeight();
        setSendClick();
        chat_input_et.requestFocus();
        chat_input_et.setOnFocusChangeListener(listener);
        initClickListener();

        initEmojiLayout((FragmentActivity) getContext());
    }

    private void initEmojiLayout(FragmentActivity activity) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.id_emoji_emojicons,
                        EmojiconsFragment.newInstance(true))
                .commit();
    }

    private void initClickListener() {
        iv_emoji.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        ll_take_pic.setOnClickListener(this);
        ll_choose_pic.setOnClickListener(this);
        chat_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(chat_input_et.getText().toString().trim())) {
                    iv_more.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.GONE);
                } else {
                    tv_send.setVisibility(View.VISIBLE);
                    iv_more.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_more:
                if (layout_more.getVisibility() != View.VISIBLE) {
                    setCurrentState(STATE_SHOW_MORE);
                } else {
                    setCurrentState(STATE_SHOW_NO);
                }
                break;
            case R.id.iv_emoji:
                if (frameLayout.getVisibility() != View.VISIBLE) {
                    setCurrentState(STATE_SHOW_EMOJI);
                } else {
                    setCurrentState(STATE_SHOW_NO);
                }
                break;
            case R.id.ll_take_pic:  // 选择图片
                if (onMoreClickListener != null) {
                    onMoreClickListener.onTickPicClick();
                }
                break;
            case R.id.ll_choose_pic: // 拍照
                if (onMoreClickListener != null) {
                    onMoreClickListener.onChoosePicClick();
                }
                break;
        }
    }

    private void setSendClick() {
        InputFilter[] filters = {Filter.emojiFilter, new InputFilter.LengthFilter(maxInputLength) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder tempResult = new StringBuilder(source).append(dest);
                if (!TextUtils.isEmpty(tempResult.toString()) && tempResult.toString().length() > maxInputLength) {
                    UiUtil.showToast("最多输入" + maxInputLength + "个字");
                }
                return super.filter(source, start, end, dest, dstart, dend);
            }
        }};
        chat_input_et.setFilters(filters);
        tv_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSendClickListener != null) {
                    String content = chat_input_et.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        if (content.length() < maxInputLength) {
                            chat_input_et.setText("");
                            onSendClickListener.onSendClick(content);
                        }
                    }
                }
            }
        });
    }

    OnFocusChangeListener listener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCurrentState(STATE_SHOW_KEYBOARD);
                    }
                }, delayTime);
            }
        }
    };

    private void setExpLayoutHeight() {
        int softKeyboardHeight = SharedPreferencesUtil.getInstance()
                .getCachedKeyboardHeight();
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.height = SharedPreferencesUtil.getInstance().getCachedKeyboardHeight();
        frameLayout.setLayoutParams(layoutParams);
        LogUtils.i("键盘高度：" + softKeyboardHeight);
        if (softKeyboardHeight > 0) {
            ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
            lp.height = softKeyboardHeight;
            frameLayout.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
            lp.height = (int) (SizeUtils.getScreenHeight(getContext()) * 0.3);
            frameLayout.setLayoutParams(lp);
            layout_more.setLayoutParams(lp);

        }
        LogUtils.i("控件高度：" + frameLayout.getHeight());
    }

    private void setMoreLayoutHeight() {
        int softKeyboardHeight = SharedPreferencesUtil.getInstance()
                .getCachedKeyboardHeight();
        ViewGroup.LayoutParams layoutParams = layout_more.getLayoutParams();
        layoutParams.height = SharedPreferencesUtil.getInstance().getCachedKeyboardHeight();
        layout_more.setLayoutParams(layoutParams);
        LogUtils.i("键盘高度：" + softKeyboardHeight);
        if (softKeyboardHeight > 0) {
            ViewGroup.LayoutParams lp = layout_more.getLayoutParams();
            lp.height = softKeyboardHeight;
            layout_more.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = layout_more.getLayoutParams();
            lp.height = (int) (SizeUtils.getScreenHeight(getContext()) * 0.3);
            layout_more.setLayoutParams(lp);
        }
        LogUtils.i("控件高度：" + frameLayout.getHeight());
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
        switch (currentState) {
            case STATE_SHOW_EMOJI:
                dismissSoftInputAndShowEmoji();
                break;
            case STATE_SHOW_NO:
                showNo();
                break;
            case STATE_SHOW_KEYBOARD:
                showSoftInputAndDismissMenu();
                break;
            case STATE_SHOW_MORE:
                dismissSoftInputAndShowMenu();
                break;
        }
    }

    private void showNo() {
        chat_input_et.clearFocus();
        CommonUtils.hideKeyboard(getWindowToken());
        dismissExpLayout();
        mShowSoftInput = false;
        mIsShowMoreMenu = false;
        isExpression = false;
    }

    private void showSoftInputAndDismissMenu() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dismissExpLayout();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chat_input_et.requestFocus();
        if (mImm != null) {
            mImm.showSoftInput(chat_input_et,
                    InputMethodManager.SHOW_FORCED);
        }
        mShowSoftInput = true;
        mIsShowMoreMenu = false;
        isExpression = false;
    }

    public void dismissSoftInputAndShowMenu() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chat_input_et.clearFocus();
        if (mImm != null) {
            mImm.hideSoftInputFromWindow(chat_input_et
                    .getWindowToken(), 0);
        }
        showMoreMenu();
        mShowSoftInput = false;
        isExpression = false;
        mIsShowMoreMenu = true;
    }

    public void dismissSoftInputAndShowEmoji() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chat_input_et.clearFocus();
        if (mImm != null) {
            mImm.hideSoftInputFromWindow(chat_input_et
                    .getWindowToken(), 0);
        }
        showEmoji();
        mShowSoftInput = false;
        mIsShowMoreMenu = false;
        isExpression = true;
    }

    public void dismissExpLayout() {
        frameLayout.setVisibility(View.GONE);
        layout_more.setVisibility(View.GONE);
        mIsShowMoreMenu = false;
        isExpression = false;
    }

    public void showMoreMenu() {
        layout_more.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    public void showEmoji() {
        layout_more.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

    public EmojiconEditText getChat_input_et() {
        return chat_input_et;
    }

    private OnSendClickListener onSendClickListener;

    public interface OnSendClickListener {
        void onSendClick(String content);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
        ll_take_pic.setOnClickListener(this);
        ll_choose_pic.setOnClickListener(this);
    }

    private OnMoreClickListener onMoreClickListener;

    public interface OnMoreClickListener {
        void onTickPicClick();

        void onChoosePicClick();
    }
}
