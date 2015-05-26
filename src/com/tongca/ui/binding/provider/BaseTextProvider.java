package com.tongca.ui.binding.provider;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

import com.tongca.ui.binding.UIBinding;
import com.tongca.ui.binding.UITextViewUtils;

import java.util.HashMap;

public class BaseTextProvider implements ITextProvider {
    public static final String SEPARATE = ";";

    private final Context context;

    public BaseTextProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public HashMap<String, String> getTextDescription(TextView textView) {
        CharSequence contentDescription = textView.getContentDescription();
        String textKey = null;
        HashMap<String, String> attrs = new HashMap<String, String>();
        if (!TextUtils.isEmpty(contentDescription)) {
            String[] descriptions = contentDescription.toString().split(SEPARATE);
            for (String desc : descriptions) {
                try {
                    if (desc.contains("=")) {
                        String[] arrDesc = desc.split("=");
                        attrs.put(arrDesc[0], arrDesc[1]);
                    } else {
                        attrs.put(UITextViewUtils.ATTR_TEXT_KEY, desc);
                    }
                } catch (Exception e) {
                    if (UIBinding.isDebug())
                        e.printStackTrace();
                }
            }

            textKey = attrs.get(UITextViewUtils.ATTR_TEXT_KEY);
        }

        // update textKey from text
        if (TextUtils.isEmpty(textKey)) {
            textKey = textView.getText().toString();
            attrs.put(UITextViewUtils.ATTR_TEXT_KEY, textKey);
        }
        return attrs;
    }

    @Override
    public CharSequence getText(String key) {
        return key;
    }

    @Override
    public CharSequence getTextByResId(int resId) {
        return context.getString(resId);
    }

    @Override
    public Typeface getTypeface(String description) {
        return null;
    }


}
