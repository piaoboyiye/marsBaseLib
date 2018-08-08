package com.mars.android.baselib.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

public class ContainsEmojiEditText extends EditText {

    public ContainsEmojiEditText(Context context) {
        this(context, null);
    }

    public ContainsEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{new EmojiFilter()});
    }

}
