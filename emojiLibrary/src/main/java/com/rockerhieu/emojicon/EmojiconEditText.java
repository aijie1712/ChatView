/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rockerhieu.emojicon;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;


import com.rockerhieu.emojicon.custom.util.AnimatedGifDrawable;
import com.rockerhieu.emojicon.custom.util.AnimatedImageSpan;
import com.rockerhieu.emojicon.custom.util.CustomEmojiUtil;
import com.rockerhieu.emojicon.custom.util.Filter;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class EmojiconEditText extends EditText {
	private static final String START_CHAR = "[";
	private static final String END_CHAR = "]";
	private boolean load = true;
	
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;

    public EmojiconEditText(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public EmojiconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
		setFilters(new InputFilter[]{Filter.emojiFilter});
        init(attrs);
    }

    public EmojiconEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    
    @Override
	public void setText(CharSequence text, BufferType type) {
		if (text != null) {
			SpannableString content = new SpannableString(text);
			emotifySpannable(null, content, 0, 0);
			super.setText(content, BufferType.SPANNABLE);
		} else {
			super.setText(text, type);
		}
	}

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, com.rockerhieu.emojicon.R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(com.rockerhieu.emojicon.R.styleable.Emojicon_Size, getTextSize());
        mEmojiconAlignment = a.getInt(com.rockerhieu.emojicon.R.styleable.Emojicon_Alignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(com.rockerhieu.emojicon.R.styleable.Emojicon_UseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
        this.addTextChangedListener(new TextWatcher() {
			private int subStart = -1;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (subStart == -1) {
					subStart = start;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!load) {
					subStart = -1;
				} else {
					if (subStart != -1) {
						emotifySpannable(s, new SpannableString(s.subSequence(subStart, s.length())), subStart, s.length());
						subStart = -1;
					}
				}
				// emotifySpannable(s);
			}
		});
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }
    
    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;

        updateText();
    }

    private void updateText() {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }

    /**
     * Set whether to use system default emojicon
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
    
    /**
	 * Work through the contents of the string, and replace any occurrences of
	 * [icon] with the imageSpan
	 * 
	 * @param spannable
	 */
	private void emotifySpannable(Editable s, Spannable spannable, int st, int en) {
		int length = spannable.length();
		if (length <= 0) {
			return;
		}

		int position = 0;
		int tagStartPosition = 0;
		int tagLength = 0;
		StringBuilder buffer = new StringBuilder();
		boolean inTag = false;
		do {
			String c = spannable.subSequence(position, position + 1).toString();
			if (!inTag && c.equals(START_CHAR)) {
				buffer = new StringBuilder();
				tagStartPosition = position;
				inTag = true;
				tagLength = 0;
			}

			if (inTag) {
				buffer.append(c);
				tagLength++;
				// Have we reached end of the tag?
				if (c.equals(END_CHAR)) {
					inTag = false;

					String tag = buffer.toString();
					int tagEnd = tagStartPosition + tagLength;

					// start by liweiping for 去除首部有多个“[”符号
					int lastIndex = tag.lastIndexOf(START_CHAR);

					if (lastIndex > 0) {
						tagStartPosition = tagStartPosition + lastIndex;
						tag = tag.substring(lastIndex, tag.length());
					}

					ImageSpan imageSpan = getImageSpan(tag);
					// DynamicDrawableSpan imageSpan = getDynamicImageSpan(tag);
					if (imageSpan != null) {
						spannable.setSpan(imageSpan, tagStartPosition, tagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						if (s != null) {
							load = false;
							s.delete(st, en);
							s.append(spannable);
							load = true;
						}
					}
				}
			}
			position++;
		} while (position < length);
	}

	private ImageSpan getImageSpan(String content) {
		String idStr = CustomEmojiUtil.getInstance().getFaceId(content);
		Resources resources = getContext().getResources();
		int id = resources.getIdentifier(CustomEmojiUtil.STATIC_FACE_PREFIX + idStr, "drawable", getContext().getPackageName());
		if (id > 0) {
			try {
				//int size = Math.round(getTextSize()) + 10;
				int size = Math.round(getTextSize()) + 2;
				Drawable emoji = getContext().getResources().getDrawable(id);
				emoji.setBounds(0, 0, size, size);
				ImageSpan imageSpan = new ImageSpan(emoji, ImageSpan.ALIGN_BASELINE);
				return imageSpan;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private DynamicDrawableSpan getDynamicImageSpan(String content) {
		String idStr = CustomEmojiUtil.getInstance().getFaceId(content);
		Resources resources = getContext().getResources();
		int id = resources.getIdentifier(CustomEmojiUtil.DYNAMIC_FACE_PREFIX + idStr, "drawable", getContext().getPackageName());
		if (id > 0) {
			try {
				//AnimatedImageSpan imageSpan = new AnimatedImageSpan(new AnimatedGifDrawable(getResources(), Math.round(getTextSize()) + 10,
				AnimatedImageSpan imageSpan = new AnimatedImageSpan(new AnimatedGifDrawable(getResources(), Math.round(getTextSize()) + 2,
						getResources().openRawResource(id), null));
				return imageSpan;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
