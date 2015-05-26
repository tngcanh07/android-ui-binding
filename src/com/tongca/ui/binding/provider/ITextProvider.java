package com.tongca.ui.binding.provider;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;

public interface ITextProvider {
    /**
     * get description from a TextView
     * <p/>
     * The format of description is
     * <code>textKey=[key];textHint=[hint];textStyle=[style]</code>
     *
     * @param textView the TextView to get description
     * @return description of TextView usually description is ContentDescription
     */
    public HashMap<String, String> getTextDescription(TextView textView);

    public CharSequence getText(String key);

    public CharSequence getTextByResId(int resId);

    public Typeface getTypeface(String typeface);

}
