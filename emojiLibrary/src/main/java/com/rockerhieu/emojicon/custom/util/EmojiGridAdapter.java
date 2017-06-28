package com.rockerhieu.emojicon.custom.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by win7 on 16/8/12.
 */
public class EmojiGridAdapter extends BaseAdapter {
    private String[] mEmojis;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder;
    private Context mContext;

    public EmojiGridAdapter(Context c, String[] emojis) {
        mInflater = LayoutInflater.from(c);
        mEmojis = emojis;
        mContext = c;
    }

    public void setEmoji(String[] emojis) {
        mEmojis = emojis;
    }

    public int getCount() {
        return mEmojis.length;
    }

    public Object getItem(int position) {
        return mEmojis[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (convertView == null) {
            rowView = mInflater.inflate(com.rockerhieu.emojicon.R.layout.emoji_cell, null);
            viewHolder = new ViewHolder((ImageView) rowView);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.imageView.setImageResource(mContext.getResources().getIdentifier(
                CustomEmojiUtil.STATIC_FACE_PREFIX + CustomEmojiUtil.getInstance().getFaceId(mEmojis[position]), "drawable", mContext.getPackageName()));
        viewHolder.imageView.setTag(viewHolder.imageView.getId(), mEmojis[position]);
        return rowView;
    }

    class ViewHolder {
        public ImageView imageView;

        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
