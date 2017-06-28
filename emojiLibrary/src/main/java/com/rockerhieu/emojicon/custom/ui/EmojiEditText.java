package com.rockerhieu.emojicon.custom.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
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

public class EmojiEditText extends EditText {
	private static final String START_CHAR = "[";
	private static final String END_CHAR = "]";
	private boolean load = true;

	public EmojiEditText(Context context) {
		super(context);
		init();
	}

	public EmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
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

	private void init() {
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
