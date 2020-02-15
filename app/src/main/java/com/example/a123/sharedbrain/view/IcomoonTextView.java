package com.example.a123.sharedbrain.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;



import com.example.a123.sharedbrain.helper.FontCustomHelper;

/**
 * 引用特殊字体的 TextView
 */
public class IcomoonTextView extends AppCompatTextView {
    public IcomoonTextView(Context context) {
        super(context);
    }

    public IcomoonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IcomoonTextView(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
    }


    @Override
    public Typeface getTypeface() {
        return FontCustomHelper.getInstance().getTypeface(getContext());
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(FontCustomHelper.getInstance().getTypeface(getContext()));
    }
}
