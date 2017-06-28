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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.rockerhieu.emojicon.custom.util.AnimatedGifDrawable;
import com.rockerhieu.emojicon.custom.util.AnimatedImageSpan;
import com.rockerhieu.emojicon.custom.util.CustomEmojiUtil;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class EmojiconTextView extends TextView {
    private static final String START_CHAR = "[";
    private static final String END_CHAR = "]";
    private boolean mIsDynamic;

    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private int mTextStart = 0;
    private int mTextLength = -1;
    private boolean mUseSystemDefault = false;

    public EmojiconTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmojiconTextSize = (int) getTextSize();
        if (attrs == null) {
            mEmojiconSize = (int) getTextSize();
            mIsDynamic = false;
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, com.rockerhieu.emojicon.R.styleable.Emojicon);
            mEmojiconSize = (int) a.getDimension(com.rockerhieu.emojicon.R.styleable.Emojicon_Size, getTextSize());
            mEmojiconAlignment = a.getInt(com.rockerhieu.emojicon.R.styleable.Emojicon_Alignment, DynamicDrawableSpan.ALIGN_BOTTOM);
            mTextStart = a.getInteger(com.rockerhieu.emojicon.R.styleable.Emojicon_TextStart, 0);
            mTextLength = a.getInteger(com.rockerhieu.emojicon.R.styleable.Emojicon_TextLength, -1);
            mUseSystemDefault = a.getBoolean(com.rockerhieu.emojicon.R.styleable.Emojicon_UseSystemDefault, false);
            mIsDynamic = a.getBoolean(com.rockerhieu.emojicon.R.styleable.Emojicon_IsDynamic, false);
            a.recycle();
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {

            if (checkCustomExp(text)) {
                text = text + " ";
                SpannableString content = new SpannableString(text);
                emotifySpannable(content);
                super.setText(content, BufferType.SPANNABLE);
                return;
            } else {
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
                text = builder;
            }
        }
        super.setText(text, type);
    }

    private boolean checkCustomExp(CharSequence text) {
        SpannableString spannable = new SpannableString(text);
        boolean result = false;
        int length = spannable.length();
        int position = 0;
        boolean inTag = false;
        if (length <= 0)
            return false;
        do {
            String c = spannable.subSequence(position, position + 1).toString();
            if (!inTag && c.equals(START_CHAR)) {
                inTag = true;
            }
            if (inTag && c.equals(END_CHAR)) {
                // Have we reached end of the tag?
                result = true;
                break;
            }
            position++;
        } while (position < length);

        return result;
    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        super.setText(getText());
    }

    /**
     * Set whether to use system default emojicon
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }

    /**
     * Work through the contents of the string, and replace any occurrences of [icon] with the imageSpan
     *
     * @param spannable
     */
    private void emotifySpannable(Spannable spannable) {
        int length = spannable.length();
        int position = 0;
        int tagStartPosition = 0;
        int tagLength = 0;
        StringBuilder buffer = new StringBuilder();
        boolean inTag = false;
        if (length <= 0)
            return;
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
                    // end by liweiping for
                    // MLog.d(TAG, "Tag: " + tag + ", started at: "
                    // + tagStartPosition + ", finished at " + tagEnd
                    // + ", length: " + tagLength);
                    if (mIsDynamic) {
                        DynamicDrawableSpan imageSpan = getDynamicImageSpan(tag);
                        if (imageSpan != null)
                            spannable.setSpan(imageSpan, tagStartPosition, tagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        ImageSpan imageSpan = getImageSpan(tag);
                        if (imageSpan != null)
                            spannable.setSpan(imageSpan, tagStartPosition, tagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            position++;
        } while (position < length);
    }

    /**
     * 解析字符串表情对应的表情png图片
     *
     * @param content
     * @return
     */
    private ImageSpan getImageSpan(String content) {
        String idStr = CustomEmojiUtil.getInstance().getFaceId(content);
        int id = getContext().getResources().getIdentifier(CustomEmojiUtil.STATIC_FACE_PREFIX + idStr, "drawable",
                getContext().getPackageName());
        if (id > 0) {
            try {
                // int size = Math.round(getTextSize()) + 10;
                int size = Math.round(getTextSize()) + 2;
                Drawable emoji = getContext().getResources().getDrawable(id);
                emoji.setBounds(0, 0, size, size);
                ImageSpan imageSpan = new ImageSpan(emoji, ImageSpan.ALIGN_BOTTOM);
                return imageSpan;
            } catch (Exception e) {
                Log.e("--lyl--", "", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解析字符串表情对应的表情gif图片
     *
     * @param content
     * @return
     */
    private DynamicDrawableSpan getDynamicImageSpan(String content) {
        String idStr = CustomEmojiUtil.getInstance().getFaceId(content);
        int id = getContext().getResources().getIdentifier(CustomEmojiUtil.DYNAMIC_FACE_PREFIX + idStr, "drawable",
                getContext().getPackageName());
        if (id > 0) {
            try {
                // AnimatedImageSpan imageSpan = new AnimatedImageSpan(new AnimatedGifDrawable(getResources(),
                // Math.round(getTextSize()) + 10,
                AnimatedImageSpan imageSpan = new AnimatedImageSpan(new AnimatedGifDrawable(getResources(),
                        Math.round(getTextSize()) + 2, getResources().openRawResource(id),
                        new AnimatedGifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                // update the textview
                                EmojiconTextView.this.postInvalidate();
                            }
                        }));
                return imageSpan;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
