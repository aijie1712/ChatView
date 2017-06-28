package com.rockerhieu.emojicon.custom.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.rockerhieu.emojicon.EmojiconCustomGridFragment.EventListener;
import com.rockerhieu.emojicon.EmojiconRecents;
import com.rockerhieu.emojicon.custom.util.CustomEmojiUtil;
import com.rockerhieu.emojicon.custom.util.EmojiAdapter;
import com.rockerhieu.emojicon.custom.util.EmojiViewPageAdapter;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

public class EmojiLinearLayout extends LinearLayout implements
		OnItemClickListener {
	// 横屏时
	private static final int LAND_COLUMN = 11;// 11列
	private static final int LAND_ROW = 2;// 2行
	// 竖屏时
	private static final int PORT_COLUMN = 7;// 7列
	private static final int PORT_ROW = 3;// 3行
	private Context mContext;
	private ViewPager mPager;
	private int mViewPagerNum;
	
	private EventListener mListener;
	
	public void setEventListener(EventListener listener) {
		this.mListener = listener;
	}

	private OnEmojiClickedListener mOnEmojiClickedListener = new OnEmojiClickedListener() {

		@Override
		public void onEmojiClicked(String emoji) {
			if (TextUtils.isEmpty(emoji))
				return;
			if (mListener != null)
				mListener.onEmojiSelected(emoji);
			if (mRecents != null) {
				Emojicon emojicon = new Emojicon(emoji);
	            mRecents.addRecentEmoji(mContext,emojicon);
	        }
		}
	};
	
	private EmojiconRecents mRecents;
	
	public void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

	public interface OnEmojiClickedListener {
		void onEmojiClicked(String emoji);
	}

	public EmojiLinearLayout(Context context) {
		super(context);
		mContext = context;
	}

	public EmojiLinearLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mPager = (ViewPager) findViewById(com.rockerhieu.emojicon.R.id.child_pager);
		mViewPagerNum = getEmojiSize();
		List<GridView> lv = new ArrayList<GridView>();
		for (int i = 0; i < mViewPagerNum; ++i)
			lv.add(getGridView(LayoutInflater.from(getContext()), i));
		EmojiViewPageAdapter adapter = new EmojiViewPageAdapter(lv);
		mPager.setAdapter(adapter);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(com.rockerhieu.emojicon.R.id.indicator);
		indicator.setViewPager(mPager);
	}

	private int getEmojiSize() {
		int size = CustomEmojiUtil.getInstance().getFaceMap().size();
		if (size % PORT_COLUMN * PORT_ROW == 0)// 刚好被整除
			return size / (PORT_COLUMN * PORT_ROW);
		return (size / (PORT_COLUMN * PORT_ROW)) + 1;
	}

	private GridView getGridView(LayoutInflater inflater, int i) {
		GridView gv = (GridView) inflater.inflate(com.rockerhieu.emojicon.R.layout.emoji_grid, null);
		gv.setAdapter(new EmojiAdapter(getContext(), i, PORT_COLUMN * PORT_ROW));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(this);
		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return event.getAction() == MotionEvent.ACTION_MOVE;
			}
		};
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String selected = (String) view.getTag(view.getId());
		if (TextUtils.isEmpty(selected))
			return;
		if (mOnEmojiClickedListener != null) {
			mOnEmojiClickedListener.onEmojiClicked(selected);
		}
	}
}
