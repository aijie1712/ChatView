package com.rockerhieu.emojicon.custom.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmojiAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private int mSize;
	private int mCurrentPage;
	private Map<String, String> mFaceMap;
	private List<String> mEmojiKeys = new ArrayList<String>();
	private List<String> mEmojiValues = new ArrayList<String>();
	private ViewHolder viewHolder;
	private int count;

	public EmojiAdapter(Context context, int currentPage, int size) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		mSize = size;
		mCurrentPage = currentPage;
		mFaceMap = EmojiUtil.getInstance().getFaceMap();
		initData();
	}

	private void initData() {
		for (Map.Entry<String, String> entry : mFaceMap.entrySet()) {
			mEmojiValues.add(entry.getValue());
			mEmojiKeys.add(entry.getKey());
		}
	}

	@Override
	public int getCount() {
		return mSize;
	}

	@Override
	public Object getItem(int position) {
		int count = getCount() * mCurrentPage + position;
		if (count < mFaceMap.size())
			return mEmojiKeys.get(count);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (convertView == null) {
			rowView = mLayoutInflater.inflate(com.rockerhieu.emojicon.R.layout.emoji_cell, null);
			viewHolder = new ViewHolder((ImageView) rowView);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}
		

		count = getCount() * mCurrentPage + position;
		if (count < mFaceMap.size()) {
			//add del key to keyBoard
			//if (position == 20) {
			//	viewHolder.imageView.setImageResource(R.drawable.selector_sym_keyboard_delete_holo);
			//	viewHolder.imageView.setTag(viewHolder.imageView.getId(),"del");
			//} else {
				viewHolder.imageView.setImageResource(mContext.getResources().getIdentifier(EmojiUtil.STATIC_FACE_PREFIX + mEmojiValues.get(count),
						"drawable", mContext.getPackageName()));
				viewHolder.imageView.setTag(viewHolder.imageView.getId(), mEmojiKeys.get(count));
			//}
		} else {
			//add del key to keyBoard
			//if (position == 20) {
			//	viewHolder.imageView.setImageResource(R.drawable.selector_sym_keyboard_delete_holo);
			//	viewHolder.imageView.setTag(viewHolder.imageView.getId(),"del");
			//} else {
				viewHolder.imageView.setBackgroundResource(android.R.color.transparent);
				viewHolder.imageView.setImageResource(com.rockerhieu.emojicon.R.drawable.expression_transparent);
			//}
		}
		return rowView;
	}

	static class ViewHolder {
		public ImageView imageView;

		public ViewHolder(ImageView imageView) {
			this.imageView = imageView;
		}
	}
}
